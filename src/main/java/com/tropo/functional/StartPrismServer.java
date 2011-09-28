package com.tropo.functional;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import com.voxeo.prism.tf.TestFramework;
import com.voxeo.prism.tf.server.PrismServer;

public class StartPrismServer {
	
	String prismLocation;
	String prismPort;
	String serverName;
	String serverPort;
	String appName;
	
	public void loadProperties() throws Exception {

		prismLocation = System.getProperty("prism.location");
		prismPort = System.getProperty("prism.port");
		serverName = System.getProperty("server.name");
		serverPort = System.getProperty("server.port");
		appName = System.getProperty("app.name");
	
		InputStream is = getClass().getClassLoader().getResourceAsStream("test.properties");
		Properties props = new Properties();
		props.load(is);
		if (prismLocation == null) {
			prismLocation = (String)props.get("prism.location");
		}
		if (prismPort == null) {
			prismPort = (String)props.get("prism.port");
		}
		if (serverName == null) {
			serverName = (String)props.get("server.name");
		}
		if (serverPort == null) {
			serverPort = (String)props.get("server.port");
		}
		if (appName == null) {
			appName = (String)props.get("app.name");
		}
	}
	
	public void start() throws Exception {
		
		TestFramework tf = null;
		PrismServer server = null;
		try {
			tf = TestFramework.create(Integer.valueOf(prismPort));
			server = tf.createPrismServer(prismLocation);
			server.start("Tropo Functional Test Server");			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			TestFramework.release(tf.report(server));
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		final CountDownLatch latch = new CountDownLatch(1);
		Thread runner = new Thread() {
			public void run() {
				try {
					StartPrismServer framework = new StartPrismServer();
					framework.loadProperties();
					framework.start();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			};
		};
		runner.setDaemon(true);
		runner.start();
		latch.await();
	}
}