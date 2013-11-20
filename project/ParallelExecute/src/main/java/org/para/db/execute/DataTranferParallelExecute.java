package org.para.db.execute;

import java.util.concurrent.CountDownLatch;

import org.para.db.task.DataTransferParallelTask;
import org.para.execute.ParallelExecute;
import org.para.execute.model.TaskProperty;
import org.para.execute.task.ParallelTask;
import org.para.jobType.db.DbSourceJobType;
import org.para.jobType.db.DbTargetJobType;
import org.para.trace.listener.FailEventListener;
import org.para.util.DBDataUtil;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-4
 * @Copyright: 2013 story All rights reserved.
 */
public class DataTranferParallelExecute extends
		ParallelExecute<DbSourceJobType> {

	@Override
	protected void init(DbSourceJobType dbSourceJobType,Object... objects) {

	}

	@Override
	protected ParallelTask<DbSourceJobType> buildParallelTask(
			CountDownLatch countDownLatch, TaskProperty taskProperty,
			DbSourceJobType srcObject, FailEventListener failEventListener,
			Object... objects) {
		DbTargetJobType dbTargetJobType = (DbTargetJobType) objects[0];
		ParallelTask<DbSourceJobType> byteFileParallelTask = new DataTransferParallelTask(
				countDownLatch, taskProperty, srcObject,failEventListener, dbTargetJobType);
		
		return byteFileParallelTask;

	}

	@Override
	protected int analyzeResultCount(DbSourceJobType srcObject) {
		return DBDataUtil.queryResultCount(srcObject);
	}

}
