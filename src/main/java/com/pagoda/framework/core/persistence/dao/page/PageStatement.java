/*
 * Copyright (c) 2016 by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.core.persistence.dao.page;

/**
 * PageStatement.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public class PageStatement {
	private String totalCountStatementId;
	private String dataStatementId;

	/**
	 * PageStatement 생성자.
	 *
	 * @param totalCountStatementId
	 *            totalCountStatementId
	 * @param dataStatementId
	 *            dataStatementId
	 */
	public PageStatement(String totalCountStatementId, String dataStatementId) {
		super();

		this.totalCountStatementId = totalCountStatementId;
		this.dataStatementId = dataStatementId;
	}

	/**
	 * PageStatement.
	 */
	public PageStatement() {
		super();
	}

	/**
	 * @return the totalCountStatementId
	 */
	public String getTotalCountStatementId() {
		return this.totalCountStatementId;
	}

	/**
	 * @return the dataStatementId
	 */
	public String getDataStatementId() {
		return this.dataStatementId;
	}

}
