package org.para.distributed.slave;

import org.apache.log4j.Logger;
import org.para.distributed.mq.AbstractJmsSend;

/**
 * 发送心跳消息2.9.5
 * 
 * @author liuyan
 */
public class HeartbeatMessageAction extends AbstractJmsSend {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void logging() {
		logger.info(("success send HeartbeatMessage JMS"));
	}

}
