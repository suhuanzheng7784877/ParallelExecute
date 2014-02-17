package distributed;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.para.distributed.task.DistributedParallelTask;
import org.para.execute.model.TaskProperty;
import org.para.trace.listener.FailEventListener;
import org.para.util.MessageOutUtil;

/**
 * 
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-1 下午4:31:39
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class StringArrayDistributedParallelTask extends DistributedParallelTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StringArrayDistributedParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, Map<String, String> targetObject,
			FailEventListener failEventListener) {
		super(taskProperty, targetObject, countDownLatch, failEventListener);
	}

	@Override
	protected int execute(Map<String, String> targetObjectConf, int currentBlockSize,
			int countBlock, int currentBlockIndex,int averageBlockSize) throws Exception {
		int startIndex = currentBlockIndex * averageBlockSize;
		int endIndex = currentBlockIndex * averageBlockSize + currentBlockSize;

		try {
			MessageOutUtil.SystemOutPrint("ThreadId:"
					+ Thread.currentThread().getId() + ":[" + targetObjectConf
					+ "]startIndex:" + startIndex + "   endIndex:" + endIndex);

		} catch (Exception e) {
			throw e;

		}
		return currentBlockIndex;
	}

}
