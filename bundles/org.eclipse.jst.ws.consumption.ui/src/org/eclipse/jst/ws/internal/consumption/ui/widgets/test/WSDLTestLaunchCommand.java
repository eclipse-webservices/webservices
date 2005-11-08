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
/*
 * Created on May 4, 2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceTester;
import org.eclipse.wst.ws.internal.wsrt.TestInfo;

/**
 * @author gilberta
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WSDLTestLaunchCommand extends AbstractDataModelOperation
{
  private String testID; 
  private String launchedServiceTestName;
  private TypeRuntimeServer serviceids;
  private String serverProject;
  private String serverModule;
  private String wsdlURI;
  private List endpoints;
  private IServer serviceExistingServer = null;
  private String serviceServerTypeID = null;
  private String serviceServerInstanceId = null;
	
  public WSDLTestLaunchCommand()
  {
  }
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
  	IStatus status = Status.OK_STATUS;
  	
  	WebServiceTestRegistry testRegistry = WebServiceTestRegistry.getInstance();
  	WebServiceTestExtension wscte = (WebServiceTestExtension)testRegistry.getWebServiceExtensionsByName(testID);
	IWebServiceTester iwst = (IWebServiceTester)wscte.getWebServiceExecutableExtension();
	TestInfo testInfo = getTestInfo();
	
	
	status = commandFactoryExecution(iwst.launch(testInfo),env, monitor);
	
    //Dont need to shut everything down because the wsdl test doesnt work
    if(status.getSeverity() != Status.OK){
      IStatusHandler sHandler = env.getStatusHandler();
      IStatus infoStatus = StatusUtils.infoStatus( ConsumptionUIMessages.MSG_ERROR_UNABLE_TO_LAUNCH_WSDL_TEST );
      sHandler.reportInfo(infoStatus);
      return infoStatus;	
    }
   return status;
  	
  }

  private IStatus commandFactoryExecution(ICommandFactory commandFactory,IEnvironment env, IProgressMonitor monitor)
  {
	IStatus status = Status.OK_STATUS;  	
	while(commandFactory.hasNext())
  { 
	  AbstractDataModelOperation operation = commandFactory.getNextCommand();
    
	  if (operation != null)
    {
      try
      {
        operation.setEnvironment( env );
	      status = operation.execute( monitor, null );
      }
      catch( Exception exc )
      {
        status = StatusUtils.errorStatus( exc );
      }
    }
    
	  if(status.getSeverity() == Status.ERROR){
	    IStatusHandler sHandler = env.getStatusHandler();
		sHandler.reportError(status);
		return status;
	  }
    }
    return status;
  }
  
  
  private TestInfo getTestInfo()
  {
    //	get the server stuff
    if (serviceids.getServerInstanceId() != null) 
	  serviceExistingServer = ServerCore.findServer(serviceids.getServerInstanceId());
    else if (serviceServerInstanceId!=null)
		serviceExistingServer = ServerCore.findServer(serviceServerInstanceId);
	
	if (serviceExistingServer != null)
	  serviceServerTypeID = serviceExistingServer.getServerType().getId();
	else
	  serviceServerTypeID = serviceids.getServerId();
	// server will be created in ServerDeployableConfigurationCommand
  
    TestInfo testInfo = new TestInfo();
	if (serviceExistingServer!=null)
		testInfo.setServiceExistingServer(serviceExistingServer);
    testInfo.setServiceServerTypeID(serviceServerTypeID);
	testInfo.setServiceProject(serverProject);
	testInfo.setWsdlServiceURL(wsdlURI);
	testInfo.setEndpoint(endpoints);
	return testInfo;
	
  }
  
  
  public void setTestID(String testID)
  {
  	this.testID = testID;
  }

  public String getLaunchedServiceTestName()
  {
    return launchedServiceTestName;	
  }
  public void setWsdlURI(String wsdlURI)
  {
  	this.wsdlURI = wsdlURI;
  }
    
  public void setServerProject(String serverProject)
  {
    this.serverProject = serverProject;
  }

  
  public void setServiceTypeRuntimeServer(TypeRuntimeServer serviceids)
  {
    this.serviceids = serviceids;
  }

  public void setExternalBrowser(boolean externalBrowser)
  {
  }
  
  public void setEndpoint(List endpoints)
  {
    this.endpoints = endpoints;
  }

public String getServerModule() {
	return serverModule;
}

public void setServerModule(String serverModule) {
	this.serverModule = serverModule;
}

public void setServiceServerInstanceId(String ssInstanceId){
	this.serviceServerInstanceId = ssInstanceId;
}

}
