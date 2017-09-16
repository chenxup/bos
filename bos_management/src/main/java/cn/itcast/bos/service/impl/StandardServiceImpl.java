package cn.itcast.bos.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.StandardRespository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	@Autowired
	private StandardRespository standardRespository;

	/**
	 * 保存
	 */
	public void save(Standard standard) {
		standard.setOperatingTime(new Date());
		standardRespository.save(standard);
	}

	/**
	 * 根据用户名查询
	 */
	public List<Standard> findByName(String name) {
		List<Standard> list = standardRespository.findByName(name);
		return list;
	}


	/**
	 * 批量逻辑删除
	 * 
	 */
	public void delete(String ids) {
		String[] strId = ids.split(",");
		for (String id : strId) {
			standardRespository.delete(Integer.parseInt(id));
		}
	}

	/**
	 * 查询所有 
	 */
	public List<Standard> findAll() {
		return standardRespository.findAll();
	}

	@Override
	public Page<Standard> pageQuery(Pageable pageable) {
		return standardRespository.findAll(pageable);
	}

}
