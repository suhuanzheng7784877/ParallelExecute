package org.para.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.para.util.jar.ExtendsJarScanner;

public class TestExtendsClassloader {

	@Test
	public void test01() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException, IOException {

		String fileAbsolutePathString1 = "e://a1.jar";
		String fileAbsolutePathString2 = "e://a2.jar";
		String classFullName = "democlass.MyDemoClass";
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString1, true);
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString2, true);

		Class<?> class1 = ExtendsJarScanner.findClass(fileAbsolutePathString1,
				classFullName);
		Class<?> class2 = ExtendsJarScanner.findClass(fileAbsolutePathString2,
				classFullName);
		MessageOutUtil.SystemOutPrint(class1.getClassLoader());
		MessageOutUtil.SystemOutPrint(class2.getClassLoader());
		Object obj1 = class1.newInstance();
		Object obj2 = class2.newInstance();
		Method method1 = class1.getDeclaredMethod("aaa");
		Method method2 = class2.getDeclaredMethod("aaa");
		method1.invoke(obj1);
		method2.invoke(obj2);
	}

	@Test
	public void test02() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException, IOException {

		String fileAbsolutePathString1 = "e://a1.jar";
		String fileAbsolutePathString2 = "e://a2.jar";
		String classFullName = "democlass.MyDemoClass";
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString1, true);
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString2, true);

		Class<?> class1 = ExtendsJarScanner.findClass(fileAbsolutePathString1,
				classFullName);
		Class<?> class2 = ExtendsJarScanner.findClass(fileAbsolutePathString2,
				classFullName);
		MessageOutUtil.SystemOutPrint(class1.getClassLoader());
		MessageOutUtil.SystemOutPrint(class2.getClassLoader());
		Object obj1 = class1.newInstance();
		Object obj2 = class2.newInstance();
		Method method1 = class1.getDeclaredMethod("addNum");
		Method method2 = class2.getDeclaredMethod("addNum");
		method1.invoke(obj1);
		method2.invoke(obj2);
	}

	@Test
	public void test03() throws ClassNotFoundException {

		String classFullName = "org.para.util.DBDataUtil";
		Class<?> clazz = Class.forName(classFullName);
		MessageOutUtil.SystemOutPrint(clazz);

	}

	@Test
	public void test04() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException, IOException {

		String fileAbsolutePathString1 = "e://a1.jar";
		String fileAbsolutePathString2 = "e://a2.jar";
		String classFullName = "democlass.MyDemoClass";
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString1, true);
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString2, true);

		Class<?> class1 = ExtendsJarScanner.findClass(fileAbsolutePathString1,
				classFullName);
		Class<?> class2 = ExtendsJarScanner.findClass(fileAbsolutePathString2,
				classFullName);
		MessageOutUtil.SystemOutPrint(class1.getClassLoader());
		MessageOutUtil.SystemOutPrint(class2.getClassLoader());
		Object obj1 = class1.newInstance();
		Object obj2 = class2.newInstance();
		Method method1 = class1.getDeclaredMethod("loadFatherClass");
		Method method2 = class2.getDeclaredMethod("loadFatherClass");
		method1.invoke(obj1);
		method2.invoke(obj2);
	}

	@Test
	public void test05() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException, IOException {

		String fileAbsolutePathString1 = "e://a1.jar";
		String fileAbsolutePathString2 = "e://a3.jar";
		String classFullName = "democlass.MyDemoClass";
		String classFullName2 = "test.MM";
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString1, true);
		ExtendsJarScanner.scanFileClass(fileAbsolutePathString2, true);

		Class<?> class1 = ExtendsJarScanner.findClass(fileAbsolutePathString1,
				classFullName);
		Class<?> class2 = ExtendsJarScanner.findClass(fileAbsolutePathString2,
				classFullName2);
		MessageOutUtil.SystemOutPrint(class1.getClassLoader());
		MessageOutUtil.SystemOutPrint(class2.getClassLoader());
		Object obj1 = class1.newInstance();
		Object obj2 = class2.newInstance();
		Method method1 = class1.getDeclaredMethod("loadFatherClass");
		Method method2 = class2.getDeclaredMethod("te1");
		method1.invoke(obj1);
		method2.invoke(obj2);
	}

	@Test
	public void test06() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException, IOException {
		String folderAbsolutePathString = "F:/jardir";

		File folder = new File(folderAbsolutePathString);

		File[] files = folder.listFiles();

		int fileNum = files.length;
		File file = null;
		for (int i = 0; i < fileNum; i++) {
			file = files[i];
			System.out.println("fileAbsolutePathString:"
					+ file.getAbsolutePath());
		}
	}

}
