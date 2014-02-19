˵���ĵ�
���ߣ�����
Email��suhuanzheng7784877@163.com
1˵��
ParallelExecute�Ǽ򵥲�������Ŀ�ܡ�
��ν����������Խ�һ�������ȵĴ������и��Ϊһ����ϸС��С����ÿһ�����и��С������Զ��̣߳�����ģʽ������Զ�̷ֲ�ʽ���ֲ�ʽ�����С�
�߼��ṹ����ͼ��ʾ
 
ʹ��Javaʵ�֣��ÿ�ܷ�Ϊ�ֲ�ʽģʽ�͵���ģʽ������ģʽ��Ҫʹ�ö��߳�ִ������������������һ��JVM�����С��ֲ�ʽģʽ��������Զ��֪ͨ�����������ȥִ�У�ÿ����������һ��������JVM���С���������ϸ���ܱ���ģʽ�ͷֲ�ʽģʽ��ԭ��
���ܣ�
����������ʽ���򵥼̳С���չģ���༴��ʵ�ֲ�������
֧�ֵ���/�ֲ�ʽ��ģʽ
�ֲ�ʽģʽ�°������������Դ
�������Ȳ���
����ʧ�ܵ�֪ͨ��������ʧЧת�Ƶȵ�failover����
����ִ�л���״̬�Զ���֪��ע�ᡢ������æµ��崻���
�������䣬������
2��װ
��github�����ز��������ܣ�https://github.com/suhuanzheng7784877/ParallelExecute��
����project\ParallelExecute�ļ��У�����ant�ű�����build.xml�������������·��(.\target\ParallelExecute)������ϵͳ��������PATH�У�������ΪPE_HOME
Windows
 
Linux
 
�������lib�µ�jar��������ҵ��Ӧ��ϵͳ��classpath�С�
3��sample
�ͻ���ֻ��Ҫ�̳���չ2����Ϳ�����ɲ�������Ĺ�����֮���ύ������ִ��ҵ���߼��Ϳ����ˡ�
--------------------------------------------------------------------------------------
��ɫ��
Job�����д���������
Task��ϸ����������
ParallelExecute��ִ�д�������������Լ�ģ�常��
ParallelTask��ִ�в�������������Լ���������
---------------------------------------------------------------------------------------
����ģʽ�µ�sample��
�����ǲ������һ�������Ƭ�����ݣ�ÿ���߳�������������һ��Ƭ�Ρ�
������չorg.para.execute.ParallelExecute�࣬��չ������Ҫ��������һ���ǹ����������ʵ���ࣻһ���Ǽ�����������������Ŀɱ�������ֵ�������ֵ��Ϊ���и������õġ������ֵ�ñ����ݿ��ҳ��ѯ�е���Ҫ����------�ܼ�¼����
IntParallelExecute�������£�ָ������-int[]����˵������������������int������е�
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
������Ĵ�Сȡ�õ�������ĳ��ȣ�ʵ����IntParallelTask��Ϊ�����������ִ���߼���
������IntParallelTask���룬����������������ִ���߼�
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
Execute�����ڲ�����ִ���߼����߼����ܾ��Ǹ������߳����������������Ƭ�Σ���ͬ�����ݿ��ҳ����
����дһ����Ԫ�����࣬����һ�����ǵĲ��������Ƿ��ܹ����С�
TestIntParallelExecute�����жȣ��߳�����Ϊ3��
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
���н��������ʾ
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
���һ��
job:5517922367113:execute job result [true],countTaskNum=0,successTaskNum=3,errorTaskNum=0
��������������job����ִ�н����
�ֲ�ʽsample
�ֲ�ʽ�ĳ����������ģ���mysql���ݿ��зֱ��ж�ȡ���е����ݡ�
����һ��Java��Ŀ����ΪParallelExecute�Ĳ����Ŀ��
���ȴ������������MysqlDistributedParallelExecute
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
 * ����ִ���߼�
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-20 ����7:41:51
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
ͬ�ֲ�ʽһ������߱�������Ҫ�Ĺ���ʵ�֣����������Ŀ��������ܸ�����ʵ����task�������ڷ�����ִ�У���
�����дMysqlDistributedParallelTask��ʵ��ÿ��������Ҫִ�е�ҵ���߼���
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.para.distributed.task.DistributedParallelTask;
import org.para.execute.model.TaskProperty;

/**
 * ������
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-12-20 ����7:41:43
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
���ҵ���߼�Ҳ�ܼ򵥣��ͽ���������ݿ��б�����ݡ�
֮�󽫴˲����Ŀ�����Ϊjar����
�������jar�����ŵ�һ��http�������ϡ�
�����������������master��������masterǰ������memcache�Լ�activemq������setup-soft�ļ���������Ҫ���ļ���activemq��Ҫ��apache��ȥ�¡��޸������ļ�distributed.properties������ݣ�ʹ���ʹ���߱���������ͬ����windows��Ϊ��������ִ��{PE_HOME}/start-master.bat��
֮����window������һ����������̣�ִ��start-slave.bat��
����һ��Ӧ����Ŀ���������붯̬�Ĳ���������������Ŀ���������������jar������ParallelExecute.jar����ʹ�ò������񡣿ͻ��˳����������������ȫ����ϡ�
ҵ��ʹ�ó�������
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
���ж�Ϊ3������ִ�н����Ϊ��1�������Ըýڵ������3�����̣���������������߼�ִ�С������ὲ�ֲ�ʽģʽ��ԭ��
4����ģʽ����ԭ��
5�ֲ�ʽģʽ����ԭ��
6����ۺϹ鲢���
7����������
