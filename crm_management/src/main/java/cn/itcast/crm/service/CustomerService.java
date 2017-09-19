package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

@Produces("*/*")
public interface CustomerService {

	//查询所有未关联到定区的客户
	@GET
	@Path("/customer")
	@Produces({"application/xml", "application/json"})
	List<Customer> findAllNoAssociationCustomer();
	
	//查询所有关联到指定定区的客户
	@GET
	@Path("/customer/{id}")
	@Produces({"application/xml", "application/json"})
	List<Customer> findAllHasAssociationCustomer(@PathParam("id") String id);
	
	//将客户关联到指定定区
	@PUT
	@Path("/customer")
	void assvictionCustomerToFixedArea(@QueryParam("customerIdStr") String customerIdStr, @QueryParam("fixedAreaId") String fixedAreaId);
	
}
