package org.para.distributed.mq;

import java.io.Serializable;
import java.util.List;

import org.para.distributed.dto.WorkerNode;
import org.para.execute.task.ParallelTask;

/**
 * 任务要分发到结点机器的实体信息
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-22
 * @Copyright: 2013 story All rights reserved.
 */
public class TaskTargetWorker implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ParallelTask<?>> parallelTaskList;

	private WorkerNode workerNode;

	public TaskTargetWorker() {
		super();
	}

	public TaskTargetWorker(List<ParallelTask<?>> parallelTaskList,
			WorkerNode workerNode) {
		super();
		this.parallelTaskList = parallelTaskList;
		this.workerNode = workerNode;
	}

	public List<ParallelTask<?>> getParallelTaskList() {
		return parallelTaskList;
	}

	public void setParallelTaskList(List<ParallelTask<?>> parallelTaskList) {
		this.parallelTaskList = parallelTaskList;
	}

	public WorkerNode getWorkerNode() {
		return workerNode;
	}

	public void setWorkerNode(WorkerNode workerNode) {
		this.workerNode = workerNode;
	}

}
