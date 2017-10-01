package cn.itcast.bos.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer> {

}
