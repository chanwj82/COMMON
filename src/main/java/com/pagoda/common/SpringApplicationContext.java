package com.pagoda.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

  private static ApplicationContext CONTEXT;
	
	public SpringApplicationContext() {
	}
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}

	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}
	
	public static <T> T getBean(String beanName, Class<T> requiredType) {
		return CONTEXT.getBean(beanName, requiredType);
	}
	
	/**
	 * @param requiredType
	 *            빈 클래스
	 * @param <T>
	 *            제너릭 타입.
	 * @return T 제너릭 타입
	 */
	public static <T> T getBean(Class<T> requiredType) {
		if (CONTEXT != null) {
			return CONTEXT.getBean(requiredType);
		} else {
			return null;
		}
	}
	
}