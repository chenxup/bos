package cn.itcast.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.base.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> pageQuery(PageRequest pageRquest);


}
