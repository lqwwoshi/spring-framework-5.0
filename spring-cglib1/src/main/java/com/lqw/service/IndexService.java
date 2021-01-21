package com.lqw.service;

import com.lqw.zoctest.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexService {

//	@Autowired
	OrderDao orderDao;

	Index index;

	public IndexService() {
		System.out.println("a");
	}

	public IndexService(String str) {
		System.out.println("str");
	}
//	@Autowired(required = false)
//	public IndexService(OrderDao orderDao) {
//		this.orderDao = orderDao;
//	}
//
////	@Autowired(required = false)
////	public IndexService(OrderDao orderDao, Index index) {
////
////	}
//
//	@Autowired(required = false)
//	public IndexService(OrderDao orderDao, Object object) {
//
//	}
}
