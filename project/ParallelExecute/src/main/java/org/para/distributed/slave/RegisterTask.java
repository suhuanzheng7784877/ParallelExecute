package org.para.distributed.slave;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.util.SystemUtil;
import org.para.util.PropertiesUtil;

/**
 * 结点注册任务
 * 
 * @author liuyan
 */
public class RegisterTask implements Runnable {

	private static Logger logger = Logger.getLogger(RegisterTask.class);

	RegisterMessageAction registerMessageAction = WorkerServer.WorkApplicationContext
			.getBean("RegisterMessageAction", RegisterMessageAction.class);

	// 等待发送注册指令的时间
	private static final long WATI_START_TIME = Long.parseLong(PropertiesUtil
			.getValue("worker.wait.heartbeat.time"));

	/**
	 * 执行结点注册逻辑
	 */
	public void execute() {

		try {

			// 先睡5秒钟
			Thread.sleep(WATI_START_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		// 获取结点硬件信息
		WorkerNode workerNode = null;
		try {
			workerNode = SystemUtil.getWorkerNode(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// 准备发送指令
		logger.info("prepare send register.....");

		// 发送注册指令
		logger.info("send register");
		registerMessageAction.sendJms(workerNode);
	}

	public void run() {

		execute();

	}
}
