package org.para.distributed.slave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.distributed.mq.RegisterWorkerSender;
import org.para.util.PropertiesUtil;

/**
 * 结点机器服务
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-27
 * @Copyright: 2013 story All rights reserved.
 */
public class WorkerServer {

	/**
	 * 是否处于运行时状态
	 */
	public volatile static boolean Is_Runing = true;

	private static final Log LOG = LogFactory.getLog(WorkerServer.class);

	private final static long interval = Long.parseLong(PropertiesUtil
			.getValue("server.sleep.interval"));

	public static void main(String[] args) {
		startWork();
	}

	public static void startWork() {
		Is_Runing = true;
		// 发送注册节点的消息
		RegisterWorkerSender.registerWorker();
		startListener();
		while (Is_Runing) {
			// 发送心跳
			try {
				Thread.sleep(interval);
				RegisterWorkerSender.heartbeatWorker();
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOG.error("error", e);
			}
		}
		System.exit(0);

	}

	/**
	 * 启动MQ的消费者监听器
	 */
	private static void startListener() {

	}

	public static void stopWork() {
		Is_Runing = false;
	}

}
