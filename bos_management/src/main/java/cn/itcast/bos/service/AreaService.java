package cn.itcast.bos.service;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.ws.rs.QueryParam;
import java.util.List;

public interface AreaService {
	void save(List<Area> list);
	void save(Area area);
	Page<Area> pageQuery(Specification<Area> spec, Pageable pageable);

    public Area findAreaByProvinceAndCityAndDistrict(@QueryParam("areaInfo") String areaInfo);
}
