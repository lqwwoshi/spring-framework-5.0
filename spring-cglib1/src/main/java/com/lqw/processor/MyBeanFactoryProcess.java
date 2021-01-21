package com.lqw.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: lqw
 * @Date: 2020/5/12
 */
//@Component
public class MyBeanFactoryProcess implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BeanDefinition indexService = beanFactory.getBeanDefinition("indexService");
		indexService.getConstructorArgumentValues().addGenericArgumentValue("str");

	}
}
