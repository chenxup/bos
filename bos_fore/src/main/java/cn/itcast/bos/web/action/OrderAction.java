package cn.itcast.bos.web.action;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import utils.Constants;

import javax.ws.rs.core.MediaType;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

    private String sendAreaInfo;
    private String recAreaInfo;

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    /**
     * 生成订单
     * @return
     * @throws Exception
     */
    @Action(value="order_add", results = {@Result(type = "redirect", location = "index.html")})
    public String save() throws Exception {
        //封装参数
        //寄件人地址
        String[] sendArea = sendAreaInfo.split("/");
        Area sendarea = new Area();
        sendarea.setProvince(sendArea[0]);
        sendarea.setCity(sendArea[1]);
        sendarea.setDistrict(sendArea[2]);
        //收件人地址
        String[] recArea = recAreaInfo.split("/");
        Area recarea = new Area();
        recarea.setProvince(sendArea[0]);
        recarea.setCity(sendArea[1]);
        recarea.setDistrict(sendArea[2]);

        this.model.setSendArea(sendarea);
        this.model.setRecArea(recarea);
        //客户id
        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("logUser");
        this.model.setCustomer_id(customer.getId());

        //调用远程服务
        WebClient.create(Constants.BOS_MANAGEMENT_HOST+"/services/orderService/order").type(MediaType.APPLICATION_JSON).post(this.model);

        return SUCCESS;
    }
}
