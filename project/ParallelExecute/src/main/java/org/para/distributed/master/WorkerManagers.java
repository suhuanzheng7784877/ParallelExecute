package org.para.distributed.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.para.distributed.dto.WorkerNode;
import org.para.distributed.util.SortStrategy;

/**
 * worker资源机器的管理者
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-16 下午2:48:08
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class WorkerManagers {

	/**
	 * 暂存节点信息
	 */
	private final static Set<WorkerNode> workerNodes = new CopyOnWriteArraySet<WorkerNode>();

	/**
	 * 选出最靠前的几个节点
	 * 
	 * @param topN
	 *            ,选择几个节点
	 * @return
	 */
	public static List<WorkerNode> selectTopFreeWorkerNode(int parallelNum) {

		List<WorkerNode> list = new ArrayList<WorkerNode>(workerNodes);
		int listSize = list.size();
		if (0 == listSize) {
			return null;
		}

		// 所有可用节点的个数
		int allWorkerNodesSize = listSize;
		if (parallelNum <= 0) {

			// 非法参数，设为1
			parallelNum = 1;
		} else if (parallelNum >= listSize) {

			// 非法参数，设为所有可用节点的个数
			parallelNum = allWorkerNodesSize;
		}

		// 重新执行负载均衡，重新对资源池进行排序
		reBlance(list);

		return list.subList(0, parallelNum);
	}

	/**
	 * 增加节点资源
	 * 
	 * @param workerNode
	 */
	public static void addWorkerNode(WorkerNode workerNode) {
		workerNodes.add(workerNode);
	}

	/**
	 * 重新执行负载均衡，重新对资源池进行排序
	 * 
	 * @param list
	 *            :候选资源的资源池集合
	 */
	public static void reBlance(List<WorkerNode> list) {

		// 开始资源排序，对候选节点资源进行排序
		SortStrategy.sortCandidateList(list);
	}

	/**
	 * 删除节点资源
	 * 
	 * @param workerNode
	 */
	public static void removeWorkerNode(WorkerNode workerNode) {
		workerNodes.remove(workerNode);
	}

	/**
	 * 删除所有节点资源信息
	 * 
	 * @param workerNode
	 */
	public static void clearWorkerNode() {
		workerNodes.clear();
	}

}
