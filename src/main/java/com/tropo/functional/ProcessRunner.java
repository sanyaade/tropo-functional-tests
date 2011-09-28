package com.tropo.functional;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessRunner {

	public static int waitFor(final Process process) throws Exception {
		
		Callable<Integer> call = new Callable<Integer>() {
			public Integer call() throws Exception {
				process.waitFor();
				return process.exitValue();
			}
		};
		
		Future<Integer> ft = Executors.newSingleThreadExecutor().submit(call);
		try {
			int exitVal = ft.get(10, TimeUnit.MINUTES);
			return exitVal;
		} catch (TimeoutException to) {
			process.destroy();
			throw to;
		}
	}
}
