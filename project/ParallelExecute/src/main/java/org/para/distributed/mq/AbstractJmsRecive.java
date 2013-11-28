package org.para.distributed.mq;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

/**
 * 监听JMS消息的抽象父类
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-28
 * @Copyright: 2013 story All rights reserved.
 */
public abstract class AbstractJmsRecive implements MessageListener {

	private static Logger logger = Logger.getLogger(AbstractJmsRecive.class);

	public void onMessage(Message message) {
		logger.info("AbstractJmsRecive~");
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Serializable serializable = objectMessage.getObject();

			DistributedTaskMessage abstractMessageBean = (DistributedTaskMessage) serializable;

			logger.info("DistributedTaskMessage消息:" + abstractMessageBean);

			handleJmsMessage(abstractMessageBean);

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public abstract void handleJmsMessage(
			DistributedTaskMessage abstractMessageBean);

}
