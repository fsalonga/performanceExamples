package com.marklogic.performanceExamples.util;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;

public class ClientManager {
	public static synchronized DatabaseClient createClient(){
		//note that xdbc app server and rest user/password need to be specified in Config.properties
		//CLIENT references are used for the Java API
		return DatabaseClientFactory.newClient(Config.host, Config.port, Config.restUser, Config.restPassword, Config.authType);	
	}
}
