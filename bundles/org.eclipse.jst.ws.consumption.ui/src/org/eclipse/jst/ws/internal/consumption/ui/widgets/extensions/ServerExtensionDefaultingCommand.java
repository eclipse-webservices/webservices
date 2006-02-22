/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060204 124408   rsinha@ca.ibm.com - Rupam Kuehner          
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.RuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ServerExtensionDefaultingCommand extends AbstractDataModelOperation
{
  private Boolean              deployService;
  private Boolean              installService;
  private Boolean              startService;
  private Boolean              testService;
  private Boolean              publishService;
  private TypeRuntimeServer    serviceIds_;
  private String               serviceRuntimeId_;
  //private SelectionListChoices serviceChoices_;
  private String               serviceProjectName_;
  private String               serviceEarProjectName_;
  private String               serviceComponentType_;
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
   * @return Returns the deployService.
   */
  public Boolean getDeployService()
  {
    return deployService;
  }
  
  /**
   * @return Returns the installService.
   */
  public Boolean getInstallService()
  {
    return installService;
  }
  /**
   * @param installService
   *            Specifies if service should be installed.
   */
  public void setInstallService(Boolean installService)
  {
    this.installService = installService;
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
  
  public void setServiceComponentType(String type)
  {
    this.serviceComponentType_ = type;
  }
  
  public String getServiceComponentType()
  {
    return serviceComponentType_;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    IEnvironment env = getEnvironment();
    //Do some basic validation to ensure the server/runtime/type combination is supported.
    //This is needed to catch problems in the defaulting
    //when the user clicks Finish prior to page 3 of the wizard.
    
    IStatus status = Status.OK_STATUS;
    
    //default deployService to true. It will get set to false later if there is no
    //server type selected.
    deployService = Boolean.TRUE;
    
    String scenario = ConsumptionUIMessages.MSG_SERVICE_SUB;

    //Ensure server and runtime are non-null
    String runtimeId = serviceIds_.getRuntimeId();
    String serverId = serviceIds_.getServerId();
    String typeId = serviceIds_.getTypeId();
    
    if( runtimeId == null || runtimeId.length()==0)
    {
      status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_RUNTIME, new String[]{ scenario } ) );
      env.getStatusHandler().reportError(status);
    }
    
    if( serverId == null || serverId.length()==0)
    {
      //Popup and error if the selected service project does not exist.
      IProject serviceProject = ProjectUtilities.getProject(serviceProjectName_);
      if (!serviceProject.exists())        
      {
        String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
        status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_ERROR_NO_SERVER_RUNTIME, new String[]{ runtimeLabel } ) );
        env.getStatusHandler().reportError(status);
      }
      else
      {

        RuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getRuntimeById(runtimeId);
        if (desc.getServerRequired())
        {
          status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_SERVER, new String[] { scenario }));
          env.getStatusHandler().reportError(status);
        } 
        else
        {
          // No server has been selected and the selected Web service runtime
          // does not require a server. Set deploy, install, run, and test to false.
          deployService = Boolean.FALSE;
          installService = Boolean.FALSE;
          startService = Boolean.FALSE;
          testService = Boolean.FALSE;
        }
      }
    }
    
    //If the server is non-null, ensure the server, runtime, and type are compatible
    if (serverId != null && serverId.length() > 0)
    {
      if (!WebServiceRuntimeExtensionUtils2.isServerRuntimeTypeSupported(serverId, runtimeId, typeId))
      {
        String serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverId);
        String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
        status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS, new String[] { serverLabel,
            runtimeLabel }));
        env.getStatusHandler().reportError(status);
      }

      // Determine if the selected server type has only stub runtimes associated
      // with it.
      // If so, set install, run, and test to false in the context.
      IRuntime nonStubRuntime = ServerUtils.getNonStubRuntime(serverId);
      if (nonStubRuntime == null)
      {
        installService = Boolean.FALSE;
        startService = Boolean.FALSE;
        testService = Boolean.FALSE;
      }
    }
    
    return status;
  }
}
