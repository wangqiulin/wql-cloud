package com.wql.cloud.basic.datasource.commonPage;

import java.io.Serializable;
import java.util.List;

public class BasePage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	// 当前页
	private int pageNum;

	// 每页的数量
	private int pageSize;

	// 当前页的数量
	private int size;

	// 结果集
	private List<T> list;

	// 总记录数
	private long total;

	// 总页数
	private int pages;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

}
