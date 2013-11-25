package org.para.distributed.util;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.util.PropertiesUtil;

/**
 * MQ消息接收者
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-24 上午10:07:58
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public abstract class MQReceiver<T extends Serializable> {

	private static final Log LOG = LogFactory.getLog(MQReceiver.class);

	final static String MQ_BrokerURL = PropertiesUtil.getValue("mq.brokerURL");

	// TODO:写入配置
	private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
			MQ_BrokerURL);

	private Connection receiverConnection = null;
	private Session session = null;

	private final static long Sleep_Interval = 5 * 60 * 1000L;
	protected volatile boolean IS_STOP_Receiver = true;

	public MQReceiver() {

		init();

	}

	/**
	 * 初始化
	 */
	protected void init() {
		try {
			receiverConnection = connectionFactory.createConnection();
			receiverConnection.start();
			session = receiverConnection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
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
	public void receiverTopicMessage(String topicName) {
		Topic topic = null;
		try {
			topic = session.createTopic(topicName);
			// 注册消息消费者
			MessageConsumer comsumer = session.createConsumer(topic);
			comsumer.setMessageListener(new MessageListener() {
				@SuppressWarnings("unchecked")
				public void onMessage(Message message) {
					try {

						// 对象类型的信息
						ObjectMessage objectMessage = ((ObjectMessage) message);

						// 将对象信息序列化
						T objectMessageSerializable = (T) objectMessage
								.getObject();

						LOG.info("start handle");

						// 处理逻辑
						handle(objectMessageSerializable);

					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});

			while (IS_STOP_Receiver) {
				Thread.sleep(Sleep_Interval);
			}

		} catch (JMSException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
				if (receiverConnection != null) {
					receiverConnection.stop();
					receiverConnection.close();
					receiverConnection = null;
				}
			} catch (JMSException e) {
				LOG.error("error", e);
			}

		}

	}

	/**
	 * 处理逻辑
	 * 
	 * @param message
	 * @return
	 */
	protected abstract boolean handle(T message);

}
