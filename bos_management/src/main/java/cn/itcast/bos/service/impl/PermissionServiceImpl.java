package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	/**
	 * 根据用户查询权限
	 */
	@Override
	public List<Permission> findByUser(User user) {
		if ("admin".equals(user.getUsername())) {
			return permissionRepository.findAll();
		}
		return permissionRepository.findByUser(user.getId());
	}

}
