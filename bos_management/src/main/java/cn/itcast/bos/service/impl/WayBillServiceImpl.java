package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;

    /**
     * 保存运单
     * @param model
     */
    @Override
    public void save(WayBill model) throws Exception {
        //根据运单号是否存在决定保存还是更新
        WayBill wayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
        if (wayBill == null) {
            //不存在就保存
            model.setId(null);
            //判断order是否存在不存在就设为null
            if (model.getOrder() != null && model.getOrder().getId() == null) {
                model.setOrder(null);
            }
            //将运单设为待发货
            model.setSignStatus(1); //1:代发货， 2：配送中
            //保存
            wayBillRepository.save(model);
            return;
        }
        //存在, 将值赋给查询查询到的对象中，order还是用原来的，不能改动
        Order order = wayBill.getOrder();
        BeanUtils.copyProperties(wayBill, model);
        wayBill.setSignStatus(1);
        wayBill.setOrder(order);
    }

    /**
     * 分页查询运单
     * @param pageRquest
     * @return
     */
    @Override
    public Page<WayBill> pageQuery(PageRequest pageRquest) {
        return wayBillRepository.findAll(pageRquest);
    }

    /**
     * 根据运单号查询运单
     * @param wayBillNum
     * @return
     */
    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }
}
