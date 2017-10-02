package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.SignInfoRepository;
import cn.itcast.bos.dao.TransitInfoRepository;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.SignInfoService;

@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {

	@Autowired
	private SignInfoRepository signInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(SignInfo model, String transitInfoId) {
		signInfoRepository.save(model);
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.valueOf(transitInfoId));
		transitInfo.setSignInfo(model);

		// 更新状态
		if ("正常".equals(model.getSignType())) {
			transitInfo.setStatus("正常签收");
			// 更新运单状态
			transitInfo.getWayBill().setSignStatus(3);
			// 更新索引库
			wayBillIndexRepository.save(transitInfo.getWayBill());
		} else {
			// 异常
			transitInfo.setStatus("异常");
			// 更改运单状态（4：表示异常）
			transitInfo.getWayBill().setSignStatus(4);
			// 更改索引库（只要运单WayBill的数据发生改变，都需要更新索引库）
			wayBillIndexRepository.save(transitInfo.getWayBill());
		}

	}

}
