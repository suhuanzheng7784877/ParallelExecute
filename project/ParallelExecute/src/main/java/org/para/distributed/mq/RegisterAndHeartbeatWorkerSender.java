package org.para.distributed.mq;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.para.constant.MQConstant;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.util.MQSender;
import org.para.distributed.util.SystemUtil;

/**
 * 发送工作结点注册和心跳消息
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-27
 * @Copyright: 2013 story All rights reserved.
 */
public class RegisterAndHeartbeatWorkerSender {

	private static Logger logger = Logger
			.getLogger(RegisterAndHeartbeatWorkerSender.class);

	private static boolean sendMessage(String Destination, WorkerNode workerNode) {
		MQSender mqSender = new MQSender();
		return mqSender.sendQueueMessage(Destination, workerNode);
	}

	/**
	 * 发送注册节点的消息
	 * 
	 * @return
	 */
	public static boolean registerWorker() {
		WorkerNode workerNode = null;
		try {
			workerNode = SystemUtil.getWorkerNode(true);
			return sendMessage(MQConstant.REGISTER_WORKER_Queue_Destination,
					workerNode);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return false;
	}

	/**
	 * 发送注册节点的消息
	 * 
	 * @return
	 */
	public static boolean heartbeatWorker() {

		WorkerNode workerNode = null;
		try {
			workerNode = SystemUtil.getWorkerNode(false);
			return sendMessage(MQConstant.HEARTBEAT_WORKER_Queue_Destination,
					workerNode);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return false;

	}

}
