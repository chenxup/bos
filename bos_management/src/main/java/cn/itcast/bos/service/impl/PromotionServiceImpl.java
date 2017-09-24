package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.PromotionRepository;
import cn.itcast.bos.domain.base.PageInfo;
import cn.itcast.bos.domain.base.Promotion;
import cn.itcast.bos.service.PromotionService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import utils.GetRandomcode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion model) throws Exception {
		//指定商品的id,当前毫秒值加上两位随机数
		Long idl = new Long(System.currentTimeMillis());
		Random random = new Random();
		String strid = idl.toString().substring(0, 4)+GetRandomcode.getCode(10, 99);
		Integer id = Integer.valueOf(strid);
		model.setId(id);
		promotionRepository.save(model);
		//生成ftl页面
		//指定版本
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		//指定模板
		configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/template")));
		Template template = configuration.getTemplate("promotion_detail.ftl");
		//将数据封装到map中
		Map map = new HashMap<>();
		map.put("promotion", model);
		String dir = ServletActionContext.getServletContext().getRealPath("/ftl")+File.separator+model.getId()+".html";
		//生成模板
		template.process(map, new FileWriter(dir));
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

	@Override
	public void updateType() {
		promotionRepository.updateType();
	}
	
	@Override
	public List<Promotion> findtest(){
		return promotionRepository.findtest();
	}

}
