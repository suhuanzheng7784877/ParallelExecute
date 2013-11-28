package org.para.trace.fail.strategy;

import org.para.trace.event.ParallelEvent;
import org.para.util.MessageOutUtil;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-16
 * @Copyright: 2013 story All rights reserved.
 */
public class DefaultFailHandleStrategy implements HandleStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final DefaultFailHandleStrategy failHandleStrategy = new DefaultFailHandleStrategy();

	private DefaultFailHandleStrategy() {

	}

	/**
	 * get Singleton Instance
	 * 
	 * @return
	 */
	public static DefaultFailHandleStrategy getInstance() {
		return failHandleStrategy;
	}

	@Override
	public int handle(ParallelEvent parallelEvent) {
		MessageOutUtil.SystemOutPrint("[FailHandleStrategy:Fail Handle source event "
				+ parallelEvent.getSource()+"]");
		return 1;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
