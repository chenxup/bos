package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill,Integer>,JpaSpecificationExecutor<WayBill> {

	WayBill findByWayBillNum(String wayBillNum);

	@Query(value="select count(t.c_sign_status) from t_way_bill t group by t.c_sign_status order by t.c_sign_status", nativeQuery=true)
	List<Object> findCountWayBill();


}
