package cn.itcast.bos.web.action;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.CourierService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends BaseAction<Courier> {

	private Courier courier = new Courier();
	
	@Autowired
	private CourierService courierService;
	
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 */
	@Action(value="courier_save", results={@Result(name="success", type="redirect", location="./pages/base/courier.html")})
	public String save() throws Exception {
		courierService.save(courier);
		return SUCCESS;
	}
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="courier_pageQuery", results={@Result(name="success", type="json")})
	public String pageQuery() throws Exception {
		final Courier courier = this.model;
		//封装条件
		Specification<Courier> spec = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				ArrayList<Predicate> plist = new ArrayList<>();
				String courierNum = courier.getCourierNum();
				if (StringUtils.isNotBlank(courierNum)) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
					plist.add(p1);
				}
				String company = courier.getCompany();
				if (StringUtils.isNotBlank(company)) {
					Predicate p2 = cb.equal(root.get("company").as(String.class), company);
					plist.add(p2);
				}
				String type = courier.getType();
				if (StringUtils.isNotBlank(type)) {
					Predicate p3 = cb.equal(root.get("type").as(String.class), type);
					plist.add(p3);
				}
				
				//多表查询
				Join<Courier, Standard> join = root.join("standard", JoinType.INNER);
				if (courier.getStandard() != null && courier.getStandard().getId() != null && StringUtils.isNotBlank(courier.getStandard().getId().toString())){
					Predicate p4 = cb.equal(join.get("id"), courier.getStandard().getId());
					plist.add(p4);
				}
				
				Predicate[] pary = new Predicate[plist.size()];
				Predicate predicate = cb.and(plist.toArray(pary));
				return predicate;
			}
		};
		Page<Courier> pageInfo = courierService.pageQuery(spec, this.getPageRquest());
		this.java2Json(pageInfo);
		return SUCCESS;
	}
	
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**
	 * 批量删除
	 */
	@Action(value="courier_delBatch", results={@Result(name="success", type="redirect", location="./pages/base/courier.html")})
	public String delBatch() throws Exception {
		courierService.delBatch(ids);
		return SUCCESS;
	}

}
