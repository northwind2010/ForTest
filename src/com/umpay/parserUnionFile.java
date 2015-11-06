package com.umpay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

//import com.umpay.common.util.StringUtil;

public class parserUnionFile {
	// 交易日志
	private static Logger logger = Logger.getLogger(parserUnionFile.class);
	
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		logger.info("启动文件处理……");
		if(args.length<3){
			logger.error("参数错误，终止文件处理！");
			return ;
		}
		String filePath="";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar startcal = Calendar.getInstance();
		Calendar endtcal = Calendar.getInstance();
		try{
			logger.info("参数信息："+args[0]+","+args[1]+","+args[2]);
			filePath=args[0];//文件目录
			startcal.setTime(dateFormat.parse(args[1]));
			endtcal.setTime(dateFormat.parse(args[2]));
		}catch (Exception e) {
			logger.error("参数异常！" + e.getMessage());
			return;
		}
		
		BufferedReader in = null;
		try {
			while(startcal.compareTo(endtcal)<=0){
				String currentDate=dateFormat.format(startcal.getTime()).substring(2);
				
				String comNfile = filePath+System.getProperty("file.separator")+"ACOMN"+System.getProperty("file.separator")+"IND"+currentDate+"01ACOMN";
				String fleefile = filePath+System.getProperty("file.separator")+"ALFEE"+System.getProperty("file.separator")+"IND"+currentDate+"99ALFEE";
				String outFile = filePath+System.getProperty("file.separator")+"union_"+dateFormat.format(startcal.getTime())+".txt";
				startcal.add(Calendar.DATE, 1);
				if (!new File(comNfile).exists()) {
					logger.error("文件不存在：【"+comNfile+"】！");
					return;
				}
				
				parserUnionFile2UMP(comNfile,fleefile,outFile);
				
			}
		} catch (Exception e) {
			logger.error("处理文件【" + filePath + "】出错：" + e.getMessage());
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
	
	@SuppressWarnings("resource")
	private static void parserUnionFile2UMP(String acomNFile,String alfeeFile, String outFile) throws Exception {
		BufferedReader acomnIn = new BufferedReader(new FileReader(acomNFile));
		BufferedReader alfeeIn = new BufferedReader(new FileReader(alfeeFile));
		File f = new File(outFile);
		String payDate = "20"+(new File(acomNFile).getName().substring(3,9));
		if(!f.exists()){
			f.createNewFile();
		}else{
			f.delete();
		}
		String acomnStr = null;
		String alfeeStr = null;
		Map<String, String> allACOMNMap = new HashMap<String, String>();// INDACOMN 集合
		Map<String, String> allALFEEMap = new HashMap<String, String>();// INDALFEE 集合
		List<String> acomnCancelList = new ArrayList<String>();// 当日撤销交易List
		//生成文件格式：
//		类型（功能码P100-正交易、P110-被撤销、冲正的消费、T100-退费、C100-撤销、冲正）--
		//+受理机构代码--
//		发送机构号--
		//接收机构号--
//银行商户号--
		//清算日期--
//		卡号--
		//系统跟踪号--
		//交易金额--
		//交易mcc--
		//交易计费码（040）--
		//交易传输时间 --
		//实际交易的银联发卡分润算法 --
		//正确的银联发卡分润算法 --
		//是否为境外卡（0-境内 1-境外）
		//交易手续费--
		//发卡机构代码 --
		//品牌服务费--
//		+instid+trace+联动支付日期（paydate）+merid+bankmerid+bankcheckdate+交易金额+功能码+正确手续费+正确mcc+mcc类别+正确计费码
//		交易mcc+交易计费码（040）+交易手续费+品牌服务费+交易传输时间  +发卡机构代码 +实际交易的银联发卡分润算法 +正确的银联发卡分润算法 
//		+instid+trace+联动支付日期（paydate）+merid+bankmerid+bankcheckdate+交易金额+功能码+正确手续费+正确mcc+mcc类别+正确计费码
		
		//读取acomn文件
		logger.info(String.format("解析%s文件...", acomNFile));
		try {
			while((acomnStr = acomnIn.readLine()) != null){
				String agentAcqCode = acomnStr.substring(0,11).trim();//受理机构代码
				String sendAcqCode = acomnStr.substring(12,23).trim();//发送机构标识码
				String recvAcqCode = acomnStr.substring(166,177).trim();//接收机构标识码
				String bankMerId = acomnStr.substring(127,142);//银行商户号
				String outAccount = acomnStr.substring(42,61).trim();//卡号
				String paySeq = acomnStr.substring(24,30);//payseq
				String amout = acomnStr.substring(62,74);//交易金额
				String mcc = acomnStr.substring(113,117).trim();//商户类型mcc
				String chargeType = acomnStr.substring(286,289);//计费类型
				String transmitTime = acomnStr.substring(31,41).trim();//交易传输时间
				String tradeFenRuan = "";//实际交易的银联发卡分润算法
				String rightFenRuan = "";//正确的银联发卡分润算法
				String overseasCard = "00010344".equals(acomnStr.substring(166,174).trim())?"1":"0";//是否为境外卡（0-境内 1-境外）
				
				String messageCode = acomnStr.substring(101, 105).trim();//报文类型
				String transTypeCode = acomnStr.substring(106, 112).trim();//交易类型
				String servCode = acomnStr.substring(156, 158).trim();//服务点条件码
				String zxTradeFee = acomnStr.substring(205,217);//正向交易手续费
				String fxTradeFee = acomnStr.substring(192,204);//反向交易手续费
				String funCode = "";//功能码
				String fee = "";//手续费
				String acomnMyContext = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", 
						agentAcqCode,sendAcqCode,recvAcqCode,bankMerId,payDate,outAccount,paySeq,amout,mcc,chargeType,
						transmitTime,tradeFenRuan,rightFenRuan,overseasCard);
				boolean isWriteFlag = false;
				boolean isPay = messageCode.equals("0200") && transTypeCode.substring(0, 2).equals("00") && transTypeCode.substring(3).equals("000") && servCode.equals("00");
				boolean isCancel = messageCode.equals("0420") && transTypeCode.substring(0, 2).equals("00") && transTypeCode.substring(3).equals("000") && servCode.equals("00");
				boolean isRevoke = messageCode.equals("0200") && transTypeCode.substring(0, 2).equals("20") && transTypeCode.substring(3).equals("000") && servCode.equals("00");
				boolean isRefund = messageCode.equals("0220") && transTypeCode.substring(0, 2).equals("20") && transTypeCode.substring(3).equals("000") && servCode.equals("00");
				if(isPay){
					isWriteFlag = true;
					funCode="P100";
					fee = zxTradeFee;
				}
				if (isCancel || isRevoke) {
					isWriteFlag = true;
					funCode = "C100";
					fee = fxTradeFee;
					String origPaySeq = acomnStr.substring(178, 184).trim();
					String origTransmitTime = acomnStr.substring(241, 251);
					acomnCancelList.add(origPaySeq + origTransmitTime);
				}
				if(isRefund){
					isWriteFlag = true;
					fee = fxTradeFee;
					funCode = "T100";
				}
				acomnMyContext = funCode +","+ acomnMyContext+","+fee;
				if(isWriteFlag && sendAcqCode.equals("48290000")){
					allACOMNMap.put(paySeq + transmitTime, acomnMyContext);
				}
				
			}
		} catch (Exception e) {
			logger.info("解析ACOMN文件异常...");
			e.printStackTrace();
			throw new Exception("解析ACOMN文件异常...");
		}
		
		//读取alfee文件
		try {
			logger.info(String.format("解析%s文件...", alfeeFile));
			while((alfeeStr = alfeeIn.readLine()) != null){
				String key = alfeeStr.substring(116,122) + alfeeStr.substring(123,133);
				String alfee = alfeeStr.substring(320,331);
				String fkAcqCode = alfeeStr.substring(65,74).trim();//发卡机构代码
				allALFEEMap.put(key, fkAcqCode+","+alfee);
			}
		} catch (Exception e) {
			logger.info("解析ALFEE文件异常...");
			e.printStackTrace();
			throw new Exception("解析ACOMN文件异常...");
		}
		
		
		// 写文件
		BufferedWriter out = new BufferedWriter(new FileWriter(outFile,true));
		for (Entry<String, String> entry : allACOMNMap.entrySet()) {
			String key = entry.getKey(); 
			String value = entry.getValue(); 
			String alfee = "00000000000";
			//拼接品牌服务费、发卡机构代码
			if(allALFEEMap.containsKey(key)){
				value = value +","+allALFEEMap.get(key);
			}else{
				value = value + ",," + alfee;//取不到时品牌服务费为0
			}
			
			//标示做过撤销或者冲正的消费交易
			if(acomnCancelList.contains(key)){
				String funCode = "P110";
				value = funCode + value.substring(value.indexOf(","));
			}
			out.write(value+System.getProperty("line.separator"));
			out.flush();
		} 
		
		acomnIn.close();
		alfeeIn.close();
		out.close();
		allACOMNMap.clear();
		acomnCancelList.clear();
	}
}