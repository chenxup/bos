package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaRepository areaRepository;

	/**
	 * 批量保存
	 */
	public void save(List<Area> list){
		areaRepository.save(list);
	}
	
	/**
	 * 保存
	 * @param area
	 */
	public void save(Area area) {
		areaRepository.save(area);
	}

	/**
	 * 分页
	 * @param spec 
	 */
	public Page<Area> pageQuery(Specification<Area> spec, Pageable pageable) {
		return areaRepository.findAll(spec, pageable);
	}

    @Override
    public Area findAreaByProvinceAndCityAndDistrict(String areaInfo) {
        String[] info = areaInfo.split("/");
        Area area = areaRepository.findAreaByProvinceAndCityAndDistrict(info[0], info[1], info[2]);
        return area;
    }
}
