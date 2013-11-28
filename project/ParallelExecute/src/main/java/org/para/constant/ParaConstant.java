package org.para.constant;

/**
 * FileConstant
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-8-26
 * @Copyright: 2013 story All rights reserved.
 */
public interface ParaConstant {

	/**
	 * 默认的子任务并行度，操作系统的核数
	 */
	public final static int DefaultBlockNum = Runtime.getRuntime()
			.availableProcessors();

	// default file read/write buffer
	public final static int DefaultFileBufferSize = 1024 * 1;

	// default File Block Num
	public final static int DefaultFileBlockNum = DefaultBlockNum;

	// default DB Block Num
	public final static int DefaultDBBlockNum = DefaultBlockNum;

	// default DB Block Num
	public final static int DefaultDBBatchNum = 20000;

	/**
	 * 默认的分布式任务上下文key的前置字符串
	 */
	public final static String SystemMapConfFrontStr = "org.para.constant.";
	
	/**
	 * Master结点使用的相关的常量
	 *
	 * @author liuyan
	 * @Email:suhuanzheng7784877@163.com
	 * @version 0.1
	 * @Date: 2013-12-18 下午9:35:00
	 * @Copyright: 2013 story All rights reserved.
	 *
	 */
	public static class MasterConstant{
		
	}
	
	/**
	 * Slave结点使用的相关的常量
	 *
	 * @author liuyan
	 * @Email:suhuanzheng7784877@163.com
	 * @version 0.1
	 * @Date: 2013-12-18 下午9:34:44
	 * @Copyright: 2013 story All rights reserved.
	 *
	 */
	public static class SlaveConstant{
		
	}

}
