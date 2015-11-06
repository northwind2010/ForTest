package com.umpay;

import java.util.HashMap;
import java.util.HashSet;

public class SetTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		HashSet<String> set = new HashSet<String>();
		set.add("1");
		map.put("1", set);
		map.clear();
		map.put("1", set);
		set.add("4");
		set.add("5");
		set.add(null);
		System.out.println(set.contains(null));
		System.out.println(map);
		
	}

}
