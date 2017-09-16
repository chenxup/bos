package cn.itcast.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

public interface StandardService {

	void save(Standard standard);

	List<Standard> findByName(String name);

	void delete(String ids);

	List<Standard> findAll();
	
	Page<Standard> pageQuery(Pageable pageable);

}
