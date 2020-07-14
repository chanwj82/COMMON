package com.pagoda.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringApplicationContextListener implements ServletContextListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		LOGGER.info("SpringApplicationContext Shutting down...");
		System.out.println("SpringApplicationContext Shutting down...");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		LOGGER.info("SpringApplicationContext contextInitialized...");
		System.out.println("SpringApplicationContext contextInitialized...");
		
	}

}
