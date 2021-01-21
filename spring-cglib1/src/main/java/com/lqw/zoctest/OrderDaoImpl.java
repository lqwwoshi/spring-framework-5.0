package com.lqw.zoctest;

import com.lqw.service.IndexService;
import org.springframework.stereotype.Repository;

/**
 * @Author: lqw
 * @Date: 2020/3/16
 */
@Repository
//@Order(1)
public class OrderDaoImpl implements OrderDao {

	public OrderDaoImpl() {

	}

	@Override
	public void selectOrder() {
		System.out.println("OrderDaoImpl selectOrder");
	}
}
