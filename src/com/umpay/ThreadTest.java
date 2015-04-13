package com.umpay;

public class ThreadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t = Thread.currentThread();
		t.setName("t1");
		System.out.println(t.getName());
		
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				Thread t = Thread.currentThread();
//				t.setName("t2");
				System.out.println(t.getName());
			}
		}).start();
		System.out.println(t.getName());

	}

}
