package cn.itcast.bos.service.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRepository courierRepository;
	
	/**
	 * 保存
	 */
	public void save(Courier courier) {
		courier.setDeltag('0');
		courierRepository.save(courier);
	}


	/**
	 * 逻辑批量删除
	 */
	public void delBatch(String ids) {
		String[] strIds = ids.split(",");
		for (String id : strIds) {
			courierRepository.updateById(Integer.parseInt(id));
		}
	}

	/**
	 * 分页查询
	 */
	public Page<Courier> pageQuery(Specification<Courier> spec, PageRequest pageRquest) {
		return courierRepository.findAll(spec, pageRquest);
	}


	
	/**
	 * 查询所有为未废除且未关联到定区的取派员
	 */
	public List<Courier> findnoassociation() {
		Specification<Courier> spec = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//未关联到定区的取派员
				Predicate p1 = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				//没有废除的取派员
				Predicate p2 = cb.equal(root.get("deltag").as(Character.class), '0');
				query.where(cb.and(p1,p2));
				return query.getRestriction();
			}
		};
		return courierRepository.findAll(spec);
	}


	
	/**
	 * 查询所有关联到指定定区的去取派员
	 * @return
	 * @throws Exception
	 */
	public List<Courier> findAssoictionToFixedArea(String fixedAreaId) {
		return courierRepository.findAssoictionToFixedArea(fixedAreaId);
	}

}
