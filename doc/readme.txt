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
进入project\ParallelExecute文件夹，运行ant脚本——build.xml。将编译打包后的路径(.\target\ParallelExecute)配置在系统环境变量PATH中
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
TestIntParallelExecute
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


4本地模式运行原理
5分布式模式运行原理
6任务聚合归并审计
7任务插件开发

