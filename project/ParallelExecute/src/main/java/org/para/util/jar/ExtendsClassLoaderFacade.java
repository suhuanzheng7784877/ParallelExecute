package org.para.util.jar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-10-6 下午5:10:28
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class ExtendsClassLoaderFacade {

	/**
	 * scan Jar File
	 * 
	 * @param jarFilePath
	 * @param isRefresh
	 * @return
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	public static boolean addSelfJarFile(String jarFilePath)
			throws IOException, SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		return ExtendsJarScanner.addSelfJarFile(jarFilePath);
	}

	/**
	 * scan Jar File
	 * 
	 * @param jarFilePath
	 * @param isRefresh
	 * @return
	 * @throws IOException
	 */
	public static boolean scanJarFile(String jarFilePath, boolean isRefresh)
			throws IOException {
		return ExtendsJarScanner.scanFileClass(jarFilePath, isRefresh);
	}

	/**
	 * scan Jar File
	 * 
	 * @param jarFilePath
	 * @param isRefresh
	 * @return
	 * @throws IOException
	 */
	public static boolean scanJarFileAtSystemClassLoader(String jarFilePath,
			boolean isRefresh) throws IOException {
		return ExtendsJarScanner.scanFileClassAtSystemClassLoader(jarFilePath,
				isRefresh);
	}

	/**
	 * scan Jar Folder
	 * 
	 * @param jarFolderPath
	 * @param isIsolate
	 * @param isRefresh
	 * @return
	 * @throws IOException
	 */
	public static boolean scanJarFolder(String jarFolderPath, boolean isRefresh)
			throws IOException {
		return ExtendsJarScanner.scanFolderClasses(jarFolderPath, isRefresh);
	}

	/**
	 * find Class by frontPath+className
	 * 
	 * @param frontPath
	 * @param className
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Class<?> findClass(String frontPath, String className)
			throws ClassNotFoundException, IOException {
		return ExtendsJarScanner.findClass(frontPath, className);
	}

}
