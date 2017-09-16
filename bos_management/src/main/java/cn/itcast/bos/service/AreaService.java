package cn.itcast.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Area;

public interface AreaService {
	void save(List<Area> list);
	void save(Area area);
	Page<Area> pageQuery(Specification<Area> spec, Pageable pageable);
}
