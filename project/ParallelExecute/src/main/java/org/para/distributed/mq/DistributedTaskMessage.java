package org.para.distributed.mq;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 分发到mq的消息实体
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-22
 * @Copyright: 2013 story All rights reserved.
 */
public class DistributedTaskMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long jobId;

	private TaskTargetWorker[] taskTargetWorker;

	public DistributedTaskMessage() {
		super();
	}

	public DistributedTaskMessage(long jobId,
			TaskTargetWorker[] taskTargetWorker) {
		super();
		this.jobId = jobId;
		this.taskTargetWorker = taskTargetWorker;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (jobId ^ (jobId >>> 32));
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
		DistributedTaskMessage other = (DistributedTaskMessage) obj;
		if (jobId != other.jobId)
			return false;
		return true;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public TaskTargetWorker[] getTaskTargetWorker() {
		return taskTargetWorker;
	}

	public void setTaskTargetWorker(TaskTargetWorker[] taskTargetWorker) {
		this.taskTargetWorker = taskTargetWorker;
	}

	@Override
	public String toString() {
		return "DistributedTaskMessage [jobId=" + jobId + ", taskTargetWorker="
				+ Arrays.toString(taskTargetWorker) + "]";
	}

}
