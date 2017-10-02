package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.InOutStorageInfoRepository;
import cn.itcast.bos.dao.TransitInfoRepository;
import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.InOutStorageInfoService;

@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {

	@Autowired
	private InOutStorageInfoRepository inOutStorageInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;

	@Override
	public void save(InOutStorageInfo model, String transitInfoId) {
		inOutStorageInfoRepository.save(model);
		//更新出入库信息
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.valueOf(transitInfoId));
		//保存网点信息
		transitInfo.getInOutStorageInfos().add(model);
		
		//修改状态
		if ("到达网点".equals(model.getOperation())) {
			transitInfo.setStatus("到达网点");
			transitInfo.setOutletAddress(model.getAddress());
		}
	}

}
