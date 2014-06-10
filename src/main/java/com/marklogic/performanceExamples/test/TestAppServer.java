package com.marklogic.performanceExamples.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.marklogic.performanceExamples.util.AppServer;
import com.marklogic.performanceExamples.util.Config;
import com.marklogic.performanceExamples.util.SessionManager;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

public class TestAppServer {
	private static Session SESSION = null;
	
	@BeforeClass
	public static void setUpShared() {
		try{
			SESSION = SessionManager.createSession(Config.admin_user, Config.admin_password, Config.host, Config.xdbc_port);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testSetUnsetDb(){								
		try {
			//to account for server restart
			Thread.sleep(1000);
			String original = null;
			
			//get original db
			original = AppServer.getDb(Config.xdbc_server_name, SESSION);
			
			//set security db
			AppServer.setDb(Config.xdbc_server_name, "Security", SESSION);
			
			//test security db set
			Assert.assertTrue(AppServer.getDb(Config.xdbc_server_name, SESSION).equals("Security"));

			//reset to original db
			AppServer.setDb(Config.xdbc_server_name, original, SESSION);
			
			//test reset to original
			Assert.assertTrue(AppServer.getDb(Config.xdbc_server_name, SESSION).equals(original));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testSetUnsetNumThreads(){
		try {
			//to account for server restart
			Thread.sleep(1000);
			
			int original = -1;			
			//get original thread count
			original = AppServer.getNumThreads(Config.xdbc_server_name, SESSION);
			
			//set new thread count
			AppServer.setNumThreads(Config.xdbc_server_name, original*2, SESSION);
			
			//test set new thread count
			Assert.assertTrue(AppServer.getNumThreads(Config.xdbc_server_name, SESSION) == original*2);
			
			//reset to original thread count
			AppServer.setNumThreads(Config.xdbc_server_name, original, SESSION);
			
			//test reset to original
			Assert.assertTrue(AppServer.getNumThreads(Config.xdbc_server_name, SESSION) == original);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDownShared(){
		if(SESSION != null){
			SESSION.close();					
		}
	}
}
