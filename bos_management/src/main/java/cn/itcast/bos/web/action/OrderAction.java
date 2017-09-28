package cn.itcast.bos.web.action;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.OrderService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

	@Autowired
	private OrderService orderService;


    @Action(value = "order_findByOrderNum", results = {@Result(type = "json")})
    public String findByOrderNum() throws Exception {
        Order order = orderService.findByOrderNum(this.model.getOrderNum());
        this.push(order);
        return SUCCESS;
    }
}
