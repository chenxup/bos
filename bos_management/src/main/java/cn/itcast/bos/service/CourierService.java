package cn.itcast.bos.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier courier);

	void delBatch(String ids);

	Page<Courier> pageQuery(Specification<Courier> spec, PageRequest pageRquest);

	List<Courier> findnoassociation();

	List<Courier> findAssoictionToFixedArea(String fixedAreaId);

}
