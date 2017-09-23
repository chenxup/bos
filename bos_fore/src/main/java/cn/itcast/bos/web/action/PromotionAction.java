package cn.itcast.bos.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.PageInfo;
import cn.itcast.bos.domain.base.Promotion;
import utils.Constants;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	
	@Action(value="promotionAction_contentPageQuery", results={@Result(type="json")})
	public String contentPageQuery() throws Exception {
		//调用远程服务
		PageInfo pageInfo = WebClient.create(Constants.BOS_MANAGEMENT_HOST).path("/services/manager/promotion/"+this.getPage()+"/"+this.getRows())
			.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(PageInfo.class);
		
		//封装数据
		this.push(pageInfo);
		return SUCCESS;
	}
	
}
