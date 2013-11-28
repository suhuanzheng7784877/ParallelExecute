package org.para.jms;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ActiveMQClient {
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(ActiveMQClient.class);

	public static void main(String[] args) throws IOException {

		// -- http://dlc.sun.com/pdf//816-5904-10/816-5904-10.pdf
		try {
			
			
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");
			// ActiveMQConnectionFactory factory = new
			// ActiveMQConnectionFactory("vm://locahost");
			Connection connection = factory.createConnection();
			connection.start();

			// create message topic
			
			Session session = connection.createSession(true,
					Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic("MyTopic");
			// register message consumer
			MessageConsumer comsumer1 = session.createConsumer(topic);
			comsumer1.setMessageListener(new MessageListener() {
				public void onMessage(Message m) {
					try {
						System.out.println("Consumer get "
								+ ((TextMessage) m).getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			Thread.sleep(60 * 60000);
			session.close();
			connection.stop();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
