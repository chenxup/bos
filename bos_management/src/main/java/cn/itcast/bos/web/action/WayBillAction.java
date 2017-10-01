package cn.itcast.bos.web.action;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.WayBillService;
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

import java.util.HashMap;
import java.util.Map;

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

    @Action(value = "wayBill_pageQuery", results = {@Result(type = "json")})
    public String pageQuery() throws Exception {
        //根据id降序
        PageRequest pageable = new PageRequest(this.getPage() - 1, this.getRows(), new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
        //封装查询条件
        Page<WayBill> page = wayBillService.pageQuery(model,pageable);
        this.java2Json(page);
        return SUCCESS;
    }

    @Action(value = "wayBill_findByWayBillNum", results = {@Result(type = "json")})
    public String findByWayBillNum() throws Exception {
        WayBill wayBill = wayBillService.findByWayBillNum(this.model.getWayBillNum());
        this.push(wayBill);
        return SUCCESS;
    }

}
