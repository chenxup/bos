package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.TransitInfoRepository;
import cn.itcast.bos.dao.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.TransitInfoService;

@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {

	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	@Autowired
	private WayBillRepository wayBillRepository;

	@Override
	public void create(String wayBillIds) {
		if (wayBillIds != null) {
			String[] wayBillAry = wayBillIds.split(",");
			for (String id : wayBillAry) {
				WayBill wayBill = wayBillRepository.findOne(Integer.valueOf(id));
				if  (wayBill.getSignStatus() == 1) {
					//创建物流信息
					TransitInfo transitInfo = new TransitInfo();
					//设置关联关系
					transitInfo.setWayBill(wayBill);
					transitInfo.setStatus("出入库中转");
					//保存
					transitInfoRepository.save(transitInfo);
					//更改运单信息
					wayBill.setSignStatus(2);//派送中
					//同步索引库
					wayBillIndexRepository.save(wayBill);
				}
			}
		}
	}

	/**
	 * 分页查询
	 */
	@Override
	public Page<TransitInfo> pageQuery(PageRequest pageRquest) {
		return transitInfoRepository.findAll(pageRquest);
	}

}
