package org.para.exception;

import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

/**
 * ParallelException
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-30 下午3:48:44
 * @Copyright: 2013 story All rights reserved.
 */
public class ParallelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Exception exception;

	public ParallelException(Exception exception) {
		this.exception = exception;
	}

	public ParallelException(InterruptedException interruptedException) {
		this.exception = interruptedException;
	}

	public ParallelException(IOException ioException) {
		this.exception = ioException;
	}

	public ParallelException(TTransportException tTransportException) {
		this.exception = tTransportException;
	}

	public ParallelException(TException tException) {
		this.exception = tException;
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		System.err.print(exception);
	}

}
