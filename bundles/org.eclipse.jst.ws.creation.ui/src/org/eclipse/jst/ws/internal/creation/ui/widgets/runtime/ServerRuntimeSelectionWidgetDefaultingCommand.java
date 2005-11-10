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
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.context.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class ServerRuntimeSelectionWidgetDefaultingCommand extends ClientRuntimeSelectionWidgetDefaultingCommand
{
  private String DEFAULT_CLIENT_EAR_PROJECT_EXT = "EAR";	
  private boolean           generateProxy_;
  
  private TypeRuntimeServer serviceIds_;
  private String serviceRuntimeId_;
  private String serviceProjectName_;
  private String serviceEarProjectName_;
  private String serviceComponentType_;
  private IProject initialProject_;
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
  
  public void setInitialSelection(IStructuredSelection selection)
  {
  }
  
  public void setInitialProject(IProject initialProject)
  {
    initialProject_ = initialProject;
  }
  
  public void setInitialComponentName(String name)
  {
    //TODO This method and any mappings to it
	// should be removed if no longer needed.
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
  
}