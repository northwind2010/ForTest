package com.umpay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class CreateFile {
	// 交易日志
	private static Logger logger = Logger.getLogger(CreateFile.class);
	private static int currentCount=0; //当前在内从中行数
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		logger.info("启动文件处理……");
		if (args.length < 3) {
			logger.error("参数错误，终止文件处理！");
			return;
		}
		boolean isOk = true;
		String filePath = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar startcal = Calendar.getInstance();
		Calendar endtcal = Calendar.getInstance();
		try {
			logger.info("参数信息：" + args[0] + "," + args[1] + "," + args[2]);
			filePath = args[0];// 文件目录
			startcal.setTime(dateFormat.parse(args[1]));
			endtcal.setTime(dateFormat.parse(args[2]));
		} catch (Exception e) {
			logger.error("参数异常！" + e.getMessage());
			return;
		}
		
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		while (startcal.compareTo(endtcal) <= 0 && isOk==true) {
			String currentDate = dateFormat.format(startcal.getTime());

			if (!readFile(resultMap,filePath + System.getProperty("file.separator")+ "dtl_" + currentDate + ".txt")) {
				logger.error("处理文件【" + filePath + "】异常！");
				isOk = false;
				break;
			}
			
			if (currentCount>20000) {
				for (Entry<String, List<String>> entry : resultMap.entrySet()) {
					String key = entry.getKey();
					List<String> valueList = entry.getValue();
					if (!writerFile(valueList, filePath+ System.getProperty("file.separator")+"exp"+System.getProperty("file.separator")+ "exp_Trans_" + key + ".txt")) {
						isOk = false;
						logger.error("写文件【" + filePath + "】出现异常！");
						break;
					}
				}
				if(isOk==true){
					resultMap.clear();
					currentCount=0;
				}
			}
			startcal.add(Calendar.DATE, 1);
		}
		//写入最后一次记录
		if (isOk == true) {
			for (Entry<String, List<String>> entry : resultMap.entrySet()) {
				String key = entry.getKey();
				List<String> valueList = entry.getValue();
				if (!writerFile(valueList, filePath+ System.getProperty("file.separator")+"exp"+System.getProperty("file.separator")+ "exp_Trans_" + key + ".txt")) {
					isOk = false;
					break;
				}
			}
		}
		
		if (isOk == true) {
			logger.info("所有文件处理完成！");
		}
		if (isOk == true) {
			filter(filePath+ System.getProperty("file.separator")+"exp");
			logger.info("所有文件处理完成！");
		}
		
	}

	private static boolean readFile(Map<String, List<String>> resultMap,String filePath) {
		BufferedReader in = null;
		boolean isOk = false;
		String s = "";
		int count = 0;
		try {
			logger.info("开始处理文件【" + filePath + "】……");
			File result = new File(filePath);
			if (result.exists()) {
				// 1、开始读取文件
				in = new BufferedReader(new FileReader(result));

				// List resultList=new ArrayList();
				while ((s = in.readLine()) != null) {
					if (s != null && !s.equals("")) {
						++count;
						// 类型-0,受理机构代码-1,发送机构号-2,接收机构号-3,银行商户号-4,
						// 清算日期-5,卡号-6,系统跟踪号-7,交易金额-8,交易mcc-9,交易计费码（040）-10,交易传输时间-11
						// ,实际交易的银联发卡分润算法-12 ,
						// 正确的银联发卡分润算法-13
						// 是否为外境卡（0为境内，1为境外）-14,交易手续费-15,发卡机构代码-16 ,品牌服务费-17,
						// instid-18,trace-19,联动支付日期（paydate）-20,merid-21,bankmerid-22,bankcheckdate-23,交易金额-24,
						// 功能码-25,正确手续费-26,正确mcc-27,mcc类别-28,正确计费码-29,正确的品牌服务费（暂定为空）-30
						// 收单机构已付银联手续费-31,收单机构已付发卡行手续费-32,收单机构应付银联手续费-33,收单机构应付发卡行手续费-34
						// 应追回的手续费（银联）-35,应追回的手续费（发卡行）-36
						String[] lineInfo = s.split(",");
						
						if (lineInfo.length != 37) {
							logger.error("文件格式错误，行数：【" + count + "】,内容：" + s);
							return isOk;
						} else {
							//如果 收单机构应付手续费19 和 收单机构已付手续费20 相等，不做处理
							if((Integer.valueOf(lineInfo[26].trim())-Integer.valueOf(lineInfo[15].trim()))>=-1&&(Integer.valueOf(lineInfo[26].trim())-Integer.valueOf(lineInfo[15].trim()))<=1){
								continue;
							}
							
							
							StringBuffer info = new StringBuffer();
							info.append(lineInfo[5]); // 清算日期1
							info.append(",").append(lineInfo[11].trim()); // 交易传输时间2
							info.append(",").append(lineInfo[7].trim()); // 系统跟踪号3
							info.append(",").append(lineInfo[6].trim()); // 卡号4
							info.append(",").append("".equals(lineInfo[1].trim()) ? "" : "08"+ lineInfo[1].trim()); // 受理机构代码5
							info.append(",").append("".equals(lineInfo[2].trim()) ? "" : "08"+ lineInfo[2].trim()); // 发送机构号6
							info.append(",").append("".equals(lineInfo[3].trim()) ? "" : "08"+ lineInfo[3].trim()); // 接收机构号7
							info.append(",").append("".equals(lineInfo[15].trim()) ? "" : "08"+ lineInfo[16].trim()); // 发卡机构代码8
							info.append(",").append(cent2Yuan(lineInfo[8].trim())); // 交易金额9
							info.append(",").append(lineInfo[9].trim()); // 交易mcc10
							info.append(",").append(lineInfo[27].trim()); // 正确mcc11
							info.append(",").append(lineInfo[10].trim()); // 交易计费码12
							info.append(",").append(lineInfo[29].trim()); // 正确计费码13
							info.append(",").append(lineInfo[12].trim()); // 实际交易的银联发卡分润算法14
							info.append(",").append(lineInfo[13].trim()); // 正确的银联发卡分润算法15
							info.append(",").append(lineInfo[4].trim()); // 银行商户号16

							
							if(lineInfo[0].trim().equals("P100")){ //正向交易
								info.append(",").append(cent2Yuan(lineInfo[36].trim())); // 应追回的手续费（发卡）17
								info.append(",").append(cent2Yuan(lineInfo[35].trim())); // 应追回的手续费（银联）18
								info.append(",").append(cent2Yuan(lineInfo[26].trim())); // 收单机构应付手续费19
								info.append(",").append(cent2Yuan(lineInfo[15].trim())); // 收单机构已付手续费20
								
								String bankMerid = lineInfo[4].trim();
								if (resultMap.containsKey(bankMerid)) {
									resultMap.get(bankMerid).add(info.toString());
								} else {
									List<String> infoList = new ArrayList<String>();
									infoList.add(info.toString());
									resultMap.put(bankMerid, infoList);
								}
								currentCount++;
							}else if(lineInfo[0].trim().equals("T100")){  //退费交易
								info.append(",").append("-"+cent2Yuan(lineInfo[36].trim())); // 应追回的手续费（发卡）17
								info.append(",").append("-"+cent2Yuan(lineInfo[35].trim())); // 应追回的手续费（银联）18
								info.append(",").append("-"+cent2Yuan(lineInfo[26].trim())); // 收单机构应付手续费19
								info.append(",").append("-"+cent2Yuan(lineInfo[15].trim())); // 收单机构已付手续费20
								
								String bankMerid = lineInfo[4].trim();
								if (resultMap.containsKey(bankMerid)) {
									resultMap.get(bankMerid).add(info.toString());
								} else {
									List<String> infoList = new ArrayList<String>();
									infoList.add(info.toString());
									resultMap.put(bankMerid, infoList);
								}
								currentCount++;
							}else{
								//
							}
						}
					}
				}

				isOk = true;
			} else {
				logger.error("文件不存在：【" + result + "】！");
			}
			return isOk;
		} catch (Exception e) {
			logger.error("处理文件【" + filePath + "】出错：" + e.getMessage());
			return isOk;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件【" + filePath + "】出错：" + e.getMessage());
			}
		}
	}

	
	
	/**
	 * 
	 * description: 将分转换为元
	 * 
	 * @param amount
	 * @return
	 */
	public static String cent2Yuan(String amount) {
		amount = amount.trim();
		int cent = Integer.parseInt(amount);
		if (cent < 0)
			return "";
		DecimalFormat df = new DecimalFormat("000");
		amount = df.format(cent);
		return amount.substring(0, amount.length() - 2) + "."
				+ amount.substring(amount.length() - 2);
	}

	/**
	 * 在指定的文件中增加记录
	 * 
	 * @param info
	 */
	public static boolean writerFile(List<String> infoList, String failFilePath) {
		boolean isOk = true;
		if(infoList==null&&infoList.size()==0){
			return true;
		}
		// logger.info("开始写文件【" + failFilePath + "】，内容:" + info);
		File failFile = new File(failFilePath);
		if (!failFile.exists()) {
			try {
				failFile.createNewFile();
			} catch (IOException e) {
				isOk = false;
				logger.error("写文件【" + failFilePath + "】，内容:【" + infoList + "】 失败：创建文件失败！");
				e.printStackTrace();
				return isOk;
			}
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(failFile, true);
			for(int i=0;i<infoList.size();i++){
				fileWriter.append(infoList.get(i) + "\n");
			}
		} catch (IOException e) {
			isOk = false;
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fileWriter = null;
		}

		if (isOk) {
			// logger.info("写文件【" + failFilePath + "】，内容:【" + info + "】 成功！");
		} else {
			logger.error("写文件【" + failFilePath + "】，内容:【" + infoList + "】 失败！");
		}
		return isOk;
	}
	
	//过滤<500块钱数据
	private static void filter(String filePath) throws Exception{
		List<String> files=getFilesByPath(filePath);
		for(int i=0;i<files.size();i++){
			if(readExpFile(files.get(i))>50000){//单位分
				copyFile(new File(files.get(i)),filePath+ System.getProperty("file.separator")+"大于500");
			}
		}
	}
	

	private static int readExpFile(String filePath) throws Exception {
		BufferedReader in = null;
		Map<String,Map<String,String>> resultMap=new HashMap<String,Map<String,String>>();
		int amount=0;
		
		try {
			logger.info("开始处理文件【" + filePath + "】……");
			File file = new File(filePath);
			if (file.exists()) {
				// 1、开始读取文件
				in = new BufferedReader(new FileReader(file));
				String s = "";
				int count = 0;
				Map<String, String> lineMap = new HashMap<String, String>();
				// List resultList=new ArrayList();
				while ((s = in.readLine()) != null) {
					if (s != null && !s.equals("")) {
						++count;
						
						/*
						info.append(lineInfo[5]); // 清算日期1
						info.append(",").append(lineInfo[11].trim()); // 交易传输时间2
						info.append(",").append(lineInfo[7].trim()); // 系统跟踪号3
						info.append(",").append(lineInfo[6].trim()); // 卡号4
						info.append(",").append("".equals(lineInfo[1].trim()) ? "" : "08"+ lineInfo[1].trim()); // 受理机构代码5
						info.append(",").append("".equals(lineInfo[2].trim()) ? "" : "08"+ lineInfo[2].trim()); // 发送机构号6
						info.append(",").append("".equals(lineInfo[3].trim()) ? "" : "08"+ lineInfo[3].trim()); // 接收机构号7
						info.append(",").append("".equals(lineInfo[15].trim()) ? "" : "08"+ lineInfo[16].trim()); // 发卡机构代码8
						info.append(",").append(cent2Yuan(lineInfo[8].trim())); // 交易金额9
						info.append(",").append(lineInfo[9].trim()); // 交易mcc10
						info.append(",").append(lineInfo[27].trim()); // 正确mcc11
						info.append(",").append(lineInfo[10].trim()); // 交易计费码12
						info.append(",").append(lineInfo[29].trim()); // 正确计费码13
						info.append(",").append(lineInfo[12].trim()); // 实际交易的银联发卡分润算法14
						info.append(",").append(lineInfo[13].trim()); // 正确的银联发卡分润算法15
						info.append(",").append(lineInfo[4].trim()); // 银行商户号16
						info.append(",").append(cent2Yuan(lineInfo[36].trim())); // 应追回的手续费（发卡）17
						info.append(",").append(cent2Yuan(lineInfo[35].trim())); // 应追回的手续费（银联）18
						info.append(",").append(cent2Yuan(lineInfo[26].trim())); // 收单机构应付手续费19
						info.append(",").append(cent2Yuan(lineInfo[15].trim())); // 收单机构已付手续费20
						*/
						
						String[] lineInfo = s.split(",");
						if (lineInfo.length <19) {
							logger.error("文件格式错误，行数：【" + count + "】,内容：" + s);
							throw new Exception("文件格式错误，行数：【" + count + "】,内容：" + s);
						} else {
							amount+=Integer.valueOf(yuan2Cent(lineInfo[20].trim()))-Integer.valueOf(yuan2Cent(lineInfo[19].trim()));
						}
					}
				}
			} else {
				logger.error("文件不存在：【" + file + "】！");
				throw new Exception("文件不存在：【" + file + "】！");
			}
		} catch (Exception e) {
			logger.error("处理文件【" + filePath + "】出错：" + e.getMessage());
			throw new Exception("处理文件【" + filePath + "】出错：" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件【" + filePath + "】出错：" + e.getMessage());
			}
		}
		return amount;
	}
	
	/**
     * 读取指定目录下所有文件，除后缀为noSuffix 文件
     * 
     * @param path 目录
     * @param noSuffix 后缀
     * @param getNumCount 获取文件数量
     * @throws Exception
     */
	public static List<String> getFilesByPath(String path){
		List<String> fileList=new ArrayList<String>();
		File filePath = new File(path); 
		
		try{
			if(filePath.exists()){
				if(filePath.isDirectory()){
					File[] file = filePath.listFiles();  
					
					if(file!=null && file.length>0){
						List<String> tempfileList=new ArrayList<String>();
						
						//获取所以的文件名
						for (int i = 0,n = file.length; i < n; i++) {  
							if (file[i].isFile()&& file[i].getName().startsWith("exp_Trans_")){
								tempfileList.add(file[i].getAbsolutePath());
							}  
						}
						//排序及放入返回中
						if(tempfileList.size()>0){
							int count=0;
							ArrayList<String>  fileNameComp=new ArrayList<String>(tempfileList.size());
							for(int i=0,n=tempfileList.size();i<n;i++){
								fileNameComp.add(tempfileList.get(i));
//								fileNameComp[count++]=new FileNameComp(tempfileList.get(i));
							}
							//按照文件名排序
//							Arrays.sort(fileNameComp);
							Collections.sort(fileNameComp);
							
							//依次获取排序后文件名
							for(int i=0;i< fileNameComp.size();i++){
								fileList.add((fileNameComp.get(i)));
							}
						}
					}
				}
			}
		}catch(Exception ex){
			logger.error("获取目录【"+path+"】中文件出错："+ex.getMessage());
		}
		
		filePath=null;
		logger.info("获取目录【"+path+"】中文件完成，文件数量："+fileList.size());
		
		return fileList;
	}
	
	/**
     * 剪切文件
     * 
     * @param  srcFile 需要剪切的文件
     * @param  destDir 剪切至目录
     * @throws IOException
     */
	public static boolean  copyFile(File srcFile, String destDir) {
		boolean flag=false;
		
		logger.info("开始剪切文件【"+srcFile+"】");
		
		if (!srcFile.exists()) { // 源文件不存在   
		     System.out.println("源文件不存在");   
		     return false;   
		 }   
		 // 获取待复制文件的文件名   
		 String destPath = destDir +File.separator+ srcFile.getName();   
		 if (destPath.equals(srcFile.getAbsolutePath())) { // 源文件路径和目标文件路径重复   
			 logger.error("源文件路径和目标文件路径重复!");   
		     return false;
		 }
		 
		 File destFile = new File(destPath);   
		 if (destFile.exists() && destFile.isFile()) { // 该路径下已经有一个同名文件   
			 logger.error("目标目录下已有同名文件!");   
			 
			 destFile = new File(destPath+".new");   
		     //return false;   
		 }

		 File destFileDir = new File(destDir);   
		 destFileDir.mkdirs();   
		 try {   
		     FileInputStream fis = new FileInputStream(srcFile);
		     FileOutputStream fos = new FileOutputStream(destFile);
		     byte[] buf = new byte[1024];
		     int c;
		     while ((c = fis.read(buf)) != -1) {   
		         fos.write(buf, 0, c);   
		     }   
		     fis.close();   
		     fos.close();   
		     
		     //删除原文件
		     srcFile.delete();
		     
		     flag = true;   
		 } catch (IOException e) {   
		     
		 }   

		 if (flag) {   
			 logger.info("剪切文件【"+srcFile+"】成功！");
		 }   

		 return flag;   
	}
	
	public static String yuan2Cent(String str) {
		if(str==null) str="";
		str = str.trim();
		//只保留小数点后两位
		int index = str.lastIndexOf(".");
		if((index != -1) && (index + 3 <= str.length()))
			str = str.substring(0, index + 3);
		DecimalFormat df = new DecimalFormat("0.00");
		String yuan = df.format(Double.parseDouble(str));
		String cent = yuan.replace(".", "");
		StringBuffer sb = new StringBuffer(cent);
		for (; sb.length() > 1;) {
			if (sb.charAt(0) == '0') 
				sb.deleteCharAt(0);
		    else break;
		}
		return sb.toString();
	}
}