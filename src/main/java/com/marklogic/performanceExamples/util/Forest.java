package com.marklogic.performanceExamples.util;

import com.marklogic.xcc.Request;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.types.ValueType;

public class Forest {
	public static void create(String forestName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $forestName as xs:string external;",
				"admin:save-configuration(admin:forest-create(admin:get-configuration(), $forestName, (xdmp:hosts()[1]), ()))"
		};
		
		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("forestName", ValueType.XS_STRING, forestName);
		session.submitRequest (request);		
	}
	
	public static void delete(String forestName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $forestName as xs:string external;",
				"let $config := admin:get-configuration()",
				"return admin:save-configuration(admin:forest-delete($config, admin:forest-get-id($config, $forestName), fn:true()))"	
		};
		
		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("forestName", ValueType.XS_STRING, forestName);
		session.submitRequest (request);		
	}
	
	public static boolean exists(String forestName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';", 
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $forestName as xs:string external;",
				"let $config := admin:get-configuration()",
				"return admin:forest-exists($config, $forestName)"
		};
		
		String requestString = null;
		Request request = null;
		ResultSequence rs = null;
		
		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("forestName", ValueType.XS_STRING, forestName);
		rs = session.submitRequest (request);
		
		return Boolean.parseBoolean(rs.asString());		
	}
	
	public static String getDb(String forestName, Session session) throws RequestException{				  
		String[] xquery = {
				"xquery version '1.0-ml';", 
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $forestName as xs:string external;",
				"let $config := admin:get-configuration()",
				"let $forest := xdmp:forest($forestName)",
				"return xdmp:database-name(admin:forest-get-database($config, $forest))"
		};
		
		String requestString = null;
		Request request = null;
		ResultSequence rs = null;
		
		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("forestName", ValueType.XS_STRING, forestName);
		rs = session.submitRequest (request);
		
		return rs.asString();				  
	}
}
