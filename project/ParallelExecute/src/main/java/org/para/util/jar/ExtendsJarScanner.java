package org.para.util.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import org.para.util.classloader.ExtendsClassLoader;

/**
 * Extends dynamic Jar Scanner,at present support local file jar and http url
 * jar
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-30 下午3:40:28
 * @Copyright: 2013 story All rights reserved.
 */
public class ExtendsJarScanner {

	// record jar file,every jar file is one ExtendsClassLoader
	private final static Map<String, ExtendsClassLoader> Extends_ClassLoader_MAP = new ConcurrentHashMap<String, ExtendsClassLoader>(
			64);

	// record class file,key is jarfileAbsolutePathString@classFullName
	private final static Map<String, Class<?>> Extends_Class_MAP = new ConcurrentHashMap<String, Class<?>>(
			512);

	/**
	 * 自己的类加载器
	 */
	private final static ClassLoader SystemClassLoader = ExtendsJarScanner.class
			.getClassLoader();

	/**
	 * delete All ExtendsClassLoader And loaded class
	 */
	public static void deleteAll() {
		Extends_Class_MAP.clear();
		Extends_ClassLoader_MAP.clear();
	}

	/**
	 * 本身classloader添加一个jar,进行加载
	 * 
	 * @param jarFilePath
	 * @throws MalformedURLException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static boolean addSelfJarFile(String jarFilePath)
			throws MalformedURLException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		Class<URLClassLoader> systemClass = URLClassLoader.class;
		URLClassLoader urlClassLoader = (URLClassLoader) SystemClassLoader;

		URL newUrl = new URL(jarFilePath);

		Method method = systemClass.getDeclaredMethod("addURL",
				new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(urlClassLoader, newUrl);
		@SuppressWarnings("unused")
		int jarFileSize = urlClassLoader.getURLs().length;

		return true;
	}

	/**
	 * scan File Class
	 * 
	 * @param fileAbsolutePathString
	 * @param isRefresh
	 * @return
	 */
	public static boolean scanFileClass(String fileAbsolutePathString,
			boolean isRefresh) throws IOException {

		if (!isJarURI(fileAbsolutePathString)) {
			throw new IOException(fileAbsolutePathString + " is not jar");
		}

		if (isHttpURI(fileAbsolutePathString)) {
			return scanHTTPJarClass(fileAbsolutePathString, isRefresh);
		} else {
			return scanLocalJarClass(fileAbsolutePathString, isRefresh);
		}

	}

	/**
	 * scan folder Classes
	 * 
	 * @param folderAbsolutePathString
	 * @param isRefresh
	 * @return
	 * @throws IOException
	 */
	public static boolean scanFolderClasses(String folderAbsolutePathString,
			boolean isRefresh) throws IOException {

		File folder = new File(folderAbsolutePathString);
		if (!folder.isDirectory()) {
			throw new IOException(folderAbsolutePathString + " is not folder");
		}
		boolean result = true;

		File[] files = folder.listFiles();

		int fileNum = files.length;
		File file = null;
		String fileAbsolutePathString = null;
		for (int i = 0; i < fileNum; i++) {
			file = files[i];
			fileAbsolutePathString = file.getAbsolutePath();
			if (!fileAbsolutePathString.endsWith(".jar")) {
				continue;
			}

			// isolate inner folder jars
			scanLocalJarClass(fileAbsolutePathString, isRefresh);

		}

		return result;
	}

	/**
	 * scan Local Jar Class
	 * 
	 * @param fileAbsolutePathString
	 * @return
	 */
	private static boolean scanLocalJarClass(String fileAbsolutePathString,
			boolean isRefresh) throws IOException {

		File file = new File(fileAbsolutePathString);

		if (!file.isFile()) {
			throw new IOException(fileAbsolutePathString + " is not file");
		}

		JarFile jarFile = null;
		Enumeration<JarEntry> entries = null;
		ExtendsClassLoader extendsClassLoader = null;
		JarEntry entry = null;
		String entryName = null;
		Class<?> clazz = null;
		String key = null;

		try {

			jarFile = new JarFile(file);

			entries = jarFile.entries();

			// get Extends ClassLoader by file AbsolutePath
			extendsClassLoader = getExtendsClassLoader(fileAbsolutePathString,
					isRefresh);

			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				entryName = entry.getName();
				if (entryName.endsWith(".class")) {
					entryName = entryName.substring(0, entryName.length() - 6);
					entryName = entryName.replaceAll("/", ".");
					clazz = extendsClassLoader.loadClass(entryName);
					key = fileAbsolutePathString + "@" + entryName;
					Extends_Class_MAP.put(key, clazz);
				}
			}
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {

				if (jarFile != null) {
					jarFile.close();
					jarFile = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * scan HTTP URI Jar Class
	 * 
	 * @param fileAbsolutePathString
	 * @return
	 * @throws IOException
	 */
	public static boolean scanFileClassAtSystemClassLoader(
			String fileAbsolutePathString, boolean isRefresh)
			throws IOException {
		URL url = null;
		InputStream is = null;
		JarInputStream jarInputStream = null;
		HttpURLConnection httpURLConnection = null;
		ZipEntry zipEntry = null;
		try {

			Class<?> clazz = null;
			String key = null;
			String classFullName = null;

			url = new URL(fileAbsolutePathString);

			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.connect();
			is = httpURLConnection.getInputStream();
			jarInputStream = new JarInputStream(is);

			while ((zipEntry = jarInputStream.getNextEntry()) != null) {
				classFullName = zipEntry.getName();
				if (classFullName.endsWith(".class")) {
					classFullName = classFullName.substring(0,
							classFullName.length() - 6);
					classFullName = classFullName.replaceAll("/", ".");
					clazz = SystemClassLoader.loadClass(classFullName);
					key = fileAbsolutePathString + "@" + classFullName;
					Extends_Class_MAP.put(key, clazz);
				}
			}
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {

				url = null;
				httpURLConnection = null;
				zipEntry = null;

				if (jarInputStream != null) {
					jarInputStream.close();
					jarInputStream = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * scan HTTP URI Jar Class
	 * 
	 * @param fileAbsolutePathString
	 * @return
	 * @throws IOException
	 */
	public static boolean scanHTTPJarClass(String fileAbsolutePathString,
			boolean isRefresh) throws IOException {
		URL url = null;
		InputStream is = null;
		JarInputStream jarInputStream = null;
		HttpURLConnection httpURLConnection = null;
		ZipEntry zipEntry = null;
		ExtendsClassLoader extendsClassLoader = null;
		try {

			Class<?> clazz = null;
			String key = null;
			String classFullName = null;
			extendsClassLoader = getExtendsClassLoader(fileAbsolutePathString,
					isRefresh);

			url = new URL(fileAbsolutePathString);

			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.connect();
			is = httpURLConnection.getInputStream();
			jarInputStream = new JarInputStream(is);

			while ((zipEntry = jarInputStream.getNextEntry()) != null) {
				classFullName = zipEntry.getName();
				if (classFullName.endsWith(".class")) {
					classFullName = classFullName.substring(0,
							classFullName.length() - 6);
					classFullName = classFullName.replaceAll("/", ".");
					clazz = extendsClassLoader.loadClass(classFullName);
					key = fileAbsolutePathString + "@" + classFullName;
					Extends_Class_MAP.put(key, clazz);
				}
			}
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {

				url = null;
				httpURLConnection = null;
				zipEntry = null;

				if (jarInputStream != null) {
					jarInputStream.close();
					jarInputStream = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * getClass
	 * 
	 * @param fileAbsolutePathString
	 * @param classFullName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?> findClass(String fileAbsolutePathString,
			String classFullName) throws ClassNotFoundException, IOException {
		String key = fileAbsolutePathString + "@" + classFullName;

		Class<?> clazz = Extends_Class_MAP.get(key);
		if (clazz != null) {
			return clazz;
		}

		//
		scanFileClass(fileAbsolutePathString, false);

		clazz = Extends_Class_MAP.get(key);

		if (clazz == null) {
			throw new ClassNotFoundException();
		}

		return clazz;
	}

	/**
	 * getClass
	 * 
	 * @param fileAbsolutePathString
	 * @param classFullName
	 * @param isRefresh
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?> findClass(String fileAbsolutePathString,
			String classFullName, boolean isRefresh)
			throws ClassNotFoundException, IOException {
		String key = fileAbsolutePathString + "@" + classFullName;

		Class<?> clazz = Extends_Class_MAP.get(key);
		if (clazz != null) {
			return clazz;
		}

		//
		scanFileClass(fileAbsolutePathString, isRefresh);

		clazz = Extends_Class_MAP.get(key);

		if (clazz == null) {
			throw new ClassNotFoundException();
		}

		return clazz;
	}

	/**
	 * transfer File AbsolutePath To URL
	 * 
	 * @param fileAbsolutePathString
	 * @return
	 */
	private static URL[] transferFileAbsolutePathToURL(
			String fileAbsolutePathString) {
		URL[] url = null;
		try {
			url = new URL[] { new URL("file:" + fileAbsolutePathString) };
			return url;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * transfer File AbsolutePath To HTTP URL
	 * 
	 * @param fileAbsolutePathString
	 * @return
	 */
	private static URL[] transferHttpFilePathToURL(String httpFilePath) {
		URL[] url = null;
		try {
			url = new URL[] { new URL(httpFilePath) };
			return url;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get Extends ClassLoader by fileAbsolutePathString
	 * 
	 * @param fileAbsolutePathString
	 * @return
	 */
	private static ExtendsClassLoader getExtendsClassLoader(
			String fileAbsolutePathString) {

		// default cache ClassLoader
		if (Extends_ClassLoader_MAP.containsKey(fileAbsolutePathString)) {
			return Extends_ClassLoader_MAP.get(fileAbsolutePathString);
		}

		ExtendsClassLoader extendsClassLoader = newInstenceExtendsClassLoader(fileAbsolutePathString);
		Extends_ClassLoader_MAP.put(fileAbsolutePathString, extendsClassLoader);
		return extendsClassLoader;
	}

	/**
	 * get Extends ClassLoader by fileAbsolutePathString
	 * 
	 * @param fileAbsolutePathString
	 * @param isRefresh
	 * @return
	 */
	private static ExtendsClassLoader getExtendsClassLoader(
			String fileAbsolutePathString, boolean isRefresh) {

		if (!isRefresh) {

			// return from cache pool
			return getExtendsClassLoader(fileAbsolutePathString);
		}

		// remove old ExtendsClassLoader
		if (Extends_ClassLoader_MAP.containsKey(fileAbsolutePathString)) {
			Extends_ClassLoader_MAP.remove(fileAbsolutePathString);
		}

		// put new ExtendsClassLoader
		ExtendsClassLoader extendsClassLoader = newInstenceExtendsClassLoader(fileAbsolutePathString);
		Extends_ClassLoader_MAP.put(fileAbsolutePathString, extendsClassLoader);
		return extendsClassLoader;
	}

	/**
	 * new Instence ExtendsClassLoader
	 * 
	 * @param fileAbsolutePathString
	 * @return
	 */
	private static ExtendsClassLoader newInstenceExtendsClassLoader(
			String fileAbsolutePathString) {
		URL[] url = null;
		if (isHttpURI(fileAbsolutePathString)) {
			url = transferHttpFilePathToURL(fileAbsolutePathString);
		} else {
			url = transferFileAbsolutePathToURL(fileAbsolutePathString);
		}
		return new ExtendsClassLoader(url);
	}

	/**
	 * uri is http jar type
	 * 
	 * @param uri
	 * @return
	 */
	private static boolean isHttpURI(String uri) {

		if (uri == null || "".equals(uri)) {
			return false;
		}

		if (uri.startsWith("http://")) {
			return true;
		}
		return false;
	}

	/**
	 * uri is jar type
	 * 
	 * @param uri
	 * @return
	 */
	private static boolean isJarURI(String uri) {

		if (uri == null || "".equals(uri)) {
			return false;
		}

		if (uri.endsWith(".jar")) {
			return true;
		}
		return false;
	}

}
