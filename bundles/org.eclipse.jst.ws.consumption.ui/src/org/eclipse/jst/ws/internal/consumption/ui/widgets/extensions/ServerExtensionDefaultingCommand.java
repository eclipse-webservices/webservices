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

import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;

public class ServerExtensionDefaultingCommand extends SimpleCommand
{
  private Boolean              startService;
  private Boolean              testService;
  private Boolean              publishService;
  private TypeRuntimeServer    serviceIds_;
  private SelectionListChoices serviceChoices_;
  private String j2eeVersion;
  private boolean serviceNeedEAR_;

  
  public void setServiceTypeRuntimeServer(TypeRuntimeServer ids)
  {
    serviceIds_ = ids;
  }

  public void setServiceExistingServerInstanceId(String serverInstId){
    
    if (serviceIds_.getServerInstanceId()==null)
      serviceIds_.setServerInstanceId(serverInstId);
  }
  
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
    return serviceChoices_.getList().getSelection();
  }

  /**
   * @return Returns the serverProjectEAR.
   */
  public String getServerProjectEAR()
  {
    return serviceChoices_.getChoice().getList().getSelection();
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
  
  public boolean getServiceNeedEAR()
  {
    return serviceNeedEAR_;
  }
  
  public void setServiceNeedEAR(boolean serviceNeedEAR)
  {
    serviceNeedEAR_ = serviceNeedEAR;
  }  
  
  public boolean getIsServiceProjectEJB()
  {
    //If the type/server/runtime suggests that an EJB project
    //is required, return true.
    boolean ejbRequired = false;
    boolean ejbRequired2 = false;
    WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
    if (serviceIds_ != null)
    {      
      String serverTypeId = wssrtRegistry.getWebServiceServerByFactoryId(serviceIds_.getServerId()).getId();
      ejbRequired = wssrtRegistry.requiresEJBModuleFor(serverTypeId, serviceIds_.getRuntimeId(), serviceIds_.getTypeId());
      ejbRequired2 = wssrtRegistry.requiresEJBProject(serviceIds_.getTypeId()); 
    }
 
    return (ejbRequired || ejbRequired2);    
  }
}