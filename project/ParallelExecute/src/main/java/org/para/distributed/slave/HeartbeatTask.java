package org.para.distributed.slave;

import java.net.UnknownHostException;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.util.SystemUtil;

/**
 * 继承TimerTask，具备定时任务的特性
 * 
 * @author liuyan
 * 
 */
public class HeartbeatTask extends TimerTask {

	private static Logger logger = Logger.getLogger(HeartbeatTask.class);

	HeartbeatMessageAction heartbeatMessageAction = WorkerServer.WorkApplicationContext
			.getBean("HeartbeatMessageAction", HeartbeatMessageAction.class);

	public void execute() {

		// 2-构建结点信息
		WorkerNode workerNode = buildNodeInfo();

		logger.info("Heartbeat nodeInfo:" + workerNode);

		// 3-准备发送指令
		logger.info("prepare send heartbeat.....");

		heartbeatMessageAction.sendJms(workerNode);
	}

	@Override
	public void run() {
		execute();
	}

	/**
	 * 构建结点信息，包括应用实例集合信息
	 * 
	 * @return
	 */
	private WorkerNode buildNodeInfo() {

		// 获取结点硬件信息
		WorkerNode workerNode = null;
		try {

			// 心跳Node信息
			workerNode = SystemUtil.getWorkerNode(false);

		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return workerNode;
	}

}
