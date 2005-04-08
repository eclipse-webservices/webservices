package org.eclipse.jst.ws.internal.barney;

import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebServiceRuntime;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;

public class BarneyWebServiceRuntime extends AbstractWebServiceRuntime
{

	public IWebService getWebService(WebServiceInfo info)
	{
    return new BarneyWebService(info);
	}

	public IWebServiceClient getWebServiceClient(WebServiceClientInfo info)
	{
		return new BarneyWebServiceClient(info);
	}

}
