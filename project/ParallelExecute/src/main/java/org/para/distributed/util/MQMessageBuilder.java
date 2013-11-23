package org.para.distributed.util;

import java.util.ArrayList;
import java.util.List;

import org.para.distributed.dto.WorkerNode;
import org.para.distributed.master.WorkerManagers;
import org.para.distributed.mq.DistributedTaskMessage;
import org.para.distributed.mq.TaskTargetWorker;
import org.para.execute.task.ParallelTask;

/**
 * 构造mq消息实体的辅助类
 *
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-23 下午4:46:46
 * @Copyright: 2013 story All rights reserved.
 *
 */
public class MQMessageBuilder {
	
	/**
	 * 分发任务逻辑
	 * 
	 * @param jobId
	 * @param taskList
	 */
	public static DistributedTaskMessage buildDistributeTasks(long jobId,
			List<ParallelTask<?>> taskList) {

		DistributedTaskMessage distributedTaskMessage = null;

		// 并行度
		int parallelNum = taskList.size();

		// 选出最靠前的几个节点
		List<WorkerNode> workerNodeList = WorkerManagers
				.selectTopFreeWorkerNode(parallelNum);

		// 候选节点的个数
		int workerNodeListSize = workerNodeList.size();

		// TODO:拼装mq任务
		TaskTargetWorker[] taskTargetWorkerArray = new TaskTargetWorker[workerNodeListSize];
		TaskTargetWorker taskTargetWorker = null;
		WorkerNode workerNode = null;
		ParallelTask<?> parallelTask = null;
		if (workerNodeListSize == parallelNum) {
			// 备选结点机器个数=任务分发个数
			for (int i = 0; i < parallelNum; i++) {
				parallelTask = taskList.get(i);
				workerNode = workerNodeList.get(i);

				List<ParallelTask<?>> parallelTaskList = new ArrayList<ParallelTask<?>>(
						1);
				parallelTaskList.add(parallelTask);

				taskTargetWorker = new TaskTargetWorker(parallelTaskList,
						workerNode);
				taskTargetWorkerArray[i] = taskTargetWorker;
			}

		} else if (workerNodeListSize < parallelNum) {
			// 备选结点机器个数<任务分发个数

			int taskTargetWorkerArrayIndex = 0;
			for (int i = 0; i < parallelNum; i++, taskTargetWorkerArrayIndex++) {

				parallelTask = taskList.get(i);
				if (i >= workerNodeListSize) {
					taskTargetWorkerArrayIndex = i - workerNodeListSize;
					taskTargetWorker = taskTargetWorkerArray[taskTargetWorkerArrayIndex];
					taskTargetWorker.getParallelTaskList().add(parallelTask);
				} else {
					workerNode = workerNodeList.get(taskTargetWorkerArrayIndex);

					List<ParallelTask<?>> parallelTaskList = new ArrayList<ParallelTask<?>>(
							3);

					parallelTaskList.add(parallelTask);
					taskTargetWorker = new TaskTargetWorker(parallelTaskList,
							workerNode);
					taskTargetWorkerArray[taskTargetWorkerArrayIndex] = taskTargetWorker;
				}

			}

		} else {
			// 中间挑选备选节点，以及任务并行度个数，出错了.不可能出现备选节点个数小于任务并行度的情况
		}
		distributedTaskMessage = new DistributedTaskMessage(jobId,
				taskTargetWorkerArray);

		return distributedTaskMessage;
	}

}
