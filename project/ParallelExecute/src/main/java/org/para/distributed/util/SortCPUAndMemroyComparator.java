package org.para.distributed.util;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.para.distributed.dto.WorkerNode;

/**
 * 按照资源总值排序
 * 
 * 
 * 按照公式：CPU剩余率*100*CPU权重+内存剩余率*100*（1-CPU权重）=负载Value，进行从大到小的排序。（
 * 硬件的性能指数还可以在细化：CPU工作频率、内存工作频率、硬盘转数、网络吞吐量）
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-16 下午3:41:22
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class SortCPUAndMemroyComparator implements Comparator<WorkerNode> {

	private static Logger logger = Logger
			.getLogger(SortCPUAndMemroyComparator.class);

	/**
	 * 实体互相进行比较
	 */
	public int compare(WorkerNode workerNode1, WorkerNode workerNode2) {

		if (null == workerNode1 || null == workerNode2) {
			logger.info("error, nodeInfo1 or nodeInfo2   is null!");
			return 0;
		}

		float node1PowerValue = getNodeValue(workerNode1);

		float node2PowerValue = getNodeValue(workerNode2);

		// 综合值进行对比
		if (node1PowerValue < node2PowerValue) {
			return 1;
		} else if (node1PowerValue == node2PowerValue) {
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * 获取结点机器的负载值
	 * 
	 * @param nodeInfo
	 * @return
	 */
	public float getNodeValue(WorkerNode workerNode) {
		// node1-CPU剩余率
		float node1CpuFreeRate = workerNode.getCpufreerate();

		// node1-内存剩余率
		float node1MemroyFreeRate = workerNode.getFreememroy();

		// node1-的综合值
		float node1PowerValue = node1CpuFreeRate * 0.5F * 100.0F
				+ node1MemroyFreeRate * (1 - 0.5f) * 100;

		return node1PowerValue;
	}
}
