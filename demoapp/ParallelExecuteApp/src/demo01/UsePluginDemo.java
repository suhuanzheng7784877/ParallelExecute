package demo01;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.para.distributed.thrift.client.DistributedParallelExecuteClient;
import org.para.exception.ParallelException;
import org.para.util.jar.ExtendsClassLoaderFacade;
import org.para.util.jar.ExtendsJarScanner;

public class UsePluginDemo {

	DistributedParallelExecuteClient distributedParallelExecuteClient = new DistributedParallelExecuteClient();

	@Test
	public void test01() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InterruptedException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {
		//
		// // 1
		// String fileAbsolutePathString1 = "e://ParalleExecutePlugin.jar";
		//
		// // 2
		// String classFullName = "execute.StringArrayParallelExecute";
		//
		// // 3
		// String methodName = "exeParalleJob";
		//
		//
		// ExtendsClassLoaderFacade.findClass(fileAbsolutePathString1,
		// classFullName);
		//
		// Class<?> class1 =
		// ExtendsJarScanner.findClass(fileAbsolutePathString1,
		// classFullName);
		//
		// Object obj1 = class1.newInstance();
		//
		// Method method1 = class1.getMethod(methodName, Serializable.class,
		// int.class, Object[].class);
		//
		// String[] sourceObject = new String[14];
		//
		// for (int i = 0; i < sourceObject.length; i++) {
		// sourceObject[i] = i + 1 + "";
		// }
		//
		// method1.invoke(obj1, sourceObject, 2, null);
		//
		// System.out.println("华丽的分割线==============");
		//
		// // 1
		// String fileAbsolutePathString2 = "e://ParalleExecutePlugin2.jar";
		//
		// // 2
		// String classFullName2 = "execute.IntParallelExecute";
		//
		// // 3
		// String methodName2 = "exeParalleJob";
		//
		// ExtendsClassLoaderFacade.findClass(fileAbsolutePathString2,
		// classFullName2);
		//
		// Class<?> class2 =
		// ExtendsJarScanner.findClass(fileAbsolutePathString2,
		// classFullName2);
		//
		// Object obj2 = class2.newInstance();
		//
		// Method method2 = class2.getMethod(methodName2, Serializable.class,
		// int.class, Object[].class);
		//
		// int[] sourceObject2 = new int[14];
		//
		// for (int i = 0; i < sourceObject.length; i++) {
		// sourceObject2[i] = i + 100;
		// }
		//
		// method2.invoke(obj2, sourceObject2, 2, null);
	}

	@Test
	public void test02() throws Exception {

		// 1
		String fileAbsolutePathString1 = "e://ParalleExecutePlugin.jar";

		// 2
		String classFullName = "execute.StringArrayParallelExecute";

		// 3
		String methodName = "exeParalleJob";

		ExtendsClassLoaderFacade.findClass(fileAbsolutePathString1,
				classFullName);

		Class<?> class1 = ExtendsJarScanner.findClass(fileAbsolutePathString1,
				classFullName);

		Object obj1 = class1.newInstance();

		Method method1 = class1.getMethod(methodName, Serializable.class,
				int.class, Object[].class);

		String[] sourceObject = new String[14];

		for (int i = 0; i < sourceObject.length; i++) {
			sourceObject[i] = i + 1 + "";
		}

		method1.invoke(obj1, sourceObject, 2, null);

		System.out.println("华丽的分割线==============");

		// 1
		String fileAbsolutePathString2 = "e://ParalleExecutePlugin2.jar";

		// 2
		String classFullName2 = "execute.IntParallelExecute";

		// 3
		String methodName2 = "exeParalleJob";

		ExtendsClassLoaderFacade.findClass(fileAbsolutePathString2,
				classFullName2);
		Class<?> class2 = ExtendsClassLoaderFacade.findClass(
				fileAbsolutePathString2, classFullName2);

		Object obj2 = class2.newInstance();

		Method method2 = class2.getMethod(methodName2, Serializable.class,
				int.class, Object[].class);

		int[] sourceObject2 = new int[14];

		for (int i = 0; i < sourceObject2.length; i++) {
			sourceObject2[i] = i + 100;
		}

		method2.invoke(obj2, sourceObject2, 2, null);

		System.out.println("华丽的分割线==============");

	}

	@Test
	public void test03Http() {
		// 1
		String fileAbsolutePathString1 = "http://127.0.0.1/ParalleExecutePlugin2_fat.jar";

		// 2
		String mainClassName = "distributed.MysqlDistributedParallelExecute";

		Map<String, String> map = new HashMap<String, String>();
		map.put("Driver", "com.mysql.jdbc.Driver");
		map.put("url", "jdbc:mysql://127.0.0.1:3306/test");
		map.put("name", "root");
		map.put("password", "111111");

		try {
			distributedParallelExecuteClient.startDistributedParallelExecute(
					fileAbsolutePathString1, mainClassName, 4, map);
		} catch (ParallelException e) {
			e.printStackTrace();
		}
	}
}
