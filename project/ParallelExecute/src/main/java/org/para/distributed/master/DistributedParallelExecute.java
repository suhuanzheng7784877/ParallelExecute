package org.para.distributed.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.para.constant.ParaConstant;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.mq.DistributedTaskMessage;
import org.para.distributed.mq.TaskTargetWorker;
import org.para.enums.TaskCycle;
import org.para.exception.ParallelException;
import org.para.execute.model.JobProperty;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.trace.listener.FailEventListener;
import org.para.util.MessageOutUtil;

/**
 * 分布式任务抽象模板父类
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-16 下午6:20:49
 * @Copyright: 2013 story All rights reserved.
 * 
 * @param <T>
 */
public abstract class DistributedParallelExecute<T extends Serializable> {

	public CountDownLatch countDownLatch = null;

	/**
	 * init job about some prepare work
	 * 
	 * @param objects
	 */
	protected abstract void init(T sourceObject, Object... objects);

	/**
	 * exe big Paralle Job template,this is croe execute logic
	 * 
	 * @param sourceObject
	 * @param blockNum
	 * @param timeOut
	 * @param failEventListener
	 * @param objects
	 * @return
	 * @throws ParallelException
	 */
	public JobProperty exeParalleJob(T sourceObject, int blockNum,
			long timeOut, FailEventListener failEventListener,
			Object... objects) throws ParallelException {
		init(sourceObject, objects);
		long jobId = System.nanoTime();
		TaskProperty[] taskPropertyArray = splitJob(sourceObject, blockNum);
		countDownLatch = new CountDownLatch(taskPropertyArray.length);
		execute(jobId, taskPropertyArray, sourceObject, failEventListener,
				objects);
		try {

			if (timeOut > 0) {
				countDownLatch.await(timeOut, TimeUnit.SECONDS);
			} else {
				countDownLatch.await();
			}
		} catch (InterruptedException interruptedException) {
			throw new ParallelException(interruptedException);
		}
		return joinJob(jobId, taskPropertyArray);
	}

	/**
	 * split big Job to TaskProperty Arrys
	 * 
	 * @param targetObject
	 * @param blockNum
	 * @return
	 */
	protected TaskProperty[] splitJob(T srcObject, int blockNum) {

		// count
		int resultCount = analyzeResultCount(srcObject);

		// actual block Num
		int currentBlockNum = (blockNum <= 0 ? ParaConstant.DefaultFileBlockNum
				: blockNum);

		int averageBlockSize = resultCount / currentBlockNum;
		int lastBlockSize = averageBlockSize + resultCount % currentBlockNum;

		TaskProperty[] taskPropertyArray = new TaskProperty[currentBlockNum];
		for (int i = 0; i < currentBlockNum; i++) {

			if (i == currentBlockNum - 1) {
				taskPropertyArray[i] = new TaskProperty(i + 1, resultCount, i,
						lastBlockSize);
				break;
			}

			taskPropertyArray[i] = new TaskProperty(i + 1, resultCount, i,
					averageBlockSize);
		}

		return taskPropertyArray;
	}

	/**
	 * analyze source job count Result num
	 * 
	 * @param srcObject
	 * @return
	 */
	protected abstract int analyzeResultCount(T srcObject);

	/**
	 * build one Parallel Task
	 * 
	 * @param countDownLatch
	 * @param taskProperty
	 * @param srcObject
	 * @param failEventListener
	 * @param objects
	 * @return
	 */
	protected abstract ParallelTask<T> buildParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			T srcObject, FailEventListener failEventListener, Object... objects);

	/**
	 * execute Parallel task
	 * 
	 * @param taskPropertyArray
	 * @param targetObject
	 * @param failEventListener
	 * @param objects
	 */
	private void execute(long jobId, TaskProperty[] taskPropertyArray,
			T targetObject, FailEventListener failEventListener,
			Object... objects) {

		int taskPropertyArrayLength = taskPropertyArray.length;

		List<ParallelTask<?>> taskList = new CopyOnWriteArrayList<ParallelTask<?>>();
		ParallelTask<?> parallelTask = null;
		for (int i = 0; i < taskPropertyArrayLength; i++) {
			taskPropertyArray[i].setTaskCycle(TaskCycle.TASK_RUNNING);
			parallelTask = buildParallelTask(countDownLatch,
					taskPropertyArray[i], targetObject, failEventListener,
					objects);
			taskList.add(parallelTask);
		}

		DistributedTaskManagers.putParallelTaskList(jobId, taskList);
		distributeTasks(jobId, taskList);
	}

	/**
	 * 分发任务逻辑
	 * 
	 * @param jobId
	 * @param taskList
	 */
	public DistributedTaskMessage distributeTasks(long jobId,
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

	/**
	 * final collect execute result
	 * 
	 * @param jobId
	 * @param taskPropertyArray
	 * @return
	 */
	protected JobProperty joinJob(long jobId, TaskProperty[] taskPropertyArray) {

		boolean executeResult = true;
		TaskCycle taskCycle = null;
		int countTaskNum = 0;
		int successTaskNum = 0;
		int errorTaskNum = 0;
		for (TaskProperty taskProperty : taskPropertyArray) {
			taskCycle = taskProperty.getTaskCycle();
			if (taskCycle != TaskCycle.TASK_SUCCEED) {
				int taskId = taskProperty.getTaskId();
				MessageOutUtil.SystemOutPrint(jobId + ":" + taskId
						+ ":executeResult is false");
				executeResult = false;
				errorTaskNum++;
			} else {
				successTaskNum++;
			}
		}
		StringBuilder sb = new StringBuilder(128);

		sb.append("Distributed job:").append(jobId)
				.append(":execute Distributed job result [")
				.append(executeResult).append("],countTaskNum=")
				.append(countTaskNum).append(",successTaskNum=")
				.append(successTaskNum).append(",errorTaskNum=")
				.append(errorTaskNum);

		MessageOutUtil.SystemOutPrint(sb);

		JobProperty jobProperty = new JobProperty(jobId, taskPropertyArray,
				executeResult);

		return jobProperty;
	}

}
