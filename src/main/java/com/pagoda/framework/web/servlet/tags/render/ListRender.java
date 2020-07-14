/*
 * Copyright (c) by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.web.servlet.tags.render;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * List Render abstract interface.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public abstract interface ListRender {

	/**
	 * @param req
	 *            HttpServletRequest
	 * @param jwt
	 *            JspWriter
	 * @param id
	 *            아이디
	 * @param title
	 *            타이틀
	 * @param className
	 *            클래스 명
	 * @param disabled
	 *            disabled 여부
	 * @param item
	 *            아이템
	 * @param funcName
	 *            javascrpit 명
	 * @param selected
	 *            선택 여부
	 * @param selectTitle
	 *            선택 타이틀
	 * @param extra
	 *            기타 속성
	 * @param valid
	 *            validation script
	 * @throws IOException
	 *             IOException
	 */
	@SuppressWarnings("rawtypes")
	public void doWriteList(HttpServletRequest req, JspWriter jwt, String id, String title, String className, String disabled, ArrayList item, String funcName,
			String selected, String selectTitle, String extra, String valid) throws IOException;

}
