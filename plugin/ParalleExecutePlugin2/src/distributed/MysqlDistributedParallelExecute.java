package distributed;

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
