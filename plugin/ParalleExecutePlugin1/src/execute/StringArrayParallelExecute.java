package execute;

import java.util.concurrent.CountDownLatch;

import org.para.exception.ParallelException;
import org.para.execute.ParallelExecute;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.trace.listener.FailEventListener;

import task.StringArrayParallelTask;

public class StringArrayParallelExecute extends ParallelExecute<String[]> {

	@Override
	protected void init(String[] strings,Object... objects) {

	}

	@Override
	protected int analyzeResultCount(String[] srcObject) {
		return srcObject.length;
	}

	@Override
	protected ParallelTask<String[]> buildParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			String[] srcObject, FailEventListener failEventListener,
			Object... objects) {
		return new StringArrayParallelTask(countDownLatch, taskProperty,
				srcObject, failEventListener);
	}

	public static void main(String[] args) throws ParallelException {
		StringArrayParallelExecute stringArrayParallelExecute = new StringArrayParallelExecute();
		String[] sourceObject = new String[14];

		for (int i = 0; i < sourceObject.length; i++) {
			sourceObject[i] = i + 1 +"";
		}

		stringArrayParallelExecute.exeParalleJob(sourceObject, 2);

	}

}
