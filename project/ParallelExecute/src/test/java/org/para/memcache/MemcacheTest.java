package org.para.memcache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.junit.Test;
import org.para.constant.ParaConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MemcacheTest {

	// 初始化
	public final static ApplicationContext WorkApplicationContext = new FileSystemXmlApplicationContext(
			new String[] { "/" + ParaConstant.PE_CONF
					+ "/applicationContext-slave.xml" });

	@Test
	public void test01() {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses("localhost:11111"));
		try {
			MemcachedClient memcachedClient = builder.build();

			memcachedClient.set("hello", 0, "Hello,xmemcached liuyan 中文");
			String value = memcachedClient.get("hello");
			
			System.out.println("hello=" + value);
			memcachedClient.delete("hello");
			value = memcachedClient.get("hello");
			System.out.println("deleted    hello=" + value);
			
			Map map = new HashMap();
			map.put("1", "1");
			map.put("2", "2");
			map.put("3", "3");
			map.put("4", "4");
			memcachedClient.set("cache1", 0, map);
			
			Map map1 = memcachedClient.get("cache1");
			
			//memcachedClient.shutdown();
			System.out.println("map1:::"+map1);
			
			
			Thread.sleep(30000L);
			memcachedClient.addServer("localhost:11211");
			while(true){
				Thread.sleep(Integer.MAX_VALUE);
			}
			
		} catch (MemcachedException e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// ignore
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void addServer(){
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses("localhost:11111"));
		try {
			MemcachedClient memcachedClient = builder.build();
			
			memcachedClient.addServer("localhost:11211");
			
			
		} catch (Exception e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		}
	}

	@Test
	public void test02() throws TimeoutException, InterruptedException, MemcachedException {

		MemcachedClient xmemcachedClient = WorkApplicationContext
				.getBean("memcachedClient", MemcachedClient.class);
		xmemcachedClient.set("hello", 0, "Hello,xmemcached liuyan 中文");
		String value = xmemcachedClient.get("hello");
		System.out.println("hello=" + value);
		xmemcachedClient.delete("hello");
		value = xmemcachedClient.get("hello");
		System.out.println("deleted    hello=" + value);
	}

}
