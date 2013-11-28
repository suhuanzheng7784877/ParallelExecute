package org.para.distributed.master;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.constant.MQConstant;
import org.para.distributed.mq.RegisterAndHeartbeatWorkerMQReceiver;
import org.para.util.PropertiesUtil;

/**
 * 管理机器服务
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-27
 * @Copyright: 2013 story All rights reserved.
 */
public class MasterServer {

	/**
	 * 是否处于运行时状态
	 */
	public volatile static boolean Is_Runing = true;

	private final static long interval = Long.parseLong(PropertiesUtil
			.getValue("server.sleep.interval"));

	private static final Log LOG = LogFactory.getLog(MasterServer.class);

	public static void main(String[] args) {
		startMaster();
	}

	public static void startMaster() {
		Is_Runing = true;

		startReceiverListener();
		LOG.info("管理结点开始守护..");
		while (Is_Runing) {
			// 发送心跳
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOG.error("error", e);
			}
		}
		LOG.info("管理结点结束守护..");
		System.exit(0);

	}

	/**
	 * 停止master
	 */
	public static void stopMaster() {
		Is_Runing = false;
	}

	/**
	 * 启动MQ的消费者监听器
	 */
	private static void startReceiverListener() {
		
		LOG.info("启动监听消息-注册");
		
		// 1-启动分布式任务的接收器
		RegisterAndHeartbeatWorkerMQReceiver registerAndHeartbeatWorkerMQReceiver1 = new RegisterAndHeartbeatWorkerMQReceiver();
		registerAndHeartbeatWorkerMQReceiver1
				.receiverQueueMessage(MQConstant.REGISTER_WORKER_Queue_Destination);
		
		LOG.info("启动监听消息-心跳");
		// 2-启动分布式任务的接收器
		RegisterAndHeartbeatWorkerMQReceiver registerAndHeartbeatWorkerMQReceiver2 = new RegisterAndHeartbeatWorkerMQReceiver();
		registerAndHeartbeatWorkerMQReceiver2
				.receiverQueueMessage(MQConstant.HEARTBEAT_WORKER_Queue_Destination);


	}

}
