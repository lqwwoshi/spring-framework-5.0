package com.lqw.aoptest;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

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
//	@AfterThrowing("pointCut()")
//	public void afterThrowing() {
//		System.out.println("afterThrowing");
//	}
////
//	@AfterReturning("pointCut()")
//	public void afterReturning() {
//		System.out.println("afterReturning");
//	}
}
