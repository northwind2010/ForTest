package com.umpay;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class trim {

	static int i = 0;
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
//		System.out.println((null+"").equals("null"));
//		System.out.println(Long.MAX_VALUE);
//		String str=null;
//		String[] array = str.split("\\|");
//		System.out.println(array.length);
//		String str1 = "123";
//		String str2 = "123";
////		System.out.println(str.trim());
//		LinkedList<String> list = new LinkedList<String>();
//		LinkedHashSet<String> set = new LinkedHashSet<String>();
//		set.add(str1);
//		set.add(null);
//		System.out.println(set.contains(str1));;
//		System.out.println();
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put(null, "123");
//		System.out.println(map.get(null));
//		Hashtable<String, String> table = new Hashtable<String, String>();
////		table.put(null, "123");
////		System.out.println(table.get(null));
////		System.out.println(URLEncoder.encode("����","GBK"));
//		trim.test();
//		Long l = 11111l;
//		System.out.println(Long.toString(l, 16).toUpperCase());
		
		
		System.out.println(("|".split("\\|")).length);
	}
	
	public static void test() throws Exception{
		System.out.println(i++);
		Thread.sleep(1);
		trim.test();
		
	}

}
