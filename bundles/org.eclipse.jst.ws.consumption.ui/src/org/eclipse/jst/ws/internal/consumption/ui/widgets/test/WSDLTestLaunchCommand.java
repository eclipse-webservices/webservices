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

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.Command;
import org.eclipse.wst.command.internal.provisional.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceTester;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

/**
 * @author gilberta
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WSDLTestLaunchCommand extends SimpleCommand
{
  private String testID; 
  private String launchedServiceTestName;
  private TypeRuntimeServer serviceids;
  private String serverProject;
  private String serverModule;
  private String wsdlURI;
  private MessageUtils msgUtils;
  private boolean externalBrowser;
  private List endpoints;
  private IServer serviceExistingServer = null;
  private String serviceServerTypeID = null;
  private String serviceServerInstanceId = null;
	
  public WSDLTestLaunchCommand()
  {
  	String pluginId = "org.eclipse.jst.ws.consumption.ui";
	msgUtils = new MessageUtils(pluginId + ".plugin", this);  	
  }
  
  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus("");
  	
  	WebServiceTestRegistry testRegistry = WebServiceTestRegistry.getInstance();
  	WebServiceTestExtension wscte = (WebServiceTestExtension)testRegistry.getWebServiceExtensionsByName(testID);
	IWebServiceTester iwst = (IWebServiceTester)wscte.getWebServiceExecutableExtension();
	TestInfo testInfo = getTestInfo();
	
	
	status = commandFactoryExecution(iwst.launch(testInfo),env);
	
    //Dont need to shut everything down because the wsdl test doesnt work
    if(status.getSeverity() != Status.OK){
      StatusHandler sHandler = env.getStatusHandler();
      Status infoStatus = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_UNABLE_TO_LAUNCH_WSDL_TEST"), Status.INFO);
      sHandler.reportInfo(infoStatus);
      return infoStatus;	
    }
   return status;
  	
  }

  private Status commandFactoryExecution(ICommandFactory commandFactory,Environment env)
  {
	Status status = new SimpleStatus( "" );  	
	while(commandFactory.hasNext()){ 
	  Command command = commandFactory.getNextCommand();
	  if (command != null)
	    status = command.execute(env);
	  if(status.getSeverity() == Status.ERROR){
	    StatusHandler sHandler = env.getStatusHandler();
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
  	this.externalBrowser = externalBrowser;
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
