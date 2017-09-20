package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerRespository extends JpaRepository<Customer, Integer>{
	
	@Query("from Customer c where c.fixedAreaId is null")
	List<Customer> findAllNoAssociationCustomer();

	@Query("from Customer c where c.fixedAreaId=?")
	List<Customer> findAllHasAssociationCustomer(String id);

	@Query("update Customer c set c.fixedAreaId=null where c.fixedAreaId=?")
	@Modifying
	void clearFixedArea(String fixedAreaId);

	@Query("update Customer c set c.fixedAreaId=?2 where c.id=?1")
	@Modifying
	void assvictionCustomerToFixedArea(Integer id, String fixedAreaId);

	Customer findByTelephone(String phone);

	@Query("update Customer c set c.type=1 where c.telephone=?")
	@Modifying
	void updateTypeByTelephone(String telephone);
}
