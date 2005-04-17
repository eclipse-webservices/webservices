/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import java.util.List;
import java.util.Vector;

import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ext.WebServiceExecutable;
import org.eclipse.jst.ws.internal.ext.test.JavaProxyTestCommand;
import org.eclipse.jst.ws.internal.ext.test.WSDLTestFinishCommand;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestFinishCommand;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;
import org.eclipse.wst.command.internal.provisional.env.core.selection.BooleanSelection;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;


/*
* The JSPGeneration task runs the jsp generation engine found in the 
* JBWizard Plugin
*
*
*/
public class ClientTestDelegateCommand extends SimpleCommand
{
  private String LABEL = "JSPGenerationTask";
  private String DESCRIPTION = "Run the JSP Generation";
  
  private ScenarioContext scenarioContext;
  private WebServiceTestRegistry testRegistry;
  private SelectionList testFacilities;
  private String folder;
  private String jspFolder;
  private BooleanSelection[] methods;
  private boolean runClientTest=true;
  private String sampleServerTypeID;
  private IServer sampleExistingServer;
  private String proxyBean;
  private String sampleProject;
  private String clientProject;
  private String clientServer;
  private TypeRuntimeServer clientIds;
  private TypeRuntimeServer serverIds;
  private String serviceProject;
  private String wsdlServiceURL;
  private boolean generateProxy;
  private boolean isTestWidget = false;
  private String setEndpointMethod;
  private List endpoints;

  public ClientTestDelegateCommand ()
  {
    setDescription(DESCRIPTION);
    setName(LABEL);  
    scenarioContext = WebServicePlugin.getInstance().getScenarioContext().copy();
    testRegistry = WebServiceTestRegistry.getInstance();
  }

  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus( "" );
  	String clientTestID = testFacilities.getSelection();
  	WebServiceTestFinishCommandFactory wtfcf = new WebServiceTestFinishCommandFactory();
  	WebServiceTestFinishCommand command = wtfcf.getWebServiceTestFinishCommand(clientTestID,env);
     
    command.setExistingServer(sampleExistingServer);
    command.setServerTypeID(sampleServerTypeID);
    status = command.execute(env);
    if(status.getSeverity() == Status.ERROR){
      StatusHandler sHandler = env.getStatusHandler();
      sHandler.reportError(status);
      return status;
    }
    return status;
  }
  
  private class WebServiceTestFinishCommandFactory
  {
  
    private WebServiceTestFinishCommand getWebServiceTestFinishCommand(String clientTestID,Environment env)
    {
  	  WebServiceTestExtension wscte = (WebServiceTestExtension)testRegistry.getWebServiceExtensionsByName(clientTestID);
  	  boolean isJava = true;
  	  if(wscte.isJava() && !generateProxy){
  	  	isJava = false;
        wscte = (WebServiceTestExtension)testRegistry.getWebServiceExtensionsByName(scenarioContext.getNonJavaTestService());   	  
  	  }
  	  if(!wscte.isJava())
  	    isJava = false;	
  	  WebServiceExecutable wse = wscte.getWebServiceExecutableExtension();
      
      if(isJava){
        JavaProxyTestCommand command = (JavaProxyTestCommand)wse.getFinishCommand();
        command.setProxyBean(proxyBean);
        command.setSampleProject(sampleProject);
        command.setClientProject(clientProject);
        command.setRunClientTest(runClientTest);
        command.setJspFolder(jspFolder); 
        command.setSetEndpointMethod(setEndpointMethod);
        command.setMethods(methods);
        command.setEndpoint(endpoints);
        if (clientIds.getServerInstanceId() != null) 
      	  sampleExistingServer = ServerCore.findServer(clientIds.getServerInstanceId());
      	if (sampleExistingServer != null)
      	  sampleServerTypeID = sampleExistingServer.getServerType().getId();
      	else
      	  sampleServerTypeID = clientIds.getServerId();
      	// server will be created in ServerDeployableConfigurationCommand
      	
        return command;
      }
      else{
      	WSDLTestFinishCommand wtfc = (WSDLTestFinishCommand)wse.getFinishCommand();
      	wtfc.setServiceProject(getWSDLProject());
    	wtfc.setWsdlServiceURL(wsdlServiceURL);
    	wtfc.setExternalBrowser(false);
    	wtfc.setEndpoint(endpoints);
    	if(serverIds != null){
    	  if (serverIds.getServerInstanceId() != null) 
    	    sampleExistingServer = ServerCore.findServer(serverIds.getServerInstanceId());
    	  if (sampleExistingServer != null)
    	    sampleServerTypeID = sampleExistingServer.getServerType().getId();
    	  else
    	    sampleServerTypeID = serverIds.getServerId();
    	  // server will be created in ServerDeployableConfigurationCommand
    	}
    	else if(clientIds != null)
    	{
          if (clientIds.getServerInstanceId() != null) 
            sampleExistingServer = ServerCore.findServer(clientIds.getServerInstanceId());
          if (sampleExistingServer != null)
            sampleServerTypeID = sampleExistingServer.getServerType().getId();
          else
            sampleServerTypeID = clientIds.getServerId();
          // server will be created in ServerDeployableConfigurationCommand
            		
    	}
    	return wtfc; 
      } 	
    }
  
    public Status getStatus()
    {
      return new SimpleStatus("");	
    }
  
  }

  //The test facilities retrieved from the extension
  //plus the default
  public void setTestFacility(SelectionList selection)
  {
    testFacilities = selection;
  } 
  
  public void setFolder(String folder)
  {
  	this.folder = folder;
  }
  
  public void setJspFolder(String jspFolder)
  {
  	this.jspFolder = jspFolder;
  }
  
  public void setMethods(BooleanSelection[] methods)
  {
    this.methods = methods;  
  }
 
  public void setRunClientTest(boolean runClientTest)
  {
  	this.runClientTest = runClientTest;
  }
  
  public void setProxyBean(String proxyBean)
  {
  	this.proxyBean = proxyBean;
  }
  
  public void setSampleProject(String sampleProject)
  {
  	this.sampleProject = sampleProject;
  }
  
  public void setClientProject(String clientProject)
  {
  	this.clientProject = clientProject;
  }
  
  public void setScenarioContext(ScenarioContext scenarioContext)
  {
  	this.scenarioContext = scenarioContext;
  }
  
  public void setClientTestRegistry(WebServiceTestRegistry testRegistry)
  {
  	this.testRegistry = testRegistry;
  }
   
  public void setSampleServerTypeID(String sampleServerTypeID)
  {
  	this.sampleServerTypeID = sampleServerTypeID;
  }
  
  public void setSampleExistingServer(IServer sampleExistingServer)
  {
  	this.sampleExistingServer = sampleExistingServer;
  }

  public void setClientTypeRuntimeServer(TypeRuntimeServer ids)
  {
    clientIds = ids;
  }
  
  public void setServiceTypeRuntimeServer(TypeRuntimeServer ids)
  {
    serverIds = ids;
  }
  
  public String getSampleServerTypeID()
  {
  	return sampleServerTypeID;
  }
  
  public IServer getSampleExistingServer()
  {
  	return sampleExistingServer;
  }

  public void setClientServer(String clientServer)
  {
  	this.clientServer = clientServer;
  }
  
  public void setServerProject(String serviceProject)
  {
  	this.serviceProject = serviceProject;
  }
  
  public String getWSDLProject()
  {
  	if(serviceProject != null)
     return serviceProject;
  	
  	return clientProject;
  }
  
  public void setWsdlURI(String wsdlServiceURL)
  {
    this.wsdlServiceURL = wsdlServiceURL;
  }
 
  public void setGenerateProxy(boolean generateProxy)
  {
  	this.generateProxy = generateProxy;
  }
  
  public boolean getIsTestWidget()
  {
  	return isTestWidget;
  }

  /**
   * @param setEndpointMethod The setEndpointMethod to set.
   */
  public void setSetEndpointMethod(String setEndpointMethod)
  {
    this.setEndpointMethod = setEndpointMethod;
  }
  /**
   * @param endpoint The endpoint to set.
   */
  public void setEndpoint(String endpoint)
  {
    if (endpoint != null && endpoint.length() > 0)
    {
      Vector v = new Vector();
      v.add(endpoint);
      setEndpoints(v);
    }
  }
  
  public void setEndpoints(List endpoints)
  {
    this.endpoints = endpoints;
  }
}
