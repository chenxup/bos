package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.DeliveryInfoRepository;
import cn.itcast.bos.dao.TransitInfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.DeliveryInfoService;

@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {

	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;

	@Override
	public void save(DeliveryInfo model, String transitInfoId) {
		deliveryInfoRepository.save(model);
		//设置关联关系
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.valueOf(transitInfoId));
		transitInfo.setDeliveryInfo(model);
		//更新状态
		transitInfo.setStatus("开始配送");
	}

}
