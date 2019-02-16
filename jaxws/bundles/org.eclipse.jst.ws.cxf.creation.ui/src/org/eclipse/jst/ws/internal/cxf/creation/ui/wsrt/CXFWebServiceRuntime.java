/*******************************************************************************
 * Copyright (c) 2008, 2009 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
