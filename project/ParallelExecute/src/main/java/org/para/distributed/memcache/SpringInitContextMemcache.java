package org.para.distributed.memcache;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.log4j.Logger;
import org.para.distributed.master.MasterServer;
import org.para.distributed.task.DistributedParallelTask;

/**
 * 进程上下文操作,使用memcache作为进程上下文信息传递-Spring创建，给Master使用
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-12 下午1:27:14
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class SpringInitContextMemcache {

	private static Logger logger = Logger
			.getLogger(SpringInitContextMemcache.class);

	// TODO:此处的memcache客户端需要使用池化管理,池化的使用
	private static final XMemcachedClientBuilder xMemcachedClientBuilder = MasterServer.MasterApplicationContext
			.getBean("memcachedClientBuilder", XMemcachedClientBuilder.class);

	private static MemcachedClient memcachedClient = null;

	static {
		try {
			memcachedClient = xMemcachedClientBuilder.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		return putDistributedParallelTask("" + jobId, "" + taskId,
				distributedParallelTask);
	}

	/**
	 * 将执行任务task也放进memcache中
	 * 
	 * @param jobId
	 * @param taskId
	 * @param distributedParallelTask
	 * @return
	 */
	public static boolean putDistributedParallelTask(String jobId,
			String taskId, DistributedParallelTask distributedParallelTask) {
		boolean result = false;
		try {
			String key = jobId + ":" + taskId;

			long threadId = Thread.currentThread().getId();
			System.out.println("threadId:" + threadId
					+ "++++++++memcachedClient+++++++::"
					+ memcachedClient.toString());
			result = memcachedClient.set(key,
					ProgressContextMemcache.memcacheCleanTimeout,
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return result;

	}

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
	public static boolean removeDistributedParallelTask(String jobId,
			String taskId) {
		boolean result = false;
		try {
			String key = jobId + "" + taskId;
			long threadId = Thread.currentThread().getId();
			System.out.println("threadId:" + threadId
					+ "++++++++memcachedClient+++++++::"
					+ memcachedClient.toString());

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
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return result;
	}

}
