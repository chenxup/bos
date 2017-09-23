package cn.itcast.bos.web.action;

import cn.itcast.bos.domain.base.PageInfo;
import cn.itcast.bos.domain.base.Promotion;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import utils.Constants;

import javax.ws.rs.core.MediaType;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	
	@Action(value="promotionAction_contentPageQuery", results={@Result(type="json")})
	public String contentPageQuery() throws Exception {
		//调用远程服务
		PageInfo pageInfo = WebClient.create(Constants.BOS_MANAGEMENT_HOST+"/services/promotion/pageQuery?page="+this.getPage()+"&size="+this.getRows())
			.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(PageInfo.class);
		
		//封装数据
		this.push(pageInfo);
		return SUCCESS;
	}
	
}
