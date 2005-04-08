package org.eclipse.jst.ws.internal.axis.creation.wsrt;

import org.eclipse.jst.ws.internal.axis.consumption.wsrt.AxisWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebServiceRuntime;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;

public class AxisWebServiceRuntime extends AbstractWebServiceRuntime
{

	public IWebService getWebService(WebServiceInfo info)
	{
    return new AxisWebService(info);
	}

	public IWebServiceClient getWebServiceClient(WebServiceClientInfo info)
	{
		return new AxisWebServiceClient(info);
	}

}
