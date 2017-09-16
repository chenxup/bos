package cn.itcast.test.springdata;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.StandardRespository;
import cn.itcast.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestSpringData {

	@Autowired
	private StandardRespository standardRespository;
	
	@Test
	public void test1() throws Exception {
		List<Standard> list = standardRespository.findByName("好货");
		System.out.println(list);
	}
	
	@Test
	public void test2() throws Exception {
		List<Standard> list = standardRespository.findAll();
		System.out.println(list);
	}
	
	@Test
	public void test3() throws Exception {
		List<Standard> list = standardRespository.QueryByName("好货");
		System.out.println(list);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void test4() throws Exception {
		standardRespository.updateName("30-40公斤", 1);
	}
	
	/**
	 * 分页
	 * @throws Exception
	 */
	@Test
	public void test5() throws Exception {
		Pageable pageable = new PageRequest(0, 2);
		Page<Standard> page = standardRespository.findAll(pageable);
		int totalPages = page.getTotalPages();
		System.out.println("总页数：" + totalPages);
		System.out.println("总记录数数：" + page.getTotalElements());
		List<Standard> list = page.getContent();
		System.out.println(list);
	}
	
	public static void main(String[] args) {
		System.out.println("ddf");
	}
	
}
