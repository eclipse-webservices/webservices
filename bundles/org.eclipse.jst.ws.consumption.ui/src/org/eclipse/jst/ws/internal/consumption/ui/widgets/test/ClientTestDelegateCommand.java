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
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.provisional.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;
import org.eclipse.wst.command.internal.provisional.env.core.selection.BooleanSelection;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceTester;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;


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
  private String sampleP;
  private String sampleC;
  private String clientProject;
  private String clientP;
  private String clientC;
  private String clientEarProjectName;
  private String clientEarComponentName;
  private String clientServer;
  private TypeRuntimeServer clientIds;
  private TypeRuntimeServer serverIds;
  private String serviceProject;
  private String serviceP;
  private String serviceC;
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
  	
	//Get the webservice extension
	
	WebServiceTestExtension wscte = (WebServiceTestExtension)testRegistry.getWebServiceExtensionsByName(clientTestID);
    IWebServiceTester iwst = (IWebServiceTester)wscte.getWebServiceExecutableExtension();
	TestInfo testInfo = getTestInfo();
	
	status = commandFactoryExecution(iwst.generate(testInfo),env);
	if(status.getSeverity() == Status.ERROR){
	  return status;	
	}
	status = commandFactoryExecution(iwst.launch(testInfo),env);
	if(status.getSeverity() == Status.ERROR){
	  return status;	
	}
	
    return status;
  }
  
  private Status commandFactoryExecution(ICommandFactory commandFactory,Environment env)
  {
    Status status = new SimpleStatus( "" );  	
	
	while(commandFactory.hasNext()){ 
      status = commandFactory.getNextCommand().execute(env);
	  if(status.getSeverity() == Status.ERROR){
	    StatusHandler sHandler = env.getStatusHandler();
		sHandler.reportError(status);
		return status;
	  }
    }
    return status;
  }
  
  
  //Helper method which sets up the TestInfo data structure
  private TestInfo getTestInfo()
  {
	IServer serviceExistingServer = null;
	String serviceServerTypeID = null;
		
	//service server info
	if(serverIds != null){
	  if (serverIds.getServerInstanceId() != null) 
	    serviceExistingServer = ServerCore.findServer(serverIds.getServerInstanceId());
	}	
	if (serviceExistingServer != null)
      serviceServerTypeID = serviceExistingServer.getServerType().getId();
    
	
	if(clientProject != null){
  	  int index = clientProject.indexOf("/");
      if (index!=-1){
        clientP = clientProject.substring(0,index);
        clientC = clientProject.substring(index + 1);
      }
  	}

  
	if(sampleProject != null){
	  int index = sampleProject.indexOf("/");
      if (index!=-1){
        sampleP = sampleProject.substring(0,index);
        sampleC = sampleProject.substring(index + 1);
      }
	}
	
    
	
	TestInfo testInfo = new TestInfo();  
	testInfo.setClientExistingServer(sampleExistingServer);
	testInfo.setClientServerTypeID(sampleServerTypeID);
	testInfo.setJspFolder(jspFolder);
	testInfo.setEndpoint(endpoints);
	testInfo.setGenerationProject(sampleP);
	testInfo.setGenerationModule(sampleC);
	testInfo.setProxyBean(proxyBean);
	testInfo.setSetEndpointMethod(setEndpointMethod);
	testInfo.setClientProject(clientP);
	testInfo.setClientModule(clientC);
	testInfo.setClientEARProject(clientEarProjectName);
	testInfo.setClientEARModule(clientEarComponentName);
	testInfo.setMethods(methods);
	
	//if this is a client scenario the service stuff is empty
	if(serviceServerTypeID == null){
	  serviceServerTypeID = sampleServerTypeID;
	  serviceExistingServer = sampleExistingServer;
	}
	
	
	testInfo.setServiceServerTypeID(serviceServerTypeID);
	testInfo.setServiceExistingServer(serviceExistingServer);
	//wsdl stuff
	testInfo.setServiceProject(getWSDLProject());
	testInfo.setWsdlServiceURL(wsdlServiceURL);
	return testInfo;
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
  	 if(serviceProject != null){
	     int index = serviceProject.indexOf("/");
       if (index!=-1) {
		     serviceP = serviceProject.substring(0,index);
		     serviceC = serviceProject.substring(index + 1);
	       return serviceP;
       }
  	}	
	
  	return clientP;
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

  public void setClientEarProjectName(String clientEarProjectName)
  {
     this.clientEarProjectName = clientEarProjectName;
  }
  
  public void setClientEarComponentName(String clientEarComponentName)
  {
	this.clientEarComponentName = clientEarComponentName;
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
