package com.lqw.aoptest;

import com.lqw.app.Appconfig;
import com.lqw.zoctest.OrderService;
import com.lqw.zoctest.OrderServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lqw
 * @Date: 2020/5/31
 */
public class AopTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext
				= new AnnotationConfigApplicationContext(Appconfig.class);
		OrderService orderService = annotationConfigApplicationContext.getBean(OrderService.class);
//		orderService.test();
	}
}
