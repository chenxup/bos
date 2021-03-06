package cn.itcast.bos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Area;

public interface AreaRepository extends JpaRepository<Area, Integer>, JpaSpecificationExecutor<Area> {
    Area findAreaByProvinceAndCityAndDistrict(String province, String city, String district);
}
