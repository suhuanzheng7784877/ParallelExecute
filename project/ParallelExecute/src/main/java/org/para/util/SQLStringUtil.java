package org.para.util;

import org.para.enums.DbType;
import org.para.jobType.db.DbSourceJobType;

/**
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-6
 * @Copyright: 2013 story All rights reserved.
 */
public class SQLStringUtil {

	/**
	 * 给sql语句加上分页
	 * 
	 * @param sourceJobObject
	 * @param blockSize
	 * @param countBlock
	 * @param currentBlockIndex
	 * @return
	 */
	public static String appendSplitPage(DbSourceJobType sourceJobObject,
			int currentBlockSize, int countBlock, int currentBlockIndex,int averageBlockSize) {

		DbType dbType = sourceJobObject.getDbType();
		StringBuilder sourceSqlStringBuffer = new StringBuilder(
				sourceJobObject.getSourceSelectSql());
		int startIndex = currentBlockIndex * averageBlockSize;
		if (dbType == DbType.MYSQL) {
			sourceSqlStringBuffer.append(" LIMIT ").append(startIndex)
					.append(",").append(currentBlockSize);
		}
		return sourceSqlStringBuffer.toString();
	}

}
