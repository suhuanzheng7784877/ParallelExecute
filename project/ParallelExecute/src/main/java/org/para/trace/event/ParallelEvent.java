package org.para.trace.event;

import java.util.EventObject;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-16
 * @Copyright: 2013 story All rights reserved.
 */
public class ParallelEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParallelEvent(Object source) {
		super(source);
	}

}
