package cn.itcast.bos.service;

import cn.itcast.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoService {

	void save(InOutStorageInfo model, String transitInfoId);


}
