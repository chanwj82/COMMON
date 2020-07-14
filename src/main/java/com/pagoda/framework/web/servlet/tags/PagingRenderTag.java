/*
 * Copyright (c) by pagoda. All right reserved.
 */
package com.pagoda.framework.web.servlet.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.pagoda.framework.core.persistence.dao.page.PagenateInfo;
import com.pagoda.framework.web.servlet.tags.render.AbstractPagingRender;
import com.pagoda.framework.web.servlet.tags.render.DefinedPagingRender;

/**
 * <pre>
 * 공통 페이징 render tag .
 * <ul>
 * 		<li> item (필수): pagingList</li>
 * 		<li> template : paging template</li>
 * 		<li> funcName : paging call function</li>
 * </ul>
 * </pre>
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
@SuppressWarnings("serial")
public class PagingRenderTag extends TagSupport {

	private PagenateInfo item;
	private String template;
	private String funcName;
	private String deviceType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	@Override
	public void release() {
		super.release();
		this.item = null;
		this.template = null;
		this.funcName = null;
		this.deviceType = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {

		if (this.item == null) {
			this.item = new PagenateInfo();
		}
		if (this.template == null) {
			this.template = "";
		}
		if (this.id == null) {
			this.id = "";
		}
		if (this.funcName == null) {
			this.funcName = "";
		}
		if (this.deviceType == null) {
			this.deviceType = "";
		}

		return SKIP_BODY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {

			JspWriter jwt;
			HttpServletRequest req;
			AbstractPagingRender pr;
			Long pagingTotalNo;
			Long pagingCurrentNo;
			Long pagingPerPageNo;
			Device device;

			boolean isRending;

			jwt = this.pageContext.getOut();
			req = (HttpServletRequest) this.pageContext.getRequest();
			device = DeviceUtils.getCurrentDevice(req);

			if (device == null) {
				isRending = true;
			} else if (StringUtils.isEmpty(deviceType)) {
				isRending = true;
			} else if (device.isNormal() && StringUtils.indexOf(deviceType, "P") > -1) {
				isRending = true;
			} else if (device.isMobile() && StringUtils.indexOf(deviceType, "M") > -1) {
				isRending = true;
			} else if (device.isTablet() && StringUtils.indexOf(deviceType, "T") > -1) {
				isRending = true;
			} else {
				isRending = false;
			}

			if (isRending) {

				if ("".equals(this.template)) {
					pr = new DefinedPagingRender();
				} else {
					pr = ((DefinedPagingRender) RequestContextUtils.getWebApplicationContext(req).getBean(this.template));
				}

				if (this.item.getTotalCount() != null) {
					pagingTotalNo = (long) this.item.getTotalCount();
				} else {
					pagingTotalNo = (long) 0;
				}

				if (this.item.getNo() != null) {
					pagingCurrentNo = (long) this.item.getNo();
				} else {
					pagingCurrentNo = (long) 1;
				}

				if (this.item.getRows() != null) {
					pagingPerPageNo = (long) this.item.getRows();
				} else {
					pagingPerPageNo = (long) 10;
				}

				pr.doWritePageIndex(req, jwt, pagingTotalNo, pagingCurrentNo, pagingPerPageNo, this.funcName);

			}

		} catch (Exception e) {
			throw new JspTagException("I/O 예외 " + e.getMessage());
		} finally {
			this.release();
		}

		return EVAL_PAGE;
	}

	/**
	 * @return item
	 */
	public PagenateInfo getItem() {
		return this.item;
	}

	/**
	 * @param item
	 *            item
	 */
	public void setItem(PagenateInfo item) {
		this.item = item;
	}

	/**
	 * @return template
	 */
	public String getTemplate() {
		return this.template;
	}

	/**
	 * @param template
	 *            template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return funcName
	 */
	public String getFuncName() {
		return this.funcName;
	}

	/**
	 * @param funcName
	 *            funcName
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType
	 *            the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
