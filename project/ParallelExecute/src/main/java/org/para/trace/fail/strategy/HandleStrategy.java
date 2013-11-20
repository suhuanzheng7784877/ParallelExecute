package org.para.trace.fail.strategy;

import java.io.Serializable;

import org.para.trace.event.ParallelEvent;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-16
 * @Copyright: 2013 story All rights reserved.
 */
public interface HandleStrategy extends Cloneable, Serializable {

	/**
	 * Handle event Strategy logic
	 * 
	 * @param parallelEvent
	 * @return
	 */
	public int handle(ParallelEvent parallelEvent);

}
