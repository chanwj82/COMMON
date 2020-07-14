package com.pagoda.framework.web.servlet.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pagoda.common.Constants;
import com.pagoda.common.model.PagodaSession;


public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);
	
	private String loginPage;

	private final String retParamName = "retUrl";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 로그인 체크.
		return this.loginCheck(request, response);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 로그인 체크.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param currentMenuInfo
	 *            MenuOutput
	 * @return boolean
	 * @throws IOException
	 *             IOException
	 */
	private boolean loginCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PagodaSession user = (PagodaSession) request.getSession().getAttribute(Constants.PAGODA_SESSION);

		if ( user == null ) {
			String retUrl = this.makeReturnUrl(request, response);
			LOGGER.debug( "[" + request.getSession().getId() + "] loginCheck Fail > Redirect to [" + retUrl + "]");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.sendRedirect(request.getContextPath() + this.loginPage + "?" + this.retParamName + "=" + retUrl);
			return false;
		} else {
			return true;
		}
		
	}

	/**
	 * 로그인 실패 시 로그인 페이지로 돌아갈 수 있도록 리턴 url 생성.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return 리턴 url
	 */
	private String makeReturnUrl(HttpServletRequest request, HttpServletResponse response) {
		String reqUrl;
		StringBuffer sb;
		String qs;

		sb = new StringBuffer();
		sb.append(request.getRequestURL());
		qs = request.getQueryString();

		if (qs != null) {
			sb.append("?").append(qs);
		}
		reqUrl = sb.toString();

		return reqUrl;
	}

	/**
	 * @param loginPage
	 *            the loginPage to set
	 */
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

}
