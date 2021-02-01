package com.lqw.aoptest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Author: lqw
 * @Date: 2020/5/31
 */
@Aspect
@Component
public class MyAop {

	@Pointcut("execution(* com.lqw.zoctest.*.*(..))")
	public void pointCut() {}

	@Before("pointCut()")
	public void before() {
		System.out.println("before");
	}

	@After("pointCut()")
	public void after() {
		System.out.println("after");
	}

//	@Pointcut("execution(* com.lqw.zoctest.*.a*(..))")
//	public void pointCut1() {}
//
//	@Before("pointCut1()")
//	public void before1() {
//		System.out.println("before");
//	}
//

//
	@AfterThrowing("pointCut()")
	public void afterThrowing() {
		System.out.println("afterThrowing");
	}
//
	@AfterReturning("pointCut()")
	public void afterReturning() {
		System.out.println("afterReturning");
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint pjd) {

		Object result = null;
		String methodName = pjd.getSignature().getName();

		try {
			result = pjd.proceed();
		} catch (Throwable e) {
		}
		System.out.println("around");
		return result;
	}
}
