package cn.itcast.bos.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRespository;

    /**
     * 保存运单
     * @param model
     */
    @Override
    public void save(WayBill model) throws Exception {
        //根据运单号是否存在决定保存还是更新
        WayBill wayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
        if (wayBill == null) {
            //不存在就保存
            model.setId(null);
            //判断order是否存在不存在就设为null
            if (model.getOrder() != null && model.getOrder().getId() == null) {
                model.setOrder(null);
            }
            //将运单设为待发货
            model.setSignStatus(1); //1:代发货， 2：配送中
            //保存
            wayBillRepository.save(model);
            //同步索引库
            wayBillIndexRespository.save(model);
            return;
        }
        //存在, 将值赋给查询查询到的对象中，order还是用原来的，不能改动
        Order order = wayBill.getOrder();
        BeanUtils.copyProperties(wayBill, model);
        wayBill.setSignStatus(1);
        wayBill.setOrder(order);
        //同步索引库
        wayBillIndexRespository.save(wayBill);
    }

    /**
     * 分页查询运单
     * @param pageRquest
     * @return
     */
    @Override
    public Page<WayBill> pageQuery(WayBill wayBill, PageRequest pageRquest) {
    	//如果没有条件。就查询数据库
		if(StringUtils.isBlank(wayBill.getWayBillNum())
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress())
				&& StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus()== null || wayBill.getSignStatus() == 0)){
			// 无条件查询，查询数据库
			return wayBillRepository.findAll(pageRquest);
		}
		
		//封装查询条件
		BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
		//运单号
		if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
			QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum",
					wayBill.getWayBillNum());
			queryBuilder.must(tempQuery);
		}
		
		//发货地，模糊查询
		//如果输入完整的就分词查询
		if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
			// 情况一：发货地输入“北”，查询索引库词条的一部分，可以使用模糊匹配词条查询
			QueryBuilder wildcardQuery = new WildcardQueryBuilder("sendAddress", "*"+wayBill.getSendAddress() + "*");
			// 情况二：发货地输入“北京市海淀区”，是多个词条组合，进行分词后，每个词条匹配查询
			QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress")
						.defaultOperator(Operator.AND);
			//这两个满足一个即可
			BoolQueryBuilder queryBuildersend = new BoolQueryBuilder();
			queryBuildersend.should(wildcardQuery).should(queryStringQueryBuilder);
			
			queryBuilder.must(queryBuildersend);
		}
		
		//收货地
		if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
			// 收货地 模糊查询
			// 情况一：收货地输入“北”，查询索引库词条的一部分，可以使用模糊匹配词条查询
			QueryBuilder wildcardQuery = new WildcardQueryBuilder(
					"recAddress", "*" + wayBill.getRecAddress() + "*");
			// 情况二：收货地输入“北京市海淀区”，是多个词条组合，进行分词后，每个词条匹配查询
			QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress")
					.defaultOperator(Operator.AND);// 分词后查询，使用and连接
			// 上面的两种情况，有一种满足即可，使用bool查询的or关系
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			boolQueryBuilder.should(wildcardQuery).should(queryStringQueryBuilder);
			queryBuilder.must(boolQueryBuilder);
		}
		
		//速运类型
		if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
			QueryBuilder tempQuery = new TermQueryBuilder("sendProNum",
					wayBill.getSendProNum());
			queryBuilder.must(tempQuery);
		}
		
		//签收状态
		if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
			QueryBuilder tempQuery = new TermQueryBuilder("signStatus",
					wayBill.getSignStatus());
			queryBuilder.must(tempQuery);
		}
		
		SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
		//分页
		searchQuery.setPageable(pageRquest);
        return wayBillIndexRespository.search(searchQuery);
    }

    /**
     * 根据运单号查询运单
     * @param wayBillNum
     * @return
     */
    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }
}
