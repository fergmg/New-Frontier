package v1.util;

public class v1Connection {

	public enum ConnType {
		USERPW, TOKEN
	}
	
	ConnType cType = null;
	
	public v1Connection(ConnType cType) {
		this.cType = cType;
	}
	

}
