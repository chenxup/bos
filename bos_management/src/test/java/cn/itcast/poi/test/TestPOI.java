package cn.itcast.poi.test;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import cn.itcast.bos.utils.PinYin4jUtils;

public class TestPOI {

	@Test
	public void test1() throws Exception {
		// 加载文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(new File(
				"E:\\java黑马\\bos\\第4天：bosv2.0_day04_取派员和批量数据导入导出、Apache POI\\资料\\新BOS项目资料_chapter04_04_区域测试数据\\正式\\area.xls")));
		// 获得第一个sheet
		HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
		// 遍历sheet
		for (Row row : sheetAt) {
			// 获得每一个单元格
			// 获得区域编号
			if (row.getRowNum() == 0) {
				continue;
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

			System.out.println(id + " " + province + " " + city + " " + district + " " + postcode + " " + shortcode + " " + citycode);

		}
	}

}
