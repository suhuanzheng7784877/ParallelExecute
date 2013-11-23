package org.para.jms;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * refer to http://activemq.apache.org/jndi-support.html
 * http://activemq.apache.org/how-do-i-embed-a-broker-inside-a-connection.html
 * 
 * @author gloomyfish
 * 
 */
public class PureJMSProducer {

	private static final Log LOG = LogFactory.getLog(PureJMSProducer.class);

	private PureJMSProducer() {
	}

	/**
	 * @param args
	 *            the destination name to send to and optionally, the number of
	 *            messages to send
	 */
	public static void main(String[] args) {
		Context jndiContext = null;
		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		BrokerService broker = null;
		final int numMsgs = 10;

		// create external TCP broker
		try {
			broker = BrokerFactory.createBroker(new URI(
					"broker:tcp://localhost:61616"));
			broker.start();
		} catch (URISyntaxException e) {
			LOG.info("Could not create broker: " + e.toString());
		} catch (Exception e) {
			LOG.info("Could not create broker: " + e.toString());
		}
		// try {
		//
		// }

		/*
		 * Look up connection factory and destination.
		 */

		connectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");

		/*
		 * Create connection. Create session from connection; false means
		 * session is not transacted. Create sender and text message. Send
		 * messages, varying text slightly. Send end-of-messages message.
		 * Finally, close connection.
		 */
		try {
			
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Topic topic = session.createTopic("MyTopic");
			
			producer = session.createProducer(topic);
			TextMessage message = session.createTextMessage();
			for (int i = 0; i < numMsgs; i++) {
				message.setText("This is 刘岩  message " + (i + 1));
				LOG.info("Sending message: " + message.getText());
				producer.send(message);
			}
		} catch (JMSException e) {
			LOG.info("Exception occurred: " + e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
				}
			}
		}

		// stop the TCP broker
		try {
			broker.stop();
		} catch (Exception e) {
			LOG.info("stop the broker failed: " + e);
		}
	}

}
