/*
 * Copyright (c) 2016 by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.core.persistence.dao.page;

import java.util.List;

/**
 * Page Info.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 *
 * @param <T>
 */
public class PageInfo<T> implements Pagenateable {

	/**
	 * 생성자.
	 */
	public PageInfo() {
	}

	/**
	 * @param data
	 *            data
	 */
	public PageInfo(List<T> data) {
		this.data = data;
	}

	/**
	 *
	 * @param totalCount
	 *            totalCount
	 * @param data
	 *            data
	 */
	public PageInfo(int totalCount, List<T> data) {
		if (this.page == null) {
			this.page = new PagenateInfo();
		}
		this.data = data;
		this.page.setTotalCount(totalCount);
	}

	/**
	 * paging 객체.
	 */
	private PagenateInfo page = new PagenateInfo();

	@Override
	public PagenateInfo getPage() {
		return this.page;
	}

	@Override
	public void setPage(PagenateInfo pim) {
		this.page = pim;
	}

	private List<T> data;

	/**
	 * get totalCount.
	 *
	 * @return totalRowCount
	 */
	public int getTotalRowCount() {
		return this.page.getTotalCount();
	}

	/**
	 * List<데이터 테이블>.
	 *
	 * @return List<T>
	 */
	public List<T> getData() {
		return this.data;
	}

}
