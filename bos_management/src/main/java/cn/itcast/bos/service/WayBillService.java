package cn.itcast.bos.service;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface WayBillService {


    void save(WayBill model) throws InvocationTargetException, IllegalAccessException, Exception;

    Page<WayBill> pageQuery(WayBill model, PageRequest pageRquest);

    WayBill findByWayBillNum(String wayBillNum);

	List<WayBill> findWayBills(WayBill model);

	List<Object> findCountWayBill();

}
