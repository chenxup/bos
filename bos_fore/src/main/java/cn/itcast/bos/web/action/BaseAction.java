package cn.itcast.bos.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	protected T model;
	@Override
	public T getModel() {
		return model;
	}
	
	private int page;
	private int rows;
	
	public PageRequest getPageRquest() {
		return new PageRequest(page-1, rows);
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public BaseAction() {
		//获得当前实例的泛型
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<?> clazz = (Class<?>) parameterizedType.getActualTypeArguments()[0];
		Object obj = null;
		try {
			obj = clazz.newInstance();
			model = (T) obj;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("实例化BaseAction失败");
		}
	}
	
	//处理分页的数据
	public void java2Json(Page pageInfo){
		Map map = new HashMap<>();
		map.put("total", pageInfo.getTotalElements());
		map.put("rows", pageInfo.getContent());
		ActionContext.getContext().getValueStack().push(map);
	}
	
	//将数据放入值栈
	public void push(Object obj) {
		ActionContext.getContext().getValueStack().push(obj);
	}
	

}
