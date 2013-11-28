package org.para.distributed.memcache;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestProgressContextMemcache {
	
	@Test
	public void testPut(){
		
		String jobId = "1";
		Map<String,String> confMap = new HashMap<String,String>();
		confMap.put("system", "linux");
		confMap.put("memroy", "512mb");
		
		
		//ProgressContextMemcache.putTaskMapConf(jobId, confMap);
		
	}
	
	@Test
	public void testGet(){
		String jobId = "1";
		//Map<String,String> confMap = ProgressContextMemcache.getTaskMapConf(jobId);
		//System.out.println(confMap);
	}

}
