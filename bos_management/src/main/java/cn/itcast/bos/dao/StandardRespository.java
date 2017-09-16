package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRespository extends JpaRepository<Standard, Integer>{

	/**
	 * 根据用户名查询
	 * @param name
	 * @return
	 */
	List<Standard> findByName(String name);
	
	/**
	 * 自定义查询语句
	 * @param name
	 * @return
	 */
	@Query(value="from Standard s where s.name=?", nativeQuery=false)
	List<Standard> QueryByName(String name);
	
	/**
	 * 修改名称
	 * @param name
	 */
	@Query(value="update Standard s set s.name=? where id = ?",nativeQuery=false)
	@Modifying
	void updateName(String name, Integer id);

}
