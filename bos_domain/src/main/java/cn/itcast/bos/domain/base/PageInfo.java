package cn.itcast.bos.domain.base;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="pageinfo")
public class PageInfo<T> {
	
	private Long totalCount;
	private List<T> pageData;
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}

	
	
	
}
