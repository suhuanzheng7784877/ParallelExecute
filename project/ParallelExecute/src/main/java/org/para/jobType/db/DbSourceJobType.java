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
public class DbSourceJobType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DbType dbType;

	private String sourceJdbcUrl;

	private String sourceUserName;

	private String sourcePassword;

	private String sourceJdbcDriver;

	private String sourceSelectSql;

	private String sourceCountSql;

	public DbSourceJobType() {
		super();
	}

	public DbSourceJobType(DbType dbType, String sourceJdbcUrl,
			String sourceUserName, String sourcePassword,
			String sourceJdbcDriver, String sourceSelectSql,
			String sourceCountSql) {
		super();
		this.dbType = dbType;
		this.sourceJdbcUrl = sourceJdbcUrl;
		this.sourceUserName = sourceUserName;
		this.sourcePassword = sourcePassword;
		this.sourceJdbcDriver = sourceJdbcDriver;
		this.sourceSelectSql = sourceSelectSql;
		this.sourceCountSql = sourceCountSql;
	}

	public DbType getDbType() {
		return dbType;
	}

	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	public String getSourceUserName() {
		return sourceUserName;
	}

	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}

	public String getSourcePassword() {
		return sourcePassword;
	}

	public void setSourcePassword(String sourcePassword) {
		this.sourcePassword = sourcePassword;
	}

	public String getSourceJdbcUrl() {
		return sourceJdbcUrl;
	}

	public void setSourceJdbcUrl(String sourceJdbcUrl) {
		this.sourceJdbcUrl = sourceJdbcUrl;
	}

	public String getSourceJdbcDriver() {
		return sourceJdbcDriver;
	}

	public void setSourceJdbcDriver(String sourceJdbcDriver) {
		this.sourceJdbcDriver = sourceJdbcDriver;
	}

	public String getSourceSelectSql() {
		return sourceSelectSql;
	}

	public void setSourceSelectSql(String sourceSelectSql) {
		this.sourceSelectSql = sourceSelectSql;
	}

	public String getSourceCountSql() {
		return sourceCountSql;
	}

	public void setSourceCountSql(String sourceCountSql) {
		this.sourceCountSql = sourceCountSql;
	}

	@Override
	public String toString() {
		return "DbSourceJobType [dbType=" + dbType + ", sourceJdbcUrl="
				+ sourceJdbcUrl + ", sourceUserName=" + sourceUserName
				+ ", sourcePassword=" + sourcePassword + ", sourceJdbcDriver="
				+ sourceJdbcDriver + ", sourceSelectSql=" + sourceSelectSql
				+ ", sourceCountSql=" + sourceCountSql + "]";
	}

}
