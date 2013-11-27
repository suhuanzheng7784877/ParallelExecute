package org.para.distributed.mq;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.para.constant.MQConstant;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.util.MQSender;
import org.para.distributed.util.SystemUtil;

/**
 * 发送工作结点注册消息
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-27
 * @Copyright: 2013 story All rights reserved.
 */
public class RegisterWorkerSender {

	private static Logger logger = Logger.getLogger(RegisterWorkerSender.class);

	/**
	 * 发送注册节点的消息
	 * 
	 * @return
	 */
	public static boolean registerWorker() {
		MQSender mqSender = new MQSender();
		boolean result = false;
		// 注册节点实例
		try {
			WorkerNode workerNode = SystemUtil.getWorkerNode(true);
			result = mqSender.sendQueueMessage(
					MQConstant.REGISTER_WORKER_Queue_Destination, workerNode);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return result;

	}
	
	/**
	 * 发送注册节点的消息
	 * 
	 * @return
	 */
	public static boolean heartbeatWorker() {
		MQSender mqSender = new MQSender();
		boolean result = false;
		// 注册节点实例
		try {
			WorkerNode workerNode = SystemUtil.getWorkerNode(false);
			result = mqSender.sendQueueMessage(
					MQConstant.REGISTER_WORKER_Queue_Destination, workerNode);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return result;

	}

}
