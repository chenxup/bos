package cn.itcast.bos.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.FixedAreaRepository;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	@Override
	public void save(FixedArea model) {
		model.setOperatingTime(new Date());
		fixedAreaRepository.save(model);
	}

	@Override
	public Page<FixedArea> pageQuery(Specification<FixedArea> spec, PageRequest pageRquest) {
		return fixedAreaRepository.findAll(spec, pageRquest);
	}

}
