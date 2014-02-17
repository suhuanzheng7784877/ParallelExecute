package distributed;

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
