package com.umpay;

/**
 * 
 * Description: 对账文件的操作
 * Copyright: Copyright (c) 2009
 * Company:联动优势
 * @author 任水
 * @version 1.0
 * @date Dec 19, 2009
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CollateFileWriter {
	private PrintWriter writer;
	private int counter = 0;
	public CollateFileWriter(String collId, String fileName) {
		
		String collateFileName = "E:/liuhaoyi/"+collId + File.separator + fileName;
		System.out.println(collateFileName);
		File collateFile = new File(collateFileName);
		
		File dir = collateFile.getParentFile();
		if(!dir.exists())
			dir.mkdirs();
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(collateFile,false)));
		} catch (IOException e) {
			//throw new AtomicTaskException(new Exception("创建清算文件[" + collateFileName + "]失败",e));
		}
	}
	public void writeLine(String line) {
		writer.println(line);
		counter++;
	}
	public void flush(){
		writer.flush();
		
	}
	public void close(){
		if(writer != null)
			writer.close();
		showSummary();
	}
	/**
	 * 显示任务运行摘要
	 */
	private void showSummary() {
//		log.info("写入对帐文件笔数=" + counter);
	}
	
}

