package org.para.distributed.mq;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.para.distributed.dto.MqMessage;
import org.springframework.jms.core.MessageCreator;

public class StartJobJmsSend extends AbstractJmsSend {

	private static Logger logger = Logger.getLogger(StartJobJmsSend.class);

	/**
	 * 启动任务的特定方法
	 * 
	 * @param jarHttpURI
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
				objectMessage.setObject(abstractMessageBean);

				return objectMessage;
			}
		});
		logging();
	}

	@Override
	protected void logging() {
		logger.info("StartJobJmsSend");
	}

}
