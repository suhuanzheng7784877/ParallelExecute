package org.para.distributed.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.para.distributed.dto.MqMessage;

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

	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;		
		try {
			//Serializable serializable = objectMessage.getObject();
			MqMessage abstractMessageBean = (MqMessage) objectMessage.getObject();
			handleJmsMessage(abstractMessageBean);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public abstract void handleJmsMessage(MqMessage abstractMessageBean);

}
