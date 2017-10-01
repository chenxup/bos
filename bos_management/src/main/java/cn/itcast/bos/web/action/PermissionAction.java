package cn.itcast.bos.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.PermissionService;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission> {

	@Autowired
	private PermissionService permissionService;
	
	/**
	 * 查询所有
	 * @return
	 * @throws Exception
	 */
	@Action(value="permission_list", results={@Result(type="json")})
	public String list() throws Exception {
		List<Permission> list = permissionService.findAll();
		this.push(list);
		return SUCCESS;
	}
	
	@Action(value="permission_save", results={@Result(type="redirect", location="./pages/system/permission.html")})
	public String save() throws Exception {
		permissionService.save(this.model);
		return SUCCESS;
	}
}
