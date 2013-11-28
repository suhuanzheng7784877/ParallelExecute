package org.para.distributed.mq;

import java.io.Serializable;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.para.distributed.dto.MqMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 发送JMS消息的抽象父类
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-28
 * @Copyright: 2013 story All rights reserved.
 */
public abstract class AbstractJmsSend {

	protected JmsTemplate template;

	protected Destination destinationSend;

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public Destination getDestinationSend() {
		return destinationSend;
	}

	public void setDestinationSend(Destination destinationSend) {
		this.destinationSend = destinationSend;
	}

	/**
	 * 发送JMS消息的抽象模版方法
	 * 
	 * @param abstractMessageBean
	 */
	public void sendJms(final MqMessage abstractMessageBean) {

		// 设置消息的传输类型-此时是非持久化的
		template.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		template.setExplicitQosEnabled(false);

		// 发送消息
		template.send(destinationSend, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {

				ObjectMessage objectMessage = session.createObjectMessage();

				objectMessage.setObject((Serializable) abstractMessageBean);

				return objectMessage;
			}
		});
		logging();
	}

	protected abstract void logging();

}
