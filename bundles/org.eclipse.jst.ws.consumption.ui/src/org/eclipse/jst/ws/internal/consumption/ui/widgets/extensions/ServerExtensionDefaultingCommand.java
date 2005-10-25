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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ServerExtensionDefaultingCommand extends AbstractDataModelOperation
{
  private Boolean              startService;
  private Boolean              testService;
  private Boolean              publishService;
  private TypeRuntimeServer    serviceIds_;
  private String               serviceRuntimeId_;
  //private SelectionListChoices serviceChoices_;
  private String               serviceProjectName_;
  private String               serviceEarProjectName_;
  //private String               serviceComponentName_;
  //private String               serviceEarComponentName_;
  //private String j2eeVersion;
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
  
  public void setServiceRuntimeId(String id)
  {
    serviceRuntimeId_ = id;
  }
  
  public String getServiceRuntimeId()
  {
    return serviceRuntimeId_;
  }

  /*
  public void setServiceProject2EARProject(SelectionListChoices choices)
  {
    serviceChoices_ = choices;
  }
  */

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
    return serviceProjectName_ + "/" + serviceProjectName_; 
  }
  
  /**
   * @return Returns the serverProjectEAR.
   */
  public String getServerProjectEAR()
  {
    if (serviceEarProjectName_!=null && serviceEarProjectName_.length()>0)
    {
      return serviceEarProjectName_ + "/" + serviceEarProjectName_;
    }
    else
    {
      return "";
    }
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
  	return "14"; //rm j2ee
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
  
  
  public void setServiceProjectName(String serviceProjectName)
  {
    this.serviceProjectName_ = serviceProjectName;
  }

  public void setServiceEarProjectName(String serviceEarProjectName)
  {
    this.serviceEarProjectName_ = serviceEarProjectName;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return Status.OK_STATUS;
  }
}