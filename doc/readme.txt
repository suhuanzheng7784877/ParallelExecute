说明文档
作者：刘岩
Email：suhuanzheng7784877@163.com
1说明
ParallelExecute是简单并行任务的框架。
所谓并行任务可以将一个粗粒度的大任务，切割成为一个个细小的小任务。每一个被切割的小任务可以多线程（单点模式）或者远程分布式（分布式）运行。
逻辑结构如下图所示
 
使用Java实现，该框架分为分布式模式和单点模式。单点模式主要使用多线程执行子任务，与主程序在一个JVM进程中。分布式模式将子任务远程通知到任务结点机器去执行，每个子任务是一个单独的JVM运行。（后续详细介绍本地模式和分布式模式的原理）
功能：
并行任务插件式，简单继承、扩展模板类即可实现并行任务
支持单机/分布式的模式
分布式模式下按需分配任务资源
任务插件热部署
任务失败的通知、重做、失效转移等等failover机制
任务执行机器状态自动感知（注册、健康、忙碌、宕机）
（待补充，迭代）
2安装
从github上下载并行任务框架（https://github.com/suhuanzheng7784877/ParallelExecute）
进入project\ParallelExecute文件夹，运行ant脚本——build.xml。将编译打包后的路径(.\target\ParallelExecute)配置在系统环境变量PATH中，变量名为PE_HOME
Windows
 
Linux
 
将编译后lib下的jar包拷贝到业务应用系统的classpath中。
3简单sample
客户端只需要继承扩展2个类就可以完成并行任务的工作，之后提交到引擎执行业务逻辑就可以了。
--------------------------------------------------------------------------------------
角色：
Job：并行粗粒度任务
Task：细粒度子任务
ParallelExecute：执行粗粒度任务入口以及模板父类
ParallelTask：执行并行子任务入口以及子任务父类
---------------------------------------------------------------------------------------
单机模式下的sample：
场景是并行输出一个数组的片段内容，每个线程输出整体数组的一个片段。
首先扩展org.para.execute.ParallelExecute类，扩展功能主要有两个，一个是构造子任务的实现类；一个是计算出整体粗粒度任务的可标量的总值，这个总值是为了切割任务用的。这个总值好比数据库分页查询中的重要参数------总记录数。
IntParallelExecute代码如下，指定泛型-int[]，是说整个并行任务是面向int数组进行的
import java.util.concurrent.CountDownLatch;

import org.para.execute.ParallelExecute;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.trace.listener.FailEventListener;

import task.IntParallelTask;

public class IntParallelExecute extends ParallelExecute<int[]> {

	@Override
	protected int analyzeResultCount(int[] arg0) {
		return arg0.length;
	}

	@Override
	protected ParallelTask<int[]> buildParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			int[] srcObject, FailEventListener failEventListener,
			Object... arg4) {

		return new IntParallelTask(countDownLatch, taskProperty, srcObject,
				failEventListener);

	}

	@Override
	protected void init(int[] arg0, Object... arg1) {

	}

}
总任务的大小取得的是数组的长度，实例化IntParallelTask作为具体的子任务执行逻辑。
下面是IntParallelTask代码，里面包含了子任务的执行逻辑
import java.util.concurrent.CountDownLatch;

import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.trace.listener.FailEventListener;
import org.para.util.MessageOutUtil;

public class IntParallelTask extends ParallelTask<int[]> {

	public IntParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, int[] targetObject,
			FailEventListener failEventListener) {
		super(taskProperty, targetObject, countDownLatch, failEventListener);
	}

	@Override
	protected int execute(int[] sourceJobObject, int currentBlockSize, int countBlock,
			int currentBlockIndex,int averageBlockSize) throws Exception {

		int startIndex = currentBlockIndex * averageBlockSize;
		int endIndex = currentBlockIndex * averageBlockSize + currentBlockSize;

		System.out.println("ThreadId" + Thread.currentThread().getId()
				+ "___currentBlockSize:" + currentBlockSize + "__countBlock:" + countBlock
				+ "___currentBlockIndex:" + currentBlockIndex + "__startIndex:"
				+ startIndex + "___endIndex:" + endIndex);

		for (int i = 0; i < currentBlockSize; i++) {
			MessageOutUtil
					.SystemOutPrint("ThreadId" + Thread.currentThread().getId()
							+ ":" + sourceJobObject[startIndex+i]);
		}
		return currentBlockIndex;

	}

}
Execute方法内部就是执行逻辑，逻辑功能就是该任务线程输出属于它的数组片段（雷同于数据库分页）。
下面写一个单元测试类，测试一下咱们的并行任务是否能够进行。
TestIntParallelExecute，并行度（线程数）为3。
import org.junit.Test;
import org.para.exception.ParallelException;

import execute.IntParallelExecute;

public class TestIntParallelExecute {
	
	@Test
	public void test() throws ParallelException{
		
		int[] sourceObject = {1,2,3,4,5,6,7,8,9,10,11,12,13,14};
		
		IntParallelExecute intParallelExecute = new IntParallelExecute();
		
		intParallelExecute.exeParalleJob(sourceObject, 3);
	}

}
运行结果如下所示
ThreadId10___currentBlockSize:4__countBlock:14___currentBlockIndex:0__startIndex:0___endIndex:4
ThreadId12___currentBlockSize:6__countBlock:14___currentBlockIndex:2__startIndex:8___endIndex:14
ThreadId11___currentBlockSize:4__countBlock:14___currentBlockIndex:1__startIndex:4___endIndex:8
ThreadId10:1
ThreadId10:2
ThreadId10:3
ThreadId10:4
ThreadId11:5
ThreadId11:6
ThreadId11:7
ThreadId12:9
ThreadId12:10
ThreadId12:11
ThreadId11:8
ThreadId12:12
ThreadId12:13
ThreadId12:14
job:5517922367113:execute job result [true],countTaskNum=0,successTaskNum=3,errorTaskNum=0
最后一行
job:5517922367113:execute job result [true],countTaskNum=0,successTaskNum=3,errorTaskNum=0
代表着整体任务（job）的执行结果。
分布式sample
分布式的场景是这样的，从mysql数据库中分别并行读取表中的内容。
建立一个Java项目，作为ParallelExecute的插件项目。
首先创建任务入口类MysqlDistributedParallelExecute
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.para.distributed.master.DistributedParallelExecute;
import org.para.distributed.task.DistributedParallelTask;
import org.para.execute.model.TaskProperty;
import org.para.trace.listener.FailEventListener;

/**
 * 任务执行逻辑
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-20 下午7:41:51
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class MysqlDistributedParallelExecute extends DistributedParallelExecute {

	@Override
	protected int analyzeResultCount(Map<String, String> targetObjectConf) {

		String Driver = targetObjectConf.get("Driver");
		String url = targetObjectConf.get("url");
		String root = targetObjectConf.get("name");
		String password = targetObjectConf.get("password");
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			Class.forName(Driver);
			con = DriverManager.getConnection(url, root, password);
			statement = con.createStatement();

			String sql = "SELECT count(*) FROM person";
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return 0;
	}

	@Override
	protected DistributedParallelTask buildDistributedParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			Map<String, String> targetObjectConf,
			FailEventListener failEventListener) {
		MysqlDistributedParallelTask mysqlDistributedParallelTask = new MysqlDistributedParallelTask(
				countDownLatch, taskProperty, targetObjectConf);
		return mysqlDistributedParallelTask;
	}

	@Override
	protected void init(Map<String, String> arg0) {

	}

}
同分布式一样此类具备两个重要的功能实现：算出大任务的可量化的总个数；实例化task对象（用于分任务执行）。
下面编写MysqlDistributedParallelTask，实现每个分任务要执行的业务逻辑。
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.para.distributed.task.DistributedParallelTask;
import org.para.execute.model.TaskProperty;

/**
 * 子任务
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-20 下午7:41:43
 * @Copyright: 2013 story All rights reserved.
 * 
 */
public class MysqlDistributedParallelTask extends DistributedParallelTask {

	public MysqlDistributedParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, Map<String, String> targetObjectConf) {
		super();
		this.countDownLatch = countDownLatch;
		this.taskProperty = taskProperty;
		this.targetObjectConf = targetObjectConf;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected int execute(Map<String, String> targetObjectConf, int currentBlockSize,
			int countBlock, int currentBlockIndex,int averageBlockSize) throws Exception {

		// Class.forName("com.mysql.jdbc.Driver");
		// String url = "jdbc:mysql://127.0.0.1:3306/test";

		String Driver = targetObjectConf.get("Driver");
		String url = targetObjectConf.get("url");
		String root = targetObjectConf.get("name");
		String password = targetObjectConf.get("password");

		Class.forName(Driver);

		int start = currentBlockIndex * averageBlockSize;
		Connection con = DriverManager.getConnection(url, root, password);
		Statement statement = con.createStatement();

		String sql = "SELECT * FROM person LIMIT " + start + "," + currentBlockSize;
		System.out.println("sql::::" + sql);
		System.out.println("plugin-version 2 ");
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			int id = resultSet.getInt(1);
			String name = resultSet.getString(2);
			int mark = resultSet.getInt(3);
			// System.out.println("[id:" + id + " name:" + name+ " mark:" + mark
			// + "]");
		}

		return currentBlockIndex;
	}

}
这个业务逻辑也很简单，就仅仅输出数据库中表的内容。
之后将此插件项目打包成为jar包。
将插件的jar包，放到一个http服务器上。
下面启动并行任务的master服务。启动master前先启动memcache以及activemq服务，在setup-soft文件夹下有需要的文件，activemq需要到apache上去下。修改配置文件distributed.properties里的内容，使其和使用者本地配置相同。在windows下为例启动，执行{PE_HOME}/start-master.bat。
之后在window下启动一个任务结点进程，执行start-slave.bat。
建立一个应用项目，用于引入动态的并行任务插件，该项目包含并行任务核心jar包——ParallelExecute.jar即可使用并行任务。客户端程序与插件程序可以完全解耦合。
业务使用程序如下
	@Test
	public void test03Http() {
		// 1
		String fileAbsolutePathString1 = "http://192.168.137.1/ParalleExecutePlugin2_fat.jar";

		// 2
		String mainClassName = "distributed.MysqlDistributedParallelExecute";

		Map<String, String> map = new HashMap<String, String>();
		map.put("Driver", "com.mysql.jdbc.Driver");
		map.put("url", "jdbc:mysql://192.168.137.1:3306/test");
		map.put("name", "root");
		map.put("password", "111111");

		try {
			distributedParallelExecuteClient.startDistributedParallelExecute(
					fileAbsolutePathString1, mainClassName, 3, map);
		} catch (ParallelException e) {
			e.printStackTrace();
		}
	}
并行度为3，任务执行结点因为是1个，所以该节点会启动3个进程，对其子任务进行逻辑执行。后续会讲分布式模式的原理。
4本地模式运行原理
5分布式模式运行原理
6任务聚合归并审计
7任务插件开发
