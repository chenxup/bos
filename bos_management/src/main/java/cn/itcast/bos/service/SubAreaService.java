package cn.itcast.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.SubArea;

public interface SubAreaService {

	void save(List<SubArea> list);

	Page<SubArea> queryPage(Pageable pageable);


}
