package org.para.util;

import java.util.UUID;

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
	
	/**
	 * generate UUID String
	 * @return
	 */
	public static final String generateUUID() {

		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		return uuidString;
	}

}
