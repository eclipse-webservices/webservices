/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions;

import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;

public class ClientExtensionOutputCommand extends AbstractDataModelOperation
{
  private IWebServiceClient webServiceClient_;
  
  /**
   * @return Returns the proxyBean.
   */
  
  public String getProxyBean()
  {
    return webServiceClient_.getWebServiceClientInfo().getImplURL();
  }
    
  public boolean getGenerateProxy()
  {
	return true;
  }
  
  /**
   * @return Returns the setEndpointMethod.
   */
  public String getSetEndpointMethod()
  {
	return "setEndpoint";
  }
  	
  public void setWebServiceClient(IWebServiceClient wsc)
  {
	webServiceClient_ = wsc;
  }
	
  public String getServerInstanceId()
  {
	return webServiceClient_.getWebServiceClientInfo().getServerInstanceId();
  }
	
  public String getServerFactoryId()
  {
	return webServiceClient_.getWebServiceClientInfo().getServerFactoryId();
  }
}
