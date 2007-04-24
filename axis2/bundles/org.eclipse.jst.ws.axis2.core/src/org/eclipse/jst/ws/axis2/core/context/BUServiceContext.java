package org.eclipse.jst.ws.axis2.core.context;

public class BUServiceContext {
	
	private static BUServiceContext instance;
	private static String serviceName; 
	
	//singleton
	private BUServiceContext(){}
	
	public static BUServiceContext getInstance(){
		if (instance == null) {
			instance = new BUServiceContext();
		}
		return instance;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		BUServiceContext.serviceName = serviceName;
	}
	
	
}
