package org.para.distributed.mq;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.slave.RegisterMessageAction;
import org.para.distributed.slave.WorkerServer;
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
public class RegisterWorkerSender implements Runnable {

	private static Logger logger = Logger.getLogger(RegisterWorkerSender.class);

	RegisterMessageAction registerMessageAction = WorkerServer.WorkApplicationContext
			.getBean("RegisterMessageAction", RegisterMessageAction.class);

	@Override
	public void run() {

		try {

			// 先睡5秒钟
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		try {
			WorkerNode workerNode = SystemUtil.getWorkerNode(false);

			registerMessageAction.sendJms(workerNode);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("error", e);
		}

	}

}
