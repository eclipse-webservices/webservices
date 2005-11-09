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

import java.util.Set;
import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.ServerSelectionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ClientRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceType;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeInfo;
import org.eclipse.jst.ws.internal.context.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class ServerRuntimeSelectionWidgetDefaultingCommand extends ClientRuntimeSelectionWidgetDefaultingCommand
{
  private String DEFAULT_CLIENT_EAR_PROJECT_EXT = "EAR";	
  private boolean           generateProxy_;
  
  // webServiceRuntimeJ2EEType contains the default Web service runtime and J2EE level based on the initial selection.
  // If the initialSeleciton does not result in a valid Web service runtime and J2EE level, webServiceRuntimeJ2EEType
  // should remain null for the life of this instance.
  private WSRuntimeJ2EEType webServiceRuntimeJ2EEType = null;
  
  private TypeRuntimeServer serviceIds_;
  private String serviceRuntimeId_;
  private String serviceProjectName_;
  private String serviceEarProjectName_;
  private String serviceComponentType_;
  private IProject initialProject_;
  private String initialComponentName_;
  //private String serviceJ2EEVersion_;
  private boolean serviceNeedEAR_ = true;
  private FacetMatcher serviceFacetMatcher_;

  public ServerRuntimeSelectionWidgetDefaultingCommand()
  {
    super();
  }
  
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    
    try
    {
     IStatus clientSideStatus = super.execute(monitor, null);
     if (clientSideStatus.getSeverity()==Status.ERROR)
     {
       return clientSideStatus;
     }
     IStatus status = Status.OK_STATUS;
     
     // Set the runtime based on the initial selection
     serviceRuntimeId_ = getDefaultServiceRuntime(initialProject_);
     if (serviceRuntimeId_ == null)
     {
       // return and error.
     }
     serviceIds_.setRuntimeId(WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId_).getRuntime()
         .getId());

     // Set the project
     if (serviceProjectName_ == null)
     {
       // Project name did not get set when the runtime was set, so set it now
       serviceProjectName_ = getDefaultServiceProjectName();
     }

     IProject serviceProject = ProjectUtilities.getProject(serviceProjectName_);
     if (!serviceProject.exists())
     {
       // Set the project template
       serviceComponentType_ = getDefaultServiceProjectTemplate();
     }
     else
     {
       //Set it to an empty String
       serviceComponentType_ = "";
     }

     updateClientProject(serviceProjectName_, serviceIds_.getTypeId());

     // Set the server
     IStatus serverStatus = setServiceDefaultServer();
     if (serverStatus.getSeverity() == Status.ERROR)
     {
       env.getStatusHandler().reportError(serverStatus);
       return serverStatus;
     }
     
     setDefaultServiceEarProject();
          
     return Status.OK_STATUS;
     
    } catch (Exception e)
    {
      // Catch all Exceptions in order to give some feedback to the user
      IStatus errorStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED,
          new String[] { e.getMessage() }), e);
      env.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
  }

  private void setDefaultServiceEarProject()
  {
    
    //Determine if an ear selection is needed based on the server type.
    boolean serviceNeedEAR_ = true;
    String serverId = serviceIds_.getServerId();
    if (serverId != null)
    {
        //Use the server type
        String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverId);
        if (serverTargetId!=null && serverTargetId.length()>0)
        {
          if (!ServerUtils.isTargetValidForEAR(serverTargetId,"13"))
          {
            //Default the EAR selection to be empty
            serviceNeedEAR_ = false;
          }
        }
    }    
    
    if (serviceNeedEAR_)
    {
      serviceEarProjectName_ = getDefaultServiceEarProjectName();

      
      //Client side
      if (getClientNeedEAR())
      {
        String defaultClientEarProjectName = getDefaultClientEarProjectName();
        IProject clientProject = ProjectUtilities.getProject(getClientProjectName());
        if(clientProject!=null && clientProject.exists() 
          && defaultClientEarProjectName.equalsIgnoreCase(serviceEarProjectName_))
        {
          setClientEarProjectName(defaultClientEarProjectName);
        }
        else
        { 
          ProjectTopologyContext ptc= WebServicePlugin.getInstance().getProjectTopologyContext();         
          if (!ptc.isUseTwoEARs()) 
          {
            setClientEarProjectName(serviceEarProjectName_);
          }
          else 
          {
            IProject proxyEARProject = getUniqueClientEAR(defaultClientEarProjectName, serviceEarProjectName_, getClientProjectName());
            setClientEarProjectName(proxyEARProject.getName());
          }     
        }
      }
    }
    else
    {
      serviceEarProjectName_ = "";
    }   
  }

  private String getDefaultServiceEarProjectName()
  {
    //Choose an appropriate default.
    
    IProject serviceProject = ProjectUtilities.getProject(serviceProjectName_);
    if (serviceProject != null && serviceProject.exists())
    {
      IVirtualComponent[] earComps = J2EEUtils.getReferencingEARComponents(serviceProject);
      if (earComps.length > 0)
      {
        //pick the first one.
        return earComps[0].getName();

      }
    }
    

    IVirtualComponent[] allEarComps = J2EEUtils.getAllEARComponents();
      
    //TODO Choose an existing EAR that can be added to the server and who's J2EE level in consistent with 
    //that of the selected project, if applicable. Picking the first one for now.
    if (allEarComps.length > 0)
    {
      return allEarComps[0].getName();
    }


    
    // there are no suitable existing EARs
    return ResourceUtils.getDefaultServiceEARProjectName();
    
  }
  
  private IStatus setServiceDefaultServer()
  {
    //Choose an existing server the module is already associated with if possible
    IProject serviceProject = ProjectUtilities.getProject(serviceProjectName_);
    IServer[] configuredServers = ServerUtil.getServersByModule(ServerUtils.getModule(serviceProject), null);
    if (configuredServers!=null && configuredServers.length>0)
    {
      serviceIds_.setServerId(configuredServers[0].getServerType().getId());
      serviceIds_.setServerInstanceId(configuredServers[0].getId());
      return Status.OK_STATUS;              
    }
    
    //If the project exists, choose a suitable server or server type based on the existing project's runtime or facets
    if (serviceProject.exists())
    {
      IServer server = getServerFromProject(serviceProjectName_, serviceFacetMatcher_);
      if (server != null)
      {
        serviceIds_.setServerId(server.getServerType().getId());
        serviceIds_.setServerInstanceId(server.getId());
        return Status.OK_STATUS;
      }
      else
      {
        IServerType serverType = getServerTypeFromProject(serviceProjectName_, serviceFacetMatcher_);
        if (serverType != null)
        {
          serviceIds_.setServerId(serverType.getId());
          return Status.OK_STATUS;
        }
      }
    }
    
    //Haven't picked a server/server type on the basis of the project. Pick a server/server type
    //that is compatible with the serviceRuntimeId.
    IServer server = getServerFromServiceRuntimeId();
    if (server!=null)
    {
      serviceIds_.setServerId(server.getServerType().getId());
      serviceIds_.setServerInstanceId(server.getId());
      return Status.OK_STATUS;
    }
    
    IServerType serverType = getServerTypeFromServiceRuntimeId();
    if (serverType != null)
    {
      serviceIds_.setServerId(serverType.getId());
      return Status.OK_STATUS;
    }    

    //No suitable server was found. Popup an error.
    String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(serviceIds_.getRuntimeId());
    String serverLabels = getServerLabels(serviceIds_.getRuntimeId());    
    IStatus status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_ERROR_NO_SERVER_RUNTIME, new String[]{runtimeLabel, serverLabels}) );
    return status;
  }  
  
  private IServer getServerFromServiceRuntimeId()
  {
    IServer[] servers = ServerCore.getServers();
    if (servers != null && servers.length > 0) {
      for (int i = 0; i < servers.length; i++)
      {
        String serverFactoryId = servers[i].getServerType().getId();
        if (WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportServer(serviceRuntimeId_, serverFactoryId))
        {
          return servers[i];
        }
      }
    }
    return null;    
  }
  
  private IServerType getServerTypeFromServiceRuntimeId()
  {
    String[] serverTypes = WebServiceRuntimeExtensionUtils2.getServerFactoryIdsByServiceRuntime(serviceRuntimeId_);
    if (serverTypes!=null && serverTypes.length>0)
    {
      //Return the preferred one if it is in the list
      PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
      String preferredServerFactoryId = context.getServerFactoryId();
      for (int i=0; i<serverTypes.length; i++)
      {
        if (serverTypes[i].equals(preferredServerFactoryId))
        {
          return ServerCore.findServerType(serverTypes[i]);
        }
      }
      
      return ServerCore.findServerType(serverTypes[0]);
    }    
    
    return null;
  }  
  
  /*
  private IServer getFirstSupportedServer(IServer[] servers, String serviceRuntimeId, IProject serviceProject)
  {
    if (servers != null && servers.length > 0) {
      for (int i = 0; i < servers.length; i++)
      {
        String serverFactoryId = servers[i].getServerType().getId();
        if (WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportServer(serviceRuntimeId, serverFactoryId))
        {
          //TODO check if the server type supports the project before returning.
          return servers[i];
        }
      }
    }
    return null;
  }
  */
  
  private String getServerLabels(String serviceRuntimeId)
  {
        String[] validServerFactoryIds = WebServiceRuntimeExtensionUtils2.getServerFactoryIdsByServiceRuntime(serviceRuntimeId);
        //String[] validServerLabels = new String[validServerFactoryIds.length];
        StringBuffer validServerLabels = new StringBuffer(); 
        for (int i=0; i<validServerFactoryIds.length; i++)
        {
            if (i>0)
            {
                validServerLabels.append(", ");
            }
            validServerLabels.append(WebServiceRuntimeExtensionUtils.getServerLabelById(validServerFactoryIds[i]));
            
        }
        return validServerLabels.toString();
  }

  private String getDefaultServiceProjectTemplate()
  {
    String[] templates = WebServiceRuntimeExtensionUtils2.getServiceProjectTemplates(serviceIds_.getTypeId(), serviceIds_.getRuntimeId());
    RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId_).getRequiredFacetVersions();    
    
    //Pick the Web one if it's there, otherwise pick the first one.    
    for (int i=0; i<templates.length; i++)
    {
      String templateId = templates[i];
      if (templateId.indexOf("web") != -1)
      {
        //Calculate the facet matcher for the template so that we know 
        //what to create and what to add during module creation.
        
        Set facetVersions = FacetUtils.getInitialFacetVersionsFromTemplate(templateId);
        FacetMatcher fm = FacetUtils.match(rfv, facetVersions);
        if (fm.isMatch())
        {
          serviceFacetMatcher_ = fm;
          return templates[i];  
        }
        
      }                                    
    }
    
    //Didn't find a "web" type. Return the first one that is a match. Calculate the facet matcher for the template
    //so that we know what to create and what to add during module creation.
    for (int j = 0; j < templates.length; j++)
    {
      String templateId = templates[j];
      Set facetVersions = FacetUtils.getInitialFacetVersionsFromTemplate(templateId);
      FacetMatcher fm = FacetUtils.match(rfv, facetVersions);
      if (fm.isMatch())
      {
        serviceFacetMatcher_ = fm;
        return templates[j];  
      }      
    }
    
    //Still nothing, return the first one if available.
    if (templates.length > 0)
      return templates[0];
    
    return "";    
    
  }  
  
  private String getDefaultServiceProjectName()
  {
    IProject[] projects = FacetUtils.getAllProjects();
    ServiceRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId_);
    RequiredFacetVersion[] rfvs = desc.getRequiredFacetVersions();
    
    //Check each project for compatibility with the serviceRuntime
    for (int i=0; i<projects.length; i++)
    {
      try
      {
        IFacetedProject fproject = ProjectFacetsManager.create(projects[i]);
        if (fproject != null)
        {
          Set facetVersions = fproject.getProjectFacets();
          FacetMatcher fm = FacetUtils.match(rfvs, facetVersions);
          if (fm.isMatch())
          {
            serviceFacetMatcher_ = fm;
            return projects[i].getName();
          }            
        }
        else
        {
          //TODO Handle the plain-old Java projects            
        }
      } catch (CoreException ce)
      {
        
      }
    }
    
    //No project was suitable, return a new project name
    return ResourceUtils.getDefaultWebProjectName();
    
  }  
  
  private String getDefaultServiceRuntime(IProject project)
  {

    String[] serviceRuntimes = WebServiceRuntimeExtensionUtils2.getServiceRuntimesByServiceType(serviceIds_.getTypeId());
    
    //Check if the preferred Web service runtime works with the initially selected project.
    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
    String runtimeId = context.getRuntimeId();
    String preferredServiceRuntimeId = null;
    for (int k=0; k<serviceRuntimes.length; k++ )
    {
      ServiceRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimes[k]);
      if (desc.getRuntime().getId().equals(runtimeId))
      {
        preferredServiceRuntimeId = desc.getId();
        break;
      }
    }    
    
    if (preferredServiceRuntimeId != null)
    {
      if (project != null && project.exists())
      {
        RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(preferredServiceRuntimeId).getRequiredFacetVersions();
        try
        {
          IFacetedProject fproject = ProjectFacetsManager.create(project);
          if (fproject != null)
          {
            Set facetVersions = fproject.getProjectFacets();
            FacetMatcher fm = FacetUtils.match(rfv, facetVersions);
            if (fm.isMatch())
            {
              serviceFacetMatcher_ = fm;
              serviceProjectName_ = project.getName();
              return preferredServiceRuntimeId;
            }            
          }
          else
          {
            //TODO Handle the plain-old Java projects            
          }
        } catch (CoreException ce)
        {
          
        }        
      }
    }
    
    
    //Either there was no initially selected project or the preferred
    //runtime did not work with the initially selected project.
    //If possible, pick a Web service runtime that works with the initially selected project.
    //If the initially selected project does not work with any of the Web service runtimes, pick the 
    //preferred Web service runtime.
    

    if (project != null && project.exists())
    {
      for (int i=0; i<serviceRuntimes.length; i++)
      {
        RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimes[i]).getRequiredFacetVersions();
        try
        {
          IFacetedProject fproject = ProjectFacetsManager.create(project);
          if (fproject != null)
          {
            Set facetVersions = fproject.getProjectFacets();
            FacetMatcher fm = FacetUtils.match(rfv, facetVersions);
            if (fm.isMatch())
            {
              serviceFacetMatcher_ = fm;
              serviceProjectName_ = project.getName();
              return serviceRuntimes[i];
            }            
          }
          else
          {
            //TODO Handle the plain-old Java projects            
          }
        } catch (CoreException ce)
        {
          
        }
      }
    }
    
    //Haven't returned yet so this means that the intitially selected project cannot be used
    //to influence the selection of the runtime. Pick the preferred Web service runtime.
    if (preferredServiceRuntimeId != null)
    {
      return preferredServiceRuntimeId;
    }
    
    if (serviceRuntimes.length > 0)
      return WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimes[0]).getId();
    else
      return null;
  }  
  /*
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    
    try
    {
     IStatus clientSideStatus = super.execute(monitor, null);
     if (clientSideStatus.getSeverity()==Status.ERROR)
     {
       return clientSideStatus;
     }
     IStatus status = Status.OK_STATUS;


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
     //setDefaultProjects();
     setDefaultEARs();
     IStatus serverStatus = setDefaultServer();
     if (serverStatus.getSeverity()== Status.ERROR)
     {
         env.getStatusHandler().reportError(serverStatus);
         return serverStatus;
     }
     updateServiceEARs();
     //updateClientProject(getServiceProject2EARProject().getList().getSelection(), serviceComponentName_, serviceIds_.getTypeId());
     //updateClientEARs();
	 	 
     return status;
    } catch (Exception e)
    {
      //Catch all Exceptions in order to give some feedback to the user
      IStatus errorStatus = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED",new String[]{e.getMessage()}), e);
      env.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
    
  }
  */

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
        isValidComponentType = J2EEUtils.isWebComponent(project) ||
                                     J2EEUtils.isEJBComponent(project);
      }
      if (isValidComponentType)
      {
        //WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
        
        //Get the J2EE level
        int versionId = J2EEUtils.getJ2EEVersion(project);
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

  

  /*
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
  */
  
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

  /*
  private void setDefaultProjects()
  {
	
	//Handle the case where no valid initial selection exists
    if (initialProject_== null || (initialProject_!=null && webServiceRuntimeJ2EEType==null))
    {
      //Select the first existing project that is valid.
      setServiceProjectToFirstValid();
      String serviceProjectName = getServiceProject2EARProject().getList().getSelection();
     
      //Select a client project with "client" added to the above project name.
      //updateClientProject(serviceProjectName, serviceComponentName_, serviceIds_.getTypeId());
      //String clientProjectName = ResourceUtils.getClientWebProjectName(serviceProjectName, serviceIds_.getTypeId() );
      //getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientProjectName);
      
      return;
    }    
	    

	//Set the service project selection to this initialProject
	getServiceProject2EARProject().getList().setSelectionValue(initialProject_.getName());
  serviceComponentName_ = initialComponentName_;
  //updateClientProject(initialProject_.getName(), serviceComponentName_, serviceIds_.getTypeId());
	//String clientProjectName = ResourceUtils.getClientWebProjectName(initialProject_.getName(), serviceIds_.getTypeId() ); 
	//Set the client project selection to clientProject
	//getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientProjectName);
		      	    
  }
  */
  

  /*
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
              serviceProjectName_ = projectNames[i];              
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
      serviceProjectName_ = ResourceUtils.getDefaultEJBProjectName();
    }
    else
    {
      serviceProjectName_ = ResourceUtils.getDefaultWebProjectName();
    }    
  }
  */


  /*
  private void setDefaultEARs()
  { 
    //Service side
    String initialProjectName = serviceProjectName_;
    IProject initialProject =   FileResourceUtils.getWorkspaceRoot().getProject(initialProjectName);
    boolean earIsSet = false;
    if (initialProject != null && initialProject.exists())
    {
      IVirtualComponent[] earComps = J2EEUtils.getReferencingEARComponents(initialProject);
      if (earComps.length > 0)
      {
        // Pick the first one
        IVirtualComponent earComp = earComps[0];
        String earProjectName = earComp.getProject().getName();
        String earComponentName = earComp.getName();
        serviceEarProjectName_ = earProjectName;        
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
            serviceEarProjectName_ = earProjectName;
            earIsSet = true;
          }

        }
      }
    }
      
    if (!earIsSet)
    {
        // there are no suitable existing EARs
        serviceEarProjectName_ = ResourceUtils.getDefaultServiceEARProjectName();
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
  */
  
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
  /*
  private IStatus setDefaultServer()
  {
	  IStatus status = Status.OK_STATUS;
    //Calculate reasonable default server based on the default project selection. 

    String initialProjectName = serviceProjectName_; 
    IProject initialProject = ProjectUtilities.getProject(initialProjectName);
    if (initialProject.exists())
    {
      String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialProject, initialProject.getName(), serviceIds_.getRuntimeId(), true);
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
      String initialEARProjectName = serviceEarProjectName_;
      IProject initialEARProject = ProjectUtilities.getProject(initialEARProjectName);
      if (initialEARProject.exists())
      {
        String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialEARProject, serviceEarProjectName_, serviceIds_.getRuntimeId(), false);
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
        	    status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_NO_SERVER_RUNTIME", new String[]{runtimeLabel, serverLabels}) );
            }
        	
        }
      }
      
    }
    
    return status;
  }
  */
  
  /*
  private void updateServiceEARs()
  {
  	//Set EAR selection to null if the project/server defaults imply an EAR should not be created
  	IProject serviceProject = FileResourceUtils.getWorkspaceRoot().getProject(serviceProjectName_);
  	if (serviceProject != null && serviceProject.exists())
  	{
  	  //Get the runtime target on the serviceProject
  	  IRuntime serviceTarget = ServerSelectionUtils.getRuntimeTarget(serviceProjectName_);
  	  String j2eeVersion = String.valueOf(J2EEUtils.getJ2EEVersion(serviceProject));
  	  if (serviceTarget != null)
  	  {
  	  	if (!ServerUtils.isTargetValidForEAR(serviceTarget.getRuntimeType().getId(),j2eeVersion))
  	  	{
  	      //Default the EAR selection to be empty
  	  	  serviceEarProjectName_="";
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
  	  	    serviceEarProjectName_ = "";
  	  	    serviceNeedEAR_ = false;
  	  	  }
  		}
      }
  	}
  	
  		
  }
  */
  public void setInitialSelection(IStructuredSelection selection)
  {
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

  public String getServiceRuntimeId()
  {
    return serviceRuntimeId_;
  }
  /*
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
  */  
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
  
  public String getServiceProjectName()
  {
    return serviceProjectName_;  
  }
  
  public String getServiceEarProjectName()
  {
    return serviceEarProjectName_;
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