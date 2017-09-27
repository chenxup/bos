package cn.itcast.bos.service;

import cn.itcast.bos.domain.take_delivery.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

public interface OrderService {
    @POST
    @Path("/order")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
    public void save(Order order);

}
