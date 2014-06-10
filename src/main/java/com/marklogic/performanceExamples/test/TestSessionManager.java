package com.marklogic.performanceExamples.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.marklogic.performanceExamples.util.Config;
import com.marklogic.performanceExamples.util.SessionManager;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

public class TestSessionManager {
	private static Session SESSION = null;
	
	@Test
	public void testSessionCreate() {
		SESSION = SessionManager.createSession(Config.admin_user, Config.admin_password, Config.host, Config.xdbc_port);
		try {
			Assert.assertTrue(SESSION.getCurrentServerPointInTime() != null);
		} catch (RequestException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@AfterClass
	public static void tearDownShared(){
		if(SESSION != null){
			SESSION.close();					
		}
	}

}
