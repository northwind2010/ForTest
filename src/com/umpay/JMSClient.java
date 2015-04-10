package com.umpay;

import java.util.Hashtable;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSClient implements ExceptionListener {

	private String initCtxFactory = "";
	private String providerURL = "";

	private Context context = null;
	private QueueConnection connection = null;

	public JMSClient() {
	}

	@SuppressWarnings("unchecked")
	public synchronized void configure() throws Exception {
		String initCtxFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
		String providerURL = "tcp://10.10.77.80:62001";
		String qConnFactory = "ConnectionFactory";
		Context context = this.context;
		QueueConnection queueConnection = null;

		try {
			boolean newCtx = false;
			if (this.context == null
					|| !this.initCtxFactory.equals(initCtxFactory)
					|| !this.providerURL.equals(providerURL)) {
				Hashtable env = new Properties();
				env.put(Context.INITIAL_CONTEXT_FACTORY, initCtxFactory);
				env.put(Context.PROVIDER_URL, providerURL);
				context = new InitialContext(env);
				newCtx = true;
			}
			boolean newQC = false;
			if (newCtx || this.connection == null) {
				QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context
						.lookup(qConnFactory);
				queueConnection = queueConnectionFactory
						.createQueueConnection();
				newQC = true;
			}
			if (newQC) {
				if (this.connection != null) {
					try {
						this.connection.close();
					} catch (JMSException ignr) {
					}
				}

				queueConnection.setExceptionListener(this);
				queueConnection.start();
			}
			this.initCtxFactory = initCtxFactory;
			this.providerURL = providerURL;
			this.context = context;
			this.connection = queueConnection;
		} catch (JMSException e) {
			throw new Exception(e);
		} catch (NamingException e) {
			throw new Exception(e);
		}
	}

	public void onException(JMSException arg0) {
		shutdown();
	}

	public QueueConnection getConnection() {
		try {
			configure();
			return connection;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public void shutdown() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (JMSException ignr) {
			ignr.printStackTrace();
		}
		context = null;
		connection = null;
	}

}
