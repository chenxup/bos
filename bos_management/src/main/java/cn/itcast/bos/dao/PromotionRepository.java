package cn.itcast.bos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion,Integer>,JpaSpecificationExecutor<Promotion> {


}
