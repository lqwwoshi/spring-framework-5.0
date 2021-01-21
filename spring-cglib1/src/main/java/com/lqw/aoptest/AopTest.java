package com.lqw.aoptest;

import com.lqw.app.Appconfig;
import com.lqw.zoctest.OrderService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: lqw
 * @Date: 2020/5/31
 */
public class AopTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext
				= new AnnotationConfigApplicationContext(Appconfig.class);
		OrderService orderService = annotationConfigApplicationContext.getBean(OrderService.class);
		orderService.test();
	}
}
