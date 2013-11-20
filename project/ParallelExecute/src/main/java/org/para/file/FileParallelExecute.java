package org.para.file;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.para.execute.ParallelExecute;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.file.task.ByteCopyFileParallelTask;
import org.para.trace.listener.FailEventListener;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-26
 * @Copyright: 2013 story All rights reserved.
 */
public class FileParallelExecute extends ParallelExecute<File> {

	@Override
	protected void init(File file,Object... objects) {

	}

	/**
	 * build Byte File ParallelTask
	 */
	@Override
	protected ParallelTask<File> buildParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			File srcObject, FailEventListener failEventListener,
			Object... objects) {

		String targetFileName = (String) objects[0];

		ParallelTask<File> byteFileParallelTask = new ByteCopyFileParallelTask(
				countDownLatch, taskProperty, srcObject, failEventListener,
				targetFileName);

		return byteFileParallelTask;
	}

	@Override
	protected int analyzeResultCount(File srcObject) {
		return (int) srcObject.length();
	}

}
