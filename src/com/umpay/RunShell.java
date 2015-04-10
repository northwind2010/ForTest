package com.umpay;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;

//import javax.mail.internet.NewsAddress;

public class RunShell {
	public static void main(String[] args) throws Exception{
		try {
			Process process = Runtime.getRuntime().exec("/Users/northwind/tmp/t.sh");
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			PrintStream ps = new PrintStream(process.getOutputStream());
			OutputStream out = process.getOutputStream();
			process.getErrorStream();

			InputStream in = process.getInputStream();
			LineNumberReader input = new LineNumberReader(ir);
			
			int count = in.available();
			System.out.println(in.available());
			byte[] b = new byte[count];
			int readCount = 0; // 
			while (readCount < count) {
				readCount += in.read(b, readCount, count - readCount);
			}
			System.out.println(new String(b));
			String str = "hello\n";
			out.write(str.getBytes());
			out.flush();
			Thread.sleep(10000);
			count = in.available();
			System.out.println(in.available());
			b = new byte[count];
			readCount = 0; // 
			while (readCount < count) {
				readCount += in.read(b, readCount, count - readCount);
				System.out.println(readCount);
			}
			System.out.println(new String(b));
			
			input.close();
			ir.close();
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}

