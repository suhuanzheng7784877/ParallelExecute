package org.para.cache;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.para.enums.DbType;
import org.para.jobType.db.DbSourceJobType;

/**
 * cache database about some value
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-9
 * @Copyright: 2013 story All rights reserved.
 */
public final class DBResultCache {

	//
	private static Map<String, ResultSetMetaData> resultSetMetaData = new ConcurrentHashMap<String, ResultSetMetaData>();

	/**
	 * 
	 * @param sourceJobObject
	 * @param preparedStatement
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetMetaData getResultSetMetaData(
			DbSourceJobType sourceJobObject, PreparedStatement preparedStatement)
			throws SQLException {
		String key = getResultSetMetaDataKey(sourceJobObject);
		if (resultSetMetaData.containsKey(key)) {
			return resultSetMetaData.get(key);
		}
		ResultSetMetaData sourceResultSetMetaData = preparedStatement
				.getMetaData();
		resultSetMetaData.put(key, sourceResultSetMetaData);
		return sourceResultSetMetaData;
	}

	private static String getResultSetMetaDataKey(
			DbSourceJobType sourceJobObject) {
		String sourceJdbcUrl = sourceJobObject.getSourceJdbcUrl();
		DbType dbType = sourceJobObject.getDbType();
		String sourceSelectSql = sourceJobObject.getSourceSelectSql();
		String key = dbType + sourceJdbcUrl + sourceSelectSql;
		return key;
	}
}
