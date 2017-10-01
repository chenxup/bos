package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.RoleRepository;
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

}
