package org.para.db.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.dbutils.DbUtils;
import org.para.cache.DBResultCache;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.jobType.db.DbSourceJobType;
import org.para.jobType.db.DbTargetJobType;
import org.para.trace.listener.FailEventListener;
import org.para.util.DBDataUtil;
import org.para.util.SQLStringUtil;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-4
 * @Copyright: 2013 story All rights reserved.
 */
public class DataTransferParallelTask extends ParallelTask<DbSourceJobType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbTargetJobType dbTargetJobType;

	public DbTargetJobType getDbTargetJobType() {
		return dbTargetJobType;
	}

	public void setDbTargetJobType(DbTargetJobType dbTargetJobType) {
		this.dbTargetJobType = dbTargetJobType;
	}
	
	/**
	 * 
	 * @param countDownLatch
	 * @param taskProperty
	 * @param targetObject
	 * @param failEventListener
	 */
	public DataTransferParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, DbSourceJobType targetObject,
			FailEventListener failEventListener) {
		super(taskProperty, targetObject, countDownLatch, failEventListener);
	}
	
	/**
	 * 
	 * @param countDownLatch
	 * @param taskProperty
	 * @param targetObject
	 * @param failEventListener
	 * @param dbTargetJobType
	 */
	public DataTransferParallelTask(CountDownLatch countDownLatch,
			TaskProperty taskProperty, DbSourceJobType targetObject,
			FailEventListener failEventListener, DbTargetJobType dbTargetJobType) {
		super(taskProperty, targetObject, countDownLatch, failEventListener);
		this.dbTargetJobType = dbTargetJobType;
	}

	@Override
	protected int execute(DbSourceJobType sourceJobObject, int blockSize,
			int countBlock, int currentBlockIndex) throws Exception {

		String exeSelectSql = SQLStringUtil.appendSplitPage(sourceJobObject,
				blockSize, countBlock, currentBlockIndex);

		// 1-get database resource
		Connection sourceConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet sourceResultSet = null;
		int result = 0;
		ResultSetMetaData sourceResultSetMetaData = null;
		try {
			sourceConnection = DBDataUtil.getConnection(sourceJobObject);

			// 2-get partition data
			preparedStatement = sourceConnection.prepareStatement(exeSelectSql);
			sourceResultSetMetaData = DBResultCache.getResultSetMetaData(
					sourceJobObject, preparedStatement);

			sourceResultSet = preparedStatement.executeQuery();

			// 3-insert into target database
			result = DBDataUtil.insertIntoTarget(sourceResultSetMetaData,
					sourceResultSet, dbTargetJobType);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(sourceResultSet);
			DbUtils.closeQuietly(preparedStatement);
			DbUtils.closeQuietly(sourceConnection);
		}
		return result;
	}

}
