package cn.itcast.bos.web.action;


import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.StandardService;

/**
 * 取派标准
 * @author HOC
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class StandardAction extends BaseAction<Standard>{

	@Autowired
	private StandardService standardService;

	/**
	 * 保存
	 */
	@Action(value="standard_save", results={@Result(name="success", type="redirect", location="./pages/base/standard.html")})
	public String save() throws Exception {
		standardService.save(this.model);
		return SUCCESS;
	}
	
	/**
	 * 验证用户名是否重复
	 */
	@Action(value="stardard_validate", results={@Result(name="success", type="json")})
	public String checkName() throws Exception {
		List<Standard> list = standardService.findByName(this.model.getName());
		if (list != null && list.size() == 0) {
			ActionContext.getContext().getValueStack().push(true);
		} else {
			ActionContext.getContext().getValueStack().push(false);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="standard_pageQuery", results={@Result(name="success", type="json")})
	public String pageQuery() throws Exception {
		Page<Standard> pageInfo = standardService.pageQuery(this.getPageRquest());
		this.java2Json(pageInfo);
		return SUCCESS;
	}
	
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**
	 * 批量删除
	 */
	@Action(value="standard_delete", results={@Result(name="success", type="redirect", location="./pages/base/standard.html")})
	public String delete() throws Exception {
		standardService.delete(ids);
		return SUCCESS;
	}
	
	/**
	 * 查询所有
	 */
	@Action(value="standard_findAll", results={@Result(name="success", type="json")})
	public String findAll() throws Exception {
		List<Standard> list = standardService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	
	
}
