package org.eclipse.jst.ws.internal.axis.creation.ui.wsrt;

import org.eclipse.jst.ws.internal.axis.consumption.ui.wsrt.AxisWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

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
