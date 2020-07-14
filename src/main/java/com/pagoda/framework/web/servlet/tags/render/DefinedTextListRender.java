/*
 * Copyright (c) by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.web.servlet.tags.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * Text Write 구현체.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public class DefinedTextListRender implements ListRender {

	/**
	 * 생성자.
	 */
	public DefinedTextListRender() {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void doWriteList(HttpServletRequest req, JspWriter jwt, String id, String title, String className, String disabled, ArrayList item, String funcName,
			String selected, String selectTitle, String extra, String valid) throws IOException {

		StringBuffer html = new StringBuffer();

		List<Pattern> listPattern = null;
		if (selected != null && !"".equals(selected)) {
			listPattern = getPattern(selected);
		}

		int count = 0;

		for (Object o : item) {
			Map itemMap = (Map) o;

			if ("true".equals(itemMap.get("ischeck"))) {
				if (count > 0) {
					html.append(",");
				}
				if (!funcName.equals("")) {
					String func = "onClick=\"javascript:" + funcName + "('" + (String) itemMap.get("value") + "')\"";
					html.append("&nbsp;<a href=\"javascript:void(0)\" " + func + " >" + (String) itemMap.get("title") + "</a>");
				} else {
					html.append("&nbsp;" + (String) itemMap.get("title") + "");
				}
				count++;

			} else if (listPattern != null) {
				for (Pattern pattern : listPattern) {
					if (pattern.matcher((String) itemMap.get("value")).matches()) {
						if (count > 0) {
							html.append(",&nbsp;");
						}
						if (!funcName.equals("")) {
							String func = "onClick=\"javascript:" + funcName + "('" + (String) itemMap.get("value") + "')\"";
							html.append("<a href=\"javascript:void(0)\" " + func + " >" + (String) itemMap.get("title") + "</a>");
						} else {
							html.append("" + (String) itemMap.get("title") + "");
						}
						count++;
					}
				}
			} else {
				continue;
			}

		}

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
