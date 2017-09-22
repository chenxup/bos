package cn.itcast.bos.web.action;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Promotion;
import cn.itcast.bos.service.PromotionService;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

	@Autowired
	private PromotionService promotionService;
	
    //上传文件的名称
    private File titleImgFile;
    private String titleImgFileFileName;
    
    public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}
    public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	
	@Action(value="promotion_add", results={@Result(type="redirect", location="./pages/take_delivery/promotion.html")})
	public String save() throws Exception {
        //文件的绝对路径
        String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
        //文件的相对路径
        String contextPath = ServletActionContext.getRequest().getContextPath() + "/upload/";

        //处理文件名
        String filename = UUID.randomUUID().toString().replace("-", "");
        filename = filename + titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));

        //保存文件
        FileUtils.copyFile(titleImgFile, new File(realPath, filename));

        this.model.setTitleImg(contextPath+filename);

        promotionService.save(this.model);
        return SUCCESS;
	}
	
	@Action(value="promotion_pageQuery", results={@Result(type="json")})
	public String pageQuery() throws Exception {
		Page<Promotion> pageInfo = promotionService.pageQuery(this.getPageRquest());
		this.java2Json(pageInfo);
		return SUCCESS;
	}
}
