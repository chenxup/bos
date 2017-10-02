package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.TransitInfoService;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {

	@Autowired
	private TransitInfoService transitInfoService;
	
	private String wayBillIds;
	public void setWayBillIds(String wayBillIds) {
		this.wayBillIds = wayBillIds;
	}
	
	/**
	 * 生成中转物流信息
	 * @return
	 * @throws Exception
	 */
	@Action(value="transit_create", results={@Result(type="json")})
	public String create() throws Exception {
		Map map = new HashMap();
		try {
			transitInfoService.create(wayBillIds);
			map.put("success", true);
			map.put("msg", "中转成功");
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "中转失败");
		}
		this.push(map);
		return SUCCESS;
	}
	
	@Action(value="transitinfo_pageQuery", results={@Result(type="json")})
	public String pageQuery() throws Exception {
		Page<TransitInfo> pageInfo = transitInfoService.pageQuery(this.getPageRquest());
		this.java2Json(pageInfo);
		return SUCCESS;
	}
	
}
