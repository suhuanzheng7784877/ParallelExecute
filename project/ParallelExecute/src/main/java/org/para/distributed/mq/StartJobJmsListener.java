package org.para.distributed.mq;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.para.distributed.dto.DistributedTaskMessage;
import org.para.distributed.dto.MqMessage;
import org.para.distributed.dto.ResponseExecuteResultMessageBean;
import org.para.distributed.dto.TaskTargetWorker;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.slave.ResponseExecuteResultMessageAction;
import org.para.distributed.slave.WorkerServer;
import org.para.distributed.util.ExecUtil;
import org.para.distributed.util.SystemUtil;
import org.para.enums.TaskCycle;
import org.para.execute.model.TaskProperty;
import org.para.util.PropertiesUtil;

/**
 * 
 * 接收分布式任务JMS监听器
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-30 下午7:22:37
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class StartJobJmsListener extends AbstractJmsRecive {

	private static Logger logger = Logger.getLogger(StartJobJmsListener.class);

	/**
	 * 执行子任务的引擎类
	 */
	private static final String EngineClassName = PropertiesUtil
			.getValue("worker.EngineClassName");

	final static WorkerNode localWorkerNode = WorkerNode.getSingle();

	public static final String worker_heap_xmx = PropertiesUtil
			.getValue("worker.heap.xmx");
	public static final String worker_heap_xms = PropertiesUtil
			.getValue("worker.heap.xms");
	public static final String worker_stack_xss = PropertiesUtil
			.getValue("worker.stack.xss");
	public static final String worker_PermSize = PropertiesUtil
			.getValue("worker.PermSize");
	public static final String worker_MaxPermSize = PropertiesUtil
			.getValue("worker.MaxPermSize");

	/**
	 * 执行接收jms后的处理线程池
	 */
	private final static ExecutorService StartTasksThreadPool = Executors
			.newFixedThreadPool(Integer.parseInt(PropertiesUtil
					.getValue("worker.start.tasks.threadpool.size")));

	public StartJobJmsListener() {
		logger.info("init StartJobJmsListener");
	}

	/**
	 * 处理注册的逻辑
	 */
	@Override
	public void handleJmsMessage(MqMessage mqMessage) {

		// 取出抽象的子任务类
		DistributedTaskMessage distributedTaskMessage = (DistributedTaskMessage) mqMessage;

		logger.info("StartJobJmsListener Message:" + distributedTaskMessage);

		TaskTargetWorker[] taskTargetWorkerArray = distributedTaskMessage
				.getTaskTargetWorker();

		List<TaskProperty> taskPropertyList = selectLocalWorkerExecuteTasks(taskTargetWorkerArray);

		if (null == taskPropertyList || 0 == taskPropertyList.size()) {
			logger.info("local workerNode is not select any Tasks so return");
			return;
		}
		logger.info("selected Local Worker Execute Tasks:");

		long jobId = distributedTaskMessage.getJobId();
		String jarHttpURI = distributedTaskMessage.getJarHttpURI();

		for (TaskProperty taskProperty : taskPropertyList) {
			// TODO:进行命令拼接发送，执行分布式任务
			logger.info(SystemUtil.localIP + "进行命令拼接发送，执行分布式任务");

			int taskId = taskProperty.getTaskId();

			// 构建java启动命令
			String command = buildJavaCmd(jobId, taskId, jarHttpURI);

			// 在线程池里面运行
			StartTasksThreadPool.submit(new StartJVMThread(command, jobId,
					taskProperty));
		}

	}

	/**
	 * 构建java启动命令
	 * 
	 * @param jobId
	 * @param taskId
	 * @param currentBlockIndex
	 * @param jarHttpURI
	 * @return
	 */
	private static String buildJavaCmd(long jobId, int taskId, String jarHttpURI) {
		StringBuilder cmdSB = new StringBuilder(1024);
		cmdSB.append("java -Xms").append(worker_heap_xms).append(" -Xmx")
				.append(worker_heap_xmx).append(" -Xss")
				.append(worker_stack_xss).append(" -XX:PermSize=")
				.append(worker_PermSize).append(" -XX:MaxPermSize=")
				.append(worker_MaxPermSize).append(" -cp '")
				.append(SystemUtil.getSystemClassPath()).append("' ")
				.append(EngineClassName).append(" ").append(jobId).append(" ")
				.append(taskId).append(" ").append(jarHttpURI);

		return cmdSB.toString();
	}

	/**
	 * 选出属于本工作节点需要执行的任务集合
	 * 
	 * @param 所有任务的集合taskTargetWorkerArray
	 * @return
	 */
	private List<TaskProperty> selectLocalWorkerExecuteTasks(
			TaskTargetWorker[] taskTargetWorkerArray) {
		String taskWorkIp = null;
		for (TaskTargetWorker taskTargetWorker : taskTargetWorkerArray) {
			taskWorkIp = taskTargetWorker.getWorkerNode().getWorkerIp();
			if (SystemUtil.localIP.equals(taskWorkIp)) {

				List<TaskProperty> taskPropertyList = taskTargetWorker
						.getTaskPropertyList();
				return taskPropertyList;
			}
		}

		return null;
	}

}

/**
 * 运行子任务的exec线程
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-17 上午11:10:36
 * @Copyright: 2013 story All rights reserved.
 * 
 */
class StartJVMThread implements Runnable {

	private static Logger logger = Logger.getLogger(StartJobJmsListener.class);

	private String cmd;
	private long jobId;
	private TaskProperty taskProperty;

	public StartJVMThread(String cmd, long jobId, TaskProperty taskProperty) {
		super();
		this.cmd = cmd;
		this.jobId = jobId;
		this.taskProperty = taskProperty;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public TaskProperty getTaskProperty() {
		return taskProperty;
	}

	public void setTaskProperty(TaskProperty taskProperty) {
		this.taskProperty = taskProperty;
	}

	@Override
	public void run() {

		logger.info("worker即将执行命令:[" + cmd + "]");

		// 执行命令
		boolean result = ExecUtil.exec(cmd);
		if (result) {
			taskProperty.setTaskCycle(TaskCycle.TASK_SUCCEED);
		} else {
			taskProperty.setTaskCycle(TaskCycle.TASK_ERROR);
		}

		// 执行之后应该给master进行jms反馈
		// 反馈给Master，子任务执行成功的反馈消息
		ResponseExecuteResultMessageBean responseExecuteResultMessageBean = new ResponseExecuteResultMessageBean(
				jobId, WorkerNode.getSingle(), taskProperty);

		ResponseExecuteResultMessageAction responseExecuteResultMessageAction = WorkerServer.WorkApplicationContext
				.getBean("ResponseExecuteResultMessageAction",
						ResponseExecuteResultMessageAction.class);

		responseExecuteResultMessageAction
				.sendJms(responseExecuteResultMessageBean);
	}

	@Override
	public String toString() {
		return "StartJVMThread [cmd=" + cmd + ", jobId=" + jobId
				+ ", taskProperty=" + taskProperty + "]";
	}
}