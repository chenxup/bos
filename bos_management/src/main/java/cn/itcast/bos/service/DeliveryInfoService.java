package cn.itcast.bos.service;

import cn.itcast.bos.domain.transit.DeliveryInfo;

public interface DeliveryInfoService {

	void save(DeliveryInfo model, String transitInfoId);


}
