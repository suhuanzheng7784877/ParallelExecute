package org.para.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.para.constant.ParaConstant;

/**
 * 读取Properties文件的内容
 * 
 * @author liuyan
 * @Email:suhuanzheng7784877@163.com
 * @version 0.1
 * @Date: 2013-11-27
 * @Copyright: 2013 story All rights reserved.
 */
public class PropertiesUtil {

	private static final Properties properties = new Properties();

	private static Logger logger = Logger.getLogger(PropertiesUtil.class);

	static {
		String confPath = ParaConstant.PE_CONF + "/"
				+ ParaConstant.Distributed_Properties;
		InputStream in = null;
		try {
			in = new FileInputStream(new File(confPath));
			logger.info("load properties file properties");
			properties.load(in);
			logger.info("load properties file properties finish");
		} catch (IOException e) {
			logger.error("error", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("error", e);
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					logger.info("InputStream close PropertiesFile");
					in.close();
					in = null;
				} catch (IOException e) {
					logger.error("error", e);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取配置文件内容
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {

		if (StringUtils.isBlank(key)) {
			logger.error("key is null");
			return null;
		}

		String value = properties.getProperty(key, null);
		return value;
	}

}
