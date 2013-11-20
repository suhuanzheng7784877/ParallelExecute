package org.para.util.jar;

import java.io.IOException;

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
	 */
	public static boolean scanJarFile(String jarFilePath, boolean isRefresh)
			throws IOException {
		return ExtendsJarScanner.scanFileClass(jarFilePath, isRefresh);
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
