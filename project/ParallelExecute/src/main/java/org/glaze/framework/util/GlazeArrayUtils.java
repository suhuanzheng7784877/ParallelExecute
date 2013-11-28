package org.glaze.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 关于数组的辅助类
 *
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-30 下午7:36:52
 * @Copyright: 2013 story All rights reserved.
 *
 */
public class GlazeArrayUtils {

	/**
	 * stringbuilder的初始大小
	 */
	public static final int BigArrayInitSize = 3;

	/**
	 * 非原始数组是否要扩容
	 * 
	 * @param sourceArray
	 * @param index
	 */
	public static <T> T[] ensureCapacity(T[] sourceArray, int index) {

		// 原始数组大小
		int oldCapacity = sourceArray.length;

		// 需要的位置
		int nowNeedCapacity = index + 1;

		// 需要扩容
		if (oldCapacity < nowNeedCapacity) {
			int newCapacity = 0;
			if (0 == oldCapacity) {
				newCapacity = 1;
			} else {
				newCapacity = oldCapacity * 2;
			}
			sourceArray = Arrays.copyOf(sourceArray, newCapacity);
		}

		return sourceArray;

	}

	public static void main(String[] args) {
		int sum = 5000000;
		List<String> stringList = new ArrayList<String>(BigArrayInitSize);
		String[] ids = new String[BigArrayInitSize];
		long start1 = System.currentTimeMillis();
		for (int i = sum; i > 0; i--) {
			stringList.add(i + "");
		}

		long end1 = System.currentTimeMillis();

		System.out.println("List耗时：" + (end1 - start1) + "ms");

		int j = 0;

		long start2 = System.currentTimeMillis();
		for (int i = sum; i > 0; i--) {
			// 原始数组是否要扩容
			ids = GlazeArrayUtils.ensureCapacity(ids, i);
			ids[j] = i + "";
			j++;
		}
		long end2 = System.currentTimeMillis();

		System.out.println(" 数组耗时：" + (end2 - start2) + "ms");
	}
}
