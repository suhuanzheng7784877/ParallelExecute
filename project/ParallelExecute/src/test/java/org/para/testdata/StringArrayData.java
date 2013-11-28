package org.para.testdata;

public class StringArrayData {

	static String[] testStrings = new String[200];
	static {
		for (int i = 0; i < testStrings.length; i++) {
			testStrings[i] = "" + i;
		}
	}

}
