package cn.itcast.bos.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.FixedAreaService;

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
}
