package org.para.distributed.master;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.para.distributed.task.DistributedParallelTask;

/**
 * 分布式任务的管理
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-16 下午9:09:26
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class DistributedTaskManagers {

	// 并行任务的暂存区域
	private final Map<Long, List<DistributedParallelTask>> distributedParallelTaskMap = new ConcurrentHashMap<Long, List<DistributedParallelTask>>(
			128, 0.75F);

	private static DistributedTaskManagers instence = new DistributedTaskManagers();

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static DistributedTaskManagers getDistributedTaskManagerInstence() {

		return instence;
	}

	/**
	 * 添加分布式任务
	 * 
	 * @param key
	 * @param taskList
	 */
	public void putParallelTaskList(Long key,
			List<DistributedParallelTask> taskList) {
		distributedParallelTaskMap.put(key, taskList);
	}
	
	/**
	 * 获取分布式的所有任务
	 * @return
	 */
	public Map<Long, List<DistributedParallelTask>> getDistributedParallelTaskMap() {
		return distributedParallelTaskMap;
	}
	
	/**
	 * 获取一个分布式任务的所有子任务
	 * @param jobId
	 * @return
	 */
	public List<DistributedParallelTask> getDistributedParallelTaskList(
			long jobId) {
		return getDistributedParallelTaskMap().get(jobId);
	}

}
