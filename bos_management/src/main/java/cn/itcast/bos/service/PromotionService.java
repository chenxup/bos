package cn.itcast.bos.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.base.PageInfo;
import cn.itcast.bos.domain.base.Promotion;

@Produces("*/*")
public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> pageQuery(PageRequest pageRquest);

	/**
	 * 提供分页方法
	 */
	@GET
	@Path("/pageQuery")
	@Consumes({"application/xml", "appcalition/json"})
	@Produces({"application/xml", "appcalition/json"})
	PageInfo<Promotion> pageQuery(@QueryParam("page") Integer page, @QueryParam("size") Integer size);

}
