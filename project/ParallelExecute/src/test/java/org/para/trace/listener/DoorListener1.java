package org.para.trace.listener;

import org.para.util.MessageOutUtil;

public class DoorListener1 implements DoorListener {

	public void doorEvent(DoorEvent event) {
		if (event.getDoorState() != null && event.getDoorState().equals("open")) {
			MessageOutUtil.SystemOutPrint("门1打开");
		} else {
			MessageOutUtil.SystemOutPrint("门1关闭");
		}
	}

}
