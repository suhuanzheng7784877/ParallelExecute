package org.para.distributed.thrift.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.para.distributed.thrift.DistributedParallelExecuteService;
import org.para.distributed.thrift.DistributedParallelExecuteServiceImpl;
import org.para.util.PropertiesUtil;

/**
 * 
 * thrift的server端
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-4 下午10:54:02
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class DistributedParalleExecuteTHsHaServer {

	/**
	 * master的thrift.port
	 */
	public static final int SERVER_PORT = Integer.parseInt(PropertiesUtil
			.getValue("master.thrift.port"));

	/**
	 * 
	 */
	private DistributedParalleExecuteTHsHaServer() {

	}

	private static DistributedParalleExecuteTHsHaServer distributedParalleExecuteTHsHaServer = new DistributedParalleExecuteTHsHaServer();

	public static DistributedParalleExecuteTHsHaServer getInstence() {
		return distributedParalleExecuteTHsHaServer;
	}

	final TProcessor tprocessor = new DistributedParallelExecuteService.Processor<DistributedParallelExecuteService.Iface>(
			new DistributedParallelExecuteServiceImpl());

	private volatile TServer server;

	/**
	 * 启动thrift的server端的服务
	 */
	public void startTHsHaServer() {
		try {
			TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(
					SERVER_PORT);
			THsHaServer.Args thhsArgs = new THsHaServer.Args(tnbSocketTransport);

			// 设置处理逻辑
			thhsArgs.processor(tprocessor);

			// 传输方式
			thhsArgs.transportFactory(new TFramedTransport.Factory());

			// 数据协议
			thhsArgs.protocolFactory(new TCompactProtocol.Factory());

			// 半同步半异步的服务模型
			server = new THsHaServer(thhsArgs);
			server.serve();

		} catch (Exception e) {
			System.out.println("Server start error!!!");
			e.printStackTrace();
		}
	}

	/**
	 * 停止thrift的server端的服务
	 */
	public void stopTHsHaServer() {
		server.stop();
	}

	public static void main(String[] args) {
		DistributedParalleExecuteTHsHaServer.getInstence().startTHsHaServer();
	}

}
