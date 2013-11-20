package org.para.distributed.dto;

import java.io.Serializable;

/**
 * 节点机器相关信息
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-16 下午2:50:10
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class WorkerNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 节点机器相关信息
	 */
	public WorkerNode() {
		super();
	}

	/**
	 * 节点机器相关信息
	 * 
	 * @param 节点ip
	 *            :workerIp
	 * @param 剩余磁盘
	 *            :freedisk
	 * @param 剩余内存
	 *            :freememroy
	 * @param 创建时间
	 *            :createtime
	 * @param 上次心跳时间
	 *            :lasthearttime
	 * @param cpu持久空闲率
	 *            :cpufreerate
	 */
	public WorkerNode(String workerIp, int freedisk, Integer freememroy,
			long createtime, long lasthearttime, float cpufreerate) {
		super();
		this.workerIp = workerIp;
		this.freedisk = freedisk;
		this.freememroy = freememroy;
		this.createtime = createtime;
		this.lasthearttime = lasthearttime;
		this.cpufreerate = cpufreerate;
	}

	/**
	 * 节点ip
	 */
	private String workerIp;

	/**
	 * 剩余硬盘
	 */
	private int freedisk;

	/**
	 * 剩余内存
	 */
	private Integer freememroy;

	/**
	 * 创建时间
	 */
	private long createtime;

	/**
	 * 上次心跳时间
	 */
	private long lasthearttime;

	/**
	 * cpu闲置率
	 */
	private float cpufreerate;

	public String getWorkerIp() {
		return workerIp;
	}

	public void setWorkerIp(String workerIp) {
		this.workerIp = workerIp;
	}

	public int getFreedisk() {
		return freedisk;
	}

	public void setFreedisk(int freedisk) {
		this.freedisk = freedisk;
	}

	public Integer getFreememroy() {
		return freememroy;
	}

	public void setFreememroy(Integer freememroy) {
		this.freememroy = freememroy;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public long getLasthearttime() {
		return lasthearttime;
	}

	public void setLasthearttime(long lasthearttime) {
		this.lasthearttime = lasthearttime;
	}

	public float getCpufreerate() {
		return cpufreerate;
	}

	public void setCpufreerate(float cpufreerate) {
		this.cpufreerate = cpufreerate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((workerIp == null) ? 0 : workerIp.hashCode());
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
		WorkerNode other = (WorkerNode) obj;
		if (workerIp == null) {
			if (other.workerIp != null)
				return false;
		} else if (!workerIp.equals(other.workerIp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkerNode [workerIp=" + workerIp + ", freedisk=" + freedisk
				+ ", freememroy=" + freememroy + ", createtime=" + createtime
				+ ", lasthearttime=" + lasthearttime + ", cpufreerate="
				+ cpufreerate + "]";
	}

}
