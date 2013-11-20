package org.para.jobType.db;

import java.io.Serializable;

import org.para.enums.DbType;

/**
 * org.para.jobType.DbTargetJobType is like as java.io.File
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-9-4
 * @Copyright: 2013 story All rights reserved.
 */
public class DbTargetJobType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DbType dbType;

	private String targetJdbcUrl;

	private String targetUserName;

	private String targetPassword;

	private String targetJdbcDriver;

	private String targetSql;

	public DbTargetJobType() {
		super();
	}

	public DbTargetJobType(DbType dbType, String targetJdbcUrl,
			String targetUserName, String targetPassword,
			String targetJdbcDriver, String targetSql) {
		super();
		this.dbType = dbType;
		this.targetJdbcUrl = targetJdbcUrl;
		this.targetUserName = targetUserName;
		this.targetPassword = targetPassword;
		this.targetJdbcDriver = targetJdbcDriver;
		this.targetSql = targetSql;
	}

	public String getTargetJdbcUrl() {
		return targetJdbcUrl;
	}

	public void setTargetJdbcUrl(String targetJdbcUrl) {
		this.targetJdbcUrl = targetJdbcUrl;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getTargetPassword() {
		return targetPassword;
	}

	public void setTargetPassword(String targetPassword) {
		this.targetPassword = targetPassword;
	}

	public String getTargetJdbcDriver() {
		return targetJdbcDriver;
	}

	public void setTargetJdbcDriver(String targetJdbcDriver) {
		this.targetJdbcDriver = targetJdbcDriver;
	}

	public String getTargetSql() {
		return targetSql;
	}

	public void setTargetSql(String targetSql) {
		this.targetSql = targetSql;
	}

	public DbType getDbType() {
		return dbType;
	}

	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	@Override
	public String toString() {
		return "DbTargetJobType [dbType=" + dbType + ", targetJdbcUrl="
				+ targetJdbcUrl + ", targetUserName=" + targetUserName
				+ ", targetPassword=" + targetPassword + ", targetJdbcDriver="
				+ targetJdbcDriver + ", targetSql=" + targetSql + "]";
	}

}
