package com.umpay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Ft {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader("/Users/northwind/Documents/workspace/ForTest/src/com/umpay/t.txt"));
		String line = br.readLine();
		HashMap<String, Long> smap = new HashMap<String, Long>();
		HashMap<String, Long> fmap = new HashMap<String, Long>();
		HashMap<String, Long> map;
		while(line!=null){
			try {
				String funcode = line.substring(74,78);
				long amount  = Long.parseLong(line.substring(126, 140).replace(" ", ""));
				String gateid = line.substring(142, 146);
				String key = funcode+","+gateid;
				if(funcode.equals("P100")){
					map = smap;
				}else
					map = fmap;
				Long total = map.get(key);
				if(total!=null){
					total+=amount;
				}else
					total=amount;
				map.put(key, total);
//				System.out.println(line.substring(74,78)+","+line.substring(126, 140)+","+line.substring(142, 146));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(line);
			}
			line = br.readLine();
		}
		System.out.println(smap);
		System.out.println(fmap);
	}

}
