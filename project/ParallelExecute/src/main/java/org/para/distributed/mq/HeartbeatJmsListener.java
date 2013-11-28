package org.para.distributed.mq;

import org.apache.log4j.Logger;
import org.para.distributed.dto.MqMessage;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.master.WorkerManagers;

/**
 * 接收结点注册
 * 
 * @author liuyan
 * 
 */
public class HeartbeatJmsListener extends AbstractJmsRecive {

	private static Logger logger = Logger.getLogger(HeartbeatJmsListener.class);

	public HeartbeatJmsListener() {
		logger.info("init HeartbeatJmsListener");
	}

	/**
	 * 处理注册的逻辑
	 */
	@Override
	public void handleJmsMessage(MqMessage mqMessage) {

		// 此段代码如果能归到父类就更好了
		WorkerNode workerNode = (WorkerNode) mqMessage;

		logger.info("HeartbeatJmsListener Message:" + workerNode);

		WorkerManagers.addOrReplaceWorkerNode(workerNode);
	}

}
