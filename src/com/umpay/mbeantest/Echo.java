package com.umpay.mbeantest;

/**
 * Implements of JMX EchoMBean
 * 
 * @author haitao.tu
 *
 */
public class Echo implements EchoMBean {

//	@Override
	public void print(String yourName) {
		System.out.println("Hi " + yourName + "!");
	}
	
}