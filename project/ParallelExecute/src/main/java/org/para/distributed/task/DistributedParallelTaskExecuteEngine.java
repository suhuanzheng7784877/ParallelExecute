package org.para.distributed.task;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.para.distributed.memcache.ProgressContextMemcache;
import org.para.util.jar.ExtendsClassLoaderFacade;

/**
 * 分布式任务在子节点的执行引擎
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-7 下午4:28:51
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class DistributedParallelTaskExecuteEngine {

	private static Logger log = Logger
			.getLogger(DistributedParallelTaskExecuteEngine.class);

	/**
	 * 启动任务进程的入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		log.info("DistributedParallelTaskExecuteEngine running");

		if (args == null || 0 == args.length || 3 != args.length) {
			// 参数不对
			System.out.print("1001");
			return;
		}

		// 1-获取相关参数
		// 任务ID
		String jobIdStr = args[0];
		String taskIdStr = args[1];
		String jarHttpURI = args[2];

		try {
			// 2-进行子任务的类加载
			ExtendsClassLoaderFacade.addSelfJarFile(jarHttpURI);

			// 3-从memcache中取出属于该任务的task类
			DistributedParallelTask distributedParallelTask = ProgressContextMemcache
					.getDistributedParallelTask(jobIdStr, taskIdStr);

			CountDownLatch countDownLatch = new CountDownLatch(1);
			distributedParallelTask.setCountDownLatch(countDownLatch);
			// 4-执行task的入口方法
			new Thread(distributedParallelTask).start();
			countDownLatch.await();

			// 5-执行结束后返回信息给slave控制台
			System.out.print("1000");

		} catch (IOException e) {
			e.printStackTrace();
			log.error("error", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error", e);
		}

	}

}
