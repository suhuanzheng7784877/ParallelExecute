package org.para.trace.listener;

import java.io.Serializable;
import java.util.EventListener;

import org.para.trace.event.ParallelEvent;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-16
 * @Copyright: 2013 story All rights reserved.
 */
public interface FailEventListener extends EventListener, Cloneable,
		Serializable {

	/**
	 * call one or many Strategy to Handle this Event
	 * 
	 * @param parallelEvent
	 * @return
	 */
	public int callStrategy(ParallelEvent parallelEvent);

}
