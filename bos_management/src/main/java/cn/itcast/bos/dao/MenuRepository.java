package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Menu;

public interface MenuRepository extends JpaRepository<Menu,Integer>,JpaSpecificationExecutor<Menu> {

	@Query("select distinct m from Menu m inner join m.roles r inner join r.users u where u.id=? order by m.priority")
	List<Menu> showMenu(Integer id);

}
