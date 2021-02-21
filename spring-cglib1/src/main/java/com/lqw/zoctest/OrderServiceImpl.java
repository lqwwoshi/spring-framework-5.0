package com.lqw.zoctest;

import com.lqw.zoctest.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: lqw
 * @Date: 2020/3/14
 */
@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private ItemService itemService;

	@Override
	public void test() {
		System.out.println("abc");
	}
}
