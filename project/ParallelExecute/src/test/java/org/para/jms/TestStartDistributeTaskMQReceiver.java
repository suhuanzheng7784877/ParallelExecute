package org.para.jms;

import org.junit.Test;
import org.para.constant.MQConstant;
import org.para.distributed.mq.StartDistributeTaskMQReceiver;

public class TestStartDistributeTaskMQReceiver {

	@Test
	public void testReceiver() {

		StartDistributeTaskMQReceiver startDistributeTaskMQReceiver = new StartDistributeTaskMQReceiver();
		startDistributeTaskMQReceiver
				.receiverTopicMessage(MQConstant.START_DISTRIBUTED_TASK_TOPIC_Destination);

	}

}
