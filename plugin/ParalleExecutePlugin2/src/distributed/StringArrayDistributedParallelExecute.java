package distributed;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.para.distributed.master.DistributedParallelExecute;
import org.para.distributed.task.DistributedParallelTask;
import org.para.execute.model.TaskProperty;
import org.para.trace.listener.FailEventListener;


/**
 * 
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-4 下午3:40:21
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class StringArrayDistributedParallelExecute extends
		DistributedParallelExecute {

	@Override
	protected void init(Map<String, String> sourceObject) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int analyzeResultCount(Map<String, String> sourceObject) {

		int resultCount = Integer.parseInt(sourceObject.get("resultCount"));

		return resultCount;
	}

	@Override
	protected DistributedParallelTask buildDistributedParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			Map<String, String> sourceObject,
			FailEventListener failEventListener) {

		return new StringArrayDistributedParallelTask(countDownLatch,
				taskProperty, sourceObject, failEventListener);
	}

}
