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
package org.eclipse.jst.ws.internal.creation.ui.widgets.runtime;

import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ServerSelectionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceType;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeInfo;
import org.eclipse.jst.ws.internal.context.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;

public class ServerRuntimeSelectionWidgetDefaultingCommand extends ClientRuntimeSelectionWidgetDefaultingCommand
{
  private String DEFAULT_CLIENT_EAR_PROJECT_EXT = "EAR";	
  private boolean           generateProxy_;
  
  // webServiceRuntimeJ2EEType contains the default Web service runtime and J2EE level based on the initial selection.
  // If the initialSeleciton does not result in a valid Web service runtime and J2EE level, webServiceRuntimeJ2EEType
  // should remain null for the life of this instance.
  private WSRuntimeJ2EEType webServiceRuntimeJ2EEType = null;
  
  private TypeRuntimeServer serviceIds_;
  private SelectionListChoices serviceProject2EARProject_;
  private String serviceComponentName_;
  private String serviceEarComponentName_;
  private String serviceComponentType_;
  private IStructuredSelection initialSelection_;
  private IProject initialProject_;
  private String initialComponentName_;
  private String serviceJ2EEVersion_;
  private boolean serviceNeedEAR_ = true;

  public ServerRuntimeSelectionWidgetDefaultingCommand()
  {
    super();
  }
  
  public Status execute(Environment env)
  {
    try
    {
     Status clientSideStatus = super.execute(env);
     if (clientSideStatus.getSeverity()==Status.ERROR)
     {
       return clientSideStatus;
     }
     Status status = new SimpleStatus("");


    setDefaultServiceRuntimeFromPreference();
    setDefaultServiceJ2EEVersionFromPreference();      
    webServiceRuntimeJ2EEType = getWSRuntimeAndJ2EEFromProject(initialProject_, initialComponentName_);
    if (webServiceRuntimeJ2EEType != null)
    {
      serviceJ2EEVersion_ = webServiceRuntimeJ2EEType.getJ2eeVersionId();
      serviceIds_.setRuntimeId(webServiceRuntimeJ2EEType.getWsrId()); 
    }	 
     
     setServiceComponentType();
     // Default projects EARs and servers.
     setDefaultProjects();
     setDefaultEARs();
     Status serverStatus = setDefaultServer();
     if (serverStatus.getSeverity()== Status.ERROR)
     {
         env.getStatusHandler().reportError(serverStatus);
         return serverStatus;
     }
     updateServiceEARs();
     //updateClientProject(getServiceProject2EARProject().getList().getSelection(), serviceComponentName_, serviceIds_.getTypeId());
     updateClientEARs();
	 	 
     return status;
    } catch (Exception e)
    {
      //Catch all Exceptions in order to give some feedback to the user
      Status errorStatus = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED",new String[]{e.getMessage()}),Status.ERROR, e);
      env.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
    
  }  

  private void setServiceComponentType()
  {
    ServiceType st = WebServiceRuntimeExtensionUtils.getServiceType(serviceIds_.getRuntimeId(), serviceIds_.getTypeId());
    int scenario = WebServiceRuntimeExtensionUtils.getScenarioFromTypeId(serviceIds_.getTypeId());
    String[] includeComponentTypes = null;
    switch (scenario)
    {
    case WebServiceScenario.BOTTOMUP:
		includeComponentTypes = st.getBottomUpModuleTypesInclude();
      break;
    case WebServiceScenario.TOPDOWN:
		includeComponentTypes = st.getTopDownModuleTypesInclude();
    }
    
    if (includeComponentTypes == null || includeComponentTypes.length==0)
    {
      serviceComponentType_ = IModuleConstants.JST_WEB_MODULE;
    }
    else
    {
	  serviceComponentType_ = includeComponentTypes[0];
    }
  }
  
  /*
  private void setWebClientType()
  {
    SelectionListChoices choices = getRuntime2ClientTypes();
    String               webId   = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Web";
    
    if( choices != null )
    {
      choices.getChoice().getList().setSelectionValue( webId );   
    }
  }
  */
  
  /**
   * Returns an object with target Runtime and J2EE value
   * @param project
   * @return
   */

  private WSRuntimeJ2EEType getWSRuntimeAndJ2EEFromProject(IProject project, String componentName)
  {
    WSRuntimeJ2EEType wsrJ2EE = null;
    //If there is a valid initial selection, use it to determine
    //reasonable J2EE version and Web service runtime values

    if (project != null && project.exists())
    {
      boolean isValidComponentType = false;
      if (componentName != null && componentName.length()>0)
      {
        isValidComponentType = J2EEUtils.isWebComponent(project, componentName) ||
                                     J2EEUtils.isEJBComponent(project, componentName);
      }
      if (isValidComponentType)
      {
        //WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
        
        //Get the J2EE level
        int versionId = J2EEUtils.getJ2EEVersion(project, componentName);
        String versionString = String.valueOf(versionId);
        
        //Get the runtime target of the project
        IRuntime runtimeTarget = ServerSelectionUtils.getRuntimeTarget(project.getName());
        String runtimeTargetId = null;
        if (runtimeTarget != null) 
          runtimeTargetId = runtimeTarget.getRuntimeType().getId();
        
        //If the preferred Web service runtime supports this J2EE level and this server target, keep it
        if (WebServiceRuntimeExtensionUtils.doesRuntimeSupportJ2EELevel(versionString, serviceIds_.getRuntimeId()) &&
            ((runtimeTarget == null) || 
             (runtimeTarget != null && WebServiceRuntimeExtensionUtils.doesRuntimeSupportServerTarget(runtimeTargetId, serviceIds_.getRuntimeId()))
            )
           )
          
        {
            //Set the J2EE level and Web service runtime
            wsrJ2EE = new WSRuntimeJ2EEType();
            wsrJ2EE.setWsrId(serviceIds_.getRuntimeId());
            wsrJ2EE.setJ2eeVersionId(versionString);
            return wsrJ2EE;

        } else
        {
          //Look for a runtime that matches both the J2EE level and the server target.
          String[] validRuntimes = WebServiceRuntimeExtensionUtils.getRuntimesByType(serviceIds_.getTypeId());
          for (int i = 0; i < validRuntimes.length; i++)
          {
            if (WebServiceRuntimeExtensionUtils.doesRuntimeSupportJ2EELevel(versionString, validRuntimes[i]) &&
                ((runtimeTarget == null) ||
                 (runtimeTarget!=null && WebServiceRuntimeExtensionUtils.doesRuntimeSupportServerTarget(runtimeTargetId, validRuntimes[i]))
                )
                )
            {
              wsrJ2EE = new WSRuntimeJ2EEType();
              wsrJ2EE.setWsrId(validRuntimes[i]);
              wsrJ2EE.setJ2eeVersionId(versionString);
              return wsrJ2EE;
            }
          }
        }
      }
    }    
    return wsrJ2EE;
  }
  
  private void setDefaultServiceRuntimeFromPreference()
  {
  	
    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
    String pRuntimeId = context.getRuntimeId();
    //WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
    //if the preferred runtime in compatible with the type, select it.
    if (WebServiceRuntimeExtensionUtils.isRuntimeSupportedForType(serviceIds_.getTypeId(), pRuntimeId))
    {
      serviceIds_.setRuntimeId(pRuntimeId);
    }
    else
    {
      //Set the Web service runtime to one that is supported by the selected type.
      String[] validRuntimes = WebServiceRuntimeExtensionUtils.getRuntimesByType(serviceIds_.getTypeId());
      if (validRuntimes != null && validRuntimes.length>0)
      {
        serviceIds_.setRuntimeId(validRuntimes[0]);
      }
    }
  }

  

  private void setDefaultServiceJ2EEVersionFromPreference()
  {
    if (serviceIds_ != null)
    {
      String runtimeId = serviceIds_.getRuntimeId();
      if (runtimeId != null)
      {
        //IWebServiceRuntime wsr = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceRuntimeById(runtimeId);
        WebServiceRuntimeInfo wsrt = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(runtimeId);
        if (wsrt != null)
        {
          String[] versions = wsrt.getJ2eeLevels();          
          //If the preferred J2EE version is in versions, pick the preferred one. 
          //Otherwise, pick the first one.
          if (versions != null && versions.length > 0)
          {
          	PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
          	String pJ2EE = context.getJ2EEVersion();
          	if (pJ2EE!=null && pJ2EE.length()>0)
          	{
              for (int i=0;i<versions.length;i++)
          	  {
          		  if (versions[i].equals(pJ2EE))
          		  {
          			  serviceJ2EEVersion_ = versions[i];
          		    return;
          		  }
          	  }
          	}
          	serviceJ2EEVersion_ = versions[0];
          	return;
          }
            
        }
      }
    }
  } 

  
  /**
   * This method needs a lot of work. It does not take into account the component types supported by the
   * selected Web service type or the fact that there are multiple components in a project.
   */
  //rskrem93309
  /*
  private void setDefaultProjectsNew()
  {
    if (initialProject_ != null)
    {
      getServiceProject2EARProject().getList().setSelectionValue(initialProject_.getName());
      String moduleName = null;
      if (initialComponentName_!=null && initialComponentName_.length()>0)
      {
        moduleName = initialComponentName_;
      }
      else
      {
        moduleName = J2EEUtils.getFirstWebModuleName(initialProject_);
      }
      
      serviceComponentName_ = moduleName;
      String version = String.valueOf(J2EEUtils.getJ2EEVersion(initialProject_, moduleName));
      String[] validVersions = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(serviceIds_.getRuntimeId()).getJ2eeLevels();
      for (int i=0; i< validVersions.length; i++)
      {
        if (validVersions[i].equals(version))
        {
          serviceJ2EEVersion_ = validVersions[i];
        }
      }
    }
    else
    {
      //Pick the first one
      IProject[] projects = WebServiceRuntimeExtensionUtils.getAllProjects();
      if (projects.length>0)
      {
        getServiceProject2EARProject().getList().setSelectionValue(projects[0].getName());
        String moduleName = J2EEUtils.getFirstWebModuleName(projects[0]);
        serviceComponentName_ = moduleName;
        String version = String.valueOf(J2EEUtils.getJ2EEVersion(projects[0], moduleName));
        String[] validVersions = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(serviceIds_.getRuntimeId()).getJ2eeLevels();
        for (int i=0; i< validVersions.length; i++)
        {
          if (validVersions[i].equals(version))
          {
            serviceJ2EEVersion_ = validVersions[i];
          }
        }        
        
      }
      else
      {
        //there are no projects in the workspace. Pass the default names for new projects.
        
        if (serviceComponentType_.equals(IModuleConstants.JST_EJB_MODULE))
        {
          getServiceProject2EARProject().getList().setSelectionValue(ResourceUtils.getDefaultEJBProjectName());
          serviceComponentName_ = ResourceUtils.getDefaultEJBComponentName();
        }
        else
        {
          getServiceProject2EARProject().getList().setSelectionValue(ResourceUtils.getDefaultWebProjectName());
          serviceComponentName_ = ResourceUtils.getDefaultWebComponentName();
        }
      }
      
      
    }
  }
  */
  //

  private void setDefaultProjects()
  {
	
	//Handle the case where no valid initial selection exists
    if (initialProject_== null || (initialProject_!=null && webServiceRuntimeJ2EEType==null))
    {
      //Select the first existing project that is valid.
      setServiceProjectToFirstValid();
      String serviceProjectName = getServiceProject2EARProject().getList().getSelection();
     
      //Select a client project with "client" added to the above project name.
      updateClientProject(serviceProjectName, serviceComponentName_, serviceIds_.getTypeId());
      //String clientProjectName = ResourceUtils.getClientWebProjectName(serviceProjectName, serviceIds_.getTypeId() );
      //getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientProjectName);
      
      return;
    }    
	    

	//Set the service project selection to this initialProject
	getServiceProject2EARProject().getList().setSelectionValue(initialProject_.getName());
  serviceComponentName_ = initialComponentName_;
  updateClientProject(initialProject_.getName(), serviceComponentName_, serviceIds_.getTypeId());
	//String clientProjectName = ResourceUtils.getClientWebProjectName(initialProject_.getName(), serviceIds_.getTypeId() ); 
	//Set the client project selection to clientProject
	//getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientProjectName);
		      	    
  }
  

  private void setServiceProjectToFirstValid()
  {
    //WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
    String[] projectNames = getServiceProject2EARProject().getList().getList();
    for (int i=0;i<projectNames.length; i++)
    {
      IProject project = ProjectUtilities.getProject(projectNames[i]);
      IVirtualComponent[] vcs = J2EEUtils.getComponentsByType(project, serviceComponentType_);
      if (project.isOpen() && vcs!=null && vcs.length>0)
      {
        //Get the runtime target of the project
        IRuntime runtimeTarget = ServerSelectionUtils.getRuntimeTarget(project.getName());
        String runtimeTargetId = null;
        if (runtimeTarget != null) 
          runtimeTargetId = runtimeTarget.getRuntimeType().getId();
        
        for (int j=0; j < vcs.length; j++)
        {  
          //Get the J2EE level
          int versionId = J2EEUtils.getJ2EEVersion(vcs[j]);
          String versionString = String.valueOf(versionId);
        
        
          if (serviceJ2EEVersion_!=null && serviceJ2EEVersion_.length()>0 && serviceJ2EEVersion_.equals(versionString))
          {
            if (WebServiceRuntimeExtensionUtils.doesRuntimeSupportJ2EELevel(versionString, serviceIds_.getRuntimeId()) &&
               ((runtimeTarget == null) || 
               ((runtimeTarget!=null) && WebServiceRuntimeExtensionUtils.doesRuntimeSupportServerTarget(runtimeTargetId, serviceIds_.getRuntimeId()))) 
               )
            {
              getServiceProject2EARProject().getList().setSelectionValue(projectNames[i]);
              serviceComponentName_ = vcs[j].getName();
              
              return;
            }
          }
        }
      }
    }
    
    //Didn't find a single project that worked. Default to a new project name.    
    //getServiceProject2EARProject().getList().setSelectionValue(ResourceUtils.getDefaultWebProjectName());
    //serviceComponentName_ = ResourceUtils.getDefaultWebProjectName();
    if (serviceComponentType_.equals(IModuleConstants.JST_EJB_MODULE))
    {
      getServiceProject2EARProject().getList().setSelectionValue(ResourceUtils.getDefaultEJBProjectName());
      serviceComponentName_ = ResourceUtils.getDefaultEJBComponentName();
    }
    else
    {
      getServiceProject2EARProject().getList().setSelectionValue(ResourceUtils.getDefaultWebProjectName());
      serviceComponentName_ = ResourceUtils.getDefaultWebComponentName();
    }    
  }


  private void setDefaultEARs()
  { 
    //Service side
    String initialProjectName = getServiceProject2EARProject().getList().getSelection();
    IProject initialProject =   FileResourceUtils.getWorkspaceRoot().getProject(initialProjectName);
    boolean earIsSet = false;
    if (initialProject != null && initialProject.exists())
    {
      IVirtualComponent[] earComps = J2EEUtils.getReferencingEARComponents(initialProject, serviceComponentName_);
      if (earComps.length > 0)
      {
        // Pick the first one
        IVirtualComponent earComp = earComps[0];
        String earProjectName = earComp.getProject().getName();
        String earComponentName = earComp.getName();
        getServiceProject2EARProject().getChoice().getList().setSelectionValue(earProjectName);
        serviceEarComponentName_ = earComponentName;
        earIsSet = true;
      }
    }
     
    if (!earIsSet)
    {
      // Either the component is not associated with any EARs or the project
      // does not exist,
      // so pick the first EAR yousee with the correct J2EE version.
      IVirtualComponent[] allEarComps = J2EEUtils.getAllEARComponents();
      if (allEarComps.length > 0)
      {
        for (int i = 0; i < allEarComps.length; i++)
        {
          IRuntime runtime = ServerSelectionUtils.getRuntimeTarget(allEarComps[i].getProject().getName());
          if (runtime != null && serviceIds_.getRuntimeId() != null && WebServiceRuntimeExtensionUtils.doesRuntimeSupportServerTarget(runtime.getRuntimeType().getId(), serviceIds_.getRuntimeId()) && serviceJ2EEVersion_.equals(String.valueOf(J2EEUtils.getJ2EEVersion(allEarComps[i]))))
          {
            String earProjectName = allEarComps[i].getProject().getName();
            getServiceProject2EARProject().getChoice().getList().setSelectionValue(earProjectName);
            serviceEarComponentName_ = allEarComps[i].getName();
            earIsSet = true;
          }

        }
      }
    }
      
    if (!earIsSet)
    {
        // there are no suitable existing EARs
        getServiceProject2EARProject().getChoice().getList().setSelectionValue(ResourceUtils.getDefaultServiceEARProjectName());
        serviceEarComponentName_ = ResourceUtils.getDefaultServiceEARComponentName();
    }
  
    
    
    //Client side    
    String initialClientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection(); 
    IProject initialClientProject = ProjectUtilities.getProject(initialClientProjectName);
    String[] clientEARInfo = getDefaultEARFromClientProject(initialClientProject, getClientComponentName());

    //If the client project exists and the default EAR is the same as that of the service project, 
    //pick the defaultClientEAR
    if(initialClientProject!=null && initialClientProject.exists() 
       && clientEARInfo[1].equalsIgnoreCase(serviceEarComponentName_))
    {
      getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(clientEARInfo[0]);
      setClientEARComponentName(clientEARInfo[1]);
    }
    else
    { 
      ProjectTopologyContext ptc= WebServicePlugin.getInstance().getProjectTopologyContext();
      String serviceEARName = getServiceProject2EARProject().getChoice().getList().getSelection();
      if (!ptc.isUseTwoEARs()) {        
        getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(serviceEARName);
        setClientEARComponentName(serviceEarComponentName_);
      }
      else {
        //I am here
        IProject proxyEARProject = getUniqueClientEAR(clientEARInfo[0], serviceEARName, initialClientProjectName);
        getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(proxyEARProject.getName());
        setClientEARComponentName(proxyEARProject.getName());
      }     
    }    
  
  }

  
/*  
    private void setDefaultEARs() { 
      // Service-side 
      String initialProjectName = getServiceProject2EARProject().getList().getSelection(); 
      IProject initialProject = FileResourceUtils.getWorkspaceRoot().getProject(initialProjectName);
    IProject defaultServiceEAR = getDefaultEARFromServiceProject(initialProject);
    getServiceProject2EARProject().getChoice().getList().setSelectionValue(defaultServiceEAR.getName());
    // Client-side 
    String initialClientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection();
   IProject initialClientProject = (IProject)((new StringToIProjectTransformer()).transform(initialClientProjectName));
    IProject defaultClientEAR = getDefaultEARFromClientProject(initialClientProject); // If the client project exists and the default EAR is the same as that of the service
    project, // pick the defaultClientEAR 
    if(initialClientProject!=null && initialClientProject.exists() && defaultClientEAR.getName().equalsIgnoreCase(defaultServiceEAR.getName())) {
    getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(defaultClientEAR.getName()); }
    else { ProjectTopologyContext ptc=
    WebServicePlugin.getInstance().getProjectTopologyContext(); if
    (!ptc.isUseTwoEARs()) {
    getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(defaultServiceEAR.getName()); }
    else { IProject proxyEARProject =
    getUniqueClientEAR(defaultClientEAR.getName(), defaultServiceEAR.getName(),
    initialClientProjectName);
    getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(proxyEARProject.getName()); } } }
  */ 
  
  private IProject getUniqueClientEAR(String earProject, String serviceProject, String clientProjectName) {

    String projectName = new String();
    if (!earProject.equalsIgnoreCase(serviceProject)) {
      projectName = earProject;
    }
    else {
      projectName = clientProjectName+DEFAULT_CLIENT_EAR_PROJECT_EXT;
      int i=1;      
      while (projectName.equalsIgnoreCase(serviceProject)) {
        projectName = projectName+i;
        i++;
      }
    }
    return projectName.equals("") ? null : ResourceUtils.getWorkspaceRoot().getProject(projectName);
    
  }
  
  /*
  private void setDefaultServer()
  {
    String initialProjectName = getServiceProject2EARProject().getList().getSelection();
	  IRuntime runtimeTarget = ServerSelectionUtils.getRuntimeTarget(initialProjectName);
    String runtimeTargetId = null;
    if (runtimeTarget != null)
    {
      runtimeTargetId = runtimeTarget.getRuntimeType().getId();
      //Pick a compatible existing server if one exists.
      IServer[] servers = ServerSelectionUtils.getCompatibleExistingServers(runtimeTarget);
      if (servers!=null && servers.length>0)
      {
        for (int i=0; i<servers.length; i++)
        {
          String thisFactoryId = servers[0].getServerType().getId();
          //if (WebServiceRuntimeExtensionUtils.doesRuntimeSupportServer(serviceIds_.getRuntimeId(), thisFactoryId))
          //{
            //Pick this server and return.
            serviceIds_.setServerId(thisFactoryId);
            serviceIds_.setServerInstanceId(servers[0].getId());
            return;
          //}			  
        }
      }
      
      //No compatible existing server, set the factory id to something the runtime supports
      String[] factoryIds = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(serviceIds_.getRuntimeId()).getServerFactoryIds();
      if (factoryIds!=null && factoryIds.length>0)
      {
        for (int i=0; i<factoryIds.length; i++)
        {
          IServerType serverType = ServerCore.findServerType(factoryIds[i]);
          if (serverType != null)
          {
            String serverRuntimeTypeId = serverType.getRuntimeType().getId();            
            if (serverRuntimeTypeId.equals(runtimeTargetId))
            {
              //Found a match
              serviceIds_.setServerId(factoryIds[i]);
              return;
            }
          }
        }        
      }
      else
      {
        //Runtime does not specify any server factory ids
        IServerType[] serverTypes = ServerCore.getServerTypes();
        serviceIds_.setServerId(serverTypes[0].getId());
      }
      
    }
    else
    {
      // The project has no server target so pick a server factory id that is supported by the runtime
      String[] fids = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(serviceIds_.getRuntimeId()).getServerFactoryIds();
      if (fids!=null && fids.length>0)
      {
        serviceIds_.setServerId(fids[0]);  
      }
      else
      {
        //Runtime does not specify any server factory ids
        IServerType[] serverTypes = ServerCore.getServerTypes();
        serviceIds_.setServerId(serverTypes[0].getId());        
      }
            
    }        
  }
  */

  private Status setDefaultServer()
  {
	Status status = new SimpleStatus("");
    //Calculate reasonable default server based on the default project selection. 

    String initialProjectName = getServiceProject2EARProject().getList().getSelection(); 
    IProject initialProject = ProjectUtilities.getProject(initialProjectName);
    if (initialProject.exists())
    {
      String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialProject, serviceComponentName_, serviceIds_.getRuntimeId(), true);
      if (serverInfo!=null)
      {
        if (serverInfo[0]!=null && serverInfo[0].length()>0)
        {
          serviceIds_.setServerId(serverInfo[0]);
        }
        if (serverInfo[1]!=null && serverInfo[1].length()>0)
        {
          serviceIds_.setServerInstanceId(serverInfo[1]);
        }        
      }      
    }
    else //the project does not exist.
    {
      //Does the EAR exist?
      String initialEARProjectName = getServiceProject2EARProject().getChoice().getList().getSelection();
      IProject initialEARProject = ProjectUtilities.getProject(initialEARProjectName);
      if (initialEARProject.exists())
      {
        String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialEARProject, serviceEarComponentName_, serviceIds_.getRuntimeId(), false);
        if (serverInfo!=null)
        {
          if (serverInfo[0]!=null && serverInfo[0].length()>0)
          {
            serviceIds_.setServerId(serverInfo[0]);
          }
          if (serverInfo[1]!=null && serverInfo[1].length()>0)
          {
            serviceIds_.setServerInstanceId(serverInfo[1]);
          }        
        }              
      }
      else
      {
        //We have a new project and EAR. Use the runtime/server selection 
        //preferences to set the default server/server type.
        //Pick a server type that is compatible with the Web service runtime
        //If there is an existing server that works, pick that one.
        
        String[] serverInfo = ServerSelectionUtils.getServerFromWebServceRuntimeAndJ2EE(serviceIds_.getRuntimeId(), serviceJ2EEVersion_);
        if (serverInfo!=null)
        {
          if (serverInfo[0]!=null && serverInfo[0].length()>0)
          {
            serviceIds_.setServerId(serverInfo[0]);
          }
          if (serverInfo[1]!=null && serverInfo[1].length()>0)
          {
            serviceIds_.setServerInstanceId(serverInfo[1]);
          }        
        }
        else
        {
        	//Since the project and the EAR are both new, try changing the J2EE level
        	boolean foundServer = false;
        	WebServiceRuntimeInfo wsrt = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(serviceIds_.getRuntimeId());
            if (wsrt != null)
            {
              String[] versions = wsrt.getJ2eeLevels();
              if (versions != null && versions.length > 0)
              {
            	  for (int k=0; k<versions.length; k++)
            	  {
            		  //If this J2EE version is different from the current one, see if there is
            		  //a server available.
            		  if (serviceJ2EEVersion_!=versions[k])
            		  {
            			  String[] si = ServerSelectionUtils.getServerFromWebServceRuntimeAndJ2EE(serviceIds_.getRuntimeId(), versions[k]);
             		      if (si!=null)
            		      {
            		        if (si[0]!=null && si[0].length()>0)
            		        {
            		          serviceIds_.setServerId(si[0]);
            		        }
            		        if (si[1]!=null && si[1].length()>0)
            		        {
            		          serviceIds_.setServerInstanceId(si[1]);
            		        }             
            		        serviceJ2EEVersion_ = versions[k];
            		        foundServer = true;
            		        break;
            		      }
            		  
            	      }
                  }
               }
            }
        	//No valid server runtimes appear to be configured, this is an error condition.
            if (!foundServer)
            {
              String runtimeLabel = WebServiceRuntimeExtensionUtils.getRuntimeLabelById(serviceIds_.getRuntimeId());
              String serverLabels = getServerLabels(serviceIds_.getRuntimeId());            	
        	  status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_NO_SERVER_RUNTIME", new String[]{runtimeLabel, serverLabels}),Status.ERROR);
            }
        	
        }
      }
      
    }
    
    return status;
  }

  
  private void updateServiceEARs()
  {
  	//Set EAR selection to null if the project/server defaults imply an EAR should not be created
  	String serviceProjectName = getServiceProject2EARProject().getList().getSelection();
  	IProject serviceProject = FileResourceUtils.getWorkspaceRoot().getProject(serviceProjectName);
  	if (serviceProject != null && serviceProject.exists())
  	{
  	  //Get the runtime target on the serviceProject
  	  IRuntime serviceTarget = ServerSelectionUtils.getRuntimeTarget(serviceProjectName);
  	  String j2eeVersion = String.valueOf(J2EEUtils.getJ2EEVersion(serviceProject, serviceComponentName_));
  	  if (serviceTarget != null)
  	  {
  	  	if (!ServerUtils.isTargetValidForEAR(serviceTarget.getRuntimeType().getId(),j2eeVersion))
  	  	{
  	      //Default the EAR selection to be empty
  	  	  getServiceProject2EARProject().getChoice().getList().setIndex(-1);
          serviceEarComponentName_ = "";
  	  	  serviceNeedEAR_ = false;
  	  	}
  	  		
  	  }
  	}
  	else
  	{
      String serverId = serviceIds_.getServerId();
      if (serverId != null)
      {
  		//Use the server type
  		String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverId);
  		if (serverTargetId!=null && serverTargetId.length()>0)
  		{
  		  if (!ServerUtils.isTargetValidForEAR(serverTargetId,serviceJ2EEVersion_))
  	  	  {
  	        //Default the EAR selection to be empty
  	  	    getServiceProject2EARProject().getChoice().getList().setIndex(-1);
            serviceEarComponentName_ = "";
  	  	    serviceNeedEAR_ = false;
  	  	  }
  		}
      }
  	}
  	
  		
  }
  public void setInitialSelection(IStructuredSelection selection)
  {
    initialSelection_ = selection;
  }
  
  public void setInitialProject(IProject initialProject)
  {
    initialProject_ = initialProject;
  }
  
  public void setInitialComponentName(String name)
  {
    initialComponentName_ = name;  
  }
  
  public void setServiceTypeRuntimeServer( TypeRuntimeServer ids )
  {
    serviceIds_ = ids;
  }
  
  public TypeRuntimeServer getServiceTypeRuntimeServer()
  {
    return serviceIds_; 
  }

  public SelectionListChoices getServiceProject2EARProject()
  {
    if (serviceProject2EARProject_ == null)
    {
	  // rskreg
      //IProject[] projects = WebServiceServerRuntimeTypeRegistry.getInstance().getProjectsByWebServiceType(serviceIds_.getTypeId());
      //Populate the list with all the projects in the workspace except Servers and ones that start with "."
	  // rskreg
	  String[] projectNames = getAllFlexibleProjects();
	  SelectionList list = new SelectionList(projectNames, 0);
	  Vector choices = new Vector();
	  for (int i = 0; i < projectNames.length; i++) {
      IProject project = ProjectUtilities.getProject(projectNames[i]);
	    choices.add(getProjectEARChoice(project));
	  }
	  serviceProject2EARProject_ = new SelectionListChoices(list, choices, getEARProjects());
    }
    return serviceProject2EARProject_; 
  }
    
  /**
   * @return Returns the generateProxy_.
   */
  public boolean getGenerateProxy()
  {
    return generateProxy_;
  }
  /**
   * @param generateProxy_ The generateProxy_ to set.
   */
  public void setGenerateProxy(boolean generateProxy_)
  {
    this.generateProxy_ = generateProxy_;
  }

  public String getServiceJ2EEVersion()
  {
    return serviceJ2EEVersion_;
  }
  
  public String getServiceProjectName()
  {
    return getServiceProject2EARProject().getList().getSelection();  
  }
  
  public String getServiceEarProjectName()
  {
    return getServiceProject2EARProject().getChoice().getList().getSelection();
  }
  
  public String getServiceComponentName()
  {
    return serviceComponentName_;
  }
  
  public String getServiceEarComponentName()
  {
    return serviceEarComponentName_;
  }
  
  public String getServiceComponentType()
  {
    return serviceComponentType_;
  }
  
  public boolean getServiceNeedEAR()
  {
    return serviceNeedEAR_;
  }
  
  /**
   * Returns the first EAR for a given project
   * @param project
   * @return
   */
  /*
  private IProject getDefaultEARFromServiceProject(IProject project)
  {
    if (project!=null && project.exists()) 
    {
      IServer[] configuredServers = ServerUtil.getServersByModule(ResourceUtils.getModule(project), null);
      IServer firstSupportedServer = ServerSelectionUtils.getFirstSupportedServer(configuredServers, serviceIds_.getTypeId() );
      
      EARNatureRuntime[] earProjects = J2EEUtils.getEARProjects(project, firstSupportedServer);
      if (earProjects!=null && earProjects[0] instanceof EARNatureRuntime) 
        return earProjects[0].getProject();
    }
    
    int versionId = -1;
    if (serviceJ2EEVersion_ != null && serviceJ2EEVersion_.length()>0)
    {
      versionId = Integer.parseInt(serviceJ2EEVersion_);
    }
    EARNatureRuntime newEAR = J2EEUtils.getEAR(versionId);
    IProject earProject = ResourceUtils.getWorkspaceRoot().getProject(ResourceUtils.getDefaultServiceEARProjectName());
      
    if (newEAR!=null)
      earProject = newEAR.getProject();

    return earProject;  	
  }
  */
  
}