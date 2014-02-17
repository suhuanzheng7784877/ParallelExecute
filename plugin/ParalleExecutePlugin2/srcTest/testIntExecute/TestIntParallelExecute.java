package testIntExecute;

import org.junit.Test;
import org.para.exception.ParallelException;

import execute.IntParallelExecute;

public class TestIntParallelExecute {
	
	@Test
	public void test() throws ParallelException{
		
		int[] sourceObject = {1,2,3,4,5,6,7,8,9,10,11,12,13,14};
		
		IntParallelExecute intParallelExecute = new IntParallelExecute();
		
		intParallelExecute.exeParalleJob(sourceObject, 3);
	}

}
