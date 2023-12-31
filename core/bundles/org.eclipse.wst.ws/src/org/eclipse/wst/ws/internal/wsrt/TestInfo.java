/*******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060608   145529 kathy@ca.ibm.com - Kathy Chan
 * 20150311   461526 jgwest@ca.ibm.com - Jonathan West, Allow OSGi bundles to be selected in the Wizard
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

import java.util.List;

import org.eclipse.wst.command.internal.env.core.selection.BooleanSelection;
import org.eclipse.wst.server.core.IServer;
/**
 * This holds info that comes from user input or some calculations that 
 * which might be useful to any test extenders
 */

public class TestInfo {
  private String jspFolder;
  private String generationProject;
  private String serviceProject;
  private String proxyBean;
  private String setEndpointMethod;
  private String clientProject;
  private String clientModule;
  private boolean clientNeedEAR;
  private String clientOsgiAppProject;
  private String clientEARProject;
  private String clientEARModule;
  private BooleanSelection[] methods;
  private String serviceServerTypeID;
  private String clientServerTypeID;
  private IServer serviceExistingServer;
  private IServer clientExistingServer;
  private List endpoints;
  private String wsdlServiceURL;
  private boolean externalBrowser;  
  private boolean runTestClient;

  /**
  * This is the folder the user has chosen to generate any jsps 
  * @param jspFolder
  */
  public void setJspFolder(String jspFolder){
	this.jspFolder = jspFolder;  
  }
  
  public String getJspFolder(){
    return jspFolder;  
  }
  
  /**
  * This is the project the user has chosen to generate artifacts to  
  * @param generationProject
  */
  public void setGenerationProject(String generationProject){
    this.generationProject = generationProject;	  
  }
  
  public String getGenerationProject(){
    return generationProject;	  
  }
  
  /**
  * The proxy bean was generated by us this is the name of it  
  * @param proxyBean
  */
  public void setProxyBean(String proxyBean){
    this.proxyBean = proxyBean;	  
  }
  
  public String getProxyBean(){
    return proxyBean;	  
  }
	  
  /**
  * This is the SetEndpointMethod   
  * @param 
  */
  public void setSetEndpointMethod(String setEndpointMethod){
	this.setEndpointMethod = setEndpointMethod;  
  }
  
  public String getSetEndpointMethod(){
    return setEndpointMethod;  
  }
  
  /**
  * The clientProject contains the client artifacts including the 
  * generated proxy bean   
  * @param clientProject String
  */
  public void setClientProject(String clientProject){
    this.clientProject = clientProject;	  
  }
  
  public String getClientProject(){
    return clientProject;	  
  }
	  
  /**
   * The clientModule contains the client artifacts including the 
   * module   
   * @param clientProject String
   */
   public void setClientModule(String clientModule){
     this.clientModule = clientModule;	  
   }
   
   public String getClientModule(){
     return clientModule;	  
   }
  
  /**
  * These are methods on the proxybean the user has checked ones 
  * they want included in the test client     
  * @param methods
  */
  public void setMethods(BooleanSelection[] methods){
	this.methods = methods;  
  }
  
  public BooleanSelection[] getMethods(){
    return methods;  
  }
  
  /**
  * If the command needs a server this is the chosen 
  * client serverID
  * @param sampleServerTypeID
  */
  public void setServiceServerTypeID(String serviceServerTypeID){
    this.serviceServerTypeID = serviceServerTypeID;  
  }
  
  public String getServiceServerTypeID(){
    return serviceServerTypeID;  
  }
  
  /**
  * If the command needs a server this is the chosen 
  * client serverID
  * @param sampleServerTypeID
  */
   public void setClientServerTypeID(String clientServerTypeID){
     this.clientServerTypeID = clientServerTypeID;  
   }
   
   public String getClientServerTypeID(){
     return clientServerTypeID;  
   }
	  
  /**
  * This is the IServer if required
  * @param sampleExistingServer
  */
  public void setServiceExistingServer(IServer serviceExistingServer){
	this.serviceExistingServer = serviceExistingServer;   
  }
  
  public IServer getServiceExistingServer(){
    return serviceExistingServer;   
  }
  
  /**
   * This is the String if required
   * @param clientEARProject
   */
   public void setClientEARProject(String clientEARProject){
 	this.clientEARProject = clientEARProject;   
   }
   
   public String getClientEARProject(){
     return clientEARProject;   
   }
   
   
   /**
    * This is the String if required
    * @param clientEARProject
    */
    public void setClientEARModule(String clientEARModule){
  	this.clientEARModule = clientEARModule;   
    }
    
    public String getClientEARModule(){
      return clientEARModule;   
    }
   
  /**
  * This is the IServer if required
  * @param sampleExistingServer
  */
  public void setClientExistingServer(IServer clientExistingServer){
    this.clientExistingServer = clientExistingServer;   
  }
   
  public IServer getClientExistingServer(){
    return clientExistingServer;   
  }
   
  /**
  * This is the endpoints if monitor service is enabled
  * @param endpoints
  */
  public void setEndpoint(List endpoints){
	this.endpoints = endpoints;  
  }
  public List getEndpoint(){
    return endpoints;  
  } 

  /**
  * This is the project that should be used when dealing with
  * wsdl
  * @param serviceProject
  */
  public void setServiceProject(String serviceProject){
    this.serviceProject = serviceProject;    	  
  }

  public String getServiceProject(){
    return serviceProject;
  }

  /**
  * WSDL URL
  */
  public void setWsdlServiceURL(String wsdlServiceURL){
	this.wsdlServiceURL = wsdlServiceURL;  
  }
  
  public String getWsdlServiceURL(){
    return wsdlServiceURL;  
  }
  
  /**
   * clientNeedEAR
   */
  public boolean getClientNeedEAR() {
		return clientNeedEAR;
  }

  public void setClientNeedEAR(boolean clientNeedEAR) {
		this.clientNeedEAR = clientNeedEAR;
  }

  /**
   * external browser
   */
  public boolean getExternalBrowser() 
  {
	  return externalBrowser;
  }

  public void setExternalBrowser(boolean externalBrowser) {
	  this.externalBrowser = externalBrowser;
  }

public boolean getRunTestClient() {
	return runTestClient;
}

public void setRunTestClient(boolean runTestClient) {
	this.runTestClient = runTestClient;
}

public void setClientOsgiAppProject(String clientOsgiAppProject) {
	this.clientOsgiAppProject = clientOsgiAppProject;
}

public String getClientOsgiAppProject() {
	return clientOsgiAppProject;
}

	
}
