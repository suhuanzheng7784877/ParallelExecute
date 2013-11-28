package org.para.distributed.dto;

import java.util.List;

import org.para.execute.model.TaskProperty;

/**
 * 任务要分发到结点机器的实体信息
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-22
 * @Copyright: 2013 story All rights reserved.
 */
public class TaskTargetWorker extends MqMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * 选择的目标节点
	 */
	private WorkerNode workerNode;

	/**
	 * 分布式任务id
	 */
	private long jobId;

	/**
	 * 每一个节点上可能具有多个任务，当任务并行度大于节点个数的时候，则排在前面的节点可能接受更多的子任务
	 */
	private List<TaskProperty> taskPropertyList;

	public TaskTargetWorker() {
		super();
	}

	public TaskTargetWorker(long jobId, List<TaskProperty> taskPropertyList,
			WorkerNode workerNode) {
		super();
		this.jobId = jobId;
		this.taskPropertyList = taskPropertyList;
		this.workerNode = workerNode;
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

	public List<TaskProperty> getTaskPropertyList() {
		return taskPropertyList;
	}

	public void setTaskPropertyList(List<TaskProperty> taskPropertyList) {
		this.taskPropertyList = taskPropertyList;
	}

	@Override
	public String toString() {
		return "TaskTargetWorker [workerNode=" + workerNode + ", jobId="
				+ jobId + ", taskPropertyList=" + taskPropertyList + "]";
	}

}
