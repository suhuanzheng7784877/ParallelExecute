package org.para.distributed.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.para.constant.ParaConstant;
import org.para.distributed.dto.DistributedTaskMessage;
import org.para.distributed.memcache.ProgressContextMemcache;
import org.para.distributed.mq.StartJobJmsSend;
import org.para.distributed.task.DistributedParallelTask;
import org.para.distributed.util.MQMessageBuilder;
import org.para.enums.TaskCycle;
import org.para.exception.ParallelException;
import org.para.execute.model.JobProperty;
import org.para.execute.model.TaskProperty;
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
public abstract class DistributedParallelExecute {

	private static Logger logger = Logger
			.getLogger(DistributedParallelExecute.class);

	public CountDownLatch countDownLatch = null;

	/**
	 * 发送topic的jms消息的发送器,直接从spring里面取得
	 */
	public StartJobJmsSend startJobJmsSend = MasterServer.MasterApplicationContext
			.getBean("startJobJmsSend", StartJobJmsSend.class);

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	public StartJobJmsSend getStartJobJmsSend() {
		return startJobJmsSend;
	}

	public void setStartJobJmsSend(StartJobJmsSend startJobJmsSend) {
		this.startJobJmsSend = startJobJmsSend;
	}

	/**
	 * 初始化分布式任务，init Distributed job about some prepare work
	 * 
	 * @param sourceObjectConf
	 */
	protected abstract void init(Map<String, String> sourceObjectConf);

	/**
	 * 执行分布式任务逻辑,exe big Paralle Job template,this is croe execute logic
	 * 
	 * @param jarHttpURI
	 * @param sourceObjectConf
	 * @param parallelism_hint
	 * @return
	 * @throws ParallelException
	 */
	public JobProperty exeParalleJob(String jarHttpURI,
			Map<String, String> sourceObjectConf, int parallelism_hint)
			throws ParallelException {
		return this.exeParalleJob(jarHttpURI, sourceObjectConf,
				parallelism_hint, 0, null);
	}

	/**
	 * 带有失败监听的分布式执行器,exe big Paralle Job template,this is croe execute logic
	 * 
	 * @param jarHttpURI
	 * @param sourceObjectConf
	 * @param parallelism_hint
	 * @param failEventListener
	 * @return
	 * @throws ParallelException
	 */
	public JobProperty exeParalleJob(String jarHttpURI,
			Map<String, String> sourceObjectConf, int parallelism_hint,
			FailEventListener failEventListener) throws ParallelException {
		return this.exeParalleJob(jarHttpURI, sourceObjectConf,
				parallelism_hint, 0, failEventListener);
	}

	/**
	 * 带有失败监听,超时器的分布式执行器,,exe big Paralle Job template,this is croe execute
	 * logic
	 * 
	 * @param jarHttpURI
	 * @param sourceObjectConf
	 * @param parallelism_hint
	 * @param timeOut
	 * @param failEventListener
	 * @return
	 * @throws ParallelException
	 */
	public JobProperty exeParalleJob(String jarHttpURI,
			Map<String, String> sourceObjectConf, int parallelism_hint,
			long timeOut, FailEventListener failEventListener)
			throws ParallelException {

		logger.info("exe Distributed Paralle Job");

		// 没有任何资源节点执行任务
		if (!WorkerManagers.isHaveWorkerNodeToExecuteDistributed()) {
			logger.info("there is not any one workernode to Execute Distributed task");
			return null;
		}

		// 做一些初始化的动作
		init(sourceObjectConf);

		// 将一个大的任务分割成为一个个小的任务属性数组
		TaskProperty[] taskPropertyArray = splitJob(sourceObjectConf,
				parallelism_hint);

		// 建立计数器
		countDownLatch = new CountDownLatch(taskPropertyArray.length);

		// 生成jobid作为标识
		long jobId = System.nanoTime();

		// 进行执行逻辑,这里实际上是将任务进行分发，分发到各个子节点去执行
		execute(jarHttpURI, jobId, taskPropertyArray, sourceObjectConf,
				failEventListener);
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
	 * 将一个大的任务分割成为一个个小的任务属性数组,split big Job to TaskProperty Arrys
	 * 
	 * @param sourceObjectConf
	 * @param 并行度
	 *            :parallelism_hint
	 * @return
	 */
	protected TaskProperty[] splitJob(Map<String, String> sourceObjectConf,
			int parallelism_hint) {

		// count
		int resultCount = analyzeResultCount(sourceObjectConf);

		// actual block Num
		int currentBlockNum = (parallelism_hint <= 0 ? ParaConstant.DefaultFileBlockNum
				: parallelism_hint);

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
	 * 分析原始任务源的大小，这个需要具体的插件子类自己实现,analyze source job count Result num
	 * 
	 * @param sourceObjectConf
	 * @return
	 */
	protected abstract int analyzeResultCount(
			Map<String, String> sourceObjectConf);

	/**
	 * 构建一个分布式任务实体
	 * 
	 * @param countDownLatch
	 * @param taskProperty
	 * @param srcObject
	 * @param failEventListener
	 * @param objects
	 * @return
	 */
	protected abstract DistributedParallelTask buildDistributedParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			Map<String, String> sourceObjectConf,
			FailEventListener failEventListener);

	/**
	 * execute Parallel task
	 * 
	 * @param taskPropertyArray
	 * @param targetObject
	 * @param failEventListener
	 * @param objects
	 */
	private void execute(String jarHttpURI, long jobId,
			TaskProperty[] taskPropertyArray,
			Map<String, String> sourceObjectConf,
			FailEventListener failEventListener) {

		// 之前已经分割的子任务个数
		int taskPropertyArrayLength = taskPropertyArray.length;

		//
		List<DistributedParallelTask> distributedParallelTaskList = new ArrayList<DistributedParallelTask>();

		DistributedParallelTask distributedParallelTask = null;

		// 将任务进行List形式的重组
		for (int i = 0; i < taskPropertyArrayLength; i++) {
			taskPropertyArray[i].setTaskCycle(TaskCycle.TASK_RUNNING);
			distributedParallelTask = buildDistributedParallelTask(
					countDownLatch, taskPropertyArray[i], sourceObjectConf,
					failEventListener);
			distributedParallelTask.setJobId(jobId);
			ProgressContextMemcache.putDistributedParallelTask(jobId,
					taskPropertyArray[i].getTaskId(), distributedParallelTask);
			distributedParallelTaskList.add(distributedParallelTask);

		}

		// 构建分布式驱动消息
		DistributedTaskMessage distributedTaskMessage = MQMessageBuilder
				.buildDistributeTasks(jobId, distributedParallelTaskList);
		distributedTaskMessage.setJarHttpURI(jarHttpURI);

		logger.info("putTaskMapConf :" + distributedTaskMessage);

		// 调用mq接口进行分发
		logger.info("send distributedTaskMessage :" + distributedTaskMessage);

		startJobJmsSend.sendJms(distributedTaskMessage);

		// 任务池暂存正在[运行的任务]
		DistributedTaskManagers.getDistributedTaskManagerInstence()
				.putParallelTaskList(jobId, distributedParallelTaskList);

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
