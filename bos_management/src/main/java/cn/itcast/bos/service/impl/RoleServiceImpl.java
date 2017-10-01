package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * 根据用户查询角色
	 */
	@Override
	public List<Role> findByUser(User user) {
		if ("admin".equals(user.getUsername())) {
			return roleRepository.findAll();
		}
		return roleRepository.findByUser(user.getId());
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public void save(Role model, String menuIds, String[] permissionIds) {
		//关联权限
		for (String permissionId : permissionIds) {
			Permission permission = new Permission();
			permission.setId(Integer.valueOf(permissionId));
			model.getPermissions().add(permission);
		}
		//关联菜单
		String[] menuAry = menuIds.split(",");
		for (String menuId : menuAry) {
			Menu menu = new Menu();
			menu.setId(Integer.valueOf(menuId));
			model.getMenus().add(menu);
		}
		roleRepository.save(model);
	}

}
