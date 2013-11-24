package org.para.distributed.util;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.jms.PureJMSProducer;

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

	/**
	 * 发送topic消息
	 * 
	 * @param 消息目的
	 *            :topic
	 * @param 消息体
	 *            :messageObject
	 * @return
	 */
	public static boolean sendTopicMessage(String topic,
			Serializable messageObject) {

		return true;
	}

}
