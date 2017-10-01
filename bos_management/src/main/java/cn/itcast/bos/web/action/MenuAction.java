package cn.itcast.bos.web.action;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.MenuService;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {

	@Autowired
	private MenuService menuService;
	
	/**
	 * 查询所有
	 * @return
	 * @throws Exception
	 */
	@Action(value="menu_pageQuery", results={@Result(type="json")})
	public String pageQuery() throws Exception {
		List<Menu> list = menuService.findAll();
		this.push(list);
		return SUCCESS;
	}
	
	@Action(value="menu_save", results={@Result(type="redirect", location="./pages/system/menu.html")})
	public String save() throws Exception {
		menuService.save(this.model);
		return SUCCESS;
	}
	
	@Action(value="menu_showMenu", results={@Result(type="json")})
	public String showMenu() throws Exception {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		List<Menu> list = menuService.showMenu(user);
		this.push(list);
		return SUCCESS;
	}
}
