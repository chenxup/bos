package cn.itcast.bos.web.action;


import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.UserService;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

	@Autowired
	private UserService userService;
	
	//验证码
	private String checkCode;
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	/**
	 * 登录
	 * @return
	 * @throws Exception
	 */
	@Action(value="user_login", results={@Result(name="success", type="redirect", location="index.html")
		,@Result(name="input", type="redirect", location="login.html") })
	public String login() throws Exception {
		//验证用户名和密码是否为空
		if (StringUtils.isBlank(this.model.getUsername()) || StringUtils.isBlank(this.model.getPassword())) {
			System.out.println("用户名或密码不能为空！");
			return INPUT;
		}
		
		//验证验证码是否正确
		/*String code = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
		if (code == null) {
			System.out.println("验证码过时，请重新登录");
			return INPUT;
		}
		
		if (!code.equals(checkCode)) {
			System.out.println("验证码错误");
			return INPUT;
		}*/
		
		try {
			//将用户名和密码封装到subject中
			Subject subject = SecurityUtils.getSubject();
			//将密码进行md5加密
			this.model.setPassword(DigestUtils.md5Hex(this.model.getPassword().getBytes()));
			AuthenticationToken authenticationToken = new UsernamePasswordToken(this.model.getUsername(), this.model.getPassword());
			subject.login(authenticationToken);
			
			//保存用户到session中
			User user = (User) subject.getPrincipal();
			ServletActionContext.getRequest().getSession().setAttribute("logUser", user);
		} catch (UnknownAccountException e) { //如果在releam中返回null，就会有这个异常。。
            this.addActionError("用户名不存在");
            return INPUT;
        } catch (IncorrectCredentialsException e) { //如果密码比较错误就会抛出这个异常。
            this.addActionError("密码不正确");
            e.printStackTrace();
            return INPUT;
        }
		
		return SUCCESS;
	}
	
	/**
	 * 注销
	 */
	@Action(value="user_logOut", results={@Result(type="redirect", location="login.html")})
	public String logOut() throws Exception {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		ServletActionContext.getRequest().getSession().removeAttribute("logUser");
		return SUCCESS;
	}
	
	/**
	 * 查询所有
	 * @return
	 * @throws Exception
	 */
	@Action(value="user_list", results={@Result(type="json")})
	public String list() throws Exception {
		List<User> list = userService.findAll();
		this.push(list);
		return SUCCESS;
	}
	
	private String[] roleIds;
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	
	@Action(value="user_save", results={@Result(type="redirect", location="./pages/system/userlist.html")})
	public String save() throws Exception {
		userService.save(this.model, roleIds);
		return SUCCESS;
	}
	
}
