package org.para.distributed.util;

import java.util.Collections;
import java.util.List;

import org.para.distributed.dto.WorkerNode;

/**
 * 
 * 排序策略
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-16 下午3:58:58
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class SortStrategy {

	/**
	 * 对候选节点资源进行排序
	 * 
	 * @param list
	 */
	public static void sortCandidateList(List<WorkerNode> list) {
		// logger.info("结点资源按照CPU、内存参数进行排序");
		Collections.sort(list, new SortCPUAndMemroyComparator());
	}
}
