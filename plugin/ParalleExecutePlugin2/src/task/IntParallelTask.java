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
	protected int execute(int[] sourceJobObject, int currentBlockSize, int countBlock,
			int currentBlockIndex,int averageBlockSize) throws Exception {

		int startIndex = currentBlockIndex * averageBlockSize;
		int endIndex = currentBlockIndex * averageBlockSize + currentBlockSize;

		System.out.println("ThreadId" + Thread.currentThread().getId()
				+ "___currentBlockSize:" + currentBlockSize + "__countBlock:" + countBlock
				+ "___currentBlockIndex:" + currentBlockIndex + "__startIndex:"
				+ startIndex + "___endIndex:" + endIndex);

		for (int i = 0; i < currentBlockSize; i++) {
			MessageOutUtil
					.SystemOutPrint("ThreadId" + Thread.currentThread().getId()
							+ ":" + sourceJobObject[startIndex+i]);
		}
		return currentBlockIndex;

	}

}
