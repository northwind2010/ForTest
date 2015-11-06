package com.umpay;

public class Worker {
	int id;
	String name;
	String job;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public static void main(String[] args) throws Exception{
		String str = "12345678901234567890";
		byte[] src = str.getBytes("UTF8");
		int len = src.length;
		for(int i=0; i<len/2;i++){
			byte b = src[i];
			src[i] = src[len-i-1];
			src[len-i-1] = b; 
		}
		System.out.println(Short.MAX_VALUE);
		int b = 0x11;
		int c = 8;
		System.out.println((b&c));
	}
}
