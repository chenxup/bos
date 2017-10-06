package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.WayBillService;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import utils.FileUtils;

@ParentPackage(value = "json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {

    @Autowired
    private WayBillService wayBillService;

    private static final Logger Logger = LoggerFactory.getLogger(WayBillAction.class);

    /**
     * 保存运单
     *
     * @return
     * @throws Exception
     */
    @Action(value = "wayBill_save", results = {@Result(type = "json")})
    public String save() throws Exception {
        Map map = new HashMap();

        try {
            wayBillService.save(this.model);
            map.put("success", true);
            //将数据写入日志
            Logger.info("运单保存成功，运单号为" + this.model.getWayBillNum());
        } catch (Exception e) {
            map.put("success", false);
            //将数据写入日志
            Logger.info("运单保存失败，运单号为" + this.model.getWayBillNum());
        }

        this.push(map);

        return SUCCESS;
    }

    /**
     * 分页查询
     * @return
     * @throws Exception
     */
    @Action(value = "wayBill_pageQuery", results = {@Result(type = "json")})
    public String pageQuery() throws Exception {
        //根据id降序
        PageRequest pageable = new PageRequest(this.getPage() - 1, this.getRows(), new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
        //封装查询条件
        Page<WayBill> page = wayBillService.pageQuery(model,pageable);
        this.java2Json(page);
        return SUCCESS;
    }

    /**
     * 根据运单编号查询运单
     * @return
     * @throws Exception
     */
    @Action(value = "wayBill_findByWayBillNum", results = {@Result(type = "json")})
    public String findByWayBillNum() throws Exception {
        WayBill wayBill = wayBillService.findByWayBillNum(this.model.getWayBillNum());
        this.push(wayBill);
        return SUCCESS;
    }
    
    /**
     * 根据条件导出execle表格
     * @return
     * @throws Exception
     */
    @Action("report_exportXls")
    public String exportXls() throws Exception {
    	//查询
    	List<WayBill> list = wayBillService.findWayBills(this.model);
    	//创建execle表格
    	HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    	HSSFSheet sheet = hssfWorkbook.createSheet();
    	//设置第一行
    	HSSFRow headRow = sheet.createRow(0);
    	
    	//创建一个样式对象
    	HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
    	//设置样式为粗体
    	HSSFFont font = hssfWorkbook.createFont();
    	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    	//设置颜色为红色
    	short color = HSSFColor.RED.index;
    	font.setColor(color);
    	cellStyle.setFont(font);
    	//设置列宽
    	sheet.setColumnWidth(0, 6000);
    	sheet.setColumnWidth(1, 6000);
    	sheet.setColumnWidth(2, 6000);
    	sheet.setColumnWidth(3, 6000);
    	sheet.setColumnWidth(4, 6000);
    	sheet.setColumnWidth(5, 6000);
    	sheet.setColumnWidth(6, 6000);
    	
		HSSFCell createCell0 = headRow.createCell(0);
		HSSFCell createCell1 = headRow.createCell(1);
		HSSFCell createCell2 = headRow.createCell(2);
		HSSFCell createCell3 = headRow.createCell(3);
		HSSFCell createCell4 = headRow.createCell(4);
		HSSFCell createCell5 = headRow.createCell(5);
		HSSFCell createCell6 = headRow.createCell(6);
		
		//设置样式
		createCell0.setCellStyle(cellStyle);
		createCell1.setCellStyle(cellStyle);
		createCell2.setCellStyle(cellStyle);
		createCell3.setCellStyle(cellStyle);
		createCell4.setCellStyle(cellStyle);
		createCell5.setCellStyle(cellStyle);
		createCell6.setCellStyle(cellStyle);

		createCell0.setCellValue("运单号");
		createCell1.setCellValue("寄件人");
		createCell2.setCellValue("寄件人电话");
		createCell3.setCellValue("寄件人地址");
		createCell4.setCellValue("收件人");
		createCell5.setCellValue("收件人电话");
		createCell6.setCellValue("收件人地址");
		
		//遍历查询出来的数据
		for (WayBill wayBill : list) {
			HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
			row.createCell(0).setCellValue(wayBill.getWayBillNum());
			row.createCell(1).setCellValue(wayBill.getSendName());
			row.createCell(2).setCellValue(wayBill.getSendMobile());
			row.createCell(3).setCellValue(wayBill.getSendAddress());
			row.createCell(4).setCellValue(wayBill.getRecName());
			row.createCell(5).setCellValue(wayBill.getRecMobile());
			row.createCell(6).setCellValue(wayBill.getRecAddress());
		}
		
		//设置返回的类型
		String filename = "运单数据.xls";
		//得到类型
		String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
		ServletActionContext.getResponse().setContentType(mimeType);
		//将文件名进行编码
		String agent = ServletActionContext.getRequest().getHeader("User-Agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		
		//设置返回流
		ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment;filename=" + filename);
		
		hssfWorkbook.write(ServletActionContext.getResponse().getOutputStream());
		hssfWorkbook.close();
		
    	return NONE;
    }

    /**
     * 使用itext根据条件导出pdf
     * @return
     * @throws Exception
     */
    @Action("report_exportPdf")
    public String exportPdf() throws Exception {
		// 查询出 满足当前条件 结果数据
		List<WayBill> wayBills = wayBillService.findWayBills(model);

		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType("application/pdf");
		String filename = "运单数据.pdf";
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		// 设置以附件的形式导出
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + filename);

		// 生成PDF文件
		Document document = new Document();
		PdfWriter.getInstance(document, ServletActionContext.getResponse().getOutputStream());
		document.open();
		// 写PDF数据
		// 向document 生成pdf表格
		Table table = new Table(7);//创建7列的表格
		table.setWidth(80); // 宽度
		table.setBorder(1); // 边框
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式

		/**设置表格属性*/
		table.setBorder(1);//设置边框大小
		table.setWidth(80);//设置表格宽度
		table.setBorderColor(new java.awt.Color(0, 0, 255)); //将边框的颜色设置为蓝色
	    table.setPadding(5);//设置表格与字体间的间距
	    //table.setSpacing(5);//设置表格上下的间距
		table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式

		// 设置表格字体
		BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",false);
		Font font = new Font(cn, 10, Font.NORMAL, java.awt.Color.RED);

		// 写表头
		table.addCell(buildCell("运单号", font));
		table.addCell(buildCell("寄件人", font));
		table.addCell(buildCell("寄件人电话", font));
		table.addCell(buildCell("寄件人地址", font));
		table.addCell(buildCell("收件人", font));
		table.addCell(buildCell("收件人电话", font));
		table.addCell(buildCell("收件人地址", font));
		// 写数据
		for (WayBill wayBill : wayBills) {
			table.addCell(buildCell(wayBill.getWayBillNum(), font));
			table.addCell(buildCell(wayBill.getSendName(), font));
			table.addCell(buildCell(wayBill.getSendMobile(), font));
			table.addCell(buildCell(wayBill.getSendAddress(), font));
			table.addCell(buildCell(wayBill.getRecName(), font));
			table.addCell(buildCell(wayBill.getRecMobile(), font));
			table.addCell(buildCell(wayBill.getRecAddress(), font));
		}
		// 将表格加入文档
		document.add(table);

		document.close();

		return NONE;
	}

    private Cell buildCell(String content, Font font)
    		throws BadElementException {
    	Phrase phrase = new Phrase(content, font);
    	return new Cell(phrase);
    }
    
    /**
     * 使用iReport根据条件导出pdf
     * @return
     * @throws Exception
     */
    @Action("report_exportJasperPdf")
    public String exportJasperPdf() throws Exception {
    	// 查询出 满足当前条件 结果数据
		List<WayBill> wayBills = wayBillService.findWayBills(model);

		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType("application/pdf");
		String filename = "运单数据jasper.pdf";
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition","attachment;filename=" + filename);

		// 根据 jasperReport模板 生成pdf
		// 读取jasper模板文件
		String jrxml = ServletActionContext.getServletContext().getRealPath("/WEB-INF/jasper/waybillcondition.jrxml");
		JasperReport report = JasperCompileManager.compileReport(jrxml);

		// 设置模板数据
		// Parameter变量
		Map<String, Object> paramerters = new HashMap<String, Object>();
		paramerters.put("company", "低调民族");
		// Field变量
		JasperPrint jasperPrint = JasperFillManager.fillReport(report,paramerters, new JRBeanCollectionDataSource(wayBills));
		// 使用JRPdfExproter导出器导出pdf
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				ServletActionContext.getResponse().getOutputStream());
		exporter.exportReport();// 导出
		ServletActionContext.getResponse().getOutputStream().close();//关闭流
		return NONE;
	}
    
    /**
     * 导出运单数量柱状图
     * @return
     * @throws Exception
     */
    @Action(value="wayBill_exportHighChart", results={@Result(type="json")})
    public String exportHighChart() throws Exception {
    	List<Object> list = wayBillService.findCountWayBill();
    	this.push(list);
    	return SUCCESS;
    }
    
}
