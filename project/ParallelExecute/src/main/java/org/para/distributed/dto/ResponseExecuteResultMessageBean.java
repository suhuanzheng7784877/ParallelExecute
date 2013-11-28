package org.para.distributed.dto;

import org.para.execute.model.TaskProperty;

/**
 * 执行任务反馈给master的消息bean
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-1 下午1:03:12
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class ResponseExecuteResultMessageBean extends MqMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long jobId;

	private WorkerNode workerNode;

	private TaskProperty taskProperty;

	public ResponseExecuteResultMessageBean(long jobId, WorkerNode workerNode,
			TaskProperty taskProperty) {
		super();
		this.jobId = jobId;
		this.workerNode = workerNode;
		this.taskProperty = taskProperty;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public WorkerNode getWorkerNode() {
		return workerNode;
	}

	public void setWorkerNode(WorkerNode workerNode) {
		this.workerNode = workerNode;
	}

	public TaskProperty getTaskProperty() {
		return taskProperty;
	}

	public void setTaskProperty(TaskProperty taskProperty) {
		this.taskProperty = taskProperty;
	}

	@Override
	public String toString() {
		return "ResponseExecuteResultMessageBean [jobId=" + jobId
				+ ", workerNode=" + workerNode + ", taskProperty="
				+ taskProperty + "]";
	}

}
