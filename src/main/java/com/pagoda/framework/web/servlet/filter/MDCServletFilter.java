package com.pagoda.framework.web.servlet.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

import com.pagoda.common.Constants;
import com.pagoda.common.model.PagodaSession;

import ch.qos.logback.classic.ClassicConstants;

public class MDCServletFilter implements Filter {
	
	public static final String REQUEST_PAGODA_MEMBER_ID = "req.pagodaMemberId";
	public static final String REQUEST_PAGODA_MEMBER_NAME = "req.pagodaMemberName";
	public static final String REQUEST_METHOD = "req.requestMethod";
	
	public static final String NOT_LOGIN = "notlogin";

	  public void destroy() {
	    // do nothing
	  }

	  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

	    insertIntoMDC(request);
	    try {
	      chain.doFilter(request, response);
	    } finally {
	      clearMDC();
	    }
	  }

	  void insertIntoMDC(ServletRequest request) {
		  
		try{
			MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, request.getRemoteAddr());

		    if (request instanceof HttpServletRequest) {
		      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		      
		      PagodaSession user = (PagodaSession) httpServletRequest.getSession().getAttribute(Constants.PAGODA_SESSION );
		      
		      if ( user != null ) {
		    	  MDC.put(REQUEST_PAGODA_MEMBER_ID, user.getId());
		    	  MDC.put(REQUEST_PAGODA_MEMBER_NAME, user.getName());
		      }
		      else{
		    	  MDC.put(REQUEST_PAGODA_MEMBER_NAME, NOT_LOGIN);
		      }

		      MDC.put(ClassicConstants.REQUEST_REQUEST_URI, httpServletRequest.getRequestURI());
		      
		      StringBuffer requestURL = httpServletRequest.getRequestURL();
		      
		      if (requestURL != null) {
		        MDC.put(ClassicConstants.REQUEST_REQUEST_URL, requestURL.toString());
		      }
		      
		      //MDC.put(ClassicConstants.REQUEST_QUERY_STRING, httpServletRequest.getQueryString());
		      MDC.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest.getHeader("User-Agent"));
		      //MDC.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, httpServletRequest.getHeader("X-Forwarded-For"));
		      MDC.put(REQUEST_METHOD, httpServletRequest.getMethod());
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	    

	  }

	  void clearMDC() {
	    MDC.remove(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY);
	    MDC.remove(REQUEST_PAGODA_MEMBER_ID);
	    MDC.remove(REQUEST_PAGODA_MEMBER_NAME);
	    MDC.remove(ClassicConstants.REQUEST_REQUEST_URI);
	    //MDC.remove(ClassicConstants.REQUEST_QUERY_STRING);
	    // removing possibly inexistent item is OK
	    MDC.remove(ClassicConstants.REQUEST_REQUEST_URL);
	    MDC.remove(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY);
	    //MDC.remove(ClassicConstants.REQUEST_X_FORWARDED_FOR);
	    MDC.remove(REQUEST_METHOD);
	  }

	  public void init(FilterConfig arg0) throws ServletException {
	    // do nothing
	  }
}