package com.tropo.functional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import com.micromethod.common.util.io.IOUtils;

public class ProcessLogger extends Thread {

	Process process;
	String outputPath;
	
	public ProcessLogger(Process process, String outputPath) {
		
		this.process = process;
		this.outputPath = outputPath;
	}
	
	@Override
	public void run() {

		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));			
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			while ((line = br.readLine()) != null) {
			  sb.append(line).append("\n");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		String answer = sb.toString();
		File output = new File(outputPath);
		System.out.println("Dumping console output to file: " + output.getAbsolutePath());
		if (output.exists()) {
			output.delete();
		} else {
			if (!output.getParentFile().exists()) {
				output.getParentFile().mkdirs();
			}
		}
		FileOutputStream fos = null; 
		try {
			fos = new FileOutputStream(output);
			IOUtils.copy(new StringReader(answer), fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
