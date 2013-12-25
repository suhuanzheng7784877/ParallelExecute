package org.para.distributed.mq;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.para.distributed.dto.MqMessage;
import org.para.distributed.dto.ResponseExecuteResultMessageBean;
import org.para.distributed.master.DistributedTaskManagers;
import org.para.distributed.memcache.SpringInitContextMemcache;
import org.para.distributed.task.DistributedParallelTask;
import org.para.enums.TaskCycle;
import org.para.execute.model.TaskProperty;

/**
 * 接收任务反馈
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-1 下午1:34:14
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class ResponseExecuteResultJmsListener extends AbstractJmsRecive {

	private static Logger logger = Logger
			.getLogger(ResponseExecuteResultJmsListener.class);

	public ResponseExecuteResultJmsListener() {
		logger.info("init ResponseExecuteResultJmsListener");
	}

	/**
	 * 处理注册的逻辑
	 */
	@Override
	public void handleJmsMessage(MqMessage mqMessage) {

		// 此段代码如果能归到父类就更好了
		ResponseExecuteResultMessageBean responseExecuteResultMessageBean = (ResponseExecuteResultMessageBean) mqMessage;

		logger.info("ResponseExecuteResultJmsListener Message:"
				+ responseExecuteResultMessageBean);

		// 分布式jobid，标识大任务的key
		long jobId = responseExecuteResultMessageBean.getJobId();

		// 分布式子任务属性
		TaskProperty responseTaskProperty = responseExecuteResultMessageBean
				.getTaskProperty();
		int responseTaskPropertyId = responseTaskProperty.getTaskId();

		// 执行任务的周期项
		TaskCycle taskCycle = responseTaskProperty.getTaskCycle();

		List<DistributedParallelTask> distributedParallelTaskList = DistributedTaskManagers
				.getDistributedTaskManagerInstence()
				.getDistributedParallelTaskList(jobId);

		// 迭代分布式子任务
		Iterator<DistributedParallelTask> iterator = distributedParallelTaskList
				.iterator();
		DistributedParallelTask distributedParallelTask = null;
		TaskProperty taskProperty = null;
		while (iterator.hasNext()) {
			distributedParallelTask = iterator.next();
			taskProperty = distributedParallelTask.getTaskProperty();

			// 分布式子任务的id
			int taskId = taskProperty.getTaskId();
			if (responseTaskPropertyId == taskId) {
				// 设置子任务生命周期
				distributedParallelTask.getTaskProperty().setTaskCycle(
						taskCycle);
				// 分布式计数器增加一个
				distributedParallelTask.getCountDownLatch().countDown();
				// 删除memcache的task数据
				SpringInitContextMemcache.removeDistributedParallelTask(jobId, taskId);
				break;
			}

		}

	}
}
