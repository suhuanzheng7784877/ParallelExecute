package org.para.execute.task;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import org.para.enums.TaskCycle;
import org.para.execute.model.TaskProperty;
import org.para.trace.event.ParallelEvent;
import org.para.trace.listener.FailEventListener;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-23
 * @Copyright: 2013 story All rights reserved.
 */
public abstract class ParallelTask<T> implements Runnable,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected TaskProperty taskProperty = null;
	protected T targetObject = null;
	protected CountDownLatch countDownLatch = null;
	protected FailEventListener failEventListener = null;

	public ParallelTask() {
		super();
	}

	public ParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, T targetObject) {
		super();
		this.countDownLatch = countDownLatch;
		this.taskProperty = taskProperty;
		this.targetObject = targetObject;
	}

	public ParallelTask(TaskProperty taskProperty, T targetObject,
			CountDownLatch countDownLatch, FailEventListener failEventListener) {
		super();
		this.taskProperty = taskProperty;
		this.targetObject = targetObject;
		this.countDownLatch = countDownLatch;
		this.failEventListener = failEventListener;
	}

	@Override
	public void run() {

		int blockSize = taskProperty.getBlockSize();
		int countBlock = taskProperty.getCountBlock();
		int currentBlockIndex = taskProperty.getCurrentBlockIndex();
		try {
			this.execute(targetObject, blockSize, countBlock, currentBlockIndex);
		} catch (Exception runtimeException) {
			taskProperty.setTaskCycle(TaskCycle.TASK_ERROR);
			runtimeException.printStackTrace();
			if (failEventListener != null) {
				ParallelEvent parallelEvent = new ParallelEvent(
						runtimeException);
				failEventListener.callStrategy(parallelEvent);
			}
		}
		taskProperty.setTaskCycle(TaskCycle.TASK_SUCCEED);
		countDownLatch.countDown();
	}

	/**
	 * execute task logic
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract int execute(T sourceJobObject, int blockSize,
			int countBlock, int currentBlockIndex) throws Exception;

	@Override
	public String toString() {
		return "ParallelTask [taskProperty=" + taskProperty + ", targetObject="
				+ targetObject + ", countDownLatch=" + countDownLatch
				+ ", failEventListener=" + failEventListener + "]";
	}

}
