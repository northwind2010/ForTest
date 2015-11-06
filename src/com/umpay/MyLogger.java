package com.umpay;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;


public class MyLogger {
	private static Logger log = Logger.getRootLogger();
	private static org.slf4j.Logger log1 = LoggerFactory.getLogger(MyLogger.class);
	public static void main(String[] args) {
		for(int i=0;i<10000;i++)
			log1.info("ffff");
	}
}
