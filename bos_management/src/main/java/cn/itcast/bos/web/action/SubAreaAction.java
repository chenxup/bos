package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.SubAreaService;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {

	@Autowired
	private SubAreaService subAreaService;
	
	private File file;
	public void setFile(File file) {
		this.file = file;
	}
	
	@Action("SubArea_impXls")
	public String impxls() throws Exception {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
		List<SubArea> list = new ArrayList<>();
		
		for (Row row : sheetAt) {
			if (row.getRowNum() == 0) {
				continue;
			}
			if (row.getCell(0) != null && StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			
			String id = row.getCell(0).getStringCellValue();
			String fixedAreaId = row.getCell(1).getStringCellValue();
			String areaId = row.getCell(2).getStringCellValue();
			String keyWords = row.getCell(3).getStringCellValue();
			String startNum = row.getCell(4).getStringCellValue();
			String endNum = row.getCell(5).getStringCellValue();
			String single = row.getCell(6).getStringCellValue();
			String assistKeyWords = row.getCell(7).getStringCellValue();
			//封装到SubArea中
			SubArea subArea = new SubArea();
			subArea.setId(id);
			//关联定区
			FixedArea fixedArea = new FixedArea();
			fixedArea.setId(fixedAreaId);
			subArea.setFixedArea(fixedArea);
			//关联分区
			Area area = new Area();
			area.setId(areaId);
			subArea.setArea(area);
			
			subArea.setKeyWords(keyWords);
			subArea.setStartNum(startNum);
			subArea.setEndNum(endNum);
			subArea.setSingle(single.charAt(0));
			subArea.setAssistKeyWords(assistKeyWords);
			list.add(subArea);
		}
		subAreaService.save(list);
		return NONE;
	}
	
	@Action(value="SubArea_findAll", results={@Result(type="json")})
	public String findAll() throws Exception {
		Page<SubArea> pageInfo = subAreaService.queryPage(this.getPageRquest());
		this.java2Json(pageInfo);
		return SUCCESS;
	}
	
}
