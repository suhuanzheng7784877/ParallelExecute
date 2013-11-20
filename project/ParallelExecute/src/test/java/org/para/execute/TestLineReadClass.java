package org.para.execute;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.junit.Test;
import org.para.util.MessageOutUtil;

public class TestLineReadClass {

	@Test
	public void testCountLineNum() {
		File test = new File("c://QGN_DSDT.log");
		long fileLength = test.length();
		LineNumberReader rf = null;
		int lines = 0;
		try {
			rf = new LineNumberReader(new FileReader(test));
			if (rf != null) {

				rf.skip(fileLength);
				lines = rf.getLineNumber();
				rf.close();
			}
			MessageOutUtil.SystemOutPrint("line is:" + lines);
		} catch (IOException e) {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException ee) {
				}
			}
		}
	}

	@Test
	public void testUTF() throws IOException {
		String code = System.getProperty("sun.jnu.encoding");
		System.out.print(code);
		String fileName = "刘";
		String fileNameCode = new String(fileName.getBytes(code), "GBK");
		File file = new File(fileNameCode);

		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
	}

	@Test
	public void readSetLine() {

		File srcFile = new File("c://num.txt");
		LineNumberReader rf = null;
		int lines = 0;
		try {
			rf = new LineNumberReader(new FileReader(srcFile));
			rf.setLineNumber(2);
			String content = rf.readLine();
			MessageOutUtil.SystemOutPrint("line is:" + lines);
			MessageOutUtil.SystemOutPrint("content is:" + content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (rf != null) {
				try {
					rf.close();
					rf = null;
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String code = System.getProperty("sun.jnu.encoding");
		System.out.print(code);
		String fileName = "刘";
		String fileNameCode = new String(fileName.getBytes(code), code);
		File file = new File(fileNameCode);

		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
	}

}
