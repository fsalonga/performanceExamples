package com.marklogic.performanceExamples.util;

import com.marklogic.xcc.Request;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.types.ValueType;

public class AppServer {
	public static String getDb(String appServerName, Session session) throws RequestException{
		String[] getAppServerDb = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';", 
				"declare variable $appServerName as xs:string external;",
				"let $config := admin:get-configuration()",
				"let $groupid := admin:group-get-id($config, 'Default')",
				"return xdmp:database-name(admin:appserver-get-database($config, admin:appserver-get-id($config, $groupid, $appServerName)))"
		};

		String requestString = null;
		Request request = null;
		ResultSequence rs = null;

		requestString = XqueryComposer.composeXquery(getAppServerDb);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("appServerName", ValueType.XS_STRING, appServerName);
		rs = session.submitRequest (request);
		return rs.asString();
	}
	
	public static void setDb(String appServerName, String dbName, Session session) throws RequestException{
		String[] setAppServerDb = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
			    "declare variable $appServerName as xs:string external;",
			    "declare variable $dbName as xs:string external;",
				"let $config := admin:get-configuration()",
				"let $groupid := admin:group-get-id($config, 'Default')",
				"return admin:save-configuration(admin:appserver-set-database($config, admin:appserver-get-id($config, $groupid, $appServerName), xdmp:database($dbName)))"
		};
		
		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(setAppServerDb);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("appServerName", ValueType.XS_STRING, appServerName);
		request.setNewVariable ("dbName", ValueType.XS_STRING, dbName);
		session.submitRequest (request);
	}
	
	public static int getNumThreads(String appServerName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';", 
				"declare variable $appServerName as xs:string external;",
				"let $config := admin:get-configuration()",
				"let $groupid := admin:group-get-id($config, 'Default')",
				"return admin:appserver-get-threads($config, admin:appserver-get-id($config, $groupid, $appServerName))"				
		};
		
		String requestString = null;
		Request request = null;
		ResultSequence rs = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("appServerName", ValueType.XS_STRING, appServerName);
		rs = session.submitRequest (request);
		return Integer.parseInt(rs.asString());				
	}
	
	public static void setNumThreads(String appServerName, int numThreads, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';", 
				"declare variable $appServerName as xs:string external;",
				"declare variable $numThreads as xs:unsignedInt external;",
				"let $config := admin:get-configuration()",
				"let $groupid := admin:group-get-id($config, 'Default')",
				"return admin:save-configuration(admin:appserver-set-threads($config, admin:appserver-get-id($config, $groupid, $appServerName), $numThreads))"
		};

		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable("numThreads", ValueType.XS_INTEGER, numThreads);
		request.setNewVariable ("appServerName", ValueType.XS_STRING, appServerName);
		session.submitRequest (request);
	}
}
