package org.para.distributed.master;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.para.execute.task.ParallelTask;

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
	public final static Map<String, List<ParallelTask<?>>> distributedParallelTaskMap = new ConcurrentHashMap<String, List<ParallelTask<?>>>(
			64, 0.75F);

}
