package v1.util;

import java.util.Properties;

public class v1Properties extends Properties {
	
	private static String sURI = "URI";
	private static String sUser = "User";
	private static String sPassword = "Password";
	private static String sToken = "Token";
	
	public v1Properties() {
		// TODO Auto-generated constructor stub
		super();
	}

	public v1Properties(Properties arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public String getURI() {
		return super.getProperty(sURI);
	}
	
	public String getUser() {
		return super.getProperty(sUser);
	}
	
	public String getPassword() {
		return super.getProperty(sPassword);
	}
	
	public String getToken() {
		return super.getProperty(sToken);
	}
}
