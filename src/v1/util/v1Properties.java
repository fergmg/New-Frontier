package v1.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("serial")
public class v1Properties extends Properties {
	
	private static String sURI = "URI";
	
	public v1Properties() {
		super();
	}

	public v1Properties(Properties arg0) {
		super(arg0);
	}

	public v1Properties(String sLoc) {
		super();
		try {
			FileInputStream in = new FileInputStream(sLoc);
			load(in);
			in.close();
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public String getURI() {
		return super.getProperty(sURI);
	}
	
	public String getToken(String sTokenID) {
		return super.getProperty(sTokenID);
	}
}
