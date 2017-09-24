package cn.itcast.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRespository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRespository customerRespository;
	
	@Override
	public List<Customer> findAllNoAssociationCustomer() {
		return customerRespository.findAllNoAssociationCustomer();
	}

	@Override
	public List<Customer> findAllHasAssociationCustomer(String id) {
		return customerRespository.findAllHasAssociationCustomer(id);
	}

	@Override
	public void assvictionCustomerToFixedArea(String customerIdStr, String fixedAreaId) {
		//首先将所有关联到这个定区的cutomer设为null
		customerRespository.clearFixedArea(fixedAreaId);
		//在将所有的客户关联
		String[] ids = customerIdStr.split(",");
		for (String id : ids) {
			customerRespository.assvictionCustomerToFixedArea(Integer.valueOf(id), fixedAreaId);
		}
	}

	@Override
	public void saveCustomer(Customer customer) {
		customerRespository.save(customer);
	}

	@Override
	public Customer rstelephone(String telephone) {
		return customerRespository.findByTelephone(telephone);
	}

	@Override
	public void updateMailType(String telephone) {
		customerRespository.updateTypeByTelephone(telephone);
	}

	@Override
	public Customer login(String username) {
		return customerRespository.findByUsername(username);
	}
}
