package com.umpay;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;


public class Consumer {

	
	public static void main(String[] args)throws Exception {
		int received=0;
		JMSClient client = new JMSClient();
		
		QueueConnection conn = client.getConnection();
		long now = System.currentTimeMillis();
		long before = now-1000;
		QueueSession session = null;
		Destination destination=null;
		MessageProducer producer=null;
		String queueName = "TEST-BANK";
		session = conn.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);		

		destination = session.createQueue(queueName);
		MessageConsumer consumer= session.createConsumer(destination,"JMSTimestamp < "+before);
		int count =0;
		for(;;){

			//,"JMSTimestamp < "+before

			Message message= consumer.receive(2000);			
			System.out.println(message);
			
			if(message!=null)
				count++;
			else 
				break;
			
		}
		consumer.close();
		session.close();
				
		System.out.println("count="+count);
		
		client.shutdown();
	}
}
