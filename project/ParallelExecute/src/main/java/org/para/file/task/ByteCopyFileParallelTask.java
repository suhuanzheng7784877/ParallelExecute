package org.para.file.task;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;

import org.para.constant.ParaConstant;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.trace.event.ParallelEvent;
import org.para.trace.listener.FailEventListener;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-23
 * @Copyright: 2013 story All rights reserved.
 */
public class ByteCopyFileParallelTask extends ParallelTask<File> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String targetFileName;

	public String getTargetFileName() {
		return targetFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public ByteCopyFileParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, File targetObject,
			FailEventListener failEventListener) {
		super(taskProperty, targetObject, countDownLatch, failEventListener);
	}

	public ByteCopyFileParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, File srcObject,
			FailEventListener failEventListener, String targetFileName) {
		super(taskProperty, srcObject, countDownLatch, failEventListener);
		this.targetFileName = targetFileName;
	}

	/**
	 * execute task logic
	 * 
	 * @return
	 * @throws Exception
	 */
	protected int execute(File sourceFile, int blockSize, int countBlock,
			int currentBlockIndex) throws Exception {

		// 取最小的数据块
		int fileBufferSize = (blockSize < ParaConstant.DefaultFileBufferSize ? blockSize
				: ParaConstant.DefaultFileBufferSize);

		RandomAccessFile sourceRAF = new RandomAccessFile(sourceFile, "r");

		File targetFile = new File(targetFileName);
		RandomAccessFile targetRaf = new RandomAccessFile(targetFile, "rw");

		try {
			int readedSize = 0;
			int readSize = 0;
			// 读/写，开始的标记
			int startIndex = currentBlockIndex * blockSize;

			sourceRAF.seek(startIndex);
			targetRaf.seek(startIndex);
			byte[] contentBytes = new byte[fileBufferSize];

			while ((readSize = sourceRAF.read(contentBytes, 0, fileBufferSize)) > 0) {
				targetRaf.write(contentBytes, 0, readSize);
				readedSize += readSize;

				if (readedSize >= blockSize) {
					break;
				}
				targetRaf.seek(startIndex + readedSize);
				sourceRAF.seek(startIndex + readedSize);
			}

			return currentBlockIndex;
		} catch (Exception e) {
			e.printStackTrace();
			if (null != failEventListener) {
				failEventListener.callStrategy(new ParallelEvent(this));
			}
			return -1;
		} finally {
			targetRaf.close();
			targetRaf = null;
			sourceRAF.close();
			sourceRAF = null;
		}

	}

}
