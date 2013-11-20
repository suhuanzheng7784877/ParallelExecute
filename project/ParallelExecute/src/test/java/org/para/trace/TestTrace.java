package org.para.trace;

import org.para.util.MessageOutUtil;

public class TestTrace {

	private A a = null;
	private String mainThreadKey = Thread.currentThread().getId() + ":"
			+ Thread.currentThread().getName();

	public String getMainThreadKey() {
		return mainThreadKey;
	}

	public void dosome(A aInstence) {
		this.a = aInstence;
		MessageOutUtil.SystemOutPrint("处理线程A:" + a);
	}

	public static void main(String[] args) {
		TestTrace testTrace = new TestTrace();
		A a1 = new A(testTrace);
		new Thread(a1).start();

		A a2 = new A(testTrace);
		new Thread(a2).start();

	}

}

class A implements Runnable {

	private TestTrace testTrace;

	public A(TestTrace testTrace) {
		this.testTrace = testTrace;
	}

	@Override
	public void run() {

		String myId = Thread.currentThread().getId() + ":"
				+ Thread.currentThread().getName();

		try {
			MessageOutUtil.SystemOutPrint("getMainThreadKey:"
					+ testTrace.getMainThreadKey());

			MessageOutUtil.SystemOutPrint("myId:" + myId);

			MessageOutUtil.SystemOutPrint("运行一段");
			throw new RuntimeException("123");
		} catch (RuntimeException e) {
			// e.printStackTrace();
			testTrace.dosome(this);
		}
	}

}
