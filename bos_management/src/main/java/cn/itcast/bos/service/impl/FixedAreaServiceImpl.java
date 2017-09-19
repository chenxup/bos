package cn.itcast.bos.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.CourierRepository;
import cn.itcast.bos.dao.FixedAreaRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private CourierRepository courierRepository;

	@Override
	public void save(FixedArea model) {
		model.setOperatingTime(new Date());
		fixedAreaRepository.save(model);
	}

	@Override
	public Page<FixedArea> pageQuery(Specification<FixedArea> spec, PageRequest pageRquest) {
		return fixedAreaRepository.findAll(spec, pageRquest);
	}

	/**
	 * 关联取派员
	 */
	public void associationCourierToFixedArea(String id, String courierId, String takeTimeId) {
		FixedArea fixedArea = fixedAreaRepository.findOne(id);
		Courier courier = courierRepository.findOne(Integer.valueOf(courierId));
		//定区关联联系人
		fixedArea.getCouriers().add(courier);
		TakeTime takeTime = new TakeTime();
		takeTime.setId(Integer.valueOf(takeTimeId));
		//联系人关联收牌时间
		courier.setTakeTime(takeTime);
	}

}
