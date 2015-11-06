package com.umpay;

import java.util.Date;


public class Concat {

	public static void main(String[] args) {
		String a = "a";
		StringBuilder b = new StringBuilder("b");
		StringBuffer c = new StringBuffer("c");
		long star = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			a+="a";
		}
		long end = System.currentTimeMillis();
		System.out.println("String:"+(end-star));
		
		star = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			b.append("b");
		}
		end = System.currentTimeMillis();
		System.out.println("StringBuilder:"+(end-star));
		
		star = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			c.append("c");
		}
		end = System.currentTimeMillis();
		System.out.println("StringBuffer:"+(end-star));
	}

}
