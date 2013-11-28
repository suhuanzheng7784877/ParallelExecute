package org.para.distributed.memcache;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.log4j.Logger;
import org.para.distributed.task.DistributedParallelTask;
import org.para.util.PropertiesUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 进程上下文操作,使用memcache作为进程上下文信息传递
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-12 下午1:27:14
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public final class ProgressContextMemcache {

	private static Logger logger = Logger
			.getLogger(ProgressContextMemcache.class);

	// memcache server
	public final static String memcacheServerURI = PropertiesUtil
			.getValue("memcache1.server");

	// memcache port
	public final static String memcacheServerPort = PropertiesUtil
			.getValue("memcache1.port");

	// memcache clean time out
	public final static int memcacheCleanTimeout = Integer
			.parseInt(PropertiesUtil.getValue("memcache1.clean.timeout"));

//	/**
//	 * 将子节点执行的任务上下文需要的配置放到memcache中，以便在子task中取出相关上下文配置信息,master进行put
//	 * 
//	 * @param jobId
//	 * @param sourceObjectConf
//	 * @return
//	 */
//	public static boolean putTaskMapConf(long jobId,
//			Map<String, String> sourceObjectConf) {
//		return putTaskMapConf("" + jobId, sourceObjectConf);
//	}
//
//	/**
//	 * 将子节点执行的任务上下文需要的配置放到memcache中，以便在子task中取出相关上下文配置信息,master进行put
//	 * 
//	 * @param jobId
//	 * @param sourceObjectConf
//	 * @return
//	 */
//	public static boolean putTaskMapConf(String jobId,
//			Map<String, String> sourceObjectConf) {
//		boolean result = false;
//		MemcachedClient memcachedClient = null;
//		try {
//			memcachedClient = initSprintMemcacheClient();
//			result = memcachedClient.set(jobId, memcacheCleanTimeout,
//					sourceObjectConf);
//		} catch (MemcachedException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} finally {
//			try {
//				memcachedClient.shutdown();
//				memcachedClient = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//				logger.error("error", e);
//			}
//		}
//		return result;
//
//	}

	/**
	 * 删除分布式子任务
	 * 
	 * @param jobId
	 * @param taskId
	 * @return
	 */
	public static boolean removeDistributedParallelTask(long jobId, int taskId) {
		return removeDistributedParallelTask("" + jobId, "" + taskId);
	}

	/**
	 * 删除分布式子任务
	 * 
	 * @param jobId
	 * @param taskId
	 * @return
	 */
	public static boolean removeDistributedParallelTask(String jobId, String taskId) {
		MemcachedClient memcachedClient = null;
		boolean result = false;
		try {
			memcachedClient = initSprintMemcacheClient();
			String key = jobId + "" + taskId;
			result = memcachedClient.delete(key);
		} catch (MemcachedException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (TimeoutException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("error", e);
		} finally {
			try {
				memcachedClient.shutdown();
				memcachedClient = null;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("error", e);
			}
		}
		return result;
	}

	/**
	 * 将执行任务task也放进memcache中
	 * 
	 * @param jobId
	 * @param taskId
	 * @param distributedParallelTask
	 * @return
	 */
	public static boolean putDistributedParallelTask(long jobId, int taskId,
			DistributedParallelTask distributedParallelTask) {
		return putDistributedParallelTask("" + jobId, "" + taskId, distributedParallelTask);
	}

	/**
	 * 将执行任务task也放进memcache中
	 * 
	 * @param jobId
	 * @param taskId
	 * @param distributedParallelTask
	 * @return
	 */
	public static boolean putDistributedParallelTask(String jobId, String taskId,
			DistributedParallelTask distributedParallelTask) {
		boolean result = false;
		MemcachedClient memcachedClient = null;
		try {
			memcachedClient = initSprintMemcacheClient();
			String key = jobId + ":" + taskId;
			result = memcachedClient.set(key, memcacheCleanTimeout,
					distributedParallelTask);
		} catch (MemcachedException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (TimeoutException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("error", e);
		} finally {
			try {
				memcachedClient.shutdown();
				memcachedClient = null;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("error", e);
			}
		}
		return result;

	}

	/**
	 * 获取子任务task
	 * 
	 * @param jobId
	 * @param taskId
	 * @return
	 */
	public static DistributedParallelTask getDistributedParallelTask(
			long jobId, int taskId) {
		return getDistributedParallelTask("" + jobId, "" + taskId);
	}

	/**
	 * 获取子任务task
	 * 
	 * @param jobId
	 * @param taskId
	 * @return
	 */
	public static DistributedParallelTask getDistributedParallelTask(
			String jobId, String taskId) {

		MemcachedClient memcachedClient = null;
		try {
			memcachedClient = initMemcacheClient();
			String key = jobId + ":" + taskId;
			DistributedParallelTask distributedParallelTask = memcachedClient
					.get(key);
			return distributedParallelTask;
		} catch (MemcachedException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (TimeoutException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("error", e);
		} finally {
			try {
				memcachedClient.shutdown();
				memcachedClient = null;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("error", e);
			}
		}
		return null;

	}

//	/**
//	 * 将子节点执行的任务上下文需要的配置从memcache中取出，以便在子task中取出相关上下文配置信息,slave的执行引擎进行get
//	 * 
//	 * @param 任务
//	 *            :jobId
//	 * @return
//	 */
//	public static Map<String, String> getTaskMapConf(long jobId) {
//		return getTaskMapConf("" + jobId);
//	}
//
//	/**
//	 * 将子节点执行的任务上下文需要的配置从memcache中取出，以便在子task中取出相关上下文配置信息,slave的执行引擎进行get
//	 * 
//	 * @param 任务
//	 *            :jobId
//	 * @return
//	 */
//	public static Map<String, String> getTaskMapConf(String jobId) {
//		MemcachedClient memcachedClient = null;
//		Map<String, String> value = null;
//		try {
//			memcachedClient = initMemcacheClient();
//			value = memcachedClient.get(jobId);
//		} catch (MemcachedException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} catch (IOException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} finally {
//			try {
//				memcachedClient.shutdown();
//				memcachedClient = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//				logger.error("error", e);
//			}
//		}
//
//		return value;
//	}

//	/**
//	 * 根据jobid删除memcache中的数据
//	 * 
//	 * @param 任务
//	 *            :jobId
//	 * @return
//	 */
//	public static boolean removeTaskMapConf(long jobId) {
//		return removeTaskMapConf("" + jobId);
//
//	}
//
//	/**
//	 * 根据jobid删除memcache中的数据
//	 * 
//	 * @param 任务
//	 *            :jobId
//	 * @return
//	 */
//	public static boolean removeTaskMapConf(String jobId) {
//
//		MemcachedClient memcachedClient = null;
//		boolean result = false;
//		try {
//			memcachedClient = initSprintMemcacheClient();
//			result = memcachedClient.delete(jobId);
//		} catch (MemcachedException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			logger.error("error", e);
//		} finally {
//			try {
//				memcachedClient.shutdown();
//				memcachedClient = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//				logger.error("error", e);
//			}
//		}
//		return result;
//	}

	/**
	 * 从spring容器获取memcache客户端
	 * 
	 * @return
	 */
	private static MemcachedClient initSprintMemcacheClient() {

		// 初始化master的配置文件
		ApplicationContext MasterApplicationContext = new ClassPathXmlApplicationContext(
				new String[] { "/applicationContext-master.xml" });

		MemcachedClient memcachedClient = null;
		memcachedClient = MasterApplicationContext.getBean("memcachedClient",
				MemcachedClient.class);
		return memcachedClient;

	}

	/**
	 * 自己构建memcache客户端
	 * 
	 * @return
	 * @throws IOException
	 */
	private static MemcachedClient initMemcacheClient() throws IOException {
		// 构建client端的链接
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(memcacheServerURI + ":"
						+ memcacheServerPort));
		MemcachedClient memcachedClient = builder.build();
		return memcachedClient;
	}

}
