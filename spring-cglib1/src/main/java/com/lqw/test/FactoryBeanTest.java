package com.lqw.test;

import com.lqw.app.Appconfig;
import com.lqw.service.IndexService;
import com.lqw.zoctest.OrderDaoImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: lqw
 * @Date: 2020/3/18
 */
public class FactoryBeanTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Appconfig.class);
		IndexService indexService = (IndexService)annotationConfigApplicationContext.getBean("indexService");

//		orderDao.selectOrder();
	}
}
