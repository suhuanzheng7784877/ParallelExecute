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

	// default file read/write buffer
	public final static int DefaultFileBufferSize = 1024 * 1;

	// default Block Num
	public final static int DefaultBlockNum = Runtime.getRuntime()
			.availableProcessors();

	// default File Block Num
	public final static int DefaultFileBlockNum = DefaultBlockNum;

	// default DB Block Num
	public final static int DefaultDBBlockNum = DefaultBlockNum;

	// default DB Block Num
	public final static int DefaultDBBatchNum = 20000;

}
