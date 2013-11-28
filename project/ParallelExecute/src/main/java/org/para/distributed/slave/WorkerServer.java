package org.para.distributed.slave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.constant.MQConstant;
import org.para.distributed.mq.RegisterAndHeartbeatWorkerSender;
import org.para.distributed.mq.StartDistributeTaskMQReceiver;
import org.para.util.PropertiesUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

	// 初始化
	public final static ApplicationContext NodeApplicationContext = new ClassPathXmlApplicationContext(
			new String[] { "/applicationContext-jms.xml" });

	public static void main(String[] args) {
		startWorker();
	}

	/**
	 * 启动worker节点
	 */
	public static void startWorker() {
		while (Is_Runing) {
			// 发送心跳
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOG.error("error", e);
			}
		}
		LOG.info("工作结点结束守护..");
		System.exit(0);

	}

	public static void stopWorker() {
		Is_Runing = false;
	}

}
