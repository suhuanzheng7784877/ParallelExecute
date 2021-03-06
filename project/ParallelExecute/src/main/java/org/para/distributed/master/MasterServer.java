package org.para.distributed.master;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.para.constant.ParaConstant;
import org.para.distributed.thrift.server.DistributedParalleExecuteTHsHaServer;
import org.para.trace.fail.strategy.DefaultFailHandleStrategy;
import org.para.trace.listener.DefaultFailEventListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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

	private static final Log LOG = LogFactory.getLog(MasterServer.class);

	/**
	 * 是否处于运行时状态
	 */
	public volatile static boolean Is_Runing = true;

	// 初始化
	public final static ApplicationContext MasterApplicationContext = new FileSystemXmlApplicationContext(
			new String[] { "/" + ParaConstant.PE_CONF
					+ "/applicationContext-master.xml" });

	/**
	 * 默认的任务执行异常跟踪器
	 */
	static DefaultFailEventListener defaultFailEventListener = DefaultFailEventListener
			.getInstance(DefaultFailHandleStrategy.getInstance());

	public static void main(String[] args) {

		// 启动master server
		startMaster();
	}

	/**
	 * 程序入口
	 * 
	 * @param args
	 */
	public static void startMaster() {
		Is_Runing = true;
		LOG.info("master starting...");
		DistributedParalleExecuteTHsHaServer.getInstence().startTHsHaServer();
		LOG.info("master started");
		System.exit(0);
	}

	/**
	 * 停止master
	 */
	public static void stopMaster() {
		LOG.info("stopTHsHaServer");
		DistributedParalleExecuteTHsHaServer.getInstence().stopTHsHaServer();
	}

}
