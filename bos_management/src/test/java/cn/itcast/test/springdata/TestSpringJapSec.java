package cn.itcast.test.springdata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.dao.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestSpringJapSec {

	@Autowired
	private CourierRepository courierRepository;
	
	@Test
	public void test1() throws Exception {
		Specification<Courier> spec = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p1 = cb.equal(root.get("name").as(String.class), "低调");
				Predicate p2 = cb.equal(root.get("courierNum").as(String.class), "001");
				//多变联查
				Join<Courier, Standard> join = root.join("standard", JoinType.INNER);
				Predicate p3 = cb.equal(join.get("id").as(Integer.class), 3);
				
				ArrayList<Object> plist = new ArrayList<>();
				plist.add(p1);
				plist.add(p2);
				plist.add(p3);
				Predicate[] pary = new Predicate[plist.size()];
				Predicate pre = cb.and(plist.toArray(pary));
				return pre;
			}
		};
		List<Courier> list = courierRepository.findAll(spec);
		System.out.println(list);
	}
	
	@Test
	public void test2() throws Exception {
		Specification<Courier> spec = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p1 = cb.equal(root.get("name").as(String.class), "低调");
				Predicate p2 = cb.equal(root.get("courierNum").as(String.class), "001");
				//多表联查
				Join<Courier, Standard> join = root.join("standard", JoinType.INNER);
				Predicate p3 = cb.equal(join.get("id").as(Integer.class), 3);
				
				ArrayList<Object> plist = new ArrayList<>();
				plist.add(p1);
				plist.add(p2);
				plist.add(p3);
				Predicate[] pary = new Predicate[plist.size()];
				
				//使用CriteriaQuery,加上排序
				query.where(plist.toArray(pary));
				query.orderBy(cb.desc(root.get("id").as(Integer.class)));
				
				return query.getRestriction();
			}
		};
		List<Courier> list = courierRepository.findAll(spec);
		System.out.println(list);
	}
	
}
