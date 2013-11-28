package org.para.distributed.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.para.distributed.dto.WorkerNode;
import org.para.distributed.task.DistributedParallelTask;
import org.para.util.PropertiesUtil;
import org.para.util.StringUtil;

import com.sun.management.OperatingSystemMXBean;

/**
 * 获取系统信息相关的辅助方法
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-18
 * @Copyright: 2013 story All rights reserved.
 */
public final class SystemUtil {

	private static Logger logger = Logger.getLogger(SystemUtil.class);

	/**
	 * 系统的classpath
	 */
	private static String java_class_path = System.getProperty(
			"java.class.path");

	/**
	 * 本机node的ip地址
	 */
	public volatile static String localIP = null;

	/**
	 * 查询CPU使用率的命令
	 */
	public final static String cpuUseCommandTop = "top -b -n 1";

	/**
	 * 查看CPU使用率命令
	 */
	public final static String cpuUseCommandVmstat = "vmstat -n 1 -a 2";

	/**
	 * 查询内存的命令
	 */
	public final static String memoryCommand = "cat /proc/meminfo";

	/**
	 * 上一次的CPU空闲率
	 */
	public volatile static float lastCPURate = 0.0F;

	/**
	 * 以mb为单位的分母
	 */
	public static final int mb = 1024 * 1024;

	/**
	 * 操作系统相关的MBean
	 */
	public static final OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();

	/**
	 * 结点机器上可用磁盘根路径
	 */
	final static String NODE_ROOT_PATH = PropertiesUtil
			.getValue("nodeagent.downloadfile.freeDisk");

	static {
		try {
			localIP = getIP();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取结点硬件信息
	 * 
	 * @param isRegister
	 * @return
	 * @throws UnknownHostException
	 */
	public static WorkerNode getWorkerNode(boolean isRegister)
			throws UnknownHostException {

		WorkerNode workerNode = WorkerNode.getSingle();

		// CPU空闲率
		float cpuFreeRate;

		// 注册时间
		long registerTime = System.currentTimeMillis();

		if (isRegister) {

			// [1]-是注册的逻辑
			workerNode.setCreatetime(registerTime);

			// cpu的闲置率
			cpuFreeRate = getCpuFreeRateForRegister();

			// 构建节点上的应用实例信息
			// buildNodeWebAppInstences(workerNode);
		} else {

			// [2]-心跳的逻辑

			// cpu的闲置率
			cpuFreeRate = getCpuFreeRateForLinuxHeartbeat();
		}

		// 本机器空余硬盘
		long freeDisk = getFreeDisk();

		// 本机器空余内存
		long freeMemroy = getRealFreeMemory();

		// 物理总内存
		long physicalTotal = getPhysicalTotal();

		// 本机器CPU核数
		int cpuProcessors = getCPUProcessors();

		// 内存空闲率
		float memroyFreeRate = Float.parseFloat(String.valueOf(freeMemroy))
				/ Float.parseFloat(String.valueOf(physicalTotal));

		logger.info("node-ip:" + localIP);
		logger.info("freeDisk:" + freeDisk);
		logger.info("freeMemroy:" + freeMemroy);
		logger.info("cpuProcessors:" + cpuProcessors);
		logger.info("cpuFreeRate:" + cpuFreeRate);
		logger.info("physicalTotal:" + physicalTotal);
		logger.info("memroyFreeRate:" + memroyFreeRate);
		logger.info("registerTime:" + registerTime);

		workerNode.setCpufreerate(cpuFreeRate);
		workerNode.setWorkerIp(localIP);
		workerNode.setFreedisk(freeDisk);
		workerNode.setFreememroy(freeMemroy);
		workerNode.setLasthearttime(registerTime);

		return workerNode;
	}

	/**
	 * 构建节点上的应用实例信息 TODO:暂时不发送节点上的任务信息，暂时由总master进行调度
	 * 
	 * @param nodeInfo
	 */
	// public static void buildNodeWebAppInstences(WorkerNode workerNode) {
	// // 应用实例Map集合
	// Map<String, WebAppInstence> webAppInstenceMaps =
	// GlobalMemroyStore.WebAppInstencePool;
	// WebAppInstence webAppInstence = null;
	// List<WebAppInstence> webAppInstenceList = new ArrayList<WebAppInstence>(
	// 16);
	// for (String webAppInstenceKey : webAppInstenceMaps.keySet()) {
	//
	// webAppInstence = webAppInstenceMaps.get(webAppInstenceKey);
	// webAppInstenceList.add(webAppInstence);
	// }
	// nodeInfo.setWebAppInstences(webAppInstenceList);
	// }

	/**
	 * 检查附着在结点上的所有应用实例情况2.9.3
	 * 
	 * @return
	 */
	public static List<DistributedParallelTask> getParallelTasks() {
		return null;
	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getIP() throws UnknownHostException {
		if (localIP == null) {

			try {

				if (StringUtil.OSisLinux()) {// linux获取ip

					Enumeration<NetworkInterface> e1 = NetworkInterface
							.getNetworkInterfaces();
					while (e1.hasMoreElements()) {
						NetworkInterface ni = e1.nextElement();
						if (ni.getName().startsWith("eth0")) {
							Enumeration<InetAddress> e2 = ni.getInetAddresses();
							InetAddress ia = null;
							while (e2.hasMoreElements()) {
								ia = e2.nextElement();
								if (!(ia instanceof Inet6Address)) {
									localIP = ia.getHostAddress();
									if (localIP != null
											&& !"".equals(localIP.trim())) {
										return localIP;
									}
								}
							}
						}
					}

				} else {// windows获取ip
					localIP = InetAddress.getLocalHost().getHostAddress();
					return localIP;
				}

			} catch (SocketException e) {
				logger.error("error", e);
			} catch (Exception e) {
				logger.error("error", e);
			}
		}

		return localIP;
	}

	/**
	 * 获取剩余硬盘信息
	 * 
	 * @return
	 */
	public static long getFreeDisk() {
		File fileRoot = new File(NODE_ROOT_PATH);

		// 如果不存在，则直接创建。
		if (!fileRoot.exists()) {
			fileRoot.mkdir();
		}

		long freeSpaceB = fileRoot.getFreeSpace();

		long freeSpaceMB = freeSpaceB / mb;

		return freeSpaceMB;

	}

	/**
	 * 获取剩余内存,适用于windows
	 * 
	 * @return
	 */
	public static long getFreeMemroy() {

		// 空闲物理内存
		long physicalFree = osmxb.getFreePhysicalMemorySize() / mb;

		// 系统总物理内存
		long physicalTotal = osmxb.getTotalPhysicalMemorySize() / mb;

		// 已用的物理内存
		long physicalUse = physicalTotal - physicalFree;

		// 获取操作系统的版本
		String os = System.getProperty("os.name");

		logger.info("OS Version：" + os);
		logger.info("physicalFree ：" + physicalFree + "MB");
		logger.info("physicalUse：" + physicalUse + "MB");
		logger.info("physicalTotal：" + physicalTotal + "MB");

		// 单位是mb
		return physicalFree;

	}

	/**
	 * 获取剩余内存
	 * 
	 * @return
	 */
	public static long getPhysicalTotal() {
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();

		// 系统总物理内存
		long physicalTotal = osmxb.getTotalPhysicalMemorySize() / mb;

		// 单位是mb
		return physicalTotal;

	}

	/**
	 * 获取CPU核数
	 * 
	 * @return
	 */
	public static int getCPUProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * 本机IP是否在该ips的List中
	 * 
	 * @param ips
	 * @return
	 * @throws UnknownHostException
	 */
	public final static boolean ipIsWillWork(List<String> ips)
			throws UnknownHostException {

		if (ips == null || 0 == ips.size()) {
			return false;
		}

		String localhostIp = getIP();

		if (localhostIp == null || "".equals(localhostIp)) {
			return false;
		}

		System.out.println("ips:" + ips);

		// List列表是否包含本机的ip地址
		return ips.contains(localhostIp);

	}

	/**
	 * 获取瞬时CPU空闲率, TODO:暂时只支持linux windows默认是50%
	 * 
	 * @return
	 */
	public final static float getCpuFreeRateForRegister() {

		// linux系统
		if (StringUtil.OSisLinux()) {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader brStat = null;
			try {

				// CPU空闲率
				Float cpuFreeFloat = readCPUFreeVMStatCommand(is, isr, brStat);

				logger.info("cpuFreeFloat.floatValue():"
						+ cpuFreeFloat.floatValue());

				// 返回CPU空闲率

				float cpuFreeRate = Float.parseFloat(String
						.valueOf(cpuFreeFloat)) / 100;

				// 记住此次的CPU空闲率
				rememberLastCPUFreeRate(cpuFreeRate);
				return cpuFreeRate;

			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out.println(ioe.getMessage());
				logger.error("error", ioe);
				return 0.5F;
			} catch (InterruptedException ee) {
				ee.printStackTrace();
				logger.error("error", ee);
				return 0.5F;
			} finally {
				freeResource(is, isr, brStat);
			}
		} else {
			return 0.5F;
		}

	}

	/**
	 * 记住此次的CPU空闲率
	 * 
	 * @param cpuFreeRate
	 */
	private static void rememberLastCPUFreeRate(float cpuFreeRate) {
		lastCPURate = cpuFreeRate;
	}

	/**
	 * CPU平均空闲率
	 * 
	 * @param currentCpuFreeRate
	 * @return
	 */
	private static float getAverageCPUFreeRate(float currentCpuFreeRate) {
		float averageCPUFreeRate = (lastCPURate + currentCpuFreeRate) / 2;
		return averageCPUFreeRate;
	}

	/**
	 * 获取CPU空闲率, TODO:暂时只支持linux
	 * 
	 * @return
	 */
	public final static float getCpuFreeRateForLinuxHeartbeat() {

		// linux系统
		if (StringUtil.OSisLinux()) {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader brStat = null;
			try {
				// CPU空闲率
				Float cpuFreeFloat = readCPUFreeVMStatCommand(is, isr, brStat);

				logger.info("cpuFreeFloat.floatValue():"
						+ cpuFreeFloat.floatValue());

				// 返回CPU空闲率

				float cpuFreeRate = Float.parseFloat(String
						.valueOf(cpuFreeFloat)) / 100;

				float averageCPUFreeRate = getAverageCPUFreeRate(cpuFreeRate);

				// CPU的平均空闲率
				logger.info("[averageCPUFreeRate]:" + averageCPUFreeRate);

				// 记住此次的CPU空闲率
				rememberLastCPUFreeRate(averageCPUFreeRate);

				return averageCPUFreeRate;

			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out.println(ioe.getMessage());
				return 0.5F;
			} catch (InterruptedException ee) {
				ee.printStackTrace();
				logger.error("error", ee);
				return 0.5F;
			} finally {
				freeResource(is, isr, brStat);
			}
		} else {
			return 0.5F;
		}

	}

	/**
	 * 释放空闲IO资源
	 * 
	 * @param is
	 * @param isr
	 * @param br
	 */
	private static void freeResource(InputStream is, InputStreamReader isr,
			BufferedReader br) {
		try {
			if (is != null)
				is.close();
			is = null;
			if (isr != null)
				isr.close();
			isr = null;
			if (br != null)
				br.close();
			isr = null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error("error", ioe);
		}
	}

	/**
	 * 获取linux真正的内存:free+buffer+cache
	 * 
	 * @return
	 */
	private static long getRealFreeMemory() {

		if (StringUtil.OSisLinux()) {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader brStat = null;
			StringTokenizer tokenStat = null;
			Process process = null;
			try {
				process = Runtime.getRuntime().exec(memoryCommand);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("error", e);
			}

			// 进程执行结果输入
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);

			// 第一行是总内存需要忽略
			try {
				brStat.readLine();
				String memFreeStringLine = brStat.readLine();

				logger.debug("memFreeStringLine:" + memFreeStringLine);

				tokenStat = new StringTokenizer(memFreeStringLine);
				tokenStat.nextToken();
				String memFreeString = tokenStat.nextToken();

				String buffersStringLine = brStat.readLine();
				logger.debug("buffersStringLine:" + buffersStringLine);
				tokenStat = new StringTokenizer(buffersStringLine);
				tokenStat.nextToken();
				String buffersString = tokenStat.nextToken();

				String cachedStringLine = brStat.readLine();
				logger.debug("cachedStringLine:" + cachedStringLine);
				tokenStat = new StringTokenizer(cachedStringLine);
				tokenStat.nextToken();
				String cachedString = tokenStat.nextToken();

				logger.debug("memFreeString:" + memFreeString);
				logger.debug("buffersString:" + buffersString);
				logger.debug("cachedString:" + cachedString);

				long memFree = Long.parseLong(memFreeString);
				long buffers = Long.parseLong(buffersString);
				long cached = Long.parseLong(cachedString);

				logger.info("memFree:" + memFree + "kb");
				logger.info("buffers:" + buffers + "kb");
				logger.info("cached:" + cached + "kb");

				// 以mb为单位
				long availableMem = (memFree + buffers + cached) / 1024;
				logger.info("availableMem:" + availableMem + "mb");

				return availableMem;

			} catch (IOException e) {
				e.printStackTrace();
				logger.error("error", e);
			} finally {

				// 释放空闲IO资源
				freeResource(is, isr, brStat);
			}
			return 0L;
		} else {

			// 适用于Windows
			return getFreeMemroy();
		}

	}

	/**
	 * 读取VMstat命令，获取CPU空闲率
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static Float readCPUFreeVMStatCommand(InputStream is,
			InputStreamReader isr, BufferedReader brStat) throws IOException,
			InterruptedException {
		StringTokenizer tokenStat = null;

		Process process = Runtime.getRuntime().exec(cpuUseCommandVmstat);

		// 等待执行完毕
		process.waitFor();

		// 进程执行结果输入s
		is = process.getInputStream();
		isr = new InputStreamReader(is);
		brStat = new BufferedReader(isr);

		// 第一行是title需要忽略
		brStat.readLine();
		brStat.readLine();
		brStat.readLine();
		// 获取top命令执行结果
		String resultString = brStat.readLine();

		logger.info("----brStat----" + resultString);

		tokenStat = new StringTokenizer(resultString);
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();
		tokenStat.nextToken();

		String cpuFree = tokenStat.nextToken();

		logger.info("CPU idle : " + cpuFree);

		// CPU空闲率
		Float cpuFreeFloat = new Float(cpuFree);

		return cpuFreeFloat;
	}

	/**
	 * 获取系统的 classpath
	 * 
	 * @return
	 */
	public static String getSystemClassPath() {
		return java_class_path;
	}
}
