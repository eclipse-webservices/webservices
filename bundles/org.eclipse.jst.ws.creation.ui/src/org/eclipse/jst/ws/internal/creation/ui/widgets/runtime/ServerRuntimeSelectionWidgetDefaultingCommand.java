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
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner     
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060315   131963 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060418   129688 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427   126780 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20060523   133714 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.widgets.runtime;

import java.util.ArrayList;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.DefaultingUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.FacetMatchCache;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.RuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class ServerRuntimeSelectionWidgetDefaultingCommand extends ClientRuntimeSelectionWidgetDefaultingCommand
{	
  private boolean           generateProxy_=true; //jvh
  
  private TypeRuntimeServer serviceIds_;
  private boolean serviceIdsFixed_ = false;
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
  
  
  /**
   * Defaults the following bits of information in the following order:
   * serviceRuntimeId_ : the serviceRuntimeId. Must be defaulted to non-empty String.
   * serviceIds_.runtimeId_: the Web service runtime id. Must be defaulted to non-empty String.
   * serviceProjectName_ : the name of the service project. Must be non-empty. May or may not exist.
   * serviceComponentType_: the id of the service project template. Must be empty if the service
   *                        project exists. Must be non-empty of the service project does not exist.
   * serviceIds_.serverId_: the server type id. May be an empty String if the defaulted Web service runtime
   *                        does not require a server.
   * serviceIds_.serverInstanceId_: the server id. May be null or an empty String.
   * serviceNeedEAR_: true if an EAR is needed. False otherwise.
   * serviceEarProjectName_: the service EAR project. Must be empty if the serviceNeedEAR_ is false.
   *                         Must be non-empty if the serviceNeedEAR_ is true.
   * Defaults the parallel information on the client-side if needed 
   * (@see ClientRuntimeSelectionWidgetDefaultingCommand#execute).                                                 
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    
    try
    {    	
     
     //**Step 1** Default the serviceRuntime and the Web service runtime.
     //serviceIdsFixed_ is set to true for the Ant scenario. It's always false for the wizard
     //scenarios.    	
     if (serviceIdsFixed_)
     {
       //Set the serviceRuntime based on the runtime and server.
       //If user set a service project use that as initial project otherwise default from initial selection.
    	 if (serviceProjectName_ != null || !serviceProjectName_.equals("")) 
    	 {
    		 initialProject_ = ResourcesPlugin.getWorkspace().getRoot().getProject(serviceProjectName_);
    		 
    		 DefaultRuntimeTriplet drt = getDefaultRuntime(initialProject_, serviceIds_.getTypeId(), false);
             serviceFacetMatcher_ = drt.getFacetMatcher();
    	     serviceProjectName_ = drt.getProjectName();
    	     serviceRuntimeId_ = drt.getRuntimeId();       

    	       if (serviceRuntimeId_ != null)
    	       {
    	    	   serviceIds_.setRuntimeId(WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId_).getRuntime()
    	      	         .getId());
    	       }
    	 }
    	 else
    	 {
    		 DefaultRuntimeTriplet drt = getDefaultServiceRuntimeForFixedRuntimeAndServer(initialProject_);  
    	     serviceFacetMatcher_ = drt.getFacetMatcher();
    	     serviceProjectName_ = drt.getProjectName();
    	     serviceRuntimeId_ = drt.getRuntimeId();	 
    	 }   
     }
     else
     {
       // Set the runtime based on the initial selection
       DefaultRuntimeTriplet drt = getDefaultRuntime(initialProject_, serviceIds_.getTypeId(), false);
       serviceFacetMatcher_ = drt.getFacetMatcher();
       serviceProjectName_ = drt.getProjectName();
       serviceRuntimeId_ = drt.getRuntimeId();       

       if (serviceRuntimeId_ != null)
       {
    	   serviceIds_.setRuntimeId(WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId_).getRuntime()
    		         .getId()); 
       } 
     }
     
     //**Step 2** Default the service project if it was not already defaulted 
     //as part of defaulting the Web service runtime.
     if (serviceProjectName_ == null)
     {
       // Project name did not get set when the runtime was set, so set it now
       serviceProjectName_ = getDefaultServiceProjectName();
     }

     //**Step 3** Default the service project type.
     IProject serviceProject = ResourcesPlugin.getWorkspace().getRoot().getProject(serviceProjectName_); 
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
     //**Step 4** Default the server if this is not an Ant scenario.
     if (!serviceIdsFixed_)
     {
       // Set the server
       IStatus serverStatus = setServiceDefaultServer();
       if (serverStatus.getSeverity() == Status.ERROR)
       {
         env.getStatusHandler().reportError(serverStatus);
         return serverStatus;
       }
     }
     
     //**Step 5** Default clientNeedEAR and client EAR if an EAR is needed
     setDefaultServiceEarProject();
     
     // jvh - for now comment out generate proxy
     //   need to look at defaulting based on an event
     //    on page 1 - when client generation goes from none
     //    to develop...
     /*if (generateProxy_)
     {*/
    	 //Default the client-side.
    	 IStatus clientSideStatus = defaultClientSide(monitor);
         if (clientSideStatus.getSeverity() == Status.ERROR)
         {
           return clientSideStatus;
         }                  
    // }
          
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

  private IStatus defaultClientSide(IProgressMonitor monitor)
  {
    IStatus clientDefaultFromServiceStatus = defaultClientSideFromServiceSide(serviceProjectName_, serviceIds_, serviceNeedEAR_, serviceEarProjectName_);

    // If an error status is returned, the only property that was set on 
    // ClientRuntimeSelectionWidgetDefaultingCommand is clientProjectName_. 
    // The rest of the properties including clientRuntimeId_, clientComponentType_, 
    // all the values in clientIds_, clientNeedEAR_, and clientEarProjectName_ 
    // have not been set because the service side server and
    // web service runtime could not be used on the client side. Run the entire
    // client-side defaulting algorithm using clientProjectName_ as the 
    // clientInitialProject_. Then update the clientEarProjectName_ based on the
    // service side EAR.
    if (clientDefaultFromServiceStatus.getSeverity() == IStatus.ERROR)
    {
      // 1. Run client side defaulting from scratch with clientInitialProject_
      // set to the new clientProjectName_.
      IProject clientProject = ResourcesPlugin.getWorkspace().getRoot().getProject(getClientProjectName());
      setClientInitialProject(clientProject);
      IStatus clientExecuteStatus = super.execute(monitor, null);
      if (clientExecuteStatus.getSeverity() == Status.ERROR)
      {
        return clientExecuteStatus;
      }

      // 2. Update the client-side EAR if serviceNeedEAR_ is true;
      if (serviceNeedEAR_)
      {
        defaultClientEarFromServiceEar(serviceProjectName_, serviceEarProjectName_);
      }
    }
    
    return Status.OK_STATUS;
	  
  }
  
  private void setDefaultServiceEarProject()
  {
    //Don't need an ear if this is a Java project, or if the selected template is jst.utility
    IProject serviceProject = ResourcesPlugin.getWorkspace().getRoot().getProject(serviceProjectName_);
    if (serviceProject.exists())
    {
      serviceNeedEAR_ = !(FacetUtils.isJavaProject(serviceProject));
    }
    else
    {
      serviceNeedEAR_ = !(FacetUtils.isUtilityTemplate(serviceComponentType_));  
    }    
    
    //If serviceNeedEAR_ is still true, it means that we're not dealing with a Java project
    //or Java project type. Check the server.
    
    
    if (serviceNeedEAR_)
    {
      
      // Determine if an ear selection is needed based on the server type.      
      String serverId = serviceIds_.getServerId();
      if (serverId != null)
      {
        // Use the server type
        String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverId);
        if (serverTargetId != null && serverTargetId.length() > 0)
        {
          if (!ServerUtils.isTargetValidForEAR(serverTargetId, "13"))
          {
            // Default the EAR selection to be empty
            serviceNeedEAR_ = false;
          }
        }
      }
    }
    
    if (serviceNeedEAR_)
    {
      serviceEarProjectName_ = DefaultingUtils.getDefaultEARProjectName(serviceProjectName_);
    }
    else
    {
      serviceEarProjectName_ = "";
    }   
  }
  
  private IStatus setServiceDefaultServer()
  {
    //Choose an existing server the module is already associated with if possible
    IProject serviceProject = ResourcesPlugin.getWorkspace().getRoot().getProject(serviceProjectName_);
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
    RuntimeDescriptor runtimeDescriptor = WebServiceRuntimeExtensionUtils2.getRuntimeById(serviceIds_.getRuntimeId());
    if (runtimeDescriptor.getServerRequired())
    {    
      String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(serviceIds_.getRuntimeId());    
      IStatus status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_ERROR_NO_SERVER_RUNTIME, new String[]{runtimeLabel}) );
      return status;
    }
    
    return Status.OK_STATUS;
  }  
  
  private IServer getServerFromServiceRuntimeId()
  {
    IServer[] servers = ServerCore.getServers();
    if (servers != null && servers.length > 0) 
    {
      PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
      String preferredServerFactoryId = context.getServerFactoryId();

      //If a server of the preferred server type is present, check that one first
      for (int j = 0; j < servers.length; j++)
      {
        String serverFactoryId = servers[j].getServerType().getId();
        if (serverFactoryId == preferredServerFactoryId)
        {
          if (WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportServer(serviceRuntimeId_, serverFactoryId))
          {
            return servers[j];
          }
        }                
      }
      
      //A server of the preferred server type could not be found or did not match. Check all the existing servers.    
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
  
  
  private String getDefaultServiceProjectTemplate()
  {
    String[] templates = WebServiceRuntimeExtensionUtils2.getServiceProjectTemplates(serviceIds_.getTypeId(), serviceIds_.getRuntimeId());    
    
    //Walk the list of service project types in the project topology preference
    ProjectTopologyContext ptc= WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
    String[] preferredTemplateIds = ptc.getServiceTypes();
    for (int j = 0; j < preferredTemplateIds.length; j++)
    {
      for (int i = 0; i < templates.length; i++)
      {
        String templateId = templates[i];
        if (templateId.equals(preferredTemplateIds[j]))
        {
          boolean matches = WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportTemplate(serviceRuntimeId_, templateId);
          if (matches)
          {
            return templates[i];
          }
        }
      }
    }
    
    //Since the preferredTemplateIds contains the union of all project types for all service runtimes, we are
    //guaranteed to have returned by now, so the code below will never be executed under normal
    //circumstances. Just return something to satisfy the compiler.
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
      Set facetVersions = FacetUtils.getFacetsForProject(projects[i].getName());
      org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = null;
      String fRuntimeName = null;
      fRuntime = FacetUtils.getFacetRuntimeForProject(projects[i].getName());
      if (fRuntime != null)
      {
        fRuntimeName = fRuntime.getName();        
      }              
      
      if (facetVersions != null)
      {
        FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(false, serviceRuntimeId_, projects[i].getName());
        boolean facetRuntimeMatches = true;
        if (fRuntimeName != null)
        {
          facetRuntimeMatches = FacetUtils.isFacetRuntimeSupported(rfvs, fRuntimeName);  
        }
        
        if (fm.isMatch() && facetRuntimeMatches)
        {
          serviceFacetMatcher_ = fm;
          return projects[i].getName();
        }                    
      }
    }
    
    //No project was suitable, return a new project name
    return ResourceUtils.getDefaultWebProjectName();
    
  }  
  
  private DefaultRuntimeTriplet getDefaultServiceRuntimeForFixedRuntimeAndServer(IProject project)
  {
    String[] serviceRuntimes = WebServiceRuntimeExtensionUtils2.getServiceRuntimesByServiceType(serviceIds_.getTypeId());
    ArrayList validServiceRuntimes = new ArrayList();
    for (int i=0; i<serviceRuntimes.length; i++ )
    {
      ServiceRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimes[i]);
      if (desc.getRuntime().getId().equals(serviceIds_.getRuntimeId()))
      {
        //Check if this service runtime supports the server
        if (WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportServer(desc.getId(), serviceIds_.getServerId()))
        {
          validServiceRuntimes.add(desc.getId());
          if (project != null && project.exists())  
          {
            Set facetVersions = FacetUtils.getFacetsForProject(project.getName());
            if (facetVersions != null)
            {
              FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(false, serviceRuntimes[i], project.getName());
              if (fm.isMatch())
              {
                DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
                drt.setFacetMatcher(fm);
                drt.setProjectName(project.getName());
                drt.setRuntimeId(desc.getId());
                return drt;
              }                      
            }
          }
          
        }
      }
      
    }
    
    if (validServiceRuntimes.size() > 0)
    {
      //We couldn't match to the initially selected project so return the first valid runtime.
      DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
      drt.setFacetMatcher(null);
      drt.setProjectName(null);
      drt.setRuntimeId(((String[])validServiceRuntimes.toArray(new String[0]))[0]);
      return drt;      
    }
    else
    {
      //There are no service runtimes that match the fixed runtime and server. Fall back to original algorithm.
      serviceIdsFixed_ = false;
      return getDefaultRuntime(project, serviceIds_.getTypeId(), false);
    }
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
  
  public void setServiceIdsFixed(boolean b)
  {
    serviceIdsFixed_ = b;  
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
  
  // This is for the Ant scenario where the service project name can be set in the property file.
  // If the user has set the ServiceProjectName use it for defaulting purposes
  public void setServiceProjectName(String name)
  {
	  serviceProjectName_ = name;
  }
  
}
