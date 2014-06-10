package com.marklogic.performanceExamples.util;

import com.marklogic.xcc.Request;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.types.ValueType;

public class Database {
	public static void create(String dbName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $dbName as xs:string external;",
				"let $config := admin:get-configuration()",
				"return admin:save-configuration(admin:database-create($config, $dbName, xdmp:database('Security'), xdmp:database('Schemas')))"
		};
		
		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("dbName", ValueType.XS_STRING, dbName);
		session.submitRequest (request);		
	}
	
	public static void delete(String dbName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $dbName as xs:string external;",
				"let $config := admin:get-configuration()",
				"return admin:save-configuration(admin:database-delete($config, admin:database-get-id($config, $dbName)))"
		};

		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("dbName", ValueType.XS_STRING, dbName);
		session.submitRequest (request);
	}
	
	public static boolean exists(String dbName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';", 
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $dbName as xs:string external;",
				"let $config := admin:get-configuration()",
				"return admin:database-exists($config, $dbName)",
		};
		
		String requestString = null;
		Request request = null;
		ResultSequence rs = null;
		
		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("dbName", ValueType.XS_STRING, dbName);
		rs = session.submitRequest (request);
		
		return Boolean.parseBoolean(rs.asString());
	}
	
	public static void attachForest(String dbName, String forestName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $dbName as xs:string external;",
				"declare variable $forestName as xs:string external;",
				"let $config := admin:get-configuration()",
				"return admin:save-configuration(admin:database-attach-forest($config, xdmp:database($dbName), xdmp:forest($forestName)))"
		};

		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("dbName", ValueType.XS_STRING, dbName);
		request.setNewVariable ("forestName", ValueType.XS_STRING, forestName);
		session.submitRequest (request);
	}
	
	public static void detachForest(String dbName, String forestName, Session session) throws RequestException{
		String[] xquery = {
				"xquery version '1.0-ml';",
				"import module namespace admin = 'http://marklogic.com/xdmp/admin' at '/MarkLogic/admin.xqy';",
				"declare variable $dbName as xs:string external;",
				"declare variable $forestName as xs:string external;",
				"let $config := admin:get-configuration()",
				"return admin:save-configuration(admin:database-detach-forest($config, xdmp:database($dbName), xdmp:forest($forestName)))"
		};

		String requestString = null;
		Request request = null;

		requestString = XqueryComposer.composeXquery(xquery);
		request = session.newAdhocQuery(requestString);
		request.setNewVariable ("dbName", ValueType.XS_STRING, dbName);
		request.setNewVariable ("forestName", ValueType.XS_STRING, forestName);
		session.submitRequest (request);
	}
}
