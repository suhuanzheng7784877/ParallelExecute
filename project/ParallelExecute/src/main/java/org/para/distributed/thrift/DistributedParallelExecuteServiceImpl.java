package org.para.distributed.thrift;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.para.distributed.master.DistributedParallelExecute;
import org.para.distributed.master.MasterServer;
import org.para.util.jar.ExtendsClassLoaderFacade;

/**
 * 
 * thrift server
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-4 下午10:04:50
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class DistributedParallelExecuteServiceImpl implements
		DistributedParallelExecuteService.Iface {

	private static final Log LOG = LogFactory.getLog(MasterServer.class);

	/**
	 * 执行并行方法的名称
	 */
	public static final String exeParalleJobMethodName = "exeParalleJob";

	public DistributedParallelExecuteServiceImpl() {
	}

	@Override
	public boolean startDistributedParallelExecute(String jarHttpURI,
			String mainClassName, int parallelism_hint,
			Map<String, String> parameterMap) throws TException {
		try {
			// 先扫描http形式的jar包
			ExtendsClassLoaderFacade.scanJarFile(jarHttpURI, true);

			// 获取执行的主类
			Class<?> distributedParallelExecuteClazz = ExtendsClassLoaderFacade
					.findClass(jarHttpURI, mainClassName);

			// 实例化一个并行任务
			DistributedParallelExecute distributedParallelExecuteObject = (DistributedParallelExecute) distributedParallelExecuteClazz
					.newInstance();

			// 获取启动分布式任务的方法
			Method exeParalleJobMethod = distributedParallelExecuteClazz
					.getMethod(exeParalleJobMethodName, String.class,
							Map.class, int.class);

			// 执行启动分布式任务
			exeParalleJobMethod.invoke(distributedParallelExecuteObject,
					jarHttpURI, parameterMap, parallelism_hint);

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (InstantiationException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (SecurityException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			LOG.error("error", e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			LOG.error("error", e);
		}

		return false;
	}

}
