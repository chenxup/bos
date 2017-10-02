package cn.itcast.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.transit.TransitInfo;

public interface TransitInfoService {

	void create(String wayBillIds);

	Page<TransitInfo> pageQuery(PageRequest pageRquest);


}
