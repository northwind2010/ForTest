package com.umpay.annotation;

public class AnnotationDemoTest {

	@AnnotationDemo(check = "main", test = true)
	public static void main(String[]args){
		AnnotationDemoInterceptor adi = new AnnotationDemoInterceptor();
		adi.checkInterceptor(AnnotationDemoTest.class);
	}
	@AnnotationDemo(check = "hello, test")
	public static void doSomething(){
		System.out.println("This is an annotation example!");
	}
	@AnnotationDemo()
	public static void doHello(){
		System.out.println("This is an annotation example!");
	}
	@Deprecated
	public static void doOtherthing(){
		System.out.println("This is normal method!");
	}
}
