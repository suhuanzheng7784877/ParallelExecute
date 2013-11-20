package org.para.util;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.junit.Test;
import org.para.util.jar.ExtendsClassLoaderFacade;

public class TestExtendsClassLoaderFacade {

	@Test
	public void testScanJarFolder() {

		try {

			String jarFolderPath = "E://jars//";
			ExtendsClassLoaderFacade.scanJarFolder(jarFolderPath, false);
			String classFullName = "execute.StringArrayParallelExecute";
			String fileAbsolutePathString1 = "E://jars//ParalleExecutePlugin1.jar";
			Class<?> class1 = ExtendsClassLoaderFacade.findClass(
					fileAbsolutePathString1, classFullName);

			String[] sourceObject = new String[14];

			for (int i = 0; i < sourceObject.length; i++) {
				sourceObject[i] = i + 1 + "";
			}

			Object obj1 = class1.newInstance();

			System.out.println("123");

			Method method1 = class1.getMethod("exeParalleJob",
					Serializable.class, int.class, Object[].class);
			method1.invoke(obj1, sourceObject, 2, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testScanJarFile() {

		try {
			String jarFilePath = "E://jars//ParalleExecutePlugin1.jar";
			ExtendsClassLoaderFacade.scanJarFile(jarFilePath, true);
			String classFullName = "execute.StringArrayParallelExecute";

			Class<?> class1 = ExtendsClassLoaderFacade.findClass(jarFilePath,
					classFullName);

			String[] sourceObject = new String[14];

			for (int i = 0; i < sourceObject.length; i++) {
				sourceObject[i] = i + 1 + "";
			}

			Object obj1 = class1.newInstance();

			System.out.println("123");

			Method method1 = class1.getMethod("exeParalleJob",
					Serializable.class, int.class, Object[].class);
			method1.invoke(obj1, sourceObject, 2, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
