package cn.itcast.test.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestDao {
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void test1() throws Exception {
		List<Customer> list = customerService.findAllNoAssociationCustomer();
		System.out.println(list);
	}
	
	@Test
	public void test2() throws Exception {
		List<Customer> list = customerService.findAllHasAssociationCustomer("p001");
		System.out.println(list);
	}


	@Test
	public void test3() throws Exception {
		customerService.assvictionCustomerToFixedArea("10001,10005", "p002");

	}
	@Test
	public void test4() throws Exception {
		Customer customer = customerService.rstelephone("13513283278");
		System.out.println(customer);
	}

	@Test
	public void test5() throws Exception {
		customerService.updateMailType("13262809661");
	}
}
