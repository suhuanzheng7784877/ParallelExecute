package org.para.execute;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.para.exception.ParallelException;
import org.para.execute.model.JobProperty;
import org.para.file.FileParallelExecute;
import org.para.file.execute.ByteFileParallelExecute;
import org.para.file.execute.BytesFileParallelExecute;
import org.para.trace.listener.DefaultFailEventListener;
import org.para.util.MessageOutUtil;
@SuppressWarnings("unused")
public class TestFileParallelExecute {

	@Test
	public void test01() {
		String srcName = "F:/myeclipse.EXE";
		String targetName = "C:/myeclipse.EXE";
		File srcFile = new File(srcName);
		File target = new File(targetName);
		if (target.exists()) {
			target.delete();
		}
		FileParallelExecute fileParallelExecute = new ByteFileParallelExecute();

		try {
			JobProperty jobProperty = fileParallelExecute.exeParalleJob(srcFile, 2, null, targetName);
			//System.out.println(jobProperty);
		} catch (ParallelException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test02() {
		String srcName = "C:/QGN_DSDT.log";
		String targetName = "C:/QGN_DSDT.log.bak";
		File srcFile = new File(srcName);
		File target = new File(targetName);
		if (target.exists()) {
			target.delete();
		}
		FileParallelExecute fileParallelExecute = new ByteFileParallelExecute();
		try {
			fileParallelExecute.exeParalleJob(srcFile, 2, null, targetName);
		} catch (ParallelException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test03() {
		String srcName = "C:/QGN_DSDT.log";
		String targetName = "C:/1";
		File srcFile = new File(srcName);
		File target = new File(targetName);
		if (target.exists()) {
			target.delete();
		}
		FileParallelExecute fileParallelExecute = new BytesFileParallelExecute();
		try {
			fileParallelExecute.exeParalleJob(srcFile, 2, null, targetName);
		} catch (ParallelException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test04() throws UnsupportedEncodingException, IOException {
		File file1 = new File("c:/1_Thread-1_block");
		RandomAccessFile raf = new RandomAccessFile(file1, "r");

		byte[] content = new byte[1024 * 1];

		while (raf.read(content, 0, 1024 * 1) > 0) {
			String newCon = new String(content, "utf-8");

			MessageOutUtil.SystemOutPrint("linue:" + newCon);

			raf.seek(1024 * 1);
		}

		raf.close();

	}
	
	@Test
	public void testEclipseCopy() {
		
		//1-指定源任务 与 目标任务
		String srcName = "E:/ECLIPSE.ZIP";
		String targetName = "C:/ECLIPSE.ZIP";
		File srcFile = new File(srcName);
		File target = new File(targetName);
		if (target.exists()) {
			target.delete();
		}
		FileParallelExecute fileParallelExecute = new ByteFileParallelExecute();
		
		try {
			long start = System.currentTimeMillis();
			fileParallelExecute.exeParalleJob(srcFile, 4,targetName);
			long end = System.currentTimeMillis();
			MessageOutUtil.SystemOutPrint("time:"+(end-start)+"ms");
		} catch (ParallelException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testFailEventListener() {
		
		//1-指定源任务 与 目标任务
		String srcName = "E:/ECLIPSE.ZIP";
		String targetName = "C:/ECLIPSE.ZIP";
		File srcFile = new File(srcName);
		File target = new File(targetName);
		if (target.exists()) {
			target.delete();
		}
		FileParallelExecute fileParallelExecute = new ByteFileParallelExecute();
		
		DefaultFailEventListener failEventListener = DefaultFailEventListener.getInstance();
		
		try {
			long start = System.currentTimeMillis();
			fileParallelExecute.exeParalleJob(srcFile, 4, failEventListener, targetName);
			long end = System.currentTimeMillis();
			MessageOutUtil.SystemOutPrint("time:"+(end-start)+"ms");
		} catch (ParallelException e) {
			e.printStackTrace();
		}

	}

}
