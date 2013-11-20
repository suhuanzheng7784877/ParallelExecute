package org.para.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;
import org.para.constant.ParaConstant;
import org.para.jobType.db.DbSourceJobType;
import org.para.jobType.db.DbTargetJobType;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-5
 * @Copyright: 2013 story All rights reserved.
 */
public class DBDataUtil {

	/**
	 * get source database connection
	 * 
	 * @param srcObject
	 * @return
	 */
	public static Connection getConnection(DbSourceJobType srcObject) {
		String sourceJdbcDriver = srcObject.getSourceJdbcDriver();
		String sourceJdbcUrl = srcObject.getSourceJdbcUrl();
		String user = srcObject.getSourceUserName();
		String password = srcObject.getSourcePassword();
		DbUtils.loadDriver(sourceJdbcDriver);
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(sourceJdbcUrl, user, password);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			conn.setReadOnly(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * get target database connection
	 * 
	 * @param targetObject
	 * @return
	 */
	public static Connection getConnection(DbTargetJobType targetObject) {

		String targetJdbcDriver = targetObject.getTargetJdbcDriver();
		String targetJdbcUrl = targetObject.getTargetJdbcUrl();
		String targetPassword = targetObject.getTargetPassword();
		String targetUserName = targetObject.getTargetUserName();

		DbUtils.loadDriver(targetJdbcDriver);
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(targetJdbcUrl, targetUserName,
					targetPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * get source database table count result
	 * 
	 * @param srcObject
	 * @return
	 */
	public static int queryResultCount(DbSourceJobType srcObject) {
		String sourceCountSql = srcObject.getSourceCountSql();
		int resultCount = 0;
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = getConnection(srcObject);
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sourceCountSql);
			resultSet.next();
			resultCount = resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(statement);
			DbUtils.closeQuietly(conn);
		}
		return resultCount;
	}

	/**
	 * insert source data into target database
	 * @param sourceResultSetMetaData
	 * @param sourceResultSet
	 * @param targetObject
	 * @return
	 */
	public static int insertIntoTarget(
			ResultSetMetaData sourceResultSetMetaData,
			ResultSet sourceResultSet, DbTargetJobType targetObject) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		String sql = targetObject.getTargetSql();
		int i = 1;
		try {
			conn = getConnection(targetObject);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);

			int sourceResultSetColumnCount = sourceResultSetMetaData
					.getColumnCount();

			while (sourceResultSet.next()) {

				Object value = null;
				for (int j = 0; j < sourceResultSetColumnCount; j++) {
					value = sourceResultSet.getObject(j + 1);
					preparedStatement.setObject(j + 1, value);
				}
				preparedStatement.addBatch();
				if (i % ParaConstant.DefaultDBBatchNum == 0) {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
					conn.commit();
				}
				i++;
			}

			preparedStatement.executeBatch();
			preparedStatement.clearBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DbUtils.closeQuietly(preparedStatement);
			DbUtils.closeQuietly(conn);
		}
		return i;
	}
}
