package cn.itcast.bos.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itcast.bos.service.PromotionService;

@Component
public class PromotionJob {
	
	@Autowired
	private PromotionService promotionService;
	
	//将过期的活动商品类型置为0
	public void execute(){
		promotionService.updateType();
	}
}
