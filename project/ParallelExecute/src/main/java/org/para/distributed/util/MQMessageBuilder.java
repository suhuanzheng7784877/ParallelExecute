package org.para.distributed.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.para.distributed.dto.DistributedTaskMessage;
import org.para.distributed.dto.TaskTargetWorker;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.master.WorkerManagers;
import org.para.distributed.task.DistributedParallelTask;
import org.para.execute.model.TaskProperty;

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

	private static Logger logger = Logger.getLogger(MQMessageBuilder.class);

	/**
	 * 分发任务逻辑
	 * TODO:此处算法有待优化
	 * @param jobId
	 * @param taskList
	 */
	public static DistributedTaskMessage buildDistributeTasks(long jobId,
			List<DistributedParallelTask> taskList) {

		DistributedTaskMessage distributedTaskMessage = null;

		// 并行度
		int parallelNum = taskList.size();

		// 选出最靠前的几个节点
		// 优化选择算法
		List<WorkerNode> workerNodeList = WorkerManagers
				.selectTopFreeWorkerNode(parallelNum);

		// 候选节点的个数
		int workerNodeListSize = workerNodeList.size();

		// 拼装mq任务
		TaskTargetWorker[] taskTargetWorkerArray = new TaskTargetWorker[workerNodeListSize];
		TaskTargetWorker taskTargetWorker = null;
		WorkerNode workerNode = null;
		TaskProperty taskProperty = null;
		
		//TODO:选择算法需要重构
		if (workerNodeListSize == parallelNum) {
			// 备选结点机器个数=任务分发个数
			for (int i = 0; i < parallelNum; i++) {
				taskProperty = taskList.get(i).getTaskProperty();
				workerNode = workerNodeList.get(i);

				List<TaskProperty> taskPropertyList = new ArrayList<TaskProperty>(
						1);
				taskPropertyList.add(taskProperty);

				taskTargetWorker = new TaskTargetWorker(jobId,
						taskPropertyList, workerNode);
				taskTargetWorkerArray[i] = taskTargetWorker;
			}

		} else if (workerNodeListSize < parallelNum) {
			// 备选结点机器个数<任务分发个数

			int taskTargetWorkerArrayIndex = 0;
			for (int i = 0; i < parallelNum; i++, taskTargetWorkerArrayIndex++) {

				taskProperty = taskList.get(i).getTaskProperty();

				if (i >= workerNodeListSize) {
					taskTargetWorkerArrayIndex = (i - workerNodeListSize) > taskTargetWorkerArray.length - 1 ? taskTargetWorkerArray.length - 1
							: i - workerNodeListSize;

					taskTargetWorker = taskTargetWorkerArray[taskTargetWorkerArrayIndex];
					taskTargetWorker.getTaskPropertyList().add(taskProperty);
				} else {
					workerNode = workerNodeList.get(taskTargetWorkerArrayIndex);

					List<TaskProperty> taskPropertyList = new ArrayList<TaskProperty>(
							3);

					taskPropertyList.add(taskProperty);
					taskTargetWorker = new TaskTargetWorker(jobId,
							taskPropertyList, workerNode);
					taskTargetWorkerArray[taskTargetWorkerArrayIndex] = taskTargetWorker;
				}

			}

		} else {
			// 中间挑选备选节点，以及任务并行度个数，出错了.不可能出现备选节点个数小于任务并行度的情况
		}
		distributedTaskMessage = new DistributedTaskMessage(jobId,
				taskTargetWorkerArray);

		logger.info("buildDistributeTasks:" + distributedTaskMessage);

		return distributedTaskMessage;
	}

}
