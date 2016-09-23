package v1.sample;

import v1.util.v1Properties;

import java.net.MalformedURLException;

import com.versionone.apiclient.V1Connector;
import com.versionone.apiclient.exceptions.V1Exception;

public class ConnectionTest {

	public static void main(String[] args) {
		V1Connector up_connector = null;
		V1Connector token_connector = null;
		v1Properties props = null;
		
		//Load V1 connection properties
		
		//create and load properties
		props = new v1Properties("M:\\V1Properties");

		//Make a connection to V1 via U/P
		try {
			up_connector = V1Connector.withInstanceUrl(props.getURI())
					.withUserAgentHeader("test", "1.0")
					.withUsernameAndPassword(props.getUser(), props.getPassword())
					.build();
		} catch (MalformedURLException e) {e.printStackTrace();
		} catch (V1Exception e) {e.printStackTrace();}
		
		System.out.println("Connected to V1 as " + props.getUser());
		
		try {
			token_connector = V1Connector.withInstanceUrl(props.getURI())
					.withUserAgentHeader("test", "1.0")
					.withAccessToken(props.getToken())
					.build();
		} catch (MalformedURLException e) {e.printStackTrace();
		} catch (V1Exception e) {e.printStackTrace();}
		
		System.out.println("Connected to V1 with Token " + props.getToken());
	}

}
