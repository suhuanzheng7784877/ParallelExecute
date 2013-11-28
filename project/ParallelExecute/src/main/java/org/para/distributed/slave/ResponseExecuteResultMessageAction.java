package org.para.distributed.slave;

import org.apache.log4j.Logger;
import org.para.distributed.mq.AbstractJmsSend;

/**
 * 发送注册消息2.9.4
 * 
 * @author liuyan
 */
public class ResponseExecuteResultMessageAction extends AbstractJmsSend {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void logging() {
		logger.info(("sucess send ResponseExecuteResultMessageAction JMS"));
	}

}
