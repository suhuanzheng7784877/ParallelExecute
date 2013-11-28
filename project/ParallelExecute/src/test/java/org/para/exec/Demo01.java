package org.para.exec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.para.distributed.mq.StartJobJmsSend;
import org.para.distributed.util.ExecUtil;

public class Demo01 {

	private static Logger log = Logger.getLogger(StartJobJmsSend.class);

	@Test
	public void test01() throws ExecuteException, IOException {

		try {

			String command = "java -version";

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

			CommandLine commandline = CommandLine.parse(command);

			DefaultExecutor exec = new DefaultExecutor();

			exec.setExitValues(null);

			PumpStreamHandler streamHandler = new PumpStreamHandler(
					outputStream, errorStream);

			exec.setStreamHandler(streamHandler);

			exec.execute(commandline);

			String out = outputStream.toString("gbk");

			String error = errorStream.toString("gbk");
			System.out.println("====================");
			System.out.println(out + error);
			System.out.println("====================");
		} catch (Exception e) {

			log.error("ping task failed.", e);

			e.printStackTrace();

		}

	}
	
	@Test
	public void test02() throws ExecuteException, IOException {
		String error = null;
		try {

			String command = "java -jar c:/test.jar test.Run";

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

			//CommandLine commandline = CommandLine.parse(command);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("name", "liuyan7784877");
			CommandLine commandline = CommandLine.parse(command, map);
			
			commandline.setSubstitutionMap(map);
			
			DefaultExecutor exec = new DefaultExecutor();

			exec.setExitValues(null);

			PumpStreamHandler streamHandler = new PumpStreamHandler(
					outputStream, errorStream);

			exec.setStreamHandler(streamHandler);
			
			System.out.println("commandline.toString():"+commandline.toString());
			
			exec.execute(commandline);

			String out = outputStream.toString("gbk");

			error = errorStream.toString("gbk");
			System.out.println("====================");
			
			
			if("1000".equals(out)){
				System.out.println("执行成功");
			}
			System.out.println("out:"+out);
			System.out.println("error:"+error);
			System.out.println("====================");
		} catch (Exception e) {
			System.out.println(error);
			log.error("ping task failed.", e);

			e.printStackTrace();

		}

	}
	
	private static final String EngineClassName = "org.para.distributed.task.DistributedParallelTaskExecuteEngine";

	
	@Test
	public void test03(){
		String command = "java -Xmx256m -Xms256m -cp c://parallelexecute-all.jar "+EngineClassName;
		ExecUtil.exec(command);
		
	}
	
	@Test
	public void test04(){
		
		String classPath = System.getProperty("java.class.path");

		System.out.println(classPath);
	}

}
