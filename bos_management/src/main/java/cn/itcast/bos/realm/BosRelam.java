package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.dao.UserRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.PermissionService;
import cn.itcast.bos.service.RoleService;

public class BosRelam extends AuthorizingRealm {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RoleService roleService;
	
	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//授权
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//查询角色
		List<Role> roleList = roleService.findByUser(user);
		for (Role role : roleList) {
			authorizationInfo.addRole(role.getKeyword());
		}
		
		//查询权限
		List<Permission> list = permissionService.findByUser(user);
		for (Permission permission : list) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		return authorizationInfo;
	}

	// 认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken passwordToken = (UsernamePasswordToken) token;
		// 得到用户名
		String username = passwordToken.getUsername();
		// 从数据库中查询密码
		User user = userRepository.findByUsername(username);
		if (user != null) {
			// 第一个参数，就是放入subject中的用户，可以从subject中取出
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
					this.getName());
			return authenticationInfo;
		}
		return null;
	}

}
