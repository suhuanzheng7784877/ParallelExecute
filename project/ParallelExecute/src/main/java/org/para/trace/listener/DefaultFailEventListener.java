package org.para.trace.listener;

import org.para.trace.event.ParallelEvent;
import org.para.trace.fail.strategy.DefaultFailHandleStrategy;
import org.para.trace.fail.strategy.HandleStrategy;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-16
 * @Copyright: 2013 story All rights reserved.
 */
public class DefaultFailEventListener implements FailEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Singleton
	 */
	private static final DefaultFailEventListener defaultFailEventListener = new DefaultFailEventListener();

	private HandleStrategy handleStrategy = DefaultFailHandleStrategy
			.getInstance();

	/**
	 * private constructor
	 */
	private DefaultFailEventListener() {

	}

	/**
	 * get Singleton Instance
	 * 
	 * @return
	 */
	public static DefaultFailEventListener getInstance() {
		try {
			// clone static instence object to client for use
			return (DefaultFailEventListener) defaultFailEventListener.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get Singleton Instance by other HandleStrategy
	 * 
	 * @param handleStrategy
	 * @return
	 */
	public static DefaultFailEventListener getInstance(
			HandleStrategy handleStrategy) {
		DefaultFailEventListener defaultFailEventListenerInstence = null;
		try {
			defaultFailEventListenerInstence = (DefaultFailEventListener) defaultFailEventListener
					.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}

		defaultFailEventListenerInstence.setHandleStrategy(handleStrategy);
		return defaultFailEventListenerInstence;
	}

	public HandleStrategy getHandleStrategy() {
		return handleStrategy;
	}

	public void setHandleStrategy(HandleStrategy handleStrategy) {
		this.handleStrategy = handleStrategy;
	}

	@Override
	public int callStrategy(ParallelEvent parallelEvent) {
		// 触发处理
		return handleStrategy.handle(parallelEvent);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
