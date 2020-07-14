/*
 * Copyright (c) 2016 by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.core.persistence.dao.page;

import java.io.Serializable;

/**
 * <pre>
 * 페이지  정보를 가지는 객체 .
 * </pre>
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
@SuppressWarnings("serial")
public class PagenateInfo implements Serializable {

	private Integer no;
	private Integer rows;
	private Integer totalCount;
	private int topPage;
	private long dataFrom;
	private long dataTo;

	/**
	 * 기본 생성자 기본 페이지당 건수는 20.
	 */
	public PagenateInfo() {
		this(20);
	}

	/**
	 * 생성자.
	 *
	 * @param rows
	 *            기본 페이지당 건수 설정
	 */
	public PagenateInfo(int rows) {
		this.no = 1;
		this.rows = rows;
		this.totalCount = 0;
	}

	/**
	 * @param pim
	 *            pim
	 */
	public void setPageInfo(PagenateInfo pim) {
		if (pim.getNo() != null) {
			this.no = pim.getNo();
		}
		if (pim.getRows() != null) {
			this.rows = pim.getRows();
		}
		if (pim.getTotalCount() != null) {
			this.totalCount = pim.getTotalCount();
		}
	}

	/**
	 * 페이지 번호를 얻습니다. 0부터 시작합니다.
	 *
	 * @return no
	 */
	public Integer getNo() {
		return this.no;
	}

	/**
	 * @param no
	 *            no
	 */
	public void setNo(Integer no) {
		this.no = no == null ? 1 : no;
	}

	/**
	 * 페이지당 row수를 얻습니다.
	 *
	 * @return rows
	 */
	public Integer getRows() {
		return this.rows;
	}

	/**
	 * @param rows
	 *            rows
	 */
	public void setRows(Integer rows) {
		this.rows = rows == null ? 20 : rows;
	}

	/**
	 * 설정된 페이지 번호와 페이지당 로우수를 기준으로 시작 로우번호를 반환합니다.
	 *
	 * @return rows
	 */
	public Integer getStartRow() {
		if (this.no == null || this.rows == null) {
			return null;
		} else {
			return Integer.valueOf((this.no.intValue() - 1) * this.rows.intValue()) + 1;
		}
	}

	/**
	 * @return rows
	 */
	public Integer getMysqlStartRow() {
		if (this.no == null || this.rows == null) {
			return null;
		} else {
			return Integer.valueOf(this.rows.intValue() * (this.no.intValue() - 1));
		}
	}

	/**
	 * 설정도니 페이지 번호와 페이지당 로우수를 기준으로 끝 로우번호를 반환합니다.
	 *
	 * @return rows
	 */
	public Integer getEndRow() {
		if (this.no == null || this.rows == null) {
			return null;
		} else {
			return this.no * this.rows;
		}
	}

	/**
	 * 페이지 조회 범위를 최대로 변경합니다.
	 */
	public void setPageAll() {
		this.no = Integer.valueOf(1);
		this.rows = Integer.valueOf(Integer.MAX_VALUE);
	}

	/**
	 * 검색에 사용된 이후에 검색 결과수 얻을수 있음.
	 *
	 * @return totalCount
	 */
	public Integer getTotalCount() {
		return this.totalCount = this.totalCount == null ? 0 : this.totalCount;
	}

	/**
	 * @param totalCount
	 *            totalCount
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount == null ? 0 : totalCount;

		if (this.totalCount == 0) {
			this.topPage = 0;
			this.dataFrom = 0;
			this.dataTo = 0;
		} else {
			this.topPage = totalCount / this.rows + (totalCount % this.rows == 0 ? 0 : 1);
			this.dataFrom = this.getStartRow();
			this.dataTo = Math.min(this.getEndRow(), totalCount);
		}
	}

	/**
	 * 제일 끝 페이비 번호 반환.
	 *
	 * @return topPage
	 */
	public int getTopPage() {
		return this.topPage;
	}

	/**
	 * 데이터 시작 인덱스.
	 *
	 * @return dataFrom
	 */
	public long getDataFrom() {
		return this.dataFrom;
	}

	/**
	 * 테이터 종료 인덱스.
	 *
	 * @return 인덱스
	 */
	public long getDataTo() {
		return this.dataTo;
	}
}
