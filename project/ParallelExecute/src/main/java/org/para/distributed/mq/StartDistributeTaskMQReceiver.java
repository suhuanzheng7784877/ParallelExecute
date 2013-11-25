package org.para.distributed.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.distributed.util.MQReceiver;

/**
 * 启动分布式任务的接收器
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-26 下午8:33:00
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class StartDistributeTaskMQReceiver extends
		MQReceiver<DistributedTaskMessage> {

	private static final Log LOG = LogFactory
			.getLog(StartDistributeTaskMQReceiver.class);

	public StartDistributeTaskMQReceiver() {
		super();
	}

	@Override
	protected boolean handle(DistributedTaskMessage message) {
		LOG.info("start handle");
		LOG.info("message:" + message);
		return false;
	}

}
