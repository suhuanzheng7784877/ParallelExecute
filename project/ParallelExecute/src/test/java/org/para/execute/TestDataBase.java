package org.para.execute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.junit.Test;
import org.para.cache.DBResultCache;
import org.para.db.execute.DataTranferParallelExecute;
import org.para.enums.DbType;
import org.para.jobType.db.DbSourceJobType;
import org.para.jobType.db.DbTargetJobType;
import org.para.util.DBDataUtil;
import org.para.util.MessageOutUtil;

public class TestDataBase {

	@Test
	public void testInsert() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/test?user=root&password=111111";
		Connection connection = DriverManager.getConnection(url);
		connection.setAutoCommit(false);
		String sql = "insert into person (name,age) values (?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);

		for (int i = 1; i <= 1000000; i++) {
			statement.setString(1, "liuyan" + i);
			statement.setInt(2, 28);
			statement.addBatch();
			if (0 == i % 250000) {
				statement.executeBatch();
				connection.commit();
				statement.clearBatch();
			}
		}
		connection.commit();
		connection.setAutoCommit(true);
		MessageOutUtil.SystemOutPrint(true);
	}

	@Test
	public void testInsertPara() throws Exception {
		DbSourceJobType dbSourceJobType = new DbSourceJobType();
		dbSourceJobType.setDbType(DbType.MYSQL);
		dbSourceJobType.setSourceCountSql("select count(id) from person");
		dbSourceJobType.setSourceJdbcDriver("com.mysql.jdbc.Driver");
		dbSourceJobType
				.setSourceJdbcUrl("jdbc:mysql://localhost:3306/test?user=root&password=111111");
		dbSourceJobType.setSourcePassword("111111");
		dbSourceJobType.setSourceUserName("root");
		dbSourceJobType.setSourceSelectSql("select name,age from person");

		DbTargetJobType dbTargetJobType = new DbTargetJobType();
		dbTargetJobType.setDbType(DbType.MYSQL);
		dbTargetJobType.setTargetJdbcDriver("com.mysql.jdbc.Driver");
		dbTargetJobType
				.setTargetJdbcUrl("jdbc:mysql://localhost:3306/test?user=root&password=111111");
		dbTargetJobType.setTargetPassword("111111");
		dbTargetJobType.setTargetUserName("root");
		dbTargetJobType
				.setTargetSql("insert into person_bak (name,age) values (?,?)");

		DataTranferParallelExecute dataTranferParallelExecute = new DataTranferParallelExecute();
		// int blockNum = ParaConstant.DefaultDBBlockNum;
		int blockNum = 1;
		long startTime = System.currentTimeMillis();
		dataTranferParallelExecute.exeParalleJob(dbSourceJobType, blockNum,dbTargetJobType);
		long endTime = System.currentTimeMillis();
		MessageOutUtil.SystemOutPrint("完成，执行时间：" + (endTime - startTime) + "ms");
	}

	@Test
	public void testSelect() {
		try {

			DbUtils.loadDriver("com.mysql.jdbc.Driver");
			String sql = "select name,age from person LIMIT 1,1000";
			String sql1 = "select count(id) from person";
			Connection connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/test?user=root&password=111111");
			connection
					.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			PreparedStatement preparedStatement = connection
					.prepareStatement(sql1);

			ResultSet sourceResultSet = preparedStatement.executeQuery();
			
			connection.close();
			
			while (sourceResultSet.next()) {
				MessageOutUtil.SystemOutPrint(sourceResultSet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testSelect2() {
		Connection sourceConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet sourceResultSet = null;
		int result = 0;
		ResultSetMetaData sourceResultSetMetaData = null;

		DbSourceJobType dbSourceJobType = new DbSourceJobType();
		dbSourceJobType.setDbType(DbType.MYSQL);
		dbSourceJobType.setSourceCountSql("select count(id) from person");
		dbSourceJobType.setSourceJdbcDriver("com.mysql.jdbc.Driver");
		dbSourceJobType
				.setSourceJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?user=root&password=111111");
		dbSourceJobType.setSourcePassword("111111");
		dbSourceJobType.setSourceUserName("root");
		dbSourceJobType.setSourceSelectSql("select name,age from person");

		try {
			sourceConnection = DBDataUtil.getConnection(dbSourceJobType);
			// sourceConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			// 2-获取数据源分区信息
			preparedStatement = sourceConnection
					.prepareStatement("select name,age from person limit 0,100000;");
			sourceResultSetMetaData = DBResultCache.getResultSetMetaData(
					dbSourceJobType, preparedStatement);

			sourceResultSet = preparedStatement.executeQuery();
			while (sourceResultSet.next()) {
				MessageOutUtil.SystemOutPrint(sourceResultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(sourceResultSet);
			DbUtils.closeQuietly(preparedStatement);
			DbUtils.closeQuietly(sourceConnection);
		}
	}
}
