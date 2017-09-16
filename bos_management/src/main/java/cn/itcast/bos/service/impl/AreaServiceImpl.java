package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaRepository areaRepository;

	/**
	 * 批量保存
	 */
	public void save(List<Area> list){
		areaRepository.save(list);
	}
	
	/**
	 * 保存
	 * @param area
	 */
	public void save(Area area) {
		areaRepository.save(area);
	}

	/**
	 * 分页
	 * @param spec 
	 */
	public Page<Area> pageQuery(Specification<Area> spec, Pageable pageable) {
		return areaRepository.findAll(spec, pageable);
	}
	
	
}
