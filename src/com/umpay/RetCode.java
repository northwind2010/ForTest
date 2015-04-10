package com.umpay;

import java.util.HashMap;

public class RetCode extends HashMap<String, String>{

	public static RetCode ret = new RetCode();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ret.put("123", "123");
		ret.put("123", "123");
		ret.put("123", "123");
		System.out.println(ret.get("123"));
		System.out.println(Integer.MAX_VALUE);
	}

}
