package com.marklogic.performanceExamples.util;

import com.marklogic.xcc.Request;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.types.ValueType;

public class User {
	public static void create(String username, String password, Session session) throws RequestException{
		String[] createUser = {
				"xquery version '1.0-ml';",
				"import module namespace sec='http://marklogic.com/xdmp/security' at '/MarkLogic/security.xqy';",
				"declare variable $username as xs:string external;",
				"declare variable $password as xs:string external;",
				"sec:create-user($username, '', $password, 'admin', (), ())"
		};		

		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(createUser);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("username", ValueType.XS_STRING, username);
		request.setNewVariable ("password", ValueType.XS_STRING, password);
		session.submitRequest (request);
	}
	
	public static boolean exists(String username, Session session) throws RequestException{
		String[] testUser = {
				"xquery version '1.0-ml';",
				"import module namespace sec='http://marklogic.com/xdmp/security' at '/MarkLogic/security.xqy';",
				"declare variable $username as xs:string external;",
				"sec:user-exists($username)"
		};
		
		String requestString = null;
		Request request = null;
		ResultSequence rs = null;
		
		requestString = XqueryComposer.composeXquery(testUser);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("username", ValueType.XS_STRING, username);
		rs = session.submitRequest (request);
		
		return Boolean.parseBoolean(rs.asString());
	}
	
	public static void delete(String username, Session session) throws RequestException{
		String[] deleteUser = {
				"xquery version '1.0-ml';",
				"import module namespace sec='http://marklogic.com/xdmp/security' at '/MarkLogic/security.xqy';",
				"declare variable $username as xs:string external;",
				"sec:remove-user($username)"
		};
		
		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(deleteUser);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("username", ValueType.XS_STRING, username);
		session.submitRequest (request);
	}
}
