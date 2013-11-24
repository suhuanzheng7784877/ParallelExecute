package org.para.distributed.util;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MQ消息发送者
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-24 上午10:07:43
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class MQSender {

	private static final Log LOG = LogFactory.getLog(MQSender.class);

	private static ConnectionFactory connectionFactory = null;
	private static Connection connection = null;
	private static Session session = null;

	static {

		// TODO:写入配置
		connectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			e.printStackTrace();
			LOG.error("error", e);
		}

	}

	/**
	 * 发送topic消息
	 * 
	 * @param 消息目的
	 *            :topicName
	 * @param 消息体
	 *            :messageObject
	 * @return
	 * @throws JMSException
	 */
	public static boolean sendTopicMessage(String topicName,
			Serializable messageObject) {
		Topic topic = null;
		try {
			topic = session.createTopic(topicName);
			MessageProducer producer = session.createProducer(topic);
			ObjectMessage objectMessage = session.createObjectMessage();
			producer.send(objectMessage);
			return true;
		} catch (JMSException e) {
			e.printStackTrace();
			LOG.error("error", e);
			return false;
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					LOG.error("error", e);
					return false;
				}
			}
		}

	}

}
