package com.umpay;

/** 
 * Simple demo of ThreadGroup usage.
 */
public class ThreadGroupDemo {
 
  class MyThreadGroup extends ThreadGroup {
    public void uncaughtException(Thread t, Throwable ex) {
      System.err.println(t+"I caught " + ex);
    }
    public MyThreadGroup(String name) {
      super(name);
    }
  }
 
  public static void main(String[] args) {
    new ThreadGroupDemo().work();
  }
 
  protected void work() {
    ThreadGroup g = new MyThreadGroup("bulk threads");
    Runnable r = new Runnable() {
      public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        for (int i=0; i<5; i++) {
          System.out.println(Thread.currentThread().getName() + ": " + i);
          try {
            Thread.sleep(1776);
            throw new RuntimeException("aaa");
          } catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        }
      }
    };
 
    // Create and start all the Threads
    for (int i = 0; i< 10; i++) {
      new Thread(g, r).start();
    }
 
    // List them.
    Thread[] list = new Thread[g.activeCount()];
    g.enumerate(list);
    for (int i=0; i<list.length; i++) {
      if (list[i] == null)
        continue;
      Thread t = list[i];
      System.out.println(i + ": " + t);
    }
  }
}
