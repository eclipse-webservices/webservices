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
 * 20060206 126408   rsinha@ca.ibm.com - Rupam Kuehner
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060222   115834 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060315   131963 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060418   129688 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427   126780 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427   126780 kathy@ca.ibm.com - Kathy Chan
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20060523   133714 joan@ca.ibm.com - Joan Haggarty
 * 20060525   143843 joan@ca.ibm.com - Joan Haggarty
 * 20060905   156230 kathy@ca.ibm.com - Kathy Chan, Handling projects with no target runtime
 * 20070119   159458 mahutch@ca.ibm.com - Mark Hutchinson
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.eclipse.jst.ws.internal.consumption.ui.common.DefaultingUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ClientRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.FacetMatchCache;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.RuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
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
  private String DEFAULT_CLIENT_EAR_PROJECT_EXT = "EAR";
  private TypeRuntimeServer    clientIds_;
  private boolean clientIdsFixed_ = false;
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
  
  //A note on initial projects ...
  //The difference between clientInitialProject_ and initialProject_ is that
  //clientInitialProject_ comes from the ObjectSelectionOutputCommand while initialProject_
  //comes from SelectionCommand (i.e. it is the project containing the thing that was selected before 
  //the wizard was launched). In the defaulting algorithm, clientInitialProject_ will 
  //be given first priority. If, however, it is deemed that clientInitialProject_ is not a valid project 
  //because it contains the J2EE Web service for which we are trying to create a client, initialProject_
  //will be given second priority.
  private IProject initialProject_; //This is the project containing the selection prior to the wizard being launched.
  private IProject clientInitialProject_; //This is the project containing the object selection from page 2.
  private String wsdlURI_;
  private WebServicesParser parser_;  

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
  
  public void setClientIdsFixed(boolean b)
  {
    clientIdsFixed_ = b;  
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
  
  /**
   * Defaults the following bits of information in the following order:
   * clientRuntimeId_ : the clientRuntimeId. Must be defaulted to non-empty String.
   * clientIds_.runtimeId_: the Web service runtime id. Must be defaulted to non-empty String.
   * clientProjectName_ : the name of the client project. Must be non-empty. May or may not exist.
   * clientComponentType_: the id of the client project template. Must be empty if the client
   *                        project exists. Must be non-empty of the client project does not exist.
   * clientIds_.serverId_: the server type id. May be an empty String if the defaulted Web service runtime
   *                        does not require a server.
   * clientIds_.serverInstanceId_: the server id. May be null or an empty String.
   * clientNeedEAR_: true if an EAR is needed. False otherwise.
   * clientEarProjectName_: the client EAR project. Must be empty if the clientNeedEAR_ is false.
   *                         Must be non-empty if the clientNeedEAR_ is true.
   * webServiceClient_ : the IWebServiceClient based on the calculated defaults. Must be non-null for "Next"
   *                     button to be enabled on the page following this command.
   * context_ : an IContext. Must be non-null for "Next" button to be enabled on the page following this command.                         
   */  
  public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable)
  {

    IEnvironment env = getEnvironment();

    try
    {
      
      //**Step 1** Default the Web service runtime.
    	
      //clientIdsFixed_ is set to true for the Ant scenario. It's always false for the wizard
      //scenarios.
      if (clientIdsFixed_ && (clientProjectName_ == null))
      {
        // Set the clientRuntime based on the runtime, server, and initial
        // selection.
        DefaultRuntimeTriplet drt = getDefaultClientRuntimeForFixedRuntimeAndServer(clientInitialProject_);
        clientFacetMatcher_ = drt.getFacetMatcher();
        clientProjectName_ = drt.getProjectName();
        clientRuntimeId_ = drt.getRuntimeId();        
      } 
      else      
      {	
        ValidationUtils vu = new ValidationUtils();
        
        // Set the runtime based on the project containing the object selection/initial selection.
        DefaultRuntimeTriplet drt = null;
        
        if (!vu.isProjectServiceProject(clientInitialProject_, wsdlURI_, parser_) && !clientIdsFixed_)
        {
          //If clientIntialProject_ does not contain the J2EE Web service, choose a clientRuntime based on it.
          drt = getDefaultRuntime(clientInitialProject_, clientIds_.getTypeId(), true);
          clientFacetMatcher_ = drt.getFacetMatcher();
          clientProjectName_ = drt.getProjectName(); 
          clientRuntimeId_ = drt.getRuntimeId();          
        }
        else
        {
          //clientInitialProject_ contains the J2EE Web service so don't use it.
          //Try using the initalProject_ instead.
          if (!vu.isProjectServiceProject(initialProject_, wsdlURI_, parser_) && !clientIdsFixed_)
          {
            //If intialProject_ does not contain the J2EE Web service, choose a clientRuntime based on it.
            drt = getDefaultRuntime(initialProject_, clientIds_.getTypeId(), true);
            clientFacetMatcher_ = drt.getFacetMatcher();
            clientProjectName_ = drt.getProjectName();
            clientRuntimeId_ = drt.getRuntimeId();            
          }
          else
          {
            //Both clientIntialProject_ and initialProject_ contain the J2EE Web service
            //and cannot be used to influence clientRuntime defaulting.
            //Choose a clientRuntime but don't choose clientInitialProject_
            //as the clientProject.
            drt = getDefaultRuntime(null, clientIds_.getTypeId(), true);
            clientRuntimeId_ = drt.getRuntimeId();                      
          }
        }

        //Set the Web service runtime id from the clientRuntime
        clientIds_.setRuntimeId(WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId_).getRuntime()
            .getId());
      }
      
      //**Step 2** Default the client project if it was not already defaulted 
      //as part of defaulting the Web service runtime.
      if (clientProjectName_ == null)
      {
        // Project name did not get set when the runtime was set, so set it now
        clientProjectName_ = getDefaultClientProjectName();
      }

      //**Step 3** Default the client project type.      
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


      //**Step 4** Default the client server if this is not an Ant scenario.
      if (!clientIdsFixed_)
      {
        IStatus serverStatus = setClientDefaultServer();
        if (serverStatus.getSeverity() == Status.ERROR)
        {
          env.getStatusHandler().reportError(serverStatus);
          return serverStatus;
        }
      }
      
      //**Step 5** Default clientNeedEAR and client EAR if an EAR is needed
      setDefaultClientEarProject();      
      
      
      //**Step 6** Calculate default IWebServiceClient. This is need to make sure that
      // Next is enabled on the page following this command.
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
      } else {  // serverId == null, assume that it does not need EAR
    	  clientNeedEAR_ = false;
    	  
      }
    }
    
    if (clientNeedEAR_)
    {
    	clientEarProjectName_ = DefaultingUtils.getDefaultEARProjectName(clientProjectName_);
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
    
    // No suitable server was found. Popup an error if the default Web service
    // runtime requires a server.
    RuntimeDescriptor runtimeDescriptor = WebServiceRuntimeExtensionUtils2.getRuntimeById(clientIds_.getRuntimeId());
    if (runtimeDescriptor.getServerRequired())
    {
      String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(clientIds_.getRuntimeId());
      IStatus status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_ERROR_NO_SERVER_RUNTIME, new String[] {
          runtimeLabel}));
      return status;
    }
    
    return Status.OK_STATUS;
  }
  
  private IServer getServerFromClientRuntimeId()
  {
    IServer[] servers = ServerCore.getServers();
    if (servers != null && servers.length > 0) {
    	
        PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
        String preferredServerFactoryId = context.getServerFactoryId();
        
        //If a server of the preferred server type is present, check that one first
        for (int j = 0; j < servers.length; j++)
        {
          String serverFactoryId = servers[j].getServerType().getId();
          if (serverFactoryId == preferredServerFactoryId)
          {
            if (WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportServer(clientRuntimeId_, serverFactoryId))
            {
              return servers[j];
            }
          }                
        }
        
        //A server of the preferred server type could not be found or did not match. Check all the existing servers.    
    	
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
      PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
      String preferredServerFactoryId = context.getServerFactoryId();
        
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
        IServer firstMatchingServer = null;    	  
        IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
        for (int i = 0; i < servers.length; i++)
        {
          IServer thisServer = servers[i];
          if (thisServer.getRuntime().getId().equals(sRuntime.getId()))
          {
            if (firstMatchingServer==null)
            {
              firstMatchingServer = thisServer;
            }
              
            if (thisServer.getServerType().getId().equals(preferredServerFactoryId))
            {
        	  
              server = thisServer;
              break;
            }
          }
        }
                
        //If a server of the preferred server type was not found but
        //there was a server that matched, return that one.
        if (server == null && firstMatchingServer != null)
        {
          server = firstMatchingServer;
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
            Set facetsClone = new HashSet();
            facetsClone.addAll(facets);            
            if (facetMatcher.getFacetsToAdd() != null)
            {
              Iterator itr = facetMatcher.getFacetsToAdd().iterator();
              while (itr.hasNext())
              {
                facetsClone.add(itr.next());  
              }            
            }
            server = getServerFromFacets(facetsClone);            
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
	    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
	    String preferredServerFactoryId = context.getServerFactoryId();
	    
	    //If one of the servers is of the preferred type, and its runtime
	    //is in the set of matching runtimes, return that server.
	    for (int j=0; j<servers.length; j++)      
	    {
	      IServer thisServer = servers[j];
	      if (thisServer.getServerType().getId().equals(preferredServerFactoryId))
	      {
	        //Check to see if it matches any of the runtimes.
	        Iterator runtimesItr = runtimes.iterator();
	        while(runtimesItr.hasNext())
	        {
	          org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime)runtimesItr.next();
	          IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
	          if (thisServer.getRuntime().getId().equals(sRuntime.getId()))
	          {
	            server = thisServer;
	          }          
	        }
	      }            
	    }
	    
	    if (server == null)
	    {
	      outer: while (itr.hasNext())
	      {
	        org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime) itr
	            .next();
	        IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
	        for (int i = 0; i < servers.length; i++)
	        {
	          IServer thisServer = servers[i];
	          if (thisServer.getRuntime().getId().equals(sRuntime.getId()))
	          {
	            server = thisServer;
	            break outer;
	          }
	        }
	      }
	    }

	    return server;

  }
  
  protected IServerType getServerTypeFromProject(String projectName, FacetMatcher facetMatcher)
  {
	    IServerType serverType = null;
	    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
	    String preferredServerFactoryId = context.getServerFactoryId();
	    
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
	      IRuntimeType sRuntimeType = sRuntime.getRuntimeType();
	      IServerType firstMatchingServerType = null;
	      for (int i=0; i<serverTypes.length; i++)
	      {
	        IServerType thisServerType = serverTypes[i];
	        if (sRuntimeType != null && thisServerType.getRuntimeType().getId().equals(sRuntimeType.getId()))
	        {
	          if (firstMatchingServerType == null)
	          {
	            firstMatchingServerType = thisServerType;
	          }
	          if (thisServerType.getId().equals(preferredServerFactoryId))
	          {
	            serverType = thisServerType;
	            break;
	          }
	        }
	      }
	      
	      //If the preferred server type was not found but
	      //there was a server type that matched, return that one.        
	      if (serverType == null && firstMatchingServerType != null)
	      {        
	        serverType = firstMatchingServerType;
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
	          Set facetsClone = new HashSet();
	          facetsClone.addAll(facets);          
	          if (facetMatcher != null && facetMatcher.getFacetsToAdd() != null)
	          {
	            Iterator itr = facetMatcher.getFacetsToAdd().iterator();
	            while (itr.hasNext())
	            {
	              facetsClone.add(itr.next());  
	            }            
	          }
	          serverType = getServerTypeFromFacets(facetsClone);          
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
	    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
	    String preferredServerFactoryId = context.getServerFactoryId();    

	    //If one of the server types is of the preferred type, and its runtime
	    //is in the set of matching runtimes, return that server type.
	    for (int j=0; j<serverTypes.length; j++)      
	    {
	      IServerType thisServerType = serverTypes[j];
	      if (thisServerType.getId().equals(preferredServerFactoryId))
	      {
	        //Check to see if it matches any of the runtimes.
	        Iterator runtimesItr = runtimes.iterator();
	        while(runtimesItr.hasNext())
	        {
	          org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime)runtimesItr.next();
	          IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
	          IRuntimeType sRuntimeType = sRuntime.getRuntimeType();
	          if (sRuntimeType != null && thisServerType.getRuntimeType().getId().equals(sRuntimeType.getId()))
	          {
	            serverType = thisServerType;
	          }          
	        }
	      }            
	    }
	    
	    
	    if (serverType == null)
	    {
	      outer: while (itr.hasNext())
	      {
	        org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime) itr
	            .next();
	        IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
	        IRuntimeType sRuntimeType = sRuntime.getRuntimeType();
	        for (int i = 0; i < serverTypes.length; i++)
	        {
	          IServerType thisServerType = serverTypes[i];
	          if (sRuntimeType != null && thisServerType.getRuntimeType().getId().equals(sRuntimeType.getId()))
	          {
	            serverType = thisServerType;
	            break outer;
	          }
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
    
    //Walk the list of client project types in the project topology preference
    ProjectTopologyContext ptc= WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
    String[] preferredTemplateIds = ptc.getClientTypes();
    for (int j=0; j<preferredTemplateIds.length; j++)
    {
      for (int i=0; i<templates.length; i++)
      {
        String templateId = templates[i];
        if (templateId.equals(preferredTemplateIds[j]))
        {
          
          boolean matches = WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportTemplate(clientRuntimeId_, templateId);
          if (matches)
          {
            return templates[i];            
          }
        }                                    
      }      
    }

    //Since the preferredTemplateIds contains the union of all project types for all client runtimes, we are
    //guaranteed to have returned by now, so the code below will never be executed under normal
    //circumstances. Just return something to satisfy the compiler.    
    if (templates.length > 0)
      return templates[0];
    
    return "";
  }
  
  private String getDefaultClientProjectName()
  {
    ValidationUtils vu = new ValidationUtils();
    IProject[] projects = FacetUtils.getAllProjects();
    ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId_);
    RequiredFacetVersion[] rfvs = desc.getRequiredFacetVersions();
    
    //Check each project and its facetRuntime for compatibility with the clientRuntime
    for (int i=0; i<projects.length; i++)
    {
      if (!vu.isProjectServiceProject(projects[i], wsdlURI_, parser_))
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

          // FacetMatcher fm = FacetUtils.match(rfvs, facetVersions);
          FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(true, clientRuntimeId_, projects[i].getName());
          boolean facetRuntimeMatches = true;
          if (fRuntimeName != null)
          {
            facetRuntimeMatches = FacetUtils.isFacetRuntimeSupported(rfvs, fRuntimeName);
          }

          if (fm.isMatch() && facetRuntimeMatches)
          {
            clientFacetMatcher_ = fm;
            return projects[i].getName();
          }
        }
      }
    }
    
    //No project was suitable, return a new project name
    return ResourceUtils.getDefaultWebProjectName();
    
  }
  
  private DefaultRuntimeTriplet getDefaultClientRuntimeForFixedRuntimeAndServer(IProject project)
  {
    String[] clientRuntimes = WebServiceRuntimeExtensionUtils2.getClientRuntimesByType(clientIds_.getTypeId());
    ArrayList validClientRuntimes = new ArrayList();
    for (int i=0; i<clientRuntimes.length; i++ )
    {
      ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimes[i]);
      if (desc.getRuntime().getId().equals(clientIds_.getRuntimeId()))
      {
        //Check if this client runtime supports the server
        if (WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportServer(desc.getId(), clientIds_.getServerId()))
        {
          validClientRuntimes.add(desc.getId());
          if (project != null && project.exists())
          {
            //RequiredFacetVersion[] rfv = desc.getRequiredFacetVersions();
            Set facetVersions = FacetUtils.getFacetsForProject(project.getName());
            if (facetVersions != null)
            {
              //FacetMatcher fm = FacetUtils.match(rfv, facetVersions);
              FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(true, clientRuntimes[i], project.getName());
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
    
    if (validClientRuntimes.size() > 0)
    {
      //We couldn't match to the initially selected project so return the first valid runtime.
      DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
      drt.setFacetMatcher(null);
      drt.setProjectName(null);
      drt.setRuntimeId(((String[])validClientRuntimes.toArray(new String[0]))[0]);
      return drt;            
    }
    else
    {
      //There are no client runtimes that match the fixed runtime and server. Fall back to original algorithm
      clientIdsFixed_ = false;
      return getDefaultRuntime(project, clientIds_.getTypeId(), true);
    }
  }  
    
  protected DefaultRuntimeTriplet getDefaultRuntime(IProject project, String typeId, boolean isClient)
  {
    String[] runtimes = null;
    if (isClient)
    {
      runtimes = WebServiceRuntimeExtensionUtils2.getClientRuntimesByType(typeId);
    }
    else
    {
      runtimes = WebServiceRuntimeExtensionUtils2.getServiceRuntimesByServiceType(typeId);
    }
        
    //Split the array of service/client runtimes into one containing the preferred set and one containing the rest.
    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
    String preferredRuntimeId = context.getRuntimeId();
    ArrayList preferredRuntimeIdsList = new ArrayList();
    ArrayList otherRuntimeIdsList = new ArrayList();
    for (int k=0; k<runtimes.length; k++ )
    {
      String descRuntimeId = null;
      if (isClient)
      {
        ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(runtimes[k]);
        descRuntimeId = desc.getRuntime().getId();
      }
      else
      {
        ServiceRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(runtimes[k]);
        descRuntimeId = desc.getRuntime().getId();
      }
      
      if (descRuntimeId.equals(preferredRuntimeId))
      {
        preferredRuntimeIdsList.add(runtimes[k]);
      }
      else
      {
        otherRuntimeIdsList.add(runtimes[k]);
      }
    }
    String[] preferredRuntimeIds = (String[])preferredRuntimeIdsList.toArray(new String[0]);
    String[] otherRuntimeIds = (String[])otherRuntimeIdsList.toArray(new String[0]);
    
    Set facetVersions = null;
    org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = null;
    String fRuntimeName = null;
    
    //If the initially selected project exists and facets can be inferred from it, look for
    //a service/client runtime that matches the project's facets and also, if possible, its facet runtime.
    //Preference should be given to the preferred service/client runtimes. 
    if (project != null && project.exists())
    {
      facetVersions = FacetUtils.getFacetsForProject(project.getName());
      fRuntime = FacetUtils.getFacetRuntimeForProject(project.getName());
      fRuntimeName = null;
      if (fRuntime != null)
      {
        fRuntimeName = fRuntime.getName();        
      }
      
      if (facetVersions != null)
      {
        //1. First check to see if one of the preferred service/client runtimes matches the existing
        //project's facets and runtime.
        for (int p = 0; p < preferredRuntimeIds.length; p++)
        {
          RequiredFacetVersion[] prfv = null;
          if (isClient)
          {
            prfv = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(preferredRuntimeIds[p]).getRequiredFacetVersions();
          }
          else
          {
            prfv = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(preferredRuntimeIds[p]).getRequiredFacetVersions();
          }
          
          //FacetMatcher fm = FacetUtils.match(prfv, facetVersions);
          FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(isClient, preferredRuntimeIds[p], project.getName());
          boolean facetRuntimeMatches = true;
          if (fRuntimeName != null)
          {
            facetRuntimeMatches = FacetUtils.isFacetRuntimeSupported(prfv, fRuntimeName);  
          }          
          
          if (fm.isMatch() && facetRuntimeMatches)
          {
            DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
            drt.setFacetMatcher(fm);
            drt.setProjectName(project.getName());
            drt.setRuntimeId(preferredRuntimeIds[p]);
            return drt;
          }
        }
        
        //2. Second, check to see if one of the other clientRuntimes matches the existing
        //project's facets and runtime.        
        for (int i=0; i<otherRuntimeIds.length; i++)
        {        
          RequiredFacetVersion[] rfv = null;
          if (isClient)
          {
            rfv = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(otherRuntimeIds[i]).getRequiredFacetVersions();  
          }
          else
          {
            rfv = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(otherRuntimeIds[i]).getRequiredFacetVersions();
          }
          
          FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(isClient, otherRuntimeIds[i], project.getName());
          boolean facetRuntimeMatches = true;
          if (fRuntimeName != null)
          {
            facetRuntimeMatches = FacetUtils.isFacetRuntimeSupported(rfv, fRuntimeName);  
          }                      
          if (fm.isMatch() && facetRuntimeMatches)
          {
            DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
            drt.setFacetMatcher(fm);
            drt.setProjectName(project.getName());
            drt.setRuntimeId(otherRuntimeIds[i]);
            return drt;
          }                              
        }
        
        //3. Third, check to see if one of the preferred clientRuntimes matches the existing
        //project's facets.
        for (int p = 0; p < preferredRuntimeIds.length; p++)
        {
            FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(isClient, preferredRuntimeIds[p], project.getName()); 
            if (fm.isMatch())
            {
              DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
              drt.setFacetMatcher(fm);
              drt.setProjectName(project.getName());
              drt.setRuntimeId(preferredRuntimeIds[p]);
              return drt;
            }         
        }
        
        //4. Fourth, check to see if the one the other clientRuntimes matches the existing
        //project's facets.        
        for (int i=0; i<otherRuntimeIds.length; i++)
        {
            FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(isClient, otherRuntimeIds[i], project.getName()); 
            if (fm.isMatch())
            {
              DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
              drt.setFacetMatcher(fm);
              drt.setProjectName(project.getName());
              drt.setRuntimeId(otherRuntimeIds[i]);
              return drt;
            }        
        }        
      }            
    }
    
    //Haven't returned yet so this means that the intitially selected project cannot be used
    //to influence the selection of the service/client runtime. 
    
      //Use the preferred project types to influence the selection of a runtime.
      ProjectTopologyContext ptc = WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
      String[] preferredTemplateIds = null;
      if (isClient)
      {
        preferredTemplateIds = ptc.getClientTypes();  
      }
      else
      {
        preferredTemplateIds = ptc.getServiceTypes();
      }
      
    
      for (int n=0; n<preferredTemplateIds.length; n++)
      {
        String preferredTemplateId = preferredTemplateIds[n];

        for (int m=0; m<preferredRuntimeIds.length; m++)
        {
          //If this client or service runtime supports this template, choose it and exit.        
          boolean matches = false;
          if (isClient)
          {
            matches = WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportTemplate(preferredRuntimeIds[m], preferredTemplateId);            
          }
          else
          {
            matches = WebServiceRuntimeExtensionUtils2.doesServiceRuntimeSupportTemplate(preferredRuntimeIds[m], preferredTemplateId);
          }
          
          if (matches)
          {
            DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
            drt.setFacetMatcher(null);
            //If the project doesn't exist, use the name of the project that was passed in.
            //If the project exists, it means that previous code in this method
            //determined it to not be a suitable project. Clear the project name.
            if (project==null || project.exists())
            {
              drt.setProjectName(null);
            }
            else
            {
              drt.setProjectName(project.getName());
            }
            drt.setRuntimeId(preferredRuntimeIds[m]);
            return drt;            
          }        
        }
      }
    
    
    //Still haven't returned. Return the first preferred service/client runtime id.
    if (preferredRuntimeIds.length > 0)
    {
      DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
      drt.setFacetMatcher(null);
      //If the project doesn't exist, use the name of the project that was passed in.
      //If the project exists, it means that previous code in this method
      //determined it to not be a suitable project. Clear the project name.      
      if (project==null || project.exists())
      {
        drt.setProjectName(null);
      }
      else
      {
        drt.setProjectName(project.getName());
      }
      drt.setRuntimeId(preferredRuntimeIds[0]);
      return drt;      
    }
      
    if (runtimes.length > 0)
    {
      DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
      drt.setFacetMatcher(null);
      //If the project doesn't exist, use the name of the project that was passed in.
      //If the project exists, it means that previous code in this method
      //determined it to not be a suitable project. Clear the project name.
      if (project==null || project.exists())
      {
        drt.setProjectName(null);
      }
      else
      {
        drt.setProjectName(project.getName());
      }
      drt.setRuntimeId(runtimes[0]);
      return drt;
    }
    else
    {
      DefaultRuntimeTriplet drt = new DefaultRuntimeTriplet();
      drt.setFacetMatcher(null);
      //If the project doesn't exist, use the name of the project that was passed in.
      //If the project exists, it means that previous code in this method
      //determined it to not be a suitable project. Clear the project name.
      if (project==null || project.exists())
      {
        drt.setProjectName(null);
      }
      else
      {
        drt.setProjectName(project.getName());
      }
      drt.setRuntimeId(null);
      return drt;
    }    
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
  
  
      
  /*
   * Set defaults for the client-side from the service-side if possible.
   * Sets clientProjectName_ to a value based on the service project name.
   * 
   * Returns an IStatus.OK if the service side values for server and Web 
   * service runtime can be used to set the values for clientRuntimeId_,
   * clientComponentType_, all the values in clientIds_, and clientEarProjectName_.
   * 
   * Returns an IStatus.ERROR otherwise, leaving only the clientProjectName_ set. 
   */
  protected IStatus defaultClientSideFromServiceSide(String serviceProjectName, TypeRuntimeServer serviceIds, boolean serviceNeedEAR, String serviceEarProjectName)
  {
    //1. Start with setting the default client project name based on the service project name.
    boolean isEJB = false;
    String implId = WebServiceRuntimeExtensionUtils2.getWebServiceImplIdFromTypeId(serviceIds.getTypeId());
    isEJB = (implId.equals("org.eclipse.jst.ws.wsImpl.ejb"));
    String[] updatedNames = ResourceUtils.getClientProjectComponentName(serviceProjectName, serviceProjectName, isEJB);
    clientProjectName_ = updatedNames[0];
    
    //2. Ideally, the server and runtime on the client-side will be defaulted
    //   to the same values as the service side. If a client runtime id that 
    //   supports clientProjectName_ and the service-side server and runtime defaults
    //   can be found, choose it.
    IProject clientProject = ProjectUtilities.getProject(clientProjectName_);
    TypeRuntimeServer testIds = new TypeRuntimeServer();
    testIds.setTypeId(clientIds_.getTypeId());
    testIds.setRuntimeId(serviceIds.getRuntimeId());
    testIds.setServerId(serviceIds.getServerId());
    testIds.setServerInstanceId(serviceIds.getServerInstanceId());
    
    if (clientProject.exists())
    {
      clientComponentType_ = "";
      clientRuntimeId_ = WebServiceRuntimeExtensionUtils2.getClientRuntimeId(testIds, serviceProjectName, clientComponentType_);
      if (clientRuntimeId_ != null && clientRuntimeId_.length()>0)
      {
        clientIds_.setRuntimeId(serviceIds.getRuntimeId());
        clientIds_.setServerId(serviceIds.getServerId());
        clientIds_.setServerInstanceId(serviceIds.getServerInstanceId());
      }      
    }
    else
    {
      //See if there is a client project type that supports the service-side
      //server and runtime defaults. Pick a client project type based on project topology preferences.
      //Choose the clientRuntimeId_ accordingly.
      String[] templateIds = WebServiceRuntimeExtensionUtils2.getClientProjectTemplates(clientIds_.getTypeId(), testIds.getRuntimeId());
      if (templateIds != null && templateIds.length > 0)
      {
        ProjectTopologyContext ptc = WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
        String[] preferredTemplateIds = ptc.getClientTypes();
        outer: for (int j = 0; j < preferredTemplateIds.length; j++)
        {
          for (int i = 0; i < templateIds.length; i++)
          {
            String templateId = templateIds[i];
            if (templateId.equals(preferredTemplateIds[j]))
            {
              // Get a clientRuntimeId for this template
              String newClientRuntimeId = WebServiceRuntimeExtensionUtils2.getClientRuntimeId(testIds, clientProjectName_,
                  templateId);
              if (newClientRuntimeId.length() > 0)
              {
                clientRuntimeId_ = newClientRuntimeId;
                clientComponentType_ = templateId;
                clientIds_.setRuntimeId(serviceIds.getRuntimeId());
                clientIds_.setServerId(serviceIds.getServerId());
                clientIds_.setServerInstanceId(serviceIds.getServerInstanceId());                
                break outer;
              }
            }
          }
        }
      }
    } 
    
    //3. If step 2 was successful, clientRuntimeId_ should now be set, along with clientProjectName_,
    //   clientComponentType_, and all the values in clientIds_. All that remains is setting the EAR.
    if (clientRuntimeId_ != null && clientRuntimeId_.length()>0)
    {
      setDefaultClientEarProject();
      //Update the client side EAR from the service side EAR if necessary.
      if (serviceNeedEAR)
      {
        defaultClientEarFromServiceEar(serviceProjectName, serviceEarProjectName); 
      }
      return Status.OK_STATUS;
    }
    else
    {
      //Step 2 was not successful. Client side defaulting cannot be done using the service side server
      //and web service runtime defaults. Return an error status.
      return StatusUtils.errorStatus("");
    }
  }
  
  protected void defaultClientEarFromServiceEar(String serviceProjectName, String serviceEarProjectName)
  {
    //Client side
    if (clientNeedEAR_)
    {
      IProject clientProject = ProjectUtilities.getProject(clientProjectName_);
      if(clientProject==null || !clientProject.exists() 
        || !(clientEarProjectName_.equalsIgnoreCase(serviceEarProjectName)))
      {
        ProjectTopologyContext ptc= WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();         
        if (!ptc.isUseTwoEARs()) 
        {
          clientEarProjectName_ = serviceEarProjectName;
        }
        else 
        {
          IProject proxyEARProject = getUniqueClientEAR(clientEarProjectName_, serviceEarProjectName, clientProjectName_, serviceProjectName);
          clientEarProjectName_ = proxyEARProject.getName();
        }     
      }
    }
    
  }
  
  private IProject getUniqueClientEAR(String earProjectName, String serviceEARProject, String clientProjectName, String serviceProjectName) {

    String projectName = new String();
    //If the client project doesn't exist and the service project does, ensure the
    //the client side EAR's J2EE level is such that the service project could be added to it.
    //This will ensure we don't default the page with a client project EAR at a lower
    //J2EE level than the service side.
    boolean goodJ2EELevel = true;
    if (!earProjectName.equalsIgnoreCase(serviceEARProject))
    {
      IProject clientProject = ProjectUtilities.getProject(clientProjectName);
      IProject serviceProject = ProjectUtilities.getProject(serviceProjectName);
      IProject earProject = ProjectUtilities.getProject(earProjectName);
      if (!clientProject.exists() && serviceProject.exists())
      {
        IStatus associationStatus = J2EEUtils.canAssociateProjectToEAR(serviceProject, earProject);
        goodJ2EELevel = (associationStatus.getSeverity() == IStatus.OK);
      }
    }
    
    if (!earProjectName.equalsIgnoreCase(serviceEARProject) && goodJ2EELevel) {
      projectName = earProjectName;
    }    
    else {
      projectName = clientProjectName+DEFAULT_CLIENT_EAR_PROJECT_EXT;
      int i=1;      
      while (projectName.equalsIgnoreCase(serviceEARProject)) {
        projectName = projectName+i;
        i++;
      }
    }
    return projectName.equals("") ? null : ResourceUtils.getWorkspaceRoot().getProject(projectName);
    
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
  
  // This is for the Ant scenario where the client project name can be set in the property file.
  // If the user has set the ClientProjectName use it for defaulting purposes
  public void setClientProjectName(String name)
  {
	  clientProjectName_ = name;
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
    initialProject_ = getProjectFromInitialSelection(initialInitialSelection);
  }
  
  public boolean getClientNeedEAR()
  {
    return clientNeedEAR_;
  }

  /**
   * @param parser_ The parser_ to set.
   */
  public void setWebServicesParser(WebServicesParser parser) {
    parser_ = parser;
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
  
  protected class FacetRuntimeMatcher
  {
    FacetMatcher facetMatcher;
    boolean runtimeMatches;
    
    public FacetMatcher getFacetMatcher()
    {
      return facetMatcher;
    }
    public void setFacetMatcher(FacetMatcher facetMatcher)
    {
      this.facetMatcher = facetMatcher;
    }
    public boolean isRuntimeMatches()
    {
      return runtimeMatches;
    }
    public void setRuntimeMatches(boolean runtimeMatches)
    {
      this.runtimeMatches = runtimeMatches;
    }   
  }
  
  protected class DefaultRuntimeTriplet
  {
    FacetMatcher facetMatcher_;
    String projectName_;
    String runtimeId_;
    
    
    public DefaultRuntimeTriplet()
    {
    }
    
    public FacetMatcher getFacetMatcher()
    {
      return facetMatcher_;
    }
    public void setFacetMatcher(FacetMatcher facetMatcher_)
    {
      this.facetMatcher_ = facetMatcher_;
    }
    public String getProjectName()
    {
      return projectName_;
    }
    public void setProjectName(String projectName_)
    {
      this.projectName_ = projectName_;
    }
    public String getRuntimeId()
    {
      return runtimeId_;
    }
    public void setRuntimeId(String runtimeId_)
    {
      this.runtimeId_ = runtimeId_;
    }        
  }
}
