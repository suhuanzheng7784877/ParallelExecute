package org.para.distributed.master;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

		startListener();

		while (Is_Runing) {
			// 发送心跳
			try {
				Thread.sleep(interval);
				startListener();
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOG.error("error", e);
			}
		}
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
	private static void startListener() {

	}

}
