package com.marklogic.performanceExamples.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.marklogic.client.DatabaseClientFactory.Authentication;

public class Config {

	private static Properties props = loadProperties();
	
	public static String host = props.getProperty("example.host");
	
	public static int port = Integer.parseInt(props.getProperty("example.port"));
	
	public static String restUser = props.getProperty("example.rest-writer_user");
	
	public static String restPassword = props.getProperty("example.rest-writer_password");
	
	public static String restAdmin_user = props.getProperty("example.rest-admin_user");
	
	public static String restAdmin_password = props.getProperty("example.rest-admin_password");
	
	public static String admin_user = props.getProperty("example.admin_user");
	public static String admin_password = props.getProperty("example.admin_password");
	public static int xdbc_port = Integer.parseInt(props.getProperty("example.xdbc_port"));
	public static String xdbc_server_name = props.getProperty("example.xdbc_server_name");
	
	public static Authentication authType = Authentication.valueOf(
				props.getProperty("example.authentication_type").toUpperCase()
				);

	// get the configuration for the example
	private static Properties loadProperties() {		
	    try {
			String propsName = "Config.properties";
			InputStream propsStream =
				Config.class.getClassLoader().getResourceAsStream(propsName);
			if (propsStream == null)
				throw new IOException("Could not read config properties");

			Properties props = new Properties();
			props.load(propsStream);

			return props;

	    } catch (final IOException exc) {
	        throw new Error(exc);
	    }  
	}
}
