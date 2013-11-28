package org.para.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.para.distributed.dto.DistributedTaskMessage;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.master.DistributedParallelExecute;
import org.para.distributed.master.MasterServer;
import org.para.distributed.master.WorkerManagers;
import org.para.distributed.mq.StartJobJmsSend;
import org.para.distributed.task.DistributedParallelTask;
import org.para.distributed.util.MQMessageBuilder;
import org.para.execute.model.TaskProperty;
import org.para.file.task.ByteCopyFileParallelTask;
import org.para.trace.listener.FailEventListener;

@SuppressWarnings("unused")
public class TestDistributeParalleExecute {

	@Test
	public void testDistributeTasks() throws CloneNotSupportedException {

		WorkerNode workerNode1 = WorkerNode.getSingle("192.168.1.1", 55, 128,
				1L, 1L, 0.9F);

		WorkerNode workerNode2 = (WorkerNode) workerNode1.clone();
		workerNode2.setWorkerIp("192.168.137.1");
		workerNode2.setFreememroy(512);
		workerNode2.setCpufreerate(0.8F);

		WorkerManagers.addOrReplaceWorkerNode(workerNode1);
		WorkerManagers.addOrReplaceWorkerNode(workerNode2);

		Set<WorkerNode> workerNodes = WorkerManagers.getWorkernodes();

		// 选出最靠前的几个节点
		List<WorkerNode> workerNodeList = WorkerManagers
				.selectTopFreeWorkerNode(3);
		System.out.println(workerNodeList);

		TestDistributedParallelExecute testDistributedParallelExecute = new TestDistributedParallelExecute();

		List<DistributedParallelTask> taskList = new ArrayList<DistributedParallelTask>();

		ByteCopyFileParallelTask byteCopyFileParallelTask1 = new ByteCopyFileParallelTask(
				null, null, null, null);
		ByteCopyFileParallelTask byteCopyFileParallelTask2 = new ByteCopyFileParallelTask(
				null, null, null, null);
		ByteCopyFileParallelTask byteCopyFileParallelTask3 = new ByteCopyFileParallelTask(
				null, null, null, null);
		ByteCopyFileParallelTask byteCopyFileParallelTask4 = new ByteCopyFileParallelTask(
				null, null, null, null);

		// taskList.add(byteCopyFileParallelTask1);
		// taskList.add(byteCopyFileParallelTask2);
		// taskList.add(byteCopyFileParallelTask3);
		// taskList.add(byteCopyFileParallelTask4);

		DistributedTaskMessage distributedTaskMessage = MQMessageBuilder
				.buildDistributeTasks(11111L, taskList);
		System.out.println(distributedTaskMessage);
		// 调用mq接口进行分发

		MasterServer masterServer = new MasterServer();
		StartJobJmsSend startJobJmsSend = testDistributedParallelExecute
				.getStartJobJmsSend();

		startJobJmsSend.sendJms(distributedTaskMessage);

	}

}

@SuppressWarnings("rawtypes")
class TestDistributedParallelExecute extends DistributedParallelExecute {

	@Override
	protected void init(Map<String, String> sourceObjectConf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int analyzeResultCount(Map<String, String> sourceObjectConf) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected DistributedParallelTask buildDistributedParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			Map<String, String> sourceObjectConf,
			FailEventListener failEventListener) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
