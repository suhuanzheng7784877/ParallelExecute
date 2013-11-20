package org.para.trace.listener;

import org.para.util.MessageOutUtil;

public class DoorListener2 implements DoorListener {

	public void doorEvent(DoorEvent event) {
		if (event.getDoorState() != null && event.getDoorState().equals("open")) {
			MessageOutUtil.SystemOutPrint("门2打开，同时打开走廊的灯");
		} else {
			MessageOutUtil.SystemOutPrint("门2关闭，同时关闭走廊的灯");
		}
	}

}
