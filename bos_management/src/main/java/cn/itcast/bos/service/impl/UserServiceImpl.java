package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}                                           

	@Override
	public void save(User model,  String[] roleIds) {
		if (roleIds != null) {
			for (String id : roleIds) {
				Role role = new Role();                 
				role.setId(Integer.valueOf(id));
				model.getRoles().add(role);
			}
		}
		userRepository.save(model);
	}

}
