package org.para.constant;

/**
 * MQ相关的常量信息
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-23 下午4:50:25
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class MQConstant {

	/**
	 * 发送任务的-topic
	 */
	public final static String START_DISTRIBUTED_TASK_TOPIC_Destination = "Start_Distributed_Task_Topic";

	/**
	 * 子节点执行任务后的反馈-目标
	 */
	public final static String RESPONSE_TASKS_Queue_Destination = "RESPONSE_TASKS_Queue";

	/**
	 * 注册worker节点
	 */
	public final static String REGISTER_WORKER_Queue_Destination = "Register_Worker_Queue";
	
	/**
	 * 心跳worker节点
	 */
	public final static String HEARTBEAT_WORKER_Queue_Destination = "Heartbeat_Worker_Queue";

}
