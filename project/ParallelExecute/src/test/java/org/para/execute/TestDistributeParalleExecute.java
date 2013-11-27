package org.para.execute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.para.constant.MQConstant;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.master.DistributedParallelExecute;
import org.para.distributed.master.WorkerManagers;
import org.para.distributed.mq.DistributedTaskMessage;
import org.para.distributed.util.MQMessageBuilder;
import org.para.distributed.util.MQSender;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.file.task.ByteCopyFileParallelTask;
import org.para.trace.listener.FailEventListener;

public class TestDistributeParalleExecute {

	@Test
	public void testDistributeTasks() throws CloneNotSupportedException {

		WorkerNode workerNode1 = WorkerNode.getSingle("192.168.1.1", 55, 128,
				1L, 1L, 0.9F);

		WorkerNode workerNode2 = (WorkerNode) workerNode1.clone();
		workerNode2.setWorkerIp("192.168.1.2");
		workerNode2.setFreememroy(512);
		workerNode2.setCpufreerate(0.8F);

		WorkerManagers.addWorkerNode(workerNode1);
		WorkerManagers.addWorkerNode(workerNode2);

		// 选出最靠前的几个节点
		List<WorkerNode> workerNodeList = WorkerManagers
				.selectTopFreeWorkerNode(3);
		System.out.println(workerNodeList);

		TestDistributedParallelExecute testDistributedParallelExecute = new TestDistributedParallelExecute();

		List<ParallelTask<?>> taskList = new ArrayList<ParallelTask<?>>();

		ByteCopyFileParallelTask byteCopyFileParallelTask1 = new ByteCopyFileParallelTask(
				null, null, null, null);
		ByteCopyFileParallelTask byteCopyFileParallelTask2 = new ByteCopyFileParallelTask(
				null, null, null, null);
		ByteCopyFileParallelTask byteCopyFileParallelTask3 = new ByteCopyFileParallelTask(
				null, null, null, null);
		ByteCopyFileParallelTask byteCopyFileParallelTask4 = new ByteCopyFileParallelTask(
				null, null, null, null);

		taskList.add(byteCopyFileParallelTask1);
		taskList.add(byteCopyFileParallelTask2);
		taskList.add(byteCopyFileParallelTask3);
		taskList.add(byteCopyFileParallelTask4);

		DistributedTaskMessage distributedTaskMessage = MQMessageBuilder
				.buildDistributeTasks(11111L, taskList);
		System.out.println(distributedTaskMessage);
		// 调用mq接口进行分发

		for (int i = 0; i < 100; i++) {
			MQSender mqSender = new MQSender();
			mqSender.sendTopicMessage(
					MQConstant.START_DISTRIBUTED_TASK_TOPIC_Destination,
					distributedTaskMessage);
		}

	}

}

class TestDistributedParallelExecute extends DistributedParallelExecute {

	@Override
	protected void init(Serializable sourceObject, Object... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int analyzeResultCount(Serializable srcObject) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ParallelTask buildParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, Serializable srcObject,
			FailEventListener failEventListener, Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

}
