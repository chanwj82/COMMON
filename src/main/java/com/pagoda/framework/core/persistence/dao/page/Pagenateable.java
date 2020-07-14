/*
 * Copyright (c) 2016 by pagoda. All right reserved.
 */
package com.pagoda.framework.core.persistence.dao.page;

/**
 * Pagenateable interface.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public interface Pagenateable {

	/**
	 * 페이지 나눔 정보를 조회 합니다.
	 *
	 * @return PagenateInfoModel
	 */
	public PagenateInfo getPage();

	/**
	 * 페이지 정보를 저장 합니다.
	 *
	 * @param page
	 *            page
	 */
	public void setPage(PagenateInfo page);
}
