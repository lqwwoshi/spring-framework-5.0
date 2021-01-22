/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.aspectj.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.reflect.PerClauseKind;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Helper for retrieving @AspectJ beans from a BeanFactory and building
 * Spring Advisors based on them, for use with auto-proxying.
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 * @see AnnotationAwareAspectJAutoProxyCreator
 */
public class BeanFactoryAspectJAdvisorsBuilder {

	private final ListableBeanFactory beanFactory;

	private final AspectJAdvisorFactory advisorFactory;

	@Nullable
	private volatile List<String> aspectBeanNames;

	private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();

	private final Map<String, MetadataAwareAspectInstanceFactory> aspectFactoryCache = new ConcurrentHashMap<>();


	/**
	 * Create a new BeanFactoryAspectJAdvisorsBuilder for the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to scan
	 */
	public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory) {
		this(beanFactory, new ReflectiveAspectJAdvisorFactory(beanFactory));
	}

	/**
	 * Create a new BeanFactoryAspectJAdvisorsBuilder for the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to scan
	 * @param advisorFactory the AspectJAdvisorFactory to build each Advisor with
	 */
	public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory) {
		Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
		Assert.notNull(advisorFactory, "AspectJAdvisorFactory must not be null");
		this.beanFactory = beanFactory;
		this.advisorFactory = advisorFactory;
	}


	/**
	 * Look for AspectJ-annotated aspect beans in the current bean factory,
	 * and return to a list of Spring AOP Advisors representing them.
	 * <p>Creates a Spring Advisor for each AspectJ advice method.
	 * @return the list of {@link org.springframework.aop.Advisor} beans
	 * @see #isEligibleBean
	 * 去容器中获取到所有的切面信息保存到缓存中
	 */
	public List<Advisor> buildAspectJAdvisors() {
		/**
		 * 用于保存切面的名称，该地方为aspectNames，是我们的类级别的缓存，用户缓存已经解析出来的切面信息
		 */
		List<String> aspectNames = this.aspectBeanNames;
		//缓存字段aspect没有值，代表实例化第一个单例bean的时候触发解析切面的操作(第一个？，就是字面上的意思)
		//初始化第一个单例Bean的时候会去触发整个容器里所有切面初始化操作
		if (aspectNames == null) {
			//做dcl(doubleCheck检查)
			synchronized (this) {
				aspectNames = this.aspectBeanNames;
				if (aspectNames == null) {
					//用户保存所有解析出的Advisors集合对象
					List<Advisor> advisors = new ArrayList<>();
					aspectNames = new ArrayList<>();
					/**
					 * 值得注意的是这里传入的是Obejct对象，代表去容器中获取所有的组件名称，然后在经过一一遍历
					 * 比对是否有加Aspect注解
					 * 这个过程是比较耗性能的，所以Spring会把切面信息进行缓存
					 * 但是事务功能不一样，事务模块的功能是直接去容器中获取Advisor类型的，选择范围小，且不消耗性能
					 * 所以Spring没有在事务模块中加入缓存保存事务相关的advisor
					 */
					String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
							this.beanFactory, Object.class, true, false);
					//遍历我们从IOC容器中拿到的所有bean名称
					for (String beanName : beanNames) {
						if (!isEligibleBean(beanName)) {
							continue;
						}
						// We must be careful not to instantiate beans eagerly as in this case they
						// would be cached by the Spring container but would not have been weaved.
						//通过beanName去容器中获取对应class对象
						Class<?> beanType = this.beanFactory.getType(beanName);
						if (beanType == null) {
							continue;
						}
						//通过class对象判断是不是切面
						//其实就是判断两个
						//1:是否有加@Aspect注解
						//2:判断是否是AspectJ编译器编译的
						if (this.advisorFactory.isAspect(beanType)) {
							//是切面类
							//加入到缓存中
							aspectNames.add(beanName);
							//把beanName和class对象构建成为一个AspectMetadata
							AspectMetadata amd = new AspectMetadata(beanType, beanName);
							//单例切面
							if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
								//构建切面注解的实例工厂
								//可以看到这个工厂是利用当前的BeanFactory和当前正在解析的beanName来构造的
								MetadataAwareAspectInstanceFactory factory =
										new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
								//真正的去获取我们的通知实例(核心方法)
								//大致看了一下就是找类里面的带四个类似@Before等注解的方法，然后封装一下
								//这里应该不进行校验，即如果声明的切点不存在，这里也不会有校验，真正校验1的地方不在这里
								//即一个切面类的一个通知方法作为一个Advisor，而整体切面类是Aspect
								List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
								//加入到缓存
								if (this.beanFactory.isSingleton(beanName)) {
									this.advisorsCache.put(beanName, classAdvisors);
								}
								else {
									this.aspectFactoryCache.put(beanName, factory);
								}
								advisors.addAll(classAdvisors);
							}
							else {
								// Per target or per this.
								if (this.beanFactory.isSingleton(beanName)) {
									throw new IllegalArgumentException("Bean with name '" + beanName +
											"' is a singleton, but aspect instantiation model is not singleton");
								}
								MetadataAwareAspectInstanceFactory factory =
										new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
								this.aspectFactoryCache.put(beanName, factory);
								advisors.addAll(this.advisorFactory.getAdvisors(factory));
							}
						}
					}
					this.aspectBeanNames = aspectNames;
					return advisors;
				}
			}
		}

		//下面都是非第一次初始化从缓存中拿的代码
		if (aspectNames.isEmpty()) {
			return Collections.emptyList();
		}
		List<Advisor> advisors = new ArrayList<>();
		for (String aspectName : aspectNames) {
			List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
			if (cachedAdvisors != null) {
				advisors.addAll(cachedAdvisors);
			}
			else {
				MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
				advisors.addAll(this.advisorFactory.getAdvisors(factory));
			}
		}
		return advisors;
	}

	/**
	 * Return whether the aspect bean with the given name is eligible.
	 * @param beanName the name of the aspect bean
	 * @return whether the bean is eligible
	 */
	protected boolean isEligibleBean(String beanName) {
		return true;
	}

}
