/*
 * Copyright (c) by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.web.servlet.tags.render;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * 공통 페이징 abstract interface.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public abstract interface AbstractPagingRender {

	/**
	 * 인터페이스.
	 *
	 * @param req
	 *            req
	 * @param jwt
	 *            jwt
	 * @param totalNo
	 *            totalNo
	 * @param currentNo
	 *            currentNo
	 * @param pagingNo
	 *            pagingNo
	 * @param jsFunctionName
	 *            jsFunctionName
	 * @throws IOException
	 *             IOException
	 */
	public void doWritePageIndex(HttpServletRequest req, JspWriter jwt, long totalNo, long currentNo, long pagingNo, String jsFunctionName) throws IOException;

}
