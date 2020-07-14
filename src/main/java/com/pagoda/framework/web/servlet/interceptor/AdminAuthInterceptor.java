package com.pagoda.framework.web.servlet.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pagoda.common.Constants;
import com.pagoda.common.model.PagodaSession;


public class AdminAuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminAuthInterceptor.class);
	
	private String returnPage;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 어드민 체크.
		return this.adminCheck(request, response);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 어드민 체크.
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
	private boolean adminCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PagodaSession user = (PagodaSession) request.getSession().getAttribute(Constants.PAGODA_SESSION);

		if ( user != null && !"9".equals(user.getLevel()) ) {
			
			LOGGER.debug( "[" + request.getSession().getId() + "] login userid [" + user.getId() + "] admin auth Fail > Redirect to [" + this.returnPage + "]");
			
			alertJs(response,"어드민 권한이 없습니다.");

			return false;
		} else {
			return true;
		}
		
	}


	/**
	 * @param returnPage
	 *            the returnPage to set
	 */
	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}

	
	public void alertJs(HttpServletResponse response, String msg) throws IOException{
		response.setCharacterEncoding("UTF-8");
	     PrintWriter writer = response.getWriter();
	     writer.println("<script type='text/javascript'>");
	     writer.println("alert('" + msg + "');");
	     writer.println("history.back();");
	     writer.println("</script>");
	     writer.flush();
	}
}
