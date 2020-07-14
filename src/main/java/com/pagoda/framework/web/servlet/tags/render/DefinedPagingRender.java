/*
 * Copyright (c) by pagoda. All right reserved.
 */
package com.pagoda.framework.web.servlet.tags.render;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;

/**
 * 공통 페이징 구현체.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public class DefinedPagingRender implements AbstractPagingRender {

	private static Pattern ptnValueExpr = Pattern.compile("\\^\\{([a-zA-Z0-9.-_]+?)\\}");

	protected String header;
	protected String footer;

	protected String topLink;
	protected String bottomLink;
	protected String nextLink;
	protected String previousLink;
	protected String incLink;
	protected String decLink;
	protected String currentPageLink;
	protected String otherPageLink;

	protected long scale;
	protected long total;

	protected String prefix;

	/**
	 * 기본 생성자.
	 */
	public DefinedPagingRender() {
		this.header = "<table><tbody><tr align=\"center\"><td>";
		this.footer = "</td></tr></tbody></table>";
		this.topLink = "";
		this.bottomLink = "";
		this.previousLink = "";
		this.nextLink = "";
		this.incLink = "[<a href=\"#none\" onclick=\"^{link}\">next</a>]";
		this.decLink = "[<a href=\"#none\" onclick=\"^{link}\">prev</a>]";
		this.currentPageLink = "<b>[<a href=\"#none\" class=\"on\">^{no}</a>]</b>";
		this.otherPageLink = "[<a href=\"#none\" onclick=\"^{link}\" title=\"go Page ^{no}\" >^{no}</a>]";

		this.total = 0;
		this.scale = 10;

		this.prefix = "ko";
	}

	@Override
	public void doWritePageIndex(HttpServletRequest req, JspWriter jwt, long totalNo, long currentNo, long pagingNo, String funcName) throws IOException {

		long fromPageNo;
		long toPageNo;
		long bottomPageNo;

		this.total = totalNo;

		// 페이지 인덱스
		fromPageNo = currentNo - ((currentNo - 1) % this.scale);
		toPageNo = fromPageNo + this.scale - 1;
		if (this.total == 0) {
			bottomPageNo = 1;
		} else {
			bottomPageNo = this.total / pagingNo;
			if ((this.total % pagingNo) != 0) {
				bottomPageNo++;
			}
		}
		if (fromPageNo < 1) {
			fromPageNo = 1;
		}
		if (toPageNo > bottomPageNo) {
			toPageNo = bottomPageNo;
		}

		/* paging script 출력 */
		jwt.println();
		jwt.print("<!-- Paging script ");
		jwt.print(funcName);
		jwt.println("() -->");

		/* header 출력 */
		if (!StringUtils.isEmpty(this.header)) {
			jwt.print(this.header);
		}

		// 탑 페이지 출력
		if (!StringUtils.isEmpty(this.topLink)) {

			if (currentNo <= this.scale) {
				this.topLink = this.topLink.replaceAll("_on", "_off");
				/* this.topLink = this.topLink.replaceAll(" on", " off"); */
				this.topLink = this.topLink.replaceAll("On", "Off");
				this.topLink = this.topLink.replaceAll("onoff", "offon");
			} else {
				this.topLink = this.topLink.replaceAll("_off", "_on");
				/* this.topLink = this.topLink.replaceAll(" off", " on"); */
				this.topLink = this.topLink.replaceAll("Off", "On");
				this.topLink = this.topLink.replaceAll("offon", "onoff");
			}

			jwt.print(this.renderLink(this.topLink, currentNo, 1, bottomPageNo, funcName));
		}

		// 이전 페이지 출력
		if (!StringUtils.isEmpty(this.previousLink)) {
			long prePageNo;

			prePageNo = currentNo - this.scale;
			if (prePageNo < 1) {
				prePageNo = 1;
			}
			if (currentNo == 1) {
				this.previousLink = this.previousLink.replaceAll("_on", "_off");
				/* this.previousLink = this.previousLink.replaceAll(" on", " off"); */
				this.previousLink = this.previousLink.replaceAll("On", "Off");
				this.previousLink = this.previousLink.replaceAll("onoff", "offon");
			} else {
				this.previousLink = this.previousLink.replaceAll("_off", "_on");
				/* this.previousLink = this.previousLink.replaceAll(" off", " on"); */
				this.previousLink = this.previousLink.replaceAll("Off", "On");
				this.previousLink = this.previousLink.replaceAll("offon", "onoff");
			}

			jwt.print(this.renderLink(this.previousLink, currentNo, prePageNo, bottomPageNo, funcName));
		}

		/* decLink 출력 */
		if (!StringUtils.isEmpty(this.decLink)) {
			long decPageNo;

			decPageNo = currentNo - 1;
			if (decPageNo < 1) {
				decPageNo = 1;
			}

			if (currentNo == 1) {
				this.decLink = this.decLink.replaceAll("_on", "_off");
				/* this.decLink = this.decLink.replaceAll(" on", " off"); */
				this.decLink = this.decLink.replaceAll("On", "Off");
				this.decLink = this.decLink.replaceAll("onoff", "offon");
			} else {
				this.decLink = this.decLink.replaceAll("_off", "_on");
				/* this.decLink = this.decLink.replaceAll(" off", " on"); */
				this.decLink = this.decLink.replaceAll("Off", "On");
				this.decLink = this.decLink.replaceAll("offon", "onoff");
			}

			jwt.print(this.renderLink(this.decLink, currentNo, decPageNo, bottomPageNo, funcName));
		}

		/* currentPageLink or otherPageLink 출력 */
		for (long i = fromPageNo; i <= toPageNo; i++) {
			if (i > fromPageNo) {
				jwt.print("");
			}
			jwt.print(this.renderLink(i == currentNo ? this.currentPageLink : this.otherPageLink, currentNo, i, bottomPageNo, funcName));
		}

		/* incLink 출력 */
		if (!StringUtils.isEmpty(this.incLink)) {
			long incPageNo;

			incPageNo = currentNo + 1;
			if (incPageNo > bottomPageNo) {
				incPageNo = bottomPageNo;
			}

			if (currentNo == bottomPageNo) {
				this.incLink = this.incLink.replaceAll("_on", "_off");
				/* this.incLink = this.incLink.replaceAll(" on", " off"); */
				this.incLink = this.incLink.replaceAll("On", "Off");
				this.incLink = this.incLink.replaceAll("onoff", "offon");
			} else {
				this.incLink = this.incLink.replaceAll("_off", "_on");
				/* this.incLink = this.incLink.replaceAll(" off", " on"); */
				this.incLink = this.incLink.replaceAll("Off", "On");
				this.incLink = this.incLink.replaceAll("offon", "onoff");
			}

			jwt.print(this.renderLink(this.incLink, currentNo, incPageNo, bottomPageNo, funcName));
		}

		// 다음 페이지 출력
		if (!StringUtils.isEmpty(this.nextLink)) {
			long nextPageNo;

			nextPageNo = currentNo + this.scale;
			if (nextPageNo > bottomPageNo) {
				nextPageNo = bottomPageNo;
			}

			if (currentNo == bottomPageNo) {
				this.nextLink = this.nextLink.replaceAll("_on", "_off");
				/* this.nextLink = this.nextLink.replaceAll(" on", " off"); */
				this.nextLink = this.nextLink.replaceAll("On", "Off");
				this.nextLink = this.nextLink.replaceAll("onoff", "offon");
			} else {
				this.nextLink = this.nextLink.replaceAll("_off", "_on");
				/* this.nextLink = this.nextLink.replaceAll(" off", " on"); */
				this.nextLink = this.nextLink.replaceAll("Off", "On");
				this.nextLink = this.nextLink.replaceAll("offon", "onoff");
			}

			jwt.print(this.renderLink(this.nextLink, currentNo, nextPageNo, bottomPageNo, funcName));
		}

		// 마지막 페이지 출력
		if (!StringUtils.isEmpty(this.bottomLink)) {

			if ((currentNo + this.scale) >= bottomPageNo) {
				this.bottomLink = this.bottomLink.replaceAll("_on", "_off");
				/* this.bottomLink = this.bottomLink.replaceAll(" on", " off"); */
				this.bottomLink = this.bottomLink.replaceAll("On", "Off");
				this.bottomLink = this.bottomLink.replaceAll("onoff", "offon");
			} else {
				this.bottomLink = this.bottomLink.replaceAll("_off", "_on");
				/* this.bottomLink = this.bottomLink.replaceAll(" off", " on"); */
				this.bottomLink = this.bottomLink.replaceAll("Off", "On");
				this.bottomLink = this.bottomLink.replaceAll("offon", "onoff");
			}

			jwt.print(this.renderLink(this.bottomLink, currentNo, bottomPageNo, bottomPageNo, funcName));
		}

		/* footer 출력 */
		if (!StringUtils.isEmpty(this.footer)) {
			jwt.print(this.footer);
		}

	}

	/**
	 * 링크 만들기.
	 *
	 * @param src
	 *            src
	 * @param pageNo
	 *            pageNo
	 * @param workPageNo
	 *            workPageNo
	 * @param topPageNo
	 *            topPageNo
	 * @param funcName
	 *            funcName
	 * @return String
	 */
	protected String renderLink(String src, long pageNo, long workPageNo, long topPageNo, String funcName) {
		Matcher mch;
		StringBuffer sb;
		String js;
		String link;
		String no;
		String style;

		String end;

		end = "";
		style = "";

		if (pageNo == workPageNo) {
			js = "";
			// link = "#";
			link = "javascript:;";
		} else {
			js = funcName + "(" + workPageNo + ")";
			link = "javascript:" + js;
		}

		String ufunc = funcName + "(this.value);";

		no = String.valueOf(workPageNo);
		sb = new StringBuffer();
		mch = ptnValueExpr.matcher(src);

		while (mch.find()) {
			String valueId;
			valueId = mch.group(1);

			if ("^".equals(valueId)) {
				mch.appendReplacement(sb, "\\^");
			} else if ("link".equals(valueId)) {
				mch.appendReplacement(sb, link);
			} else if ("js".equals(valueId)) {
				mch.appendReplacement(sb, js);
			} else if ("no".equals(valueId)) {
				mch.appendReplacement(sb, no);
			} else if ("style".equals(valueId)) {
				mch.appendReplacement(sb, style);
			} else if ("totPage".equals(valueId)) {
				mch.appendReplacement(sb, String.valueOf(topPageNo));
			} else if ("totPagev".equals(valueId)) {
				DecimalFormat decformater = new java.text.DecimalFormat("###,###,###");
				mch.appendReplacement(sb, String.valueOf(decformater.format(topPageNo)));
			} else if ("pageNo".equals(valueId)) {
				mch.appendReplacement(sb, String.valueOf(pageNo));
			} else if ("end".equals(valueId)) {
				mch.appendReplacement(sb, end);
			} else if ("total".equals(valueId)) {
				DecimalFormat decformater = new java.text.DecimalFormat("###,###,###");
				mch.appendReplacement(sb, String.valueOf(decformater.format(this.total)));
			} else if ("ufunc".equals(valueId)) {
				mch.appendReplacement(sb, ufunc);
			} else if ("pageNoSize".equals(valueId)) {
				mch.appendReplacement(sb, String.valueOf(String.valueOf(pageNo).length()));
			} else if ("totPageSize".equals(valueId)) {
				mch.appendReplacement(sb, String.valueOf(String.valueOf(topPageNo).length()));
			} else if ("prefix".equals(valueId)) {
				mch.appendReplacement(sb, this.prefix);
			} else {
				mch.appendReplacement(sb, "\\^{" + valueId + "}");
			}
		}

		mch.appendTail(sb);

		return sb.toString();
	}

	/**
	 * @return header
	 */
	public String getHeader() {
		return this.header;
	}

	/**
	 * @param header
	 *            header
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return footer
	 */
	public String getFooter() {
		return this.footer;
	}

	/**
	 * @param footer
	 *            footer
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @param topLink
	 *            topLink
	 */
	public void setTopLink(String topLink) {
		this.topLink = topLink;
	}

	/**
	 * @param bottomLink
	 *            bottomLink
	 */
	public void setBottomLink(String bottomLink) {
		this.bottomLink = bottomLink;
	}

	/**
	 * @param nextLink
	 *            nextLink
	 */
	public void setNextLink(String nextLink) {
		this.nextLink = nextLink;
	}

	/**
	 * @param previousLink
	 *            previousLink
	 */
	public void setPreviousLink(String previousLink) {
		this.previousLink = previousLink;
	}

	/**
	 * @return incLink
	 */
	public String getIncLink() {
		return this.incLink;
	}

	/**
	 * @param incLink
	 *            incLink
	 */
	public void setIncLink(String incLink) {
		this.incLink = incLink;
	}

	/**
	 * @return decLink
	 */
	public String getDecLink() {
		return this.decLink;
	}

	/**
	 * @param decLink
	 *            decLink
	 */
	public void setDecLink(String decLink) {
		this.decLink = decLink;
	}

	/**
	 * @return currentPageLink
	 */
	public String getCurrentPageLink() {
		return this.currentPageLink;
	}

	/**
	 * @param currentPageLink
	 *            currentPageLink
	 */
	public void setCurrentPageLink(String currentPageLink) {
		this.currentPageLink = currentPageLink;
	}

	/**
	 * @return otherPageLink
	 */
	public String getOtherPageLink() {
		return this.otherPageLink;
	}

	/**
	 * @param otherPageLink
	 *            otherPageLink
	 */
	public void setOtherPageLink(String otherPageLink) {
		this.otherPageLink = otherPageLink;
	}

	/**
	 * @return scale
	 */
	public long getScale() {
		return this.scale;
	}

	/**
	 * @param scale
	 *            scale
	 */
	public void setScale(long scale) {
		this.scale = scale;
	}

	/**
	 * @return total
	 */
	public long getTotal() {
		return this.total;
	}

	/**
	 * @param total
	 *            total
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * @return prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * @param prefix
	 *            prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
