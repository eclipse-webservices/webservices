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
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
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
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ClientExtensionDefaultingCommand extends AbstractDataModelOperation
{
  // ClientWizardWidget
  private TypeRuntimeServer    clientIds_;
  private String               clientRuntimeId_;
  private Boolean              testService;
  private Boolean              deployClient;
  private Boolean              installClient;
  private boolean              runTestClient;
  private ResourceContext      resourceContext;
  
  // ClientRuntimeSelectionWidget
  //private SelectionListChoices clientChoices_;
  private String clientProjectName_;
  private String clientEarProjectName_;
  //private String clientComponentName_;
  //private String clientEarComponentName_;
  private String clientComponentType_;
  //private String j2eeVersion;
  private boolean clientNeedEAR_;
  
  private String serviceServerFactoryId_;
  private String serviceServerInstanceId_;
  
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
  
  /*
  public void setRuntime2ClientTypes( SelectionListChoices choices )
  {
    clientChoices_ = choices;  
  }
  
  
  public SelectionListChoices getRuntime2ClientTypes()
  {
    return clientChoices_;  
  }
  */
  
  public void setClientTypeRuntimeServer(TypeRuntimeServer ids)
  {
    clientIds_ = ids;
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
	if (clientIds_.getServerInstanceId()==null || clientIds_.getServerInstanceId().length()==0)
	{
		//Set the instance id from the service side if the factory ids match
		if (serviceServerInstanceId_!=null && serviceServerInstanceId_.length()>0)
		{
			if (serviceServerFactoryId_!=null && serviceServerFactoryId_.length()>0)
			{
				if (serviceServerFactoryId_.equals(clientIds_.getServerId()))
				{
					clientIds_.setServerInstanceId(serviceServerInstanceId_);
				}
			}
		}
	}
    return clientIds_;
  }
  
  public void setClientRuntimeId(String id)
  {
    clientRuntimeId_ = id;
  }
  
  public String getClientRuntimeId()
  {
    return clientRuntimeId_;
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
	  //return clientProjectName_ + "/" + clientComponentName_;
      return clientProjectName_ + "/" + clientProjectName_;
  }

  /**
   * @return Returns the clientProjectEAR.
   */
  public String getClientProjectEAR()
  {
    if (clientEarProjectName_!=null && clientEarProjectName_.length()>0)
    {
	    //return clientEarProjectName_ + "/" + clientEarComponentName_;
      return clientEarProjectName_ + "/" + clientEarProjectName_;
    }
    else
    {
      return "";
    }
  }
  
  /*
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
  */
  /**
   * @return Returns the clientProjectType.
   */
  
  public String getClientProjectType()
  {
    //return clientChoices_.getChoice().getList().getSelection();
    return clientComponentType_;
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
   * @return Returns the testProxySelected.
   */
  public boolean getRunTestClient()
  {
    return runTestClient;
  }

  /**
   * @param testProxySelected
   *            The testProxySelected to set.
   */
  public void setRunTestClient(boolean runTestClient)
  {
    this.runTestClient = runTestClient;
  }
  
  /**
   * @return Returns the deployService.
   */
  public Boolean getDeployClient()
  {
    return deployClient;
  }
  
  /**
   * @return Returns the installClient.
   */
  public Boolean getInstallClient()
  {
    return installClient;
  }

  /**
   * @param testProxySelected
   *            The testProxySelected to set.
   */
  public void setInstallClient(Boolean installClient)
  {
    this.installClient = installClient;
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
	  return "14"; //rm j2ee
  }

  /**
   * @param version The j2eeVersion to set.
   */
  /*
  public void setClientJ2EEVersion(String version)
  {
	  j2eeVersion = version;
  }
  */
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

  
  public void setClientComponentType(String clientComponentType)
  {
    this.clientComponentType_ = clientComponentType;
  }

  public void setClientProjectName(String clientProjectName)
  {
    this.clientProjectName_ = clientProjectName;
  }

  /*
  public void setClientComponentName(String clientComponentName)
  {
    this.clientComponentName_ = clientComponentName;
  }
  */
  
  public void setClientEarProjectName(String clientEarProjectName)
  {
    this.clientEarProjectName_ = clientEarProjectName;
  }
  
  /*
  public void setClientEarComponentName(String clientEarComponentName)
  {
    this.clientEarComponentName_ = clientEarComponentName;
  }
  */
  
  public String getClientEarProjectName()
  {
    return clientEarProjectName_;
  }
  
  /*
  public String getClientEarComponentName()
  {
    return clientEarComponentName_;
  }
  */
  
  public void setServiceServerFactoryId(String id)
  {
  	serviceServerFactoryId_ = id;
  }

  public void setServiceServerInstanceId(String id)
  {
  	serviceServerInstanceId_ = id;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    IEnvironment env = getEnvironment();
    //Do some basic validation to ensure the server/runtime/type combination is supported.
    //This is needed to catch problems in the defaulting
    //when the user clicks Finish prior to page 3 of the wizard.
    
    IStatus status = Status.OK_STATUS;
    
    //default deployClient to true. It will get set to false later if there is no
    //server type selected.
    deployClient = Boolean.TRUE;
    
    String scenario = ConsumptionUIMessages.MSG_CLIENT_SUB;

    //Ensure server and runtime are non-null
    String runtimeId = clientIds_.getRuntimeId();
    String serverId = clientIds_.getServerId();
    String typeId = clientIds_.getTypeId();
    
    if( runtimeId == null || runtimeId.length()==0)
    {
      status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_RUNTIME, new String[]{ scenario } ) );
      env.getStatusHandler().reportError(status);
    }
    
    if( serverId == null || serverId.length()==0)
    {
      //Popup and error if the selected client project does not exist.
      IProject clientProject = ProjectUtilities.getProject(clientProjectName_);
      if (!clientProject.exists())
      {
        String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
        status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_ERROR_NO_SERVER_RUNTIME, new String[]{runtimeLabel } ) );
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
          // does not
          // require a server. Set deploy, install, run, and test to false.
          deployClient = Boolean.FALSE;
          installClient = Boolean.FALSE;
          testService = Boolean.FALSE;
        }
      }
    }
    
    //If the server is non-null, ensure the server, runtime, and type are compatible
    if (serverId != null && serverId.length() > 0)
    {
      if (!WebServiceRuntimeExtensionUtils2.isServerClientRuntimeTypeSupported(serverId, runtimeId, typeId))
      {
        String serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverId);
        String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(runtimeId);
        status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS, new String[] { serverLabel,
            runtimeLabel }));
        env.getStatusHandler().reportError(status);
      }

      // Determine if the selected server type has only stub runtimes associated
      // with it.
      // If so, set install and test to false in the context.
      IRuntime nonStubRuntime = ServerUtils.getNonStubRuntime(serverId);
      if (nonStubRuntime == null)
      {
        installClient = Boolean.FALSE;
        testService = Boolean.FALSE;
      }
    }

    return status;
  }
  
  
}
