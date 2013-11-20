package execute;

import java.util.concurrent.CountDownLatch;

import org.para.execute.ParallelExecute;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.trace.listener.FailEventListener;

import task.IntParallelTask;

public class IntParallelExecute extends ParallelExecute<int[]> {

	@Override
	protected int analyzeResultCount(int[] arg0) {
		return arg0.length;
	}

	@Override
	protected ParallelTask<int[]> buildParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			int[] srcObject, FailEventListener failEventListener,
			Object... arg4) {

		return new IntParallelTask(countDownLatch, taskProperty, srcObject,
				failEventListener);

	}

	@Override
	protected void init(int[] arg0, Object... arg1) {

	}

}
