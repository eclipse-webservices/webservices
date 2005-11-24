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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ClientRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.SimpleContext;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;
import org.eclipse.wst.ws.internal.wsrt.WebServiceState;

public class ClientRuntimeSelectionWidgetDefaultingCommand extends AbstractDataModelOperation
{   
  
  private TypeRuntimeServer    clientIds_;
  private String clientRuntimeId_;
  private String clientProjectName_;
  private String clientEarProjectName_;
  protected boolean clientNeedEAR_ = true;
  private String clientComponentType_;
  private FacetMatcher clientFacetMatcher_;
  
  private IContext          context_;
  private ISelection        selection_;
  private IWebServiceClient webServiceClient_;
  private ResourceContext   resourceContext_;
  private boolean           test_;
  
  //A note on initialSelections ...
  //The difference between clientInitialProject/Component_ and initialInitialSelection is that
  //clientInitialProject/Component_ comes from the ObjectSelectionOutputCommand while initialInitialSelection
  //is the actual thing that was selected before the wizard was launched. In the runtime/j2ee/project 
  //defaulting algorithm, clientInitialSelection will be given first priority. If, however, it is 
  //deemed that clientInitialProject is not a valid initial selection, initialInitialSelection
  //will be given second priority. Things that could make an initialSelection invalid include
  //1. The containing project contains the Web service for which we are trying to create a client
  //2. The containing project has a J2EE level, server target, and project type combination which
  //   is not supported by any of the registered Web service runtimes.
  private IProject clientInitialProject_;
  private String wsdlURI_;

  public ClientRuntimeSelectionWidgetDefaultingCommand()
  {
    super();
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer ids )
  {
    clientIds_ = ids;
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    return clientIds_; 
  }
  
  public String getClientRuntimeId()
  {
    return clientRuntimeId_;
  }
  
  public String getClientProjectName()
  {
    return clientProjectName_;
  }
  
  public String getClientEarProjectName()
  {
    return clientEarProjectName_;
  }
  
  public void setClientEarProjectName(String name)
  {
    clientEarProjectName_ = name;
  }
  
  public String getClientComponentType()
  {
    return  clientComponentType_;
  }
  
  public IWebServiceClient getWebService()
  {
    return webServiceClient_;  
  }
    
  public IContext getContext()
  {
    return context_;
  }  
  
  public ISelection getSelection()
  {
    return selection_;    
  }
  
  public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable)
  {

    IEnvironment env = getEnvironment();

    try
    {
      // Set the runtime based on the initial selection
      clientRuntimeId_ = getDefaultClientRuntime(clientInitialProject_);
      if (clientRuntimeId_ == null)
      {
        // return and error.
      }
      clientIds_.setRuntimeId(WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId_).getRuntime()
          .getId());

      // Set the project
      if (clientProjectName_ == null)
      {
        // Project name did not get set when the runtime was set, so set it now
        clientProjectName_ = getDefaultClientProjectName();
      }

      IProject clientProject = ProjectUtilities.getProject(clientProjectName_);
      if (!clientProject.exists())
      {
        // Set the project template
        clientComponentType_ = getDefaultClientProjectTemplate();
      }
      else
      {
        //Set it to an empty String
        clientComponentType_ = "";
      }


      // Set the server
      IStatus serverStatus = setClientDefaultServer();
      if (serverStatus.getSeverity() == Status.ERROR)
      {
        env.getStatusHandler().reportError(serverStatus);
        return serverStatus;
      }

      setDefaultClientEarProject();      
      // Calculate default IWebServiceClient
      setDefaultsForExtension(env);

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
  
  private void setDefaultClientEarProject()
  {    
    //Don't need an ear if this is a Java project, or if the selected template is jst.utility
    IProject clientProject = ProjectUtilities.getProject(clientProjectName_);
    if (clientProject.exists())
    {
      clientNeedEAR_ = !(FacetUtils.isJavaProject(clientProject));
    }
    else
    {
      clientNeedEAR_ = !(FacetUtils.isUtilityTemplate(clientComponentType_));  
    }
    
    //If clientNeedEAR_ is still true, it means that we're not dealing with a Java project
    //or Java project type. Check the server.
    if (clientNeedEAR_)
    {
      // Determine if an ear selection is needed based on the server type.

      String serverId = clientIds_.getServerId();
      if (serverId != null)
      {
        // Use the server type
        String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverId);
        if (serverTargetId != null && serverTargetId.length() > 0)
        {
          if (!ServerUtils.isTargetValidForEAR(serverTargetId, "13"))
          {
            // Default the EAR selection to be empty
            clientNeedEAR_ = false;
          }
        }
      }
    }
    
    if (clientNeedEAR_)
    {
      clientEarProjectName_ = getDefaultClientEarProjectName();
    }
    else
    {
      clientEarProjectName_ = "";
    }   
  }
  
  private IStatus setClientDefaultServer()
  {
    //Choose an existing server the module is already associated with if possible
    IProject clientProject = ProjectUtilities.getProject(clientProjectName_);
    IServer[] configuredServers = ServerUtil.getServersByModule(ServerUtils.getModule(clientProject), null);
    
    if (configuredServers!=null && configuredServers.length>0)
    {
      clientIds_.setServerId(configuredServers[0].getServerType().getId());
      clientIds_.setServerInstanceId(configuredServers[0].getId());
      return Status.OK_STATUS;            	
    }
    
    //If the project exists, choose a suitable server or server type based on the existing project's runtime or facets
    if (clientProject.exists())
    {
      IServer server = getServerFromProject(clientProjectName_, clientFacetMatcher_);
      if (server != null)
      {
        clientIds_.setServerId(server.getServerType().getId());
        clientIds_.setServerInstanceId(server.getId());
        return Status.OK_STATUS;
      }
      else
      {
        IServerType serverType = getServerTypeFromProject(clientProjectName_, clientFacetMatcher_);
        if (serverType != null)
        {
          clientIds_.setServerId(serverType.getId());
          return Status.OK_STATUS;
        }
      }
    }

    //Haven't picked a server/server type on the basis of the project. Pick a server/server type
    //that is compatible with the clientRuntimeId.
    IServer server = getServerFromClientRuntimeId();
    if (server!=null)
    {
      clientIds_.setServerId(server.getServerType().getId());
      clientIds_.setServerInstanceId(server.getId());
      return Status.OK_STATUS;
    }
    
    IServerType serverType = getServerTypeFromClientRuntimeId();
    if (serverType != null)
    {
      clientIds_.setServerId(serverType.getId());
      return Status.OK_STATUS;
    }    
    
    //No suitable server was found. Popup an error.
    String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(clientIds_.getRuntimeId());
    String serverLabels = getServerLabels(clientRuntimeId_);    
    IStatus status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_ERROR_NO_SERVER_RUNTIME, new String[]{runtimeLabel, serverLabels}) );
    return status;
  }
  
  private IServer getServerFromClientRuntimeId()
  {
    IServer[] servers = ServerCore.getServers();
    if (servers != null && servers.length > 0) {
      for (int i = 0; i < servers.length; i++)
      {
        String serverFactoryId = servers[i].getServerType().getId();
        if (WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportServer(clientRuntimeId_, serverFactoryId))
        {
          return servers[i];
        }
      }
    }
    return null;    
  }
  
  private IServerType getServerTypeFromClientRuntimeId()
  {
    String[] serverTypes = WebServiceRuntimeExtensionUtils2.getServerFactoryIdsByClientRuntime(clientRuntimeId_);
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
  
  protected IServer getServerFromProject(String projectName, FacetMatcher facetMatcher)
  {
    IServer server = null;
    
    IProject project = ProjectUtilities.getProject(projectName);
    IServer[] servers = ServerCore.getServers();
    
    if (servers.length > 0)
    {
      // Get the runtime.
      org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = null;
      IFacetedProject fProject = null;

      if (project != null && project.exists())
      {
        try
        {
          fProject = ProjectFacetsManager.create(project);
          if (fProject != null)
          {
            fRuntime = fProject.getRuntime();
          }
        } catch (CoreException ce)
        {

        }
      }

      if (fRuntime != null)
      {
        // Get an existing server that has the same runtime.
        IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
        for (int i = 0; i < servers.length; i++)
        {
          IServer thisServer = servers[i];
          if (thisServer.getRuntime().getId().equals(sRuntime.getId()))
          {
            server = thisServer;
            break;
          }
        }
      }

      // If an existing server could not be chosen on the basis of the project's
      // runtime,
      // try to find an existing server using the project's facets and the
      // facetsToAdd.
      if (server == null)
      {
        if (project!=null && project.exists())
        {
          Set facets = FacetUtils.getFacetsForProject(project.getName());
          if (facets != null)
          {
            if (facetMatcher.getFacetsToAdd() != null)
            {
              Iterator itr = facetMatcher.getFacetsToAdd().iterator();
              while (itr.hasNext())
              {
                facets.add(itr.next());  
              }            
            }
            server = getServerFromFacets(facets);            
          }
        }
      }
    }
    return server;
  }
  
  protected IServer getServerFromFacets(Set facets)
  {
    IServer server = null;
    Set runtimes = FacetUtils.getRuntimes(new Set[]{facets});
    Iterator itr = runtimes.iterator();
    IServer[] servers = ServerCore.getServers();
    
    outer:
    while(itr.hasNext())
    {
      org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime)itr.next();
      IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
      for (int i=0; i<servers.length; i++)
      {
        IServer thisServer = servers[i];
        if (thisServer.getRuntime().getId().equals(sRuntime.getId()))
        {
          server = thisServer;
          break outer;
        }
      }      
    }

    return server;
  }
  
  protected IServerType getServerTypeFromProject(String projectName, FacetMatcher facetMatcher)
  {
    IServerType serverType = null;
    
    IProject project = ProjectUtilities.getProject(projectName);
    IServerType[] serverTypes = ServerCore.getServerTypes();
    
    //Get the runtime.
    org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = null;
    IFacetedProject fProject = null;
    
    if (project != null && project.exists())
    {
      try
      {
        fProject = ProjectFacetsManager.create(project);
        if (fProject != null)
        {
          fRuntime = fProject.getRuntime();
        }
      } catch (CoreException ce)
      {
        
      }
    }
    
    if (fRuntime != null)
    {
      //Get a server type that has the same runtime type.
      IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
      for (int i=0; i<serverTypes.length; i++)
      {
        IServerType thisServerType = serverTypes[i];
        if (thisServerType.getRuntimeType().getId().equals(sRuntime.getRuntimeType().getId()))
        {
          serverType = thisServerType;
          break;
        }
      }
    }

    //If a server type could not be chosen on the basis of the project's runtime,
    //try to find a server type using the project's facets and the facetsToAdd.
    if (serverType == null)
    {
      if (project != null && project.exists())
      {
        Set facets = FacetUtils.getFacetsForProject(project.getName());
        if (facets != null)
        {
          if (facetMatcher.getFacetsToAdd() != null)
          {
            Iterator itr = facetMatcher.getFacetsToAdd().iterator();
            while (itr.hasNext())
            {
              facets.add(itr.next());  
            }            
          }
          serverType = getServerTypeFromFacets(facets);          
        }
      }
    }
    
    return serverType;
  }
  
  protected IServerType getServerTypeFromFacets(Set facets)
  {
    IServerType serverType = null;
    Set runtimes = FacetUtils.getRuntimes(new Set[]{facets});
    Iterator itr = runtimes.iterator();
    IServerType[] serverTypes = ServerCore.getServerTypes();
    
    outer:
    while(itr.hasNext())
    {
      org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime)itr.next();
      IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
      for (int i=0; i<serverTypes.length; i++)
      {
        IServerType thisServerType = serverTypes[i];
        if (thisServerType.getRuntimeType().getId().equals(sRuntime.getRuntimeType().getId()))
        {
          serverType = thisServerType;
          break outer;
        }
      }      
    }

    return serverType;
  }  
  
  protected IServer getServerFromProjectType(String templateId, FacetMatcher facetMatcher)
  {
    IServer server = null;
    Set facets = FacetUtils.getInitialFacetVersionsFromTemplate(templateId);
    if (facetMatcher.getFacetsToAdd() != null)
    {
      Iterator itr = facetMatcher.getFacetsToAdd().iterator();
      while (itr.hasNext())
      {
        facets.add(itr.next());  
      }            
    }
    server = getServerFromFacets(facets);
    return server;
  }
  
  protected IServerType getServerTypeFromProjectType(String templateId, FacetMatcher facetMatcher)
  {
    IServerType serverType = null;
    Set facets = FacetUtils.getInitialFacetVersionsFromTemplate(templateId);
    if (facetMatcher.getFacetsToAdd() != null)
    {
      Iterator itr = facetMatcher.getFacetsToAdd().iterator();
      while (itr.hasNext())
      {
        facets.add(itr.next());  
      }            
    }
    //TODO Instead of passing in a single set of facets, pass in multiple sets, if the
    //jst.java facet is one of them and the clientRuntimeId allows newer.
    serverType = getServerTypeFromFacets(facets);
    return serverType;    
  }
  
  private String getDefaultClientProjectTemplate()
  {
    String[] templates = WebServiceRuntimeExtensionUtils2.getClientProjectTemplates(clientIds_.getTypeId(), clientIds_.getRuntimeId());
    RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId_).getRequiredFacetVersions();
    
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
          clientFacetMatcher_ = fm;
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
        clientFacetMatcher_ = fm;
        return templates[j];  
      }      
    }
    
    //Still nothing, return the first one if available.
    if (templates.length > 0)
      return templates[0];
    
    return "";
  }
  
  private String getDefaultClientProjectName()
  {
    IProject[] projects = FacetUtils.getAllProjects();
    ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId_);
    RequiredFacetVersion[] rfvs = desc.getRequiredFacetVersions();
    
    //Check each project for compatibility with the clientRuntime
    for (int i=0; i<projects.length; i++)
    {
      Set facetVersions = FacetUtils.getFacetsForProject(projects[i].getName());
      if (facetVersions != null)
      {
        FacetMatcher fm = FacetUtils.match(rfvs, facetVersions);
        if (fm.isMatch())
        {
          clientFacetMatcher_ = fm;
          return projects[i].getName();
        }                    
      }
    }
    
    //No project was suitable, return a new project name
    return ResourceUtils.getDefaultWebProjectName();
    
  }
  
  private String getDefaultClientRuntime(IProject project)
  {
    

    String[] clientRuntimes = WebServiceRuntimeExtensionUtils2.getClientRuntimesByType(clientIds_.getTypeId());
    
    //Check if the preferred Web service runtime works with the initially selected project.
    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
    String runtimeId = context.getRuntimeId();
    String preferredClientRuntimeId = null;
    for (int k=0; k<clientRuntimes.length; k++ )
    {
      ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimes[k]);
      if (desc.getRuntime().getId().equals(runtimeId))
      {
        preferredClientRuntimeId = desc.getId();
        break;
      }
    }
    
    if (preferredClientRuntimeId != null)
    {
      if (project != null && project.exists())
      {
        RequiredFacetVersion[] prfv = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(preferredClientRuntimeId)
            .getRequiredFacetVersions();
        Set facetVersions = FacetUtils.getFacetsForProject(project.getName());
        if (facetVersions != null)
        {
          FacetMatcher fm = FacetUtils.match(prfv, facetVersions);
          if (fm.isMatch())
          {
            clientFacetMatcher_ = fm;
            clientProjectName_ = project.getName();
            return preferredClientRuntimeId;
          }          
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
      for (int i=0; i<clientRuntimes.length; i++)
      {
        RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimes[i]).getRequiredFacetVersions();
        Set facetVersions = FacetUtils.getFacetsForProject(project.getName());
        if (facetVersions != null)
        {
          FacetMatcher fm = FacetUtils.match(rfv, facetVersions);
          if (fm.isMatch())
          {
            clientFacetMatcher_ = fm;
            clientProjectName_ = project.getName();
            return clientRuntimes[i];
          }
        }
      }
    }
    
    //Haven't returned yet so this means that the intitially selected project cannot be used
    //to influence the selection of the runtime. Pick the preferred Web service runtime if it is 
    //not null.
    if (preferredClientRuntimeId != null)
    {
      return preferredClientRuntimeId;
    }
      
    
    if (clientRuntimes.length > 0)
      return WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimes[0]).getId();
    else
      return null;
  }
   
  /**
 * 
 * @param project
 * @return
 */
  protected SelectionListChoices getProjectEARChoice(IProject project)
  {
    String[] flexProjects = getAllFlexibleProjects();
    SelectionList list = new SelectionList(flexProjects, 0);
    return new SelectionListChoices(list, null);
  }
  
  protected SelectionList getEARProjects()
  {
    String[] flexProjects = getAllFlexibleProjects();
    return new SelectionList(flexProjects, 0);
  }
  
  protected IResource getResourceFromInitialSelection(IStructuredSelection selection)
  {
    if (selection != null && selection.size() == 1)
    {
      Object obj = selection.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = ResourceUtils.getResourceFromSelection(obj);
          return resource;
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;
  }
  
  
  protected String getDefaultClientEarProjectName()
  {
    IProject clientProject = ProjectUtilities.getProject(clientProjectName_);
    IVirtualComponent[] earComps = J2EEUtils.getReferencingEARComponents(clientProject);
    if (earComps.length>0)
    {
      //Pick the first one
      return earComps[0].getName();
    }    

    //Either project does not exist or component is not associated with any EARs, so pick the first EAR you see with the correct J2EE version.
    IVirtualComponent[] allEarComps = J2EEUtils.getAllEARComponents();
    if (allEarComps.length>0)
    {
        //TODO Choose an existing EAR that can be added to the server and who's J2EE level in consistent with 
        //that of the selected project, if applicable. Picking the first one for now.        
        return allEarComps[0].getName();             
    }
    else
    {
      //there are no Ears.
      return ResourceUtils.getDefaultClientEARProjectName();
    }    
  }
    
    
  private String getServerLabels(String clientRuntimeId)
  {
        String[] validServerFactoryIds = WebServiceRuntimeExtensionUtils2.getServerFactoryIdsByClientRuntime(clientRuntimeId);
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
  
  
  protected void updateClientProject(String projectName, String serviceTypeId)
  {
    boolean isEJB = false;
    String implId = WebServiceRuntimeExtensionUtils2.getWebServiceImplIdFromTypeId(serviceTypeId);
    isEJB = (implId.equals("org.eclipse.jst.ws.wsImpl.ejb"));
    String[] updatedNames = ResourceUtils.getClientProjectComponentName(projectName, projectName, isEJB);
    clientProjectName_ = updatedNames[0];
    clientComponentType_ = getDefaultClientProjectTemplate();
    
  }
  
  
  private void setDefaultsForExtension(IEnvironment env)
  {
    IWebServiceRuntime wsrt = WebServiceRuntimeExtensionUtils2.getClientRuntime(clientRuntimeId_);
    if (wsrt != null)
    {
      WebServiceClientInfo wsInfo = new WebServiceClientInfo();      
      wsInfo.setServerFactoryId(clientIds_.getServerId());
      wsInfo.setServerInstanceId(clientIds_.getServerInstanceId());
      wsInfo.setState(WebServiceState.UNKNOWN_LITERAL);
      wsInfo.setWebServiceRuntimeId(clientIds_.getRuntimeId());
      wsInfo.setWsdlURL(wsdlURI_);

      webServiceClient_ = wsrt.getWebServiceClient(wsInfo);
      WebServiceScenario scenario = WebServiceScenario.CLIENT_LITERAL;
      if (resourceContext_ != null)
      {
        context_ = new SimpleContext(true, true, true, true, true, true, test_, false, scenario, resourceContext_.isOverwriteFilesEnabled(), resourceContext_
            .isCreateFoldersEnabled(), resourceContext_.isCheckoutFilesEnabled());
      }
    }
  
  }
  
  public void setClientInitialSelection(IStructuredSelection selection)
  {
    if (clientInitialProject_ == null)
    {
      clientInitialProject_ = getProjectFromInitialSelection(selection);
    }
  }

  public void setClientInitialProject(IProject clientInitialProject)
  {
    clientInitialProject_ = clientInitialProject;
  }
  
  /**
   * @param initialInitialSelection_ The initialInitialSelection_ to set.
   */
  public void setInitialInitialSelection(IStructuredSelection initialInitialSelection)
  {
    // TODO Delete this method and corresponding mappings
	// if no longer required.
  }
  
  public boolean getClientNeedEAR()
  {
    return clientNeedEAR_;
  }

  /**
   * @param parser_ The parser_ to set.
   */
  public void setWebServicesParser(WebServicesParser parser) {
    // TODO Delete this method and corresponding mappings
	// if no longer required.
  }
  
  public void setWsdlURI(String wsdlURI)
  {
    wsdlURI_ = wsdlURI;
  }
  
  public void setTestService(boolean testService)
  {
    test_ = testService;
  }   
  
  public void setResourceContext( ResourceContext resourceContext )
  {
    resourceContext_ = resourceContext;   
  }  
  
  private IProject getProjectFromInitialSelection(IStructuredSelection selection)
  {
    if (selection != null && selection.size() == 1)
    {
      Object obj = selection.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = ResourceUtils.getResourceFromSelection(obj);
          if (resource==null) 
            return null;
          IProject p = ResourceUtils.getProjectOf(resource.getFullPath());
          return p;
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;
  }
  
  protected String[] getAllFlexibleProjects()
  {
    Vector v = new Vector();
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    for (int i = 0; i < projects.length; i++)
    {
      if (!projects[i].getName().equals("Servers") && !projects[i].getName().startsWith("."))
      {
        v.add(projects[i].getName());
      }
    }    
    
    return (String[])v.toArray(new String[0]);
    
  }
  
  /**
   * This inner class is being used to pass around Web service runtime
   * and J2EE level information.
   * 
   */
  protected class WSRuntimeJ2EEType
  {
    private String wsrId_;
    private String j2eeVersionId_;
    private String clientProjectTypeId; //only used for client-side defaulting
    
    public WSRuntimeJ2EEType()
    {
     //making this ctor public so that subclasses can instantiate. 
    }    
    /**
     * @return Returns the j2eeVersionId_.
     */
    public String getJ2eeVersionId()
    {
      return j2eeVersionId_;
    }
    /**
     * @param versionId_ The j2eeVersionId_ to set.
     */
    public void setJ2eeVersionId(String versionId_)
    {
      j2eeVersionId_ = versionId_;
    }
    /**
     * @return Returns the wsrId_.
     */
    public String getWsrId()
    {
      return wsrId_;
    }
    /**
     * @param wsrId_ The wsrId_ to set.
     */
    public void setWsrId(String wsrId_)
    {
      this.wsrId_ = wsrId_;
    }    
    
    /**
     * @return Returns the clientProjectTypeId.
     */
    public String getClientProjectTypeId()
    {
      return clientProjectTypeId;
    }
    /**
     * @param clientProjectTypeId The clientProjectTypeId to set.
     */
    public void setClientProjectTypeId(String clientProjectTypeId)
    {
      this.clientProjectTypeId = clientProjectTypeId;
    }
  }
}