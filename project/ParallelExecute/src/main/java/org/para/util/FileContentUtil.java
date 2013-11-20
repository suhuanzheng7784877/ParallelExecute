package org.para.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-27
 * @Copyright: 2013 story All rights reserved.
 */
public class FileContentUtil {

	/**
	 * getFileContentLineNum
	 * 
	 * @param srcFile
	 * @return
	 */
	public static int getFileContentLineNum(File srcFile) {
		long fileLength = srcFile.length();
		LineNumberReader rf = null;
		int lines = 0;
		try {
			rf = new LineNumberReader(new FileReader(srcFile));
			if (rf != null) {

				rf.skip(fileLength);
				lines = rf.getLineNumber();
				rf.close();
			}
			MessageOutUtil.SystemOutPrint("line is:" + lines);
			return lines;
		} catch (IOException e) {
			if (rf != null) {
				try {
					rf.close();
					rf = null;
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
			return -1;
		}
	}

}
