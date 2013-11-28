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
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-4 下午10:22:57
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class DistributedParallelExecuteClient {
	
	private static final Log LOG = LogFactory.getLog(DistributedParallelExecuteClient.class);

	public static final String DEFAULE_SERVER_IP = "localhost";
	public static final int DEFAULE_SERVER_PORT = 8090;
	public static final int DEFAULE_TIMEOUT = 30000;

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
				DEFAULE_SERVER_IP, DEFAULE_SERVER_PORT, DEFAULE_TIMEOUT));

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

}
