package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion,Integer>,JpaSpecificationExecutor<Promotion> {

	@Query(value="update T_PROMOTION t set t.c_status='2' where t.c_end_date < sysdate", nativeQuery=true)
	@Modifying
	void updateType();
	
	@Query(value="select t.* from T_PROMOTION t where t.c_end_date < sysdate", nativeQuery=true)
	List<Promotion> findtest();

}
