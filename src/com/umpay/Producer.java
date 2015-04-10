package com.umpay;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;

//import org.apache.commons.lang.time.StopWatch;




public class Producer {
	
	public static void main(String[] args) throws Exception{
		
		int cycle = 10000;
		JMSClient client = new JMSClient();
		
		QueueConnection conn = client.getConnection();
		
//		StopWatch watch = new StopWatch();
//		watch.start();
		QueueSession session = null;
		Destination destination=null;
		MessageProducer producer=null;
		String queueName = "TEST-BANK";
		
		
				
		for(int messageId=0;messageId<cycle;messageId++){
			session = conn.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);		
			destination = session.createQueue(queueName);					
			producer = session.createProducer(destination);		
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);		
			producer.setTimeToLive(3600 * 1000);
			MapMessage mapmes = session.createMapMessage();	
			mapmes.setString("paySeq",String.valueOf(messageId));
			mapmes.setString("bankId","WYE00064");
			mapmes.setString("bankType","1");
			mapmes.setString("platDate","20140313");
			mapmes.setString("count","3");
			mapmes.setString("source","1");
			producer.send(mapmes);
			producer.close();
			session.close();
			
		}
		    
//		watch.stop();
//		System.out.println(cycle/(watch.getTime()/1000.0));
		
		producer.close();
		client.shutdown();
		
		
		
	}

	

}



