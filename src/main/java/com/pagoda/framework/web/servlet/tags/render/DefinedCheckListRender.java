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
 * CheckBox 구현체.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public class DefinedCheckListRender implements ListRender {

	/**
	 * 생성자.
	 */
	public DefinedCheckListRender() {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void doWriteList(HttpServletRequest req, JspWriter jwt, String id, String title, String className, String disabled, ArrayList item, String funcName,
			String selected, String selectTitle, String extra, String valid) throws IOException {
		StringBuffer html = new StringBuffer();
		String checked = "";
		String validFunc = "";
		int k = 0;

		if (disabled.equals("true")) {
			disabled = "disabled=\"disabled\"";
		}

		if (!funcName.equals("")) {
			funcName = "onClick=\"javascript:" + funcName + "(this)\"";
		}

		List<Pattern> listPattern = null;
		if (selected != null && !"".equals(selected)) {
			listPattern = getPattern(selected);
		}

		for (Object o : item) {
			Map itemMap = (Map) o;
			if ("true".equals(itemMap.get("ischeck"))) {
				checked = "checked=\"checked\"";
			} else if (listPattern != null) {
				for (Pattern pattern : listPattern) {
					checked = "";
					if (pattern.matcher((String) itemMap.get("value")).matches()) {
						checked = "checked=\"checked\"";
						break;
					}
				}
			} else {
				checked = "";
			}

			if (k == 0) {
				validFunc = valid;
			}

			html.append("<input type=\"checkbox\" id=\"" + id + "\" name=\"" + id + "\" value=\"" + (String) itemMap.get("value") + "\" " + funcName + " "
					+ checked + " " + disabled + " nameOption=\"" + (String) itemMap.get("title") + "\" " + extra + " " + validFunc + " /> "
					+ (String) itemMap.get("title") + "&nbsp;\n");

			k++;

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
