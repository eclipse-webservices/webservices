package org.eclipse.jst.ws.internal.dummy;

import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebServiceRuntime;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;

public class DummyWebServiceRuntime extends AbstractWebServiceRuntime
{

	public IWebService getWebService(WebServiceInfo info)
	{
    return new DummyWebService(info);
	}

	public IWebServiceClient getWebServiceClient(WebServiceClientInfo info)
	{
		return new DummyWebServiceClient(info);
	}

}
