package com.joparis2024.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.joparis2024.dto.OrderDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
public class OrderServiceTest {
	@Autowired
	private OrderService orderService;
	
	@Test(expected = Exception.class)
	public void createOrderKO () {
		int a=1, b=3/a;
		OrderDTO dto = new OrderDTO(null, null, null, null, null, null);
		
		this.orderService.createOrder(dto);
	}

}

