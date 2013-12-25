package org.para.distributed.thrift.client;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.para.distributed.thrift.DistributedParallelExecuteService;
import org.para.exception.ParallelException;

/**
 * 
 * 连接分布式任务的客户端接口,实际是使用了thrift的客户端
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-4 下午10:22:57
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class DistributedParallelExecuteClient {

	private static final Log LOG = LogFactory
			.getLog(DistributedParallelExecuteClient.class);

	public static final String DEFAULE_SERVER_IP = "localhost";
	public static final int DEFAULE_SERVER_PORT = 8090;
	public static final int DEFAULE_TIMEOUT = 30000;

	/**
	 * 服务ip:serverIp
	 */
	private String serverIp = null;

	/**
	 * 服务端口号
	 */
	private int serverPort = -1;

	/**
	 * 服务超时时间
	 */
	private int serverTimeout = -1;

	public DistributedParallelExecuteClient() {
		super();
	}

	/**
	 * 客户端API
	 * 
	 * @param 服务ip
	 *            :serverIp
	 * @param 服务端口号
	 *            :serverPort
	 * @param 服务超时时间
	 *            :serverTimeout
	 */
	public DistributedParallelExecuteClient(String serverIp, int serverPort,
			int serverTimeout) {
		super();
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serverTimeout = serverTimeout;
	}

	/**
	 * 开始执行分布式任务
	 * 
	 * @param jarHttpURI
	 * @param mainClassName
	 * @param blockNum
	 * @param parameterMap
	 * @throws ParallelException
	 */
	public void startDistributedParallelExecute(String jarHttpURI,
			String mainClassName, int blockNum, Map<String, String> parameterMap)
			throws ParallelException {
		TTransport transport = new TFramedTransport(new TSocket(
				serverIp == null ? DEFAULE_SERVER_IP : serverIp,
				serverPort == -1 ? DEFAULE_SERVER_PORT : serverPort,
				serverTimeout == -1 ? DEFAULE_TIMEOUT : serverTimeout));

		// 协议要和服务端一致
		TProtocol protocol = new TBinaryProtocol(transport);
		DistributedParallelExecuteService.Client client = new DistributedParallelExecuteService.Client(
				protocol);
		try {
			transport.open();
			boolean result = client.startDistributedParallelExecute(jarHttpURI,
					mainClassName, blockNum, parameterMap);

			LOG.info("Thrify client result =: " + result);

		} catch (TTransportException e) {
			e.printStackTrace();
			throw new ParallelException(e);
		} catch (TException e) {
			e.printStackTrace();
			throw new ParallelException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParallelException(e);
		} finally {

			if (null != transport) {
				transport.close();
			}
		}
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getServerTimeout() {
		return serverTimeout;
	}

	public void setServerTimeout(int serverTimeout) {
		this.serverTimeout = serverTimeout;
	}

}
