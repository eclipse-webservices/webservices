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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ClientExtensionDefaultingCommand extends SimpleCommand
{
  // ClientWizardWidget
  private TypeRuntimeServer    clientIds_;
  private Boolean              testService;
  private ResourceContext      resourceContext;
  
  // ClientRuntimeSelectionWidget
  private SelectionListChoices clientChoices_;
  private String j2eeVersion;
  private boolean clientNeedEAR_;
  
  // WSDLSelectionWidget
  private boolean           genWSIL;
  private String            wsilURI;
  
  // WSDLSelectionConditionCommand
  private WebServicesParser webServicesParser;
  
  // WSDLSelectionTreeWidget
  private String            wsdlURI;
  
  private boolean           isClientScenario_;
  private boolean           genProxy_ = true;
  private String launchedServiceTestName;
  
  //ServerDeployableConfigurationCommand
  private String serverInstId_;
  
  public ClientExtensionDefaultingCommand( boolean isClientScenario )
  {
    isClientScenario_ = isClientScenario;
  }

  /**
   * 
   * @return returns true if this command is running in the client wizard and
   * false if this command is running in the create web service wizard.
   */
  public boolean getIsClientScenario()
  {
    return isClientScenario_; 
  }
  
  public void setRuntime2ClientTypes( SelectionListChoices choices )
  {
    clientChoices_ = choices;  
  }
  
  public SelectionListChoices getRuntime2ClientTypes()
  {
    return clientChoices_;  
  }
  
  public void setClientTypeRuntimeServer(TypeRuntimeServer ids)
  {
    clientIds_ = ids;
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    return clientIds_;
  }
  
  public void setServiceExistingServerInstId(String serverInstId) {
  	this.serverInstId_ = serverInstId;
  }
  
  public String getServiceExistingServerInstId(){
  	return this.serverInstId_;
  }
  
  /**
   * @return Returns the clientIsExistingServer.
   */
  public boolean isClientIsExistingServer()
  {
    return clientIds_.getServerInstanceId() != null;
  }

  /**
   * @return Returns the clientProject.
   */
  public String getClientProject()
  {
    String projectName = clientChoices_.getChoice().getChoice().getList().getSelection();
	
	return getModuleProjectName( projectName );
  }

  /**
   * @return Returns the clientProjectEAR.
   */
  public String getClientProjectEAR()
  {
    String projectName = clientChoices_.getChoice().getChoice().getChoice().getList().getSelection();
	
	return getModuleProjectName( projectName );	
  }
  
  private String getModuleProjectName( String projectName )
  {
	String result = "";
	
	if( projectName != null && !projectName.equals("") )
	{
	  IPath    projectPath = new Path( projectName );
	  IProject project     = (IProject)ResourceUtils.findResource( projectPath );
	  String   moduleName  = J2EEUtils.getFirstWebModuleName( project );
	  
	  result = projectName + "/" + moduleName;
	}
	
	return result;
  }

  /**
   * @return Returns the clientProjectType.
   */
  public String getClientProjectType()
  {
    return clientChoices_.getChoice().getList().getSelection();
  }

  /**
   * @return Returns the clientRuntime.
   */
  public String getClientRuntime()
  {
    return clientIds_.getRuntimeId();
  }

  /**
   * @return Returns the clientServer.
   */
  public String getClientServer()
  {
    return clientIds_.getServerId();
  }
  
  public String getClientServerInstance()
  {
    return clientIds_.getServerInstanceId();    
  }

  public void setClientExistingServerInstanceId(String serverInstId){
    
    if (clientIds_.getServerInstanceId()==null)
      clientIds_.setServerInstanceId(serverInstId);
  }
  
  /**
   * @return Returns the genWSIL.
   */
  public boolean isGenWSIL()
  {
    return genWSIL;
  }

  /**
   * @param genWSIL
   *            The genWSIL to set.
   */
  public void setGenWSIL(boolean genWSIL)
  {
    this.genWSIL = genWSIL;
  }

  /**
   * @return Returns the resourceContext.
   */
  public ResourceContext getResourceContext()
  {
    return resourceContext;
  }

  /**
   * @param resourceContext
   *            The resourceContext to set.
   */
  public void setResourceContext(ResourceContext resourceContext)
  {
    this.resourceContext = resourceContext;
  }

  /**
   * @return Returns the testProxySelected.
   */
  public Boolean getTestService()
  {
    return testService;
  }

  /**
   * @param testProxySelected
   *            The testProxySelected to set.
   */
  public void setTestService(Boolean testService)
  {
    this.testService = testService;
  }

  /**
   * @return Returns the wsdlURI.
   */
  public String getWsdlURI()
  {
    return wsdlURI;
  }

  /**
   * @param wsdlURI
   *            The wsdlURI to set.
   */
  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }

  /**
   * @return Returns the wsilURI.
   */
  public String getWsilURI()
  {
    return wsilURI;
  }

  /**
   * @param wsilURI
   *            The wsilURI to set.
   */
  public void setWsilURI(String wsilURI)
  {
    this.wsilURI = wsilURI;
  }

  /**
   * @return Returns the webServicesParser.
   */
  public WebServicesParser getWebServicesParser()
  {
    if (webServicesParser != null)
      return webServicesParser;
    else
      return new WebServicesParserExt();
  }

  /**
   * @param webServicesParser
   *            The webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }

  /**
   * @return Returns the j2eeVersion.
   */
  public String getClientJ2EEVersion()
  {
	  return j2eeVersion;
  }

  /**
   * @param version The j2eeVersion to set.
   */
  public void setClientJ2EEVersion(String version)
  {
	  j2eeVersion = version;
  }
  
  /**
   * 
   * @return returns true if a proxy should be generated and false otherwise.
   */
  public boolean getGenerateProxy()
  {
    return genProxy_;
  }
  
  public void setGenerateProxy( boolean genProxy )
  {
    genProxy_ = genProxy;
  }

  public void setLaunchedServiceTestName(String launchedServiceTestName)
  {
  	this.launchedServiceTestName = launchedServiceTestName;
  }

  /**
   * 
   * @return returns the name of the test facility to launch.
   */
  public String getLaunchedServiceTestName()
  {
  	return launchedServiceTestName;
  }
  
  /**
   * 
   * @return returns true if the client project needs an EAR project.
   */
  public boolean getClientNeedEAR()
  {
    return clientNeedEAR_;
  }
  
  public void setClientNeedEAR(boolean clientNeedEAR)
  {
    clientNeedEAR_ = clientNeedEAR;
  }
}