package org.para.distributed.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

/**
 * 执行脚本命令封装
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-16 下午10:43:35
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class ExecUtil {

	private static Logger logger = Logger.getLogger(ExecUtil.class);

	private final static String encode = "GBK";

	public final static String SUCESS_CODE = "1000";

	public final static String ERROR_CODE = "1001";

	/**
	 * 执行命令
	 * 
	 * @param 命令
	 *            :commond
	 * @return
	 */
	public static boolean exec(String command) {

		ByteArrayOutputStream outputStream = null;
		ByteArrayOutputStream errorStream = null;
		try {

			outputStream = new ByteArrayOutputStream();

			errorStream = new ByteArrayOutputStream();

			CommandLine commandline = CommandLine.parse(command);

			DefaultExecutor exec = new DefaultExecutor();

			exec.setExitValues(null);

			PumpStreamHandler streamHandler = new PumpStreamHandler(
					outputStream, errorStream);

			exec.setStreamHandler(streamHandler);
			exec.execute(commandline);
			String out = outputStream.toString(encode);
			String error = errorStream.toString(encode);
			logger.info("==============exec start=====================");
			System.out.println(out + error);
			logger.info("==============exec over======================");
			if (-1 != out.lastIndexOf(SUCESS_CODE,
					out.length() - SUCESS_CODE.length())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		} finally {
			try {
				errorStream.close();
				outputStream.close();
				errorStream = null;
				outputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("error", e);
			}
		}
		return false;

	}

}
