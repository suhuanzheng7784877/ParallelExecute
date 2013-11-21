package org.para.distributed.master;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.para.constant.ParaConstant;
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
	 * exe big Paralle Job template
	 * <p>
	 * same this.exeParalleJob(sourceObject, blockNum, 0, null, objects)
	 * 
	 * @param sourceObject
	 * @param blockNum
	 * @param objects
	 * @return
	 * @throws ParallelException
	 */
	public JobProperty exeParalleJob(T sourceObject, int blockNum,
			Object... objects) throws ParallelException {
		return this.exeParalleJob(sourceObject, blockNum, 0, null, objects);
	}

	/**
	 * exe big Paralle Job template
	 * <p>
	 * same return this.exeParalleJob(sourceObject, blockNum, timeOut,
	 * null,objects);
	 * 
	 * @param sourceObject
	 * @param blockNum
	 * @param objects
	 * @return
	 * @throws ParallelException
	 */
	public JobProperty exeParalleJob(T sourceObject, int blockNum,
			long timeOut, Object... objects) throws ParallelException {
		return this.exeParalleJob(sourceObject, blockNum, timeOut, null,
				objects);
	}

	/**
	 * exe big Paralle Job template
	 * <p>
	 * same return this.exeParalleJob(sourceObject, blockNum, 0,
	 * failEventListener,objects);
	 * 
	 * @param sourceObject
	 * @param blockNum
	 * @param failEventListener
	 * @param objects
	 * @return
	 * @throws ParallelException
	 */
	public JobProperty exeParalleJob(T sourceObject, int blockNum,
			FailEventListener failEventListener, Object... objects)
			throws ParallelException {
		return this.exeParalleJob(sourceObject, blockNum, 0, failEventListener,
				objects);
	}

	/**
	 * exe big Paralle Job template,this is croe execute logic
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
		init(sourceObject,objects);
		long jobId = System.nanoTime();
		TaskProperty[] taskPropertyArray = splitJob(sourceObject, blockNum);
		countDownLatch = new CountDownLatch(taskPropertyArray.length);
		execute(taskPropertyArray, sourceObject, failEventListener, objects);
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
	 * init job about some prepare work
	 * 
	 * @param objects
	 */
	protected abstract void init(T sourceObject,Object... objects);

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
	 * execute Parallel task
	 * 
	 * @param taskPropertyArray
	 * @param targetObject
	 * @param failEventListener
	 * @param objects
	 */
	private void execute(TaskProperty[] taskPropertyArray, T targetObject,
			FailEventListener failEventListener, Object... objects) {
		ParallelTask<T> parallelTask = null;

		int taskPropertyArrayLength = taskPropertyArray.length;
		for (int i = 0; i < taskPropertyArrayLength; i++) {
			taskPropertyArray[i].setTaskCycle(TaskCycle.TASK_RUNNING);
			parallelTask = buildParallelTask(countDownLatch,
					taskPropertyArray[i], targetObject, failEventListener,
					objects);
			new Thread(parallelTask).start();
		}
	}

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

		sb.append("job:").append(jobId).append(":execute job result [")
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
