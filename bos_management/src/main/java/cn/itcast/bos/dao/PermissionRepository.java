package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Integer>,JpaSpecificationExecutor<Permission> {

	@Query("select distinct p from Permission p inner join p.roles r inner join r.users u where u.id=?")
	List<Permission> findByUser(Integer id);


}
