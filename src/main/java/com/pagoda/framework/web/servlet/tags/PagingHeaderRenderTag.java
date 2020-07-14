/*
 * Copyright (c) by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.web.servlet.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.pagoda.framework.core.persistence.dao.page.PagenateInfo;
import com.pagoda.common.SpringApplicationContext;

/**
 * <pre>
 * 공통 페이징 해더  render tag.
 * <ul>
 * 		<li> item (필수): pagingList</li>
 * 		<li> mTemplate : paging header message template</li>
 * 		<li> funcName : paging call function</li>
 * </ul>
 * </pre>
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
@SuppressWarnings("serial")
public class PagingHeaderRenderTag extends TagSupport {

	private PagenateInfo item;
	private String mTemplate;

	private final String headerStr = "<div style='white-space:nowrap;'><div style='float:left'>Total {0}</div> <div style='float:right'>{1} of {2} Page</div></div>";

	@Override
	public void release() {
		super.release();
		this.item = null;
		this.mTemplate = null;
	}

	@Override
	public int doStartTag() throws JspException {

		if (this.item == null) {
			this.item = new PagenateInfo();
		}
		if (this.mTemplate == null) {
			this.mTemplate = "";
		}

		return SKIP_BODY;
	}

	@SuppressWarnings("unused")
	@Override
	public int doEndTag() throws JspException {
		try {

			JspWriter jwt;
			HttpServletRequest req;

			Long pagingTotalNo;
			Long pagingCurrentNo;
			Long pagingPerPageNo;
			Long pagingMaxPageNo;

			String vstr;
			String msgId;

			jwt = this.pageContext.getOut();
			req = (HttpServletRequest) this.pageContext.getRequest();

			if (this.item != null) {
				pagingTotalNo = (long) this.item.getTotalCount();
				pagingCurrentNo = (long) this.item.getNo();
				pagingPerPageNo = (long) this.item.getRows();
			} else {
				pagingTotalNo = (long) 0;
				pagingCurrentNo = (long) 1;
				pagingPerPageNo = (long) 10;
			}

			if (pagingTotalNo == 0) {
				pagingMaxPageNo = (long) 1;
			} else {
				pagingMaxPageNo = pagingTotalNo / pagingPerPageNo;

				if (pagingTotalNo % pagingPerPageNo > 0) {
					++pagingMaxPageNo;
				}
			}

			vstr = SpringApplicationContext.getBean(MessageSourceAccessor.class).getMessage(this.mTemplate,
					new Object[] { pagingTotalNo, pagingCurrentNo, pagingMaxPageNo }, this.headerStr, RequestContextUtils.getLocale(req));

			jwt.print(vstr);

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
	 * @return mTemplate
	 */
	public String getmTemplate() {
		return this.mTemplate;
	}

	/**
	 * @param mTemplate
	 *            mTemplate
	 */
	public void setmTemplate(String mTemplate) {
		this.mTemplate = mTemplate;
	}

}
