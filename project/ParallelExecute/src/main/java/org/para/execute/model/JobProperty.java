package org.para.execute.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-25
 * @Copyright: 2013 story All rights reserved.
 */
public class JobProperty implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	private long jobId;

	private TaskProperty[] taskPropertyArray;

	private boolean jobExecuteResult;

	/**
	 * 
	 * @param jobId
	 * @param taskPropertyArray
	 * @param jobExecuteResult
	 */
	public JobProperty(long jobId, TaskProperty[] taskPropertyArray,
			boolean jobExecuteResult) {
		super();
		this.jobId = jobId;
		this.taskPropertyArray = taskPropertyArray;
		this.jobExecuteResult = jobExecuteResult;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public TaskProperty[] getTaskPropertyArray() {
		return taskPropertyArray;
	}

	public void setTaskPropertyArray(TaskProperty[] taskPropertyArray) {
		this.taskPropertyArray = taskPropertyArray;
	}

	public boolean isJobExecuteResult() {
		return jobExecuteResult;
	}

	public void setJobExecuteResult(boolean jobExecuteResult) {
		this.jobExecuteResult = jobExecuteResult;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "JobProperty [jobId=" + jobId + ", taskPropertyArray="
				+ Arrays.toString(taskPropertyArray) + ", jobExecuteResult="
				+ jobExecuteResult + "]";
	}

}
