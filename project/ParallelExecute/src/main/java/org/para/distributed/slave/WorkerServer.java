package org.para.distributed.slave;

import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.constant.ParaConstant;
import org.para.constant.ParaConstant.SlaveConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 工作结点机器服务
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

	private final static Log LOG = LogFactory.getLog(WorkerServer.class);

	// 初始化
	public final static ApplicationContext WorkApplicationContext = new FileSystemXmlApplicationContext(
			new String[] { "/" + ParaConstant.PE_CONF
					+ "/applicationContext-slave.xml" });

	/**
	 * 程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		startWorker();
	}

	/**
	 * 启动worker节点
	 */
	public static void startWorker() {

		LOG.info("starting slave.......");

		startTask();

		while (Is_Runing) {
			// 发送心跳
			try {
				Thread.sleep(SlaveConstant.SLEEP_TIME);

			} catch (InterruptedException e) {
				e.printStackTrace();
				LOG.error("error", e);
			}
		}
		LOG.info("over slave");
		System.exit(0);

	}

	/**
	 * 停止节点守护进程
	 */
	public static void stopWorker() {
		Is_Runing = false;
	}

	/**
	 * 启动任务
	 */
	private static void startTask() {

		// 启动结点机器注册任务
		startRegisterTask();

		// 启动结点心跳任务
		startHeartbeatTask();
	}

	/**
	 * 启动结点机器注册任务
	 */
	private static void startRegisterTask() {
		// 启动结点机器注册任务
		new Thread(new RegisterTask()).start();
	}

	/**
	 * 启动结点心跳任务
	 */
	private static void startHeartbeatTask() {

		Timer timer = new Timer();

		// 第2个参数是几毫秒后开始，第3个参数是每隔几毫秒进行一次任务的执行
		timer.schedule(new HeartbeatTask(), SlaveConstant.WATI_Heartbeat_TIME,
				SlaveConstant.Heartbeat_TIME);

	}

}
