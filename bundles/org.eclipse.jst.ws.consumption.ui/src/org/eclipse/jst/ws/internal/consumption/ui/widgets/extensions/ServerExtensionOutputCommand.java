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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public class ServerExtensionOutputCommand extends AbstractDataModelOperation
{
  //private String            wsdlURI_;
  private WebServicesParser wsdlParser_;
	private IWebService 			webService_;
  
  private boolean isWebProjectStartupRequested_ = false;
    
  
  /**
   * @return Returns the wsdlURI.
   */
  public String getWsdlURI()
  {
    return webService_.getWebServiceInfo().getWsdlURL();
  }

  /**
   * @param wsdlURI
   *            The wsdlURI to set.
   */
	/*
  public void setWsdlURI(String wsdlURI)
  {
    wsdlURI_ = wsdlURI;
  }
  */
	
	public void setWebService(IWebService ws)
	{
		webService_ = ws;
	}
	
  /**
   * @return Returns the wsdlParser_.
   */
  public WebServicesParser getWebServicesParser()
  {
    if( wsdlParser_ == null )
    {
      wsdlParser_ = new WebServicesParserExt();  
    }
    
    return wsdlParser_;
  }
  /**
   * @param wsdlParser_ The wsdlParser_ to set.
   */
  public void setWebServicesParser(WebServicesParser wsdlParser_)
  {
    this.wsdlParser_ = wsdlParser_;
  }
/**
 * @param earProjectName The earProjectName to set.
 */
public void setEarProjectName(String earProjectName) {
}
/**
 * @param existingServerId The existingServerId to set.
 */
public void setExistingServerId(String existingServerId) {
}

/**
 * @return Returns the isRestartProjectNeeded.
 */
public boolean getIsWebProjectStartupRequested() {
	return isWebProjectStartupRequested_;
}
/**
 * @param isRestartProjectNeeded The isRestartProjectNeeded to set.
 */
public void setIsWebProjectStartupRequested(boolean isRestartProjectNeeded) {
	this.isWebProjectStartupRequested_ = isRestartProjectNeeded;
}

public String getServiceServerFactoryId()
{
	return webService_.getWebServiceInfo().getServerFactoryId();
}

public String getServiceServerInstanceId()
{
	return webService_.getWebServiceInfo().getServerInstanceId();
}

public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
{
  return Status.OK_STATUS;
}
}
