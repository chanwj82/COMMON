/*
 * Copyright (c) by pagoda. All right reserved.
 */
package com.pagoda.framework.web.servlet.tags.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.pagoda.common.SpringApplicationContext;


/**
 * SelectBox 구현체.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public class DefinedSelectListRender implements ListRender {

	/**
	 * 생성자.
	 */
	public DefinedSelectListRender() {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void doWriteList(HttpServletRequest req, JspWriter jwt, String id, String title, String className, String disabled, ArrayList item, String funcName,
			String selected, String selectTitle, String extra, String valid) throws IOException {

		StringBuffer html = new StringBuffer();
		String checked = "";

		if (disabled.equals("true")) {
			disabled = "disabled=\"disabled\"";
		}

		if (!funcName.equals("")) {
			funcName = "onChange=\"javascript:" + funcName + "(this)\"";
		}

		html.append("<select id=\"" + id + "\" name=\"" + id + "\" class=\"" + className + "\" title=\"" + title + "\" " + funcName + " " + disabled + " "
				+ extra + " " + valid + " >" + "\n");

		List<Pattern> listPattern = null;
		if (selected != null && !"".equals(selected)) {
			listPattern = getPattern(selected);
		}

		if (!"".equals(selectTitle)) {
			String[] stitle = selectTitle.split(",");
			String value;

			if (stitle.length > 1) {
				value = stitle[1];
			} else {
				value = "";
			}

			html.append("	<option value=\"" + value + "\" >"
					+ SpringApplicationContext.getBean(MessageSourceAccessor.class).getMessage(stitle[0], RequestContextUtils.getLocale(req)) + "</option>"
					+ "\n");

		}

		for (Object o : item) {
			Map itemMap = (Map) o;
			if ("true".equals(itemMap.get("ischeck"))) {
				checked = "selected=\"selected\"";
			} else if (listPattern != null) {
				for (Pattern pattern : listPattern) {
					checked = "";
					if (pattern.matcher((String) itemMap.get("value")).matches()) {
						checked = "selected=\"selected\"";
						break;
					}
				}
			} else {
				checked = "";
			}

			html.append("	<option value=\"" + (String) itemMap.get("value") + "\" " + checked + "  " + extra + "  >" + (String) itemMap.get("title")
					+ "</option>" + "\n");
		}
		html.append("</select>" + "\n");

		jwt.print(html);
	}

	/**
	 * @param src
	 *            String
	 * @return List<Pattern>
	 */
	public List<Pattern> getPattern(String src) {
		List<Pattern> patternList;
		patternList = new ArrayList<Pattern>();
		String[] strArc = src.split(",");

		for (int i = 0; i < strArc.length; i++) {
			String str = strArc[i];
			Pattern pattern = Pattern.compile(str);
			patternList.add(pattern);
		}
		return patternList;
	}

}
