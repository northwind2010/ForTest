package com.umpay;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Java 异步线程池的使用
 * @author Administrator
 *
 */

public class MyFutureTask {
	
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);
	
	//异步任务的关键在Future类
    private Future<List<Result>> submit(final String sentence){
    	
    	//EXECUTOR_SERVICE.execute(command);
        return EXECUTOR_SERVICE.submit(new Callable<List<Result>>(){
        	
//            @Override
            public List<Result> call() {
            	
                return segSentence(sentence);
            }

			
        });
    }
    private List<Result> segSentence(String sentence) {
		
    	System.out.println("异步线程开始执行");
    	//http请求或者链接数据库
    	List<Result> sentences=new ArrayList<Result>();
    	sentences.add(new Result("ok")); 
    	sentences.add(new Result("异步线程池的使用"));
    	sentences.add(new Result("ok异步线程池的使用"));
    	sentences.add(new Result("test"));
    	
    	System.out.println("异步线程执行完成");
    	try {
			Thread.sleep(1000);
			System.out.println("异步线程休息完成");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	
    	
		return sentences;
	}
    
	public static void main(String[] args) throws Exception, ExecutionException {
		
		Future<List<Result>>  futures =new MyFutureTask().submit("");//提交异步任务，任务会在异步线程执行。
		//主线程继续执行其他。http://blog.csdn.net/yangyan19870319/article/details/6093481
		int i=0;
		while (i<10) {
			
			System.out.println("主线程做其他事情去了");
			i++;
			Thread.sleep(500);
		}	
		//在调用futures.get()方法获取异步任务数据之前，我们可以做其他事情。
		for(Result word:futures.get(5000, TimeUnit.MILLISECONDS))//取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果 
		{
			System.out.println(word);
			
		}
		MyFutureTask.EXECUTOR_SERVICE.shutdown();
	}

}
