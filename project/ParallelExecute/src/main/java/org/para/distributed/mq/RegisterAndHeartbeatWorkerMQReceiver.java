package org.para.distributed.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.distributed.dto.WorkerNode;
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
public class RegisterAndHeartbeatWorkerMQReceiver extends
		MQReceiver<WorkerNode> {

	private static final Log LOG = LogFactory
			.getLog(RegisterAndHeartbeatWorkerMQReceiver.class);

	public RegisterAndHeartbeatWorkerMQReceiver() {
		super();
	}

	@Override
	protected boolean handle(WorkerNode message) {
		LOG.info("message:" + message);
		return false;
	}

}