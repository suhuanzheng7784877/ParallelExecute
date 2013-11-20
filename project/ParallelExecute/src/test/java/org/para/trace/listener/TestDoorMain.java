package org.para.trace.listener;

import org.para.util.MessageOutUtil;

public class TestDoorMain {
	public static void main(String[] args) {
		DoorManager manager = new DoorManager();
		manager.addDoorListener(new DoorListener1());// 给门1增加监听器
		manager.addDoorListener(new DoorListener2());// 给门2增加监听器
		// 开门
		manager.fireWorkspaceOpened();
		MessageOutUtil.SystemOutPrint("我已经进来了");
		// 关门
		manager.fireWorkspaceClosed();
	}

}
