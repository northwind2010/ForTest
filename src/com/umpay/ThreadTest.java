package com.umpay;

public class ThreadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t = Thread.currentThread();
		t.setName("t1");
		System.out.println(t.getName());

	}

}
