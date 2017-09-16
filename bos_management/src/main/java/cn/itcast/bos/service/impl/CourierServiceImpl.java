package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRepository courierRepository;
	
	/**
	 * 保存
	 */
	public void save(Courier courier) {
		courier.setDeltag('0');
		courierRepository.save(courier);
	}


	/**
	 * 逻辑批量删除
	 */
	public void delBatch(String ids) {
		String[] strIds = ids.split(",");
		for (String id : strIds) {
			courierRepository.updateById(Integer.parseInt(id));
		}
	}

	@Override
	public Page<Courier> pageQuery(Specification<Courier> spec, PageRequest pageRquest) {
		return courierRepository.findAll(spec, pageRquest);
	}

}
