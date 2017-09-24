package cn.itcast.bos.service;

import cn.itcast.bos.domain.base.PageInfo;
import cn.itcast.bos.domain.base.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces("*/*")
public interface PromotionService {

	void save(Promotion model) throws Exception;

	Page<Promotion> pageQuery(PageRequest pageRquest);

	/**
	 * 提供分页方法
	 */
	@GET
	@Path("/pageQuery")
	@Consumes({MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON})
	PageInfo<Promotion> pageQuery(@QueryParam("page") Integer page, @QueryParam("size") Integer size);

	void updateType();
	
	
	List<Promotion> findtest();

}
