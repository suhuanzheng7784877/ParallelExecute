package org.para.constant;

import org.para.util.PropertiesUtil;

/**
 * FileConstant
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-26
 * @Copyright: 2013 story All rights reserved.
 */
public interface ParaConstant {

	/**
	 * 默认的子任务并行度，操作系统的核数
	 */
	public final static int DefaultBlockNum = Runtime.getRuntime()
			.availableProcessors();

	// default file read/write buffer
	public final static int DefaultFileBufferSize = 1024 * 1;

	// default File Block Num
	public final static int DefaultFileBlockNum = DefaultBlockNum;

	// default DB Block Num
	public final static int DefaultDBBlockNum = DefaultBlockNum;

	// default DB Block Num
	public final static int DefaultDBBatchNum = 20000;

	/**
	 * 默认的分布式任务上下文key的前置字符串
	 */
	public final static String SystemMapConfFrontStr = "org.para.constant.";

	public final static String Distributed_Properties = "distributed.properties";

	public final static String PE_CONF = System.getProperty("pe.conf");

	// memcache server
	public final static String memcacheServerURI = PropertiesUtil
			.getValue("memcache1.server");

	// memcache port
	public final static String memcacheServerPort = PropertiesUtil
			.getValue("memcache1.port");

	// memcache clean time out
	public final static int memcacheCleanTimeout = Integer
			.parseInt(PropertiesUtil.getValue("memcache1.clean.timeout"));

	/**
	 * Master结点使用的相关的常量
	 * 
	 * @author liuyan
	 * @Email:suhuanzheng7784877@163.com
	 * @version 0.1
	 * @Date: 2013-12-18 下午9:35:00
	 * @Copyright: 2013 story All rights reserved.
	 * 
	 */
	public static class MasterConstant {

	}

	/**
	 * Slave结点使用的相关的常量
	 * 
	 * @author liuyan
	 * @Email:suhuanzheng7784877@163.com
	 * @version 0.1
	 * @Date: 2013-12-18 下午9:34:44
	 * @Copyright: 2013 story All rights reserved.
	 * 
	 */
	public static class SlaveConstant {
		/**
		 * 睡眠时间
		 */
		public final static long SLEEP_TIME = Long.parseLong(PropertiesUtil
				.getValue("worker.sleep.interval"));

		
		/**
		 * 等待心跳任务启动的时间
		 */
		public final static long WATI_Heartbeat_TIME = Long
				.parseLong(PropertiesUtil
						.getValue("worker.wait.heartbeat.time"));

		/**
		 * 心跳的时间频率
		 */
		public final static long Heartbeat_TIME = Long.parseLong(PropertiesUtil
				.getValue("worker.heartbeat.interval"));

		public static final String worker_heap_xmx = PropertiesUtil
				.getValue("worker.heap.xmx");
		public static final String worker_heap_xms = PropertiesUtil
				.getValue("worker.heap.xms");
		public static final String worker_stack_xss = PropertiesUtil
				.getValue("worker.stack.xss");
		public static final String worker_PermSize = PropertiesUtil
				.getValue("worker.PermSize");
		public static final String worker_MaxPermSize = PropertiesUtil
				.getValue("worker.MaxPermSize");
		
		/**
		 * work线程池个数
		 */
		public static final int worker_start_threadpool_size = Integer
				.parseInt(PropertiesUtil
						.getValue("worker.start.tasks.threadpool.size"));
		
		/**
		 * 执行子任务的引擎类
		 */
		public static final String EngineClassName = PropertiesUtil
				.getValue("worker.EngineClassName");
		
		/**
		 * 启动任务JVM的前缀字符串:java -cp 等等这些一般项目
		 */
		public static final StringBuilder StartJVMPrefix = new StringBuilder(512);
		
		/**
		 * 初始化前缀字符串
		 */
		static {
			StartJVMPrefix.append("java -Dpe.conf=").append(ParaConstant.PE_CONF)
			.append(" -Djava.ext.dirs='")
			.append(System.getProperty("java.ext.dirs")).append("' ")
			.append(" -Xms").append(SlaveConstant.worker_heap_xms)
			.append(" -Xmx").append(SlaveConstant.worker_heap_xmx)
			.append(" -Xss").append(SlaveConstant.worker_stack_xss)
			.append(" -XX:PermSize=").append(SlaveConstant.worker_PermSize)
			.append(" -XX:MaxPermSize=")
			.append(SlaveConstant.worker_MaxPermSize).append(" ")
			.append(SlaveConstant.EngineClassName).append(" ");
		}
		
		
		public final static String encode = "GBK";

		public final static String SUCESS_CODE = "1000";

		public final static String ERROR_CODE = "1001";
		

	}

}
