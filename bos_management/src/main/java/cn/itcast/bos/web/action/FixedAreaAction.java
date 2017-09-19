package cn.itcast.bos.web.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.FixedAreaService;
import cn.itcast.bos.utils.Constants;
import cn.itcast.crm.domain.Customer;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {

	@Autowired
	private FixedAreaService fixedAreaService;

	/**
	 * 保存
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedArea_save", results={@Result(type="redirect",location="./pages/base/fixed_area.html")})
	public String save() throws Exception {
		fixedAreaService.save(this.model);
		return SUCCESS;
	}
	
	/**
	 * 分页
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedArea_pageQuery", results={@Result(type="json")})
	public String pageQuery() throws Exception {
		final FixedArea fixedArea = this.model;
		Specification<FixedArea> spec = new Specification<FixedArea>() {
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.isNotBlank(fixedArea.getId())) {
					Predicate p1 = cb.equal(root.get("id").as(String.class), fixedArea.getId());
					list.add(p1);
				}
				if (StringUtils.isNotBlank(fixedArea.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), fixedArea.getCompany());
					list.add(p2);
				}
				
				Predicate[] pary = new Predicate[list.size()];
				query.where(list.toArray(pary));
				return query.getRestriction();
			}
		};
		Page<FixedArea> page = fixedAreaService.pageQuery(spec, this.getPageRquest());
		this.java2Json(page);
		return SUCCESS;
	}
	
	/**
	 * 通过restful远程查询所有未关联到定区的客户
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedArea_noassociationCustomer", results={@Result(type="json")})
	public String noassociationCustomer() throws Exception {
		//创建连接
		WebClient client = WebClient.create(Constants.CRM_MANAGEMENT_HOST);
		//获取数据
		Collection<? extends Customer> customerList = client.path("services/customerService/customer")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		this.push(customerList);
		return SUCCESS;
	}
	
	/**
	 * 通过restful远程查询所有关联到指定定区的客户
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedArea_associationCustomer", results={@Result(type="json")})
	public String associationCustomer() throws Exception {
		//创建连接
		WebClient client = WebClient.create(Constants.CRM_MANAGEMENT_HOST);
		//获取数据
		Collection<? extends Customer> customerList = client.path("services/customerService/customer/"
				+this.model.getId()+"").accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		this.push(customerList);
		return SUCCESS;
	}
	
	private String[] customerIds;
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	
	
	/**
	 * 通过restful远程将客户关联到定区
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedArea_associationCustomersToFixedArea", results={@Result(type="redirect", location="./pages/base/fixed_area.html")})
	public String associationCustomersToFixedArea() throws Exception {
		//处理ids
		String strids = StringUtils.join(customerIds,",");
		String URL = Constants.CRM_MANAGEMENT_HOST 
				+ "/services/customerService/customer/?customerIdStr="+strids+
				"&fixedAreaId="+this.model.getId();
		//创建连接
		WebClient.create(URL).put(null);
		return SUCCESS;
	}
	
	private String courierId;
	private String takeTimeId;
	public void setCourierId(String courierId) {
		this.courierId = courierId;
	}
	public void setTakeTimeId(String takeTimeId) {
		this.takeTimeId = takeTimeId;
	}
	
	/**
	 * 关联快递员
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedArea_associationCourierToFixedArea", results={@Result(type="redirect",location="./pages/base/fixed_area.html")})
	public String associationCourierToFixedArea() throws Exception {
		fixedAreaService.associationCourierToFixedArea(this.model.getId(),courierId,takeTimeId);
		return SUCCESS;
	}

	
}
