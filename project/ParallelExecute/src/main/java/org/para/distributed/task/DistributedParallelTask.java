package org.para.distributed.task;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.para.execute.model.TaskProperty;
import org.para.trace.event.ParallelEvent;
import org.para.trace.listener.FailEventListener;

/**
 * 分布式的子任务实体
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-23
 * @Copyright: 2013 story All rights reserved.
 */
public abstract class DistributedParallelTask implements Runnable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected long jobId = 0L;
	protected TaskProperty taskProperty = null;
	protected Map<String, String> targetObjectConf = null;
	protected transient CountDownLatch countDownLatch = null;
	protected FailEventListener failEventListener = null;

	public DistributedParallelTask() {
		super();
	}

	public DistributedParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, Map<String, String> targetObjectConf) {
		super();
		this.countDownLatch = countDownLatch;
		this.taskProperty = taskProperty;
		this.targetObjectConf = targetObjectConf;
	}

	public DistributedParallelTask(TaskProperty taskProperty,
			Map<String, String> targetObjectConf,
			CountDownLatch countDownLatch, FailEventListener failEventListener) {
		super();
		this.taskProperty = taskProperty;
		this.targetObjectConf = targetObjectConf;
		this.countDownLatch = countDownLatch;
		this.failEventListener = failEventListener;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	public TaskProperty getTaskProperty() {
		return taskProperty;
	}

	public void setTaskProperty(TaskProperty taskProperty) {
		this.taskProperty = taskProperty;
	}

	@Override
	public void run() {

		int averageBlockSize = taskProperty.getAverageBlockSize();
		int currentBlockSize = taskProperty.getCurrentBlockSize();
		int countBlock = taskProperty.getCountBlock();
		int currentBlockIndex = taskProperty.getCurrentBlockIndex();
		try {
			this.execute(targetObjectConf, currentBlockSize, countBlock,
					currentBlockIndex,averageBlockSize);
			countDownLatch.countDown();
		} catch (Exception runtimeException) {
			runtimeException.printStackTrace();
			if (failEventListener != null) {
				ParallelEvent parallelEvent = new ParallelEvent(this);
				failEventListener.callStrategy(parallelEvent);
			}
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (jobId ^ (jobId >>> 32));
		result = prime * result
				+ ((taskProperty == null) ? 0 : taskProperty.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistributedParallelTask other = (DistributedParallelTask) obj;
		if (jobId != other.jobId)
			return false;
		if (taskProperty == null) {
			if (other.taskProperty != null)
				return false;
		} else if (!taskProperty.equals(other.taskProperty))
			return false;
		return true;
	}

	/**
	 * execute task logic
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract int execute(Map<String, String> targetObjectConf,
			int currentBlockSize, int countBlock, int currentBlockIndex,int averageBlockSize)
			throws Exception;

	@Override
	public String toString() {
		return "DistributedParallelTask [jobId=" + jobId + ", taskProperty="
				+ taskProperty + ", targetObjectConf=" + targetObjectConf
				+ ", failEventListener=" + failEventListener + "]";
	}

}
