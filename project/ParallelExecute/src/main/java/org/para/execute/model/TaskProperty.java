package org.para.execute.model;

import java.io.Serializable;

import org.para.enums.TaskCycle;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-23
 * @Copyright: 2013 story All rights reserved.
 */
public final class TaskProperty implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskProperty() {

	}

	/**
	 * 
	 * @param 总大小
	 *            ：countBlock
	 * @param 当前索引位置
	 *            ：currentBlockIndex
	 * @param 数据块大小
	 *            ：blockSize
	 */
	public TaskProperty(int taskId, int countBlock, int currentBlockIndex,
			int currentBlockSize,int averageBlockSize) {
		this.taskId = taskId;
		this.countBlock = countBlock;
		this.currentBlockIndex = currentBlockIndex;
		this.currentBlockSize = currentBlockSize;
		this.averageBlockSize = averageBlockSize;
	}

	private int taskId;

	private TaskCycle taskCycle = TaskCycle.TASK_INIT;

	private int countBlock;

	private int currentBlockIndex;

	private int currentBlockSize;
	
	private int averageBlockSize;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public TaskCycle getTaskCycle() {
		return taskCycle;
	}

	public void setTaskCycle(TaskCycle taskCycle) {
		this.taskCycle = taskCycle;
	}

	public int getCountBlock() {
		return countBlock;
	}

	public void setCountBlock(int countBlock) {
		this.countBlock = countBlock;
	}

	public int getCurrentBlockIndex() {
		return currentBlockIndex;
	}

	public void setCurrentBlockIndex(int currentBlockIndex) {
		this.currentBlockIndex = currentBlockIndex;
	}

	public int getCurrentBlockSize() {
		return currentBlockSize;
	}

	public void setCurrentBlockSize(int currentBlockSize) {
		this.currentBlockSize = currentBlockSize;
	}

	public int getAverageBlockSize() {
		return averageBlockSize;
	}

	public void setAverageBlockSize(int averageBlockSize) {
		this.averageBlockSize = averageBlockSize;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + taskId;
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
		TaskProperty other = (TaskProperty) obj;
		if (taskId != other.taskId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskProperty [taskId=" + taskId + ", taskCycle=" + taskCycle
				+ ", countBlock=" + countBlock + ", currentBlockIndex="
				+ currentBlockIndex + ", currentBlockSize=" + currentBlockSize
				+ ", averageBlockSize=" + averageBlockSize + "]";
	}

}
