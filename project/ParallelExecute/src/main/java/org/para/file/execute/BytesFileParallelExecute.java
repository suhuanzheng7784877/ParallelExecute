package org.para.file.execute;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.file.task.BytesCopyFileParallelTask;
import org.para.trace.listener.FailEventListener;

public class BytesFileParallelExecute extends ByteFileParallelExecute {

	/**
	 * build Byte File ParallelTask
	 */
	@Override
	protected ParallelTask<File> buildParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			File srcObject, FailEventListener failEventListener,
			Object... objects) {
		String targetFileName = (String) objects[0];

		ParallelTask<File> byteFileParallelTask = new BytesCopyFileParallelTask(
				countDownLatch, taskProperty, srcObject, failEventListener,
				targetFileName);

		return byteFileParallelTask;
	}

}
