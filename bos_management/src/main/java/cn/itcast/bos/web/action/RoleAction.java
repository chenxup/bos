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
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.RoleService;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	@Autowired
	private RoleService roleService;
	
	@Action(value="role_list", results={@Result(type="json")})
	public String list() throws Exception {
		List<Role> list = roleService.findAll();
		this.push(list);
		return SUCCESS;
	}
	
	private String menuIds;
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	private String[] permissionIds;
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}
	
	@Action(value="role_save", results={@Result(type="redirect", location="./pages/system/role.html")})
	public String save() throws Exception {
		roleService.save(this.model, menuIds, permissionIds);
		return SUCCESS;
	}
}
