package com.umpay;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Java 异步线程池的使用
 * @author Administrator
 *
 */

public class MyFutureTask1 {
	
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);
	
    private Future<List<Word>> submit(final String sentence){
        return EXECUTOR_SERVICE.submit(new Callable<List<Word>>(){
        	
//            @Override
            public List<Word> call() {
            	
                return segSentence(sentence);
            }

			
        });
    }
    private List<Word> segSentence(String sentence) {
		
    	//http请求或者链接数据库
    	List<Word> sentences=new ArrayList<Word>();
    	sentences.add(new Word("ok")); 
    	sentences.add(new Word("异步线程池的使用"));
    	sentences.add(new Word("ok异步线程池的使用"));
    	sentences.add(new Word("test"));
    	
		return sentences;
	}
    
	public static void main(String[] args) throws Exception, ExecutionException {
		
		Future<List<Word>>  futures =new MyFutureTask1().submit("");
		for(Word word:futures.get())
		{
			System.out.println(word);
			
		}
		MyFutureTask1.EXECUTOR_SERVICE.shutdown();
	}

}
