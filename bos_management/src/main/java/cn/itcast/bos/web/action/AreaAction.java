package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.AreaService;
import utils.PinYin4jUtils;

/**
 * 取派标准
 * 
 * @author HOC
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {
	
	@Autowired
	private AreaService areaService;
	
	private File file;
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * 批量上传
	 */
	@Action(value = "area_impXls")
	public String save() throws Exception {
		
		long start = System.currentTimeMillis();
		// 加载文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 获得第一个sheet
		HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
		int j = 0;
		List<Area> list = new ArrayList<Area>();
		
		// 遍历sheet
		for (Row row : sheetAt) {
			// 获得每一个单元格
			// 获得区域编号
			if (row.getRowNum() == 0) {
				continue;
			}
			if (j % 50 == 0) {
				areaService.save(list);
				list.clear();
			}

			Cell c1 = row.getCell(0);
			String id = c1.getStringCellValue();
			// 获得省
			Cell c2 = row.getCell(1);
			String province = c2.getStringCellValue();
			// 城市
			Cell c3 = row.getCell(2);
			String city = c3.getStringCellValue();
			// 区域
			Cell c4 = row.getCell(3);
			String district = c4.getStringCellValue();
			// 邮编
			Cell c5 = row.getCell(4);
			String postcode = c5.getStringCellValue();
			//封装到area中
			Area area = new Area();
			area.setCity(city);
			area.setId(id);
			area.setProvince(province);
			area.setDistrict(district);
			area.setPostcode(postcode);
			
			// 去掉最后一位
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			
			String shortcode = province + city + district;
			//简码
			String[] shortcodeAry = PinYin4jUtils.getHeadByString(shortcode);
			shortcode = StringUtils.join(shortcodeAry);
			//城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			//封装
			area.setShortcode(shortcode);
			area.setCitycode(citycode);
			list.add(area);
			j++;
		}
		
		areaService.save(list);
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000);
		return NONE;
	}
	
	/**
	 * 条件查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="area_pageQuery", results={@Result(type="json")})
	public String pageQuery() throws Exception {
		final Area area = this.model;
		//封装条件
		Specification<Area> spec = new Specification<Area>() {
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> plist = new ArrayList<Predicate>();
				//省
				if (StringUtils.isNotBlank(area.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+area.getProvince()+"%");
					plist.add(p1);
				}
				//市
				if (StringUtils.isNotBlank(area.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+area.getCity()+"%");
					plist.add(p2);
				}
				//区
				if (StringUtils.isNotBlank(area.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+area.getDistrict()+"%");
					plist.add(p3);
				}
				Predicate[] pary = new Predicate[plist.size()];
				query.where(plist.toArray(pary));
				//按照id升序查询
				query.orderBy(cb.asc(root.get("id").as(Integer.class)));
				return query.getRestriction();
			}
		};
		
		Page<Area> pageInfo = areaService.pageQuery(spec, this.getPageRquest());
		this.java2Json(pageInfo);
		return SUCCESS;
	}

}
