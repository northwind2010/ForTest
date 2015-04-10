package com.umpay.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationDemoInterceptor {

	public void checkInterceptor(Class<?>cl){
		for(Method m :cl.getDeclaredMethods()){
			 AnnotationDemo annoDemo = m.getAnnotation(AnnotationDemo.class);
			 if(annoDemo !=null){
				 System.out.println("Found AnnotationDemo in "+m.getName()+" :"+annoDemo.check()+" :"+annoDemo.test());
			 }else{
				 System.out.println("Found nothing in "+m.getName()); 
			 }
		}
	}
}
