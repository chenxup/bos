package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {

	/**
	 * 废除
	 * @param id
	 */
	@Query(value="update Courier c set c.deltag = 1 where c.id=?", nativeQuery=false)
	@Modifying
	void updateById(Integer id);

	/**
	 * 查询所有关联到指定定区的去取派员
	 * @return
	 * @throws Exception
	 */
	@Query("from Courier c join fetch c.fixedAreas f where f.id=?")
	List<Courier> findAssoictionToFixedArea(String fixedAreaId);

}
