package task;

import java.util.concurrent.CountDownLatch;

import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.trace.listener.FailEventListener;
import org.para.util.MessageOutUtil;

public class IntParallelTask extends ParallelTask<int[]> {

	public IntParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, int[] targetObject,
			FailEventListener failEventListener) {
		super(taskProperty, targetObject, countDownLatch, failEventListener);
	}

	@Override
	protected int execute(int[] sourceJobObject, int blockSize, int countBlock,
			int currentBlockIndex) throws Exception {

		int startIndex = currentBlockIndex * blockSize;
		int endIndex = currentBlockIndex * blockSize + blockSize;
		for (int i = startIndex; i < endIndex; i++) {
			MessageOutUtil
					.SystemOutPrint("ThreadId" + Thread.currentThread().getId()
							+ ":" + sourceJobObject[i]);
		}
		return currentBlockIndex;

	}

}
