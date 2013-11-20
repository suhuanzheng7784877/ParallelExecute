package org.para.trace;

import org.junit.Test;
import org.para.trace.event.ParallelEvent;
import org.para.trace.fail.strategy.DefaultFailHandleStrategy;
import org.para.trace.listener.DefaultFailEventListener;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-16
 * @Copyright: 2013 story All rights reserved.
 */
public class TestParalleEventListener {

	DefaultFailEventListener defaultFailEventListener = DefaultFailEventListener
			.getInstance(DefaultFailHandleStrategy.getInstance());
	
	@Test
	public void test01() {
		
		ParallelEvent parallelEvent = new ParallelEvent("1");
		
		defaultFailEventListener.callStrategy(parallelEvent);
	}

}
