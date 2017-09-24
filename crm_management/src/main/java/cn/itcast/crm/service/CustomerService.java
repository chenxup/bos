package cn.itcast.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;

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

    //保存客户信息
    @POST
    @Path("/customer")
    @Consumes({"application/xml", "application/json"})
    void saveCustomer(Customer customer);

    //根据手机号查询
    @GET
    @Path("/rstelephone/{telephone}")
    @Produces({"application/xml", "application/json"})
    @Consumes({"application/xml", "application/json"})
    Customer rstelephone(@PathParam("telephone") String telephone);

    //更新邮箱操作
    @PUT
    @Path("updateMailType/{telephone}")
    @Consumes({"application/xml", "application/json"})
    void updateMailType(@PathParam("telephone") String telephone);
    
    //根据用户名查询
    @GET
    @Path("login/{username}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Customer login(@PathParam("username") String username);

}
