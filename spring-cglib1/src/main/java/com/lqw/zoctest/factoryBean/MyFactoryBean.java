package com.lqw.zoctest.factoryBean;

import com.lqw.zoctest.OrderDaoImpl;
import org.springframework.beans.factory.FactoryBean;

/**
 * @Author: lqw
 * @Date: 2020/3/18
 */
//@Component
public class MyFactoryBean implements FactoryBean {

	public void selectOrder() {
		System.out.println("MyFactoryBean selectOrder");
	}

	@Override
	public Object getObject() throws Exception {
		return new OrderDaoImpl();
	}

	@Override
	public Class<?> getObjectType() {
		return OrderDaoImpl.class;
	}
}
