package org.para.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * String some util Method
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-10 下午3:55:38
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class StringUtil {
	
	private static Logger logger = Logger.getLogger(StringUtil.class);
	
	// 操作系统类型
	private final static String osType = System.getProperty("os.name");
	
	/**
	 * generate UUID String
	 * @return
	 */
	public static final String generateUUID() {

		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		return uuidString;
	}
	
	/**
	 * 判断操作系统的类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean OSisLinux() throws RuntimeException {

		if (StringUtils.isBlank(osType)) {
			RuntimeException exception = new RuntimeException("osType is null");
			logger.error("error", exception);
			throw exception;
		}

		if (osType.startsWith("Windows")) {
			// 按照windows
			return false;
		} else {
			// 按照linux进行
			return true;
		}
	}

}
