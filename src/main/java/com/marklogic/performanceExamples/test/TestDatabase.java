package com.marklogic.performanceExamples.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.marklogic.performanceExamples.util.AppServer;
import com.marklogic.performanceExamples.util.Config;
import com.marklogic.performanceExamples.util.Database;
import com.marklogic.performanceExamples.util.Forest;
import com.marklogic.performanceExamples.util.SessionManager;
import com.marklogic.performanceExamples.util.User;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

public class TestDatabase{
	private static Session SESSION = null;
	private static String ORIGINAL = null;
	
	@BeforeClass
	public static void setUpShared() {
		try{
			SESSION = SessionManager.createSession(Config.admin_user, Config.admin_password, Config.host, Config.xdbc_port);

			//get original db
			ORIGINAL = AppServer.getDb(Config.xdbc_server_name, SESSION);
			
			//set security db (because you can't do security operations like add users unless appserver db is set to Security)
			AppServer.setDb(Config.xdbc_server_name, "Security", SESSION);
			
			//test security db set
			Assert.assertTrue(AppServer.getDb(Config.xdbc_server_name, SESSION).equals("Security"));

			//create test admin
			User.create("test-admin", "test-admin", SESSION);
			
			//test test admin creation
			Assert.assertTrue(User.exists("test-admin", SESSION));

			//create test forest
			Forest.create("testForest", SESSION);
			
			//test test forest creation
			Assert.assertTrue(Forest.exists("testForest", SESSION));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateDeleteDb(){						
		try {			
			//create test db
			Database.create("testDb", SESSION);
			
			//test test db creation
			Assert.assertTrue(Database.exists("testDb", SESSION));
			
			//attach forest
			Database.attachForest("testDb", "testForest", SESSION);
			
			//test attach forest
			Assert.assertTrue(Forest.getDb("testForest", SESSION).equals("testDb"));
			
			//detach forest
			Database.detachForest("testDb", "testForest", SESSION);
			
			//test detach forest
			Assert.assertTrue(Forest.getDb("testForest", SESSION).equals(""));
			
			//delete test db
			Database.delete("testDb", SESSION);
			
			//test test db deletion
			Assert.assertFalse(Database.exists("testDb", SESSION));			
		} catch (RequestException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@AfterClass
	public static void tearDownShared() throws RequestException{
		if(SESSION != null){
			//delete test forest
			Forest.delete("testForest", SESSION);
			
			//test test forest deletion
			Assert.assertFalse(Forest.exists("testForest", SESSION));
			
			//delete test admin
			User.delete("test-admin", SESSION);
			
			//test test admin deletion
			Assert.assertFalse(User.exists("test-admin", SESSION));
			
			//reset to original db
			AppServer.setDb(Config.xdbc_server_name, ORIGINAL, SESSION);
			
			//test reset to original
			Assert.assertTrue(AppServer.getDb(Config.xdbc_server_name, SESSION).equals(ORIGINAL));
			
			SESSION.close();					
		}
	}
}
