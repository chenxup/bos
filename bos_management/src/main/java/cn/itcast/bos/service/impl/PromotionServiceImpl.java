package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.PromotionRepository;
import cn.itcast.bos.domain.base.PageInfo;
import cn.itcast.bos.domain.base.Promotion;
import cn.itcast.bos.service.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion model) {
		promotionRepository.save(model);
	}

	@Override
	public Page<Promotion> pageQuery(PageRequest pageRquest) {
		return promotionRepository.findAll(pageRquest);
	}

	@Override
	public PageInfo<Promotion> pageQuery(Integer page, Integer size) {
		Page<Promotion> pagecount = promotionRepository.findAll(new PageRequest(page-1, size));
		PageInfo<Promotion> pageInfo = new PageInfo<>();
		pageInfo.setTotalCount(pagecount.getTotalElements());
		pageInfo.setPageData(pagecount.getContent());
		return pageInfo;
	}

}
