package org.para.util;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-25
 * @Copyright: 2013 story All rights reserved.
 */
public class MessageOutUtil {

	/**
	 * 
	 * @param message
	 */
	public static void SystemOutPrint(String message) {
		
		System.out.println(message);
	}

	/**
	 * 
	 * @param message
	 */
	public static void SystemOutPrint(StringBuilder message) {
		System.out.println(message.toString());
	}

	/**
	 * 
	 * @param message
	 */
	public static void SystemOutPrint(StringBuffer message) {
		System.out.println(message.toString());
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void SystemOutPrint(boolean message) {
		System.out.println(message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void SystemOutPrint(Object message) {
		System.out.println(message);
	}

}
