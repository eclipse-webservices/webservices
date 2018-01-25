/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.ui.wsrt;

import org.eclipse.jst.ws.internal.cxf.consumption.ui.wsrt.CXFWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

@SuppressWarnings("restriction")
public class CXFWebServiceRuntime extends AbstractWebServiceRuntime {

    @Override
    public IWebService getWebService(WebServiceInfo info) {
        return new CXFWebService(info);
    }

    @Override
    public IWebServiceClient getWebServiceClient(WebServiceClientInfo info) {
        return new CXFWebServiceClient(info);
    }
}
