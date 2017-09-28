package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.AreaRepository;
import cn.itcast.bos.dao.FixedAreaRepository;
import cn.itcast.bos.dao.OrderRepository;
import cn.itcast.bos.dao.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.OrderService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.Constants;

import javax.jms.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
    private FixedAreaRepository fixedAreaRepository;

	@Autowired
    private AreaRepository areaRepository;

	@Autowired
    private WorkBillRepository workBillRepository;

	@Autowired
    private JmsTemplate jmsTemplate;

	@Autowired
    private Destination destination;

    @Override
    public void save(Order order) {
        //设置订单号
        order.setOrderNum(UUID.randomUUID().toString().replace("-",""));
        //设置下单时间
        order.setOrderTime(new Date());
        order.setStatus("1");//订单状态 1 待取件 2 运输中 3 已签收 4 异常
        //将order中的区域持久化
        Area sendArea = areaRepository.findAreaByProvinceAndCityAndDistrict(order.getSendArea().getProvince(), order.getSendArea().getCity(), order.getSendArea().getDistrict());
        order.setSendArea(sendArea);
        Area recArea = areaRepository.findAreaByProvinceAndCityAndDistrict(order.getRecArea().getProvince(), order.getRecArea().getCity(), order.getRecArea().getDistrict());
        order.setRecArea(recArea);
        //自动分单
        //1. 根据客户系统找到定区
        String fixedId = WebClient.create(Constants.CRM_MANAGEMENT_HOST + "/services/customerService/findFixedByIdAndAddress?id=" + order.getCustomer_id() + "&address=" + order.getSendAddress())
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(String.class);

        if (fixedId != null) {
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedId);
            Set<Courier> couriers = fixedArea.getCouriers();
            for (Courier courier : couriers) {
                if (courier != null) {
                    //保存订单
                    order.setCourier(courier);
                    order.setOrderType("1");//1自动分单，2手工分单
                    orderRepository.save(order);
                    //生成工单
                    generatorWorkBill(order);
                    return;
                }
            }
        }

        //2. 根据地址查询分区再找到对应的取派员

        if (sendArea != null) {
            Set<SubArea> subareas = sendArea.getSubareas();
            if (subareas != null) {
                for (SubArea subarea : subareas) {
                    if (order.getSendAddress().contains(subarea.getKeyWords())) {
                        FixedArea fixedArea = subarea.getFixedArea();
                        Set<Courier> couriers = fixedArea.getCouriers();
                        if (couriers != null) {
                            for (Courier courier : couriers) {
                                //保存订单
                                order.setCourier(courier);
                                order.setOrderType("1");//1自动分单，2手工分单
                                orderRepository.save(order);
                                //生成工单
                                generatorWorkBill(order);
                                return;
                            }
                        }
                    }
                }
            }
        }

        //人工分单
        order.setOrderType("2");
        orderRepository.save(order);


    }

    private void generatorWorkBill(final Order order) {
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        //生成短信序列号
        final String seccode = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(seccode);
        workBill.setCourier(order.getCourier());
        //发送短信
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                //短信验证码
                mapMessage.setString("seccode", seccode);
                //快递员手机号
                mapMessage.setString("telephone", order.getCourier().getTelephone());
                //寄件人地址
                mapMessage.setString("sendAddress", order.getSendAddress());
                //寄件人姓名
                mapMessage.setString("sendName", order.getSendName());
                //寄件人手机号
                mapMessage.setString("sendMobile", order.getSendMobile());
                //带给快递员的悄悄话
                mapMessage.setString("sendmodbileMsg", order.getSendMobileMsg());
                return mapMessage;
            }
        });
        workBill.setPickstate("已通知");
        workBillRepository.save(workBill);
    }

    /**
     * 根据订单号查询订单
     * @param orderNum
     * @return
     */
    @Override
    public Order findByOrderNum(String orderNum) {
        return orderRepository.findByOrderNum(orderNum);
    }
}
