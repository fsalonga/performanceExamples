package com.marklogic.performanceExamples.util;

import java.net.URI;

import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Session;

public class SessionManager {
	protected static ContentSource CONTENT_SOURCE = null;
	
	public static synchronized Session createSession(String user, String password, String host, int port){
		Session s = null;
		
		try{
			URI uri = new URI("xcc://" + user + ":" + password + "@" + host + ":" + port);
			
			if(CONTENT_SOURCE == null){
				CONTENT_SOURCE = ContentSourceFactory.newContentSource (uri);				
			}

			s = CONTENT_SOURCE.newSession();
		}catch(Exception e){
			e.printStackTrace();
			s = null;
		}		
		
		return s;
	}
}
