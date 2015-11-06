package com.umpay;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * Simple demo of ThreadGroup usage.
 */
public class ThreadGroupDemo {
	public Map<String, Integer> map = new HashMap<String, Integer>();
	public int num=0;

	private static Logger log = Logger.getRootLogger();
	class MyThreadGroup extends ThreadGroup {
		public void uncaughtException(Thread t, Throwable ex) {
			System.err.println(t + "I caught " + ex);
		}

		public MyThreadGroup(String name) {
			super(name);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new ThreadGroupDemo().work();
	}

	protected void work() throws InterruptedException {
		ThreadGroup g = new MyThreadGroup("bulk threads");
		Runnable r = new Runnable() {
			public void run() {
				System.out.println(Thread.currentThread().getName() + " started");
				for (int i = 0; i < 10; i++) {
					System.out.println(Thread.currentThread().getName() + ": " + i);
					try {
//						Integer num;
//						synchronized (map) {
//							if ((num = map.get("key")) == null) {
//								map.put("key"+i, 1);
//							} else {
//								map.put("key"+i, num + 1);
//							}
//						}
						num+=1;
						map.put(num+"", num);
						log.info(map.size());
						// Thread.sleep(1776);
						// throw new RuntimeException("aaa");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};

		// Create and start all the Threads
		log.info("start");
		for (int i = 0; i < 20; i++) {
			new Thread(g, r).start();
		}
//		Thread.sleep(2000);
//		System.out.println("num=" + map.get("key"));
		// List them.
		Thread[] list = new Thread[g.activeCount()];
		g.enumerate(list);
		for (int i = 0; i < list.length; i++) {
			if (list[i] == null)
				continue;
			Thread t = list[i];
			System.out.println(i + ": " + t);
		}
	}
}
