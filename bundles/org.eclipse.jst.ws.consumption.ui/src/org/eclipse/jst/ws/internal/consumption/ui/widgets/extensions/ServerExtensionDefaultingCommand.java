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
import org.eclipse.jst.ws.internal.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.ws.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;


public class ServerExtensionDefaultingCommand extends SimpleCommand
{
  private Boolean              startService;
  private Boolean              testService;
  private Boolean              publishService;
  private TypeRuntimeServer    serviceIds_;
  private SelectionListChoices serviceChoices_;
  private String j2eeVersion;
  private boolean serviceNeedEAR_;
  private WebServicesParser wsdlParser_;

  
  public void setServiceTypeRuntimeServer(TypeRuntimeServer ids)
  {
    serviceIds_ = ids;
  }

  public void setServiceExistingServerInstanceId(String serverInstId){
    
    if (serviceIds_.getServerInstanceId()==null)
      serviceIds_.setServerInstanceId(serverInstId);
  }
  
  /**
   * 
   * @return returns the TypeRuntimeService object that the user has selected on
   * page 3 of the wizard.
   */
  public TypeRuntimeServer getServiceTypeRuntimeServer()
  {
    return serviceIds_;
  }

  public void setServiceProject2EARProject(SelectionListChoices choices)
  {
    serviceChoices_ = choices;
  }


  /**
   * @return Returns the publishService.
   */
  public Boolean getPublishService()
  {
    return publishService;
  }

  /**
   * @param publishService
   *            The publishService to set.
   */
  public void setPublishService(Boolean publishService)
  {
    this.publishService = publishService;
  }

  /**
   * @return Returns the serverIsExistingServer.
   */
  public boolean getServerIsExistingServer()
  {
    return serviceIds_.getServerInstanceId() != null;
  }

  /**

   * @return Returns the serverInstanceId
   */
  public String getServiceServerInstanceId()
  {
    return serviceIds_.getServerInstanceId();
  }
  
  /**
   * @return Returns the serverProject.
   */
  public String getServerProject()
  {
	String   projectName       = serviceChoices_.getList().getSelection();
	
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
   * @return Returns the serverProjectEAR.
   */
  public String getServerProjectEAR()
  {
    String earName = serviceChoices_.getChoice().getList().getSelection();
	
	return getModuleProjectName( earName );
  }

  /**
   * @return Returns the serverRuntime.
   */
  public String getServerRuntime()
  {
    return serviceIds_.getRuntimeId();
  }

  /**
   * @return Returns the serverServer.
   */
  public String getServerServer()
  {
    return serviceIds_.getServerId();
  }

  /**
   * @return Returns the serviceScenarioId.
   */
  public String getServiceScenarioId()
  {
    return serviceIds_.getTypeId();
  }

  /**
   * @return Returns the startService.
   */
  public Boolean getStartService()
  {
    return startService;
  }

  /**
   * @param startService
   *            The startService to set.
   */
  public void setStartService(Boolean startService)
  {
    this.startService = startService;
  }

  /**
   * @return Returns the testService.
   */
  public Boolean getTestService()
  {
    return testService;
  }

  /**
   * @param testService
   *            The testService to set.
   */
  public void setTestService(Boolean testService)
  {
    this.testService = testService;
  }

  public Boolean getPublish()
  {
    return new Boolean(true);
  }

  /**
   * @return Returns the j2eeVersion.
   */
  public String getServiceJ2EEVersion()
  {
  	return j2eeVersion;
  }
  
  /**
   * @param version The j2eeVersion to set.
   */
  public void setServiceJ2EEVersion(String version)
  {
  	j2eeVersion = version;
  }
  
  /**
   * 
   * @return returns true if the web service project needs to be in an EAR project.
   */
  public boolean getServiceNeedEAR()
  {
    return serviceNeedEAR_;
  }
  
  public void setServiceNeedEAR(boolean serviceNeedEAR)
  {
    serviceNeedEAR_ = serviceNeedEAR;
  }  
  
  /**
   * 
   * @return returns true if the web service project is an EJB project.
   */
  public boolean getIsServiceProjectEJB()
  {
    //If the type/server/runtime suggests that an EJB project
    //is required, return true.
    boolean ejbRequired = false;
    boolean ejbRequired2 = false;
	// rskreg
    //WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
    if (serviceIds_ != null)
    {      
      //String serverTypeId = wssrtRegistry.getWebServiceServerByFactoryId(serviceIds_.getServerId()).getId();
	  //String serverTypeId = wssrtRegistry.getWebServiceServerByFactoryId(serviceIds_.getServerId()).getId();
      //ejbRequired = wssrtRegistry.requiresEJBModuleFor(serverTypeId, serviceIds_.getRuntimeId(), serviceIds_.getTypeId());
	  ejbRequired = WebServiceRuntimeExtensionUtils.requiresEJBModuleFor(serviceIds_.getServerId(), serviceIds_.getRuntimeId(), serviceIds_.getTypeId());
      //ejbRequired2 = wssrtRegistry.requiresEJBProject(serviceIds_.getTypeId()); 
	  ejbRequired2 = WebServiceRuntimeExtensionUtils.requiresEJBProject(serviceIds_.getTypeId());
    }

	// rskreg
    return (ejbRequired || ejbRequired2);    
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
  public void setWebServicesParser(WebServicesParser wsdlParser )
  {
    wsdlParser_ = wsdlParser;
  }
}