package org.para.execute;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

public class TestGenerateNumber {

	public static final int maxNum = 10000000;
	public static final Random r1 = new Random();
	public static final int round = 10000000;

	@Test
	public void generateBigNumber() throws IOException {

		File targetFile = new File("c://num.txt");
		if (targetFile.exists()) {
			targetFile.delete();
		}
		targetFile.createNewFile();
		FileWriter fw = new FileWriter(targetFile, true);

		for (int i = 0; i < round; i++) {
			int num = r1.nextInt(maxNum);
			fw.write(num + "\n");
		}
		fw.flush();
		fw.close();
	}

}
