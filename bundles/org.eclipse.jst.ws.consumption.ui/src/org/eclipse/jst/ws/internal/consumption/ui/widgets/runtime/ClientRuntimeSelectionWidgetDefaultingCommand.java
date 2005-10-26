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
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.ui.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ServerSelectionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.ClientRuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeInfo;
import org.eclipse.jst.ws.internal.context.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceState;
import org.eclipse.wst.ws.internal.wsrt.SimpleContext;

public class ClientRuntimeSelectionWidgetDefaultingCommand extends AbstractDataModelOperation
{   
  protected MessageUtils msgUtils_;
  // clientRuntimeJ2EEType contains the default client runtime and J2EE level based on the initial selection.
  // If the initialSeleciton does not result in a valid client runtime and J2EE level, clientRuntimeJ2EEType
  // should remain null for the life of this instance.  
  private WSRuntimeJ2EEType    clientRuntimeJ2EEType_; //usage is internal to this class
  
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
  private IProject initialInitialProject_;


  
  private String wsdlURI_;
  private WebServicesParser parser_;


  public ClientRuntimeSelectionWidgetDefaultingCommand()
  {
    super();
    String  pluginId = "org.eclipse.jst.ws.consumption.ui";
    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
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

      // Set the EAR
      //clientEarProjectName_ = ""; // TODO fix this!
      //clientNeedEAR_ = false;


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
      IStatus errorStatus = StatusUtils.errorStatus(msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED",
          new String[] { e.getMessage() }), e);
      env.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
  }
  
  private void setDefaultClientEarProject()
  {
    
    //Determine if an ear selection is needed based on the server type.
    boolean clientNeedEAR_ = true;
    String serverId = clientIds_.getServerId();
    if (serverId != null)
    {
        //Use the server type
        String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverId);
        if (serverTargetId!=null && serverTargetId.length()>0)
        {
          if (!ServerUtils.isTargetValidForEAR(serverTargetId,"13"))
          {
            //Default the EAR selection to be empty
            clientNeedEAR_ = false;
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
    IServer firstSupportedServer = getFirstSupportedServer(configuredServers, clientRuntimeId_, clientProject);
    if (firstSupportedServer != null)
    {
      clientIds_.setServerId(firstSupportedServer.getServerType().getId());
      clientIds_.setServerInstanceId(firstSupportedServer.getId());
      return Status.OK_STATUS;        
    }    
    
    
    //Choose any suitable existing server
    IServer[] servers = ServerCore.getServers();
    IServer supportedServer = getFirstSupportedServer(servers, clientRuntimeId_, clientProject);
    if (supportedServer != null)
    {
      clientIds_.setServerId(supportedServer.getServerType().getId());
      clientIds_.setServerInstanceId(supportedServer.getId());
      return Status.OK_STATUS;        
    }        
    
    //No suitable existing server was found. Choose a suitable server type
    String[] serverTypes = WebServiceRuntimeExtensionUtils2.getServerFactoryIdsByClientRuntime(clientRuntimeId_);
    if (serverTypes!=null && serverTypes.length>0)
    {
      //TODO give priority to a server type that corresponds to the runtime associated with
      //this project, if any.
      clientIds_.setServerId(serverTypes[0]);
      return Status.OK_STATUS;
    }
    
    //No suitable server was found. Popup an error.
    String runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(clientIds_.getRuntimeId());
    String serverLabels = getServerLabels(clientIds_.getRuntimeId());    
    IStatus status = StatusUtils.errorStatus(msgUtils_.getMessage("MSG_ERROR_NO_SERVER_RUNTIME", new String[]{runtimeLabel, serverLabels}) );
    return status;
  }
  
  private IServer getFirstSupportedServer(IServer[] servers, String clientRuntimeId, IProject clientProject)
  {
    if (servers != null && servers.length > 0) {
      for (int i = 0; i < servers.length; i++)
      {
        String serverFactoryId = servers[i].getServerType().getId();
        if (WebServiceRuntimeExtensionUtils2.doesClientRuntimeSupportServer(clientRuntimeId, serverFactoryId))
        {
          //TODO check if the server type supports the project before returning.
          return servers[i];
        }
      }
    }
    return null;
  }    
  
  private String getDefaultClientProjectTemplate()
  {
    String[] templates = WebServiceRuntimeExtensionUtils2.getClientProjectTemplates(clientIds_.getTypeId(), clientIds_.getRuntimeId());
    return templates[0];
  }
  
  private String getDefaultClientProjectName()
  {
    IProject[] projects = FacetUtils.getAllProjects();
    ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId_);
    RequiredFacetVersion[] rfvs = desc.getRequiredFacetVersions();
    
    //Check each project for compatibility with the clientRuntime
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
            clientFacetMatcher_ = fm;
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
  
  private String getDefaultClientRuntime(IProject project)
  {
    //If possible, pick a Web service runtime that works with the initially selected project.
    //If the initially selected project does not work with any of the Web service runtimes, pick the 
    //preferred Web service runtime.
    
    String[] clientRuntimes = WebServiceRuntimeExtensionUtils2.getClientRuntimesByType(clientIds_.getTypeId());
    if (project != null && project.exists())
    {
      for (int i=0; i<clientRuntimes.length; i++)
      {
        RequiredFacetVersion[] rfv = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimes[i]).getRequiredFacetVersions();
        try
        {
          IFacetedProject fproject = ProjectFacetsManager.create(project);
          if (fproject != null)
          {
            Set facetVersions = fproject.getProjectFacets();
            FacetMatcher fm = FacetUtils.match(rfv, facetVersions);
            if (fm.isMatch())
            {
              clientFacetMatcher_ = fm;
              clientProjectName_ = project.getName();
              return clientRuntimes[i];
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
    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
    String runtimeId = context.getRuntimeId();
    for (int j=0; j<clientRuntimes.length; j++ )
    {
      ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimes[j]);
      if (desc.getRuntime().getId().equals(runtimeId))
      {
        return desc.getId();
      }
    }
    
    if (clientRuntimes.length > 0)
      return WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimes[0]).getId();
    else
      return null;
  }
   
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.internal.provisional.env.core.common.Environment)
   */
  /*
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    
    try
    {
        	
	  String[] runtimeIds = WebServiceRuntimeExtensionUtils.getRuntimesByClientType(clientIds_.getTypeId()); 
      SelectionList list = new SelectionList(runtimeIds, 0);
      Vector choices = new Vector();
      for (int i = 0; i < runtimeIds.length; i++) {
        choices.add(getClientTypesChoice(runtimeIds[i]));
      }
      runtimeClientTypes_ = new SelectionListChoices(list, choices);
      setClientDefaultRuntimeFromPreference();
      setClientDefaultJ2EEVersionFromPreference();
      setClientComponentType();
      clientRuntimeJ2EEType_ = getClientRuntimeAndJ2EEFromProject(clientInitialProject_, clientInitialComponentName_);
      if (clientRuntimeJ2EEType_ != null)
      {
        clientJ2EEVersion_ = clientRuntimeJ2EEType_.getJ2eeVersionId();
        setClientRuntimeId(clientRuntimeJ2EEType_.getWsrId());
        setClientProjectType(clientRuntimeJ2EEType_.getClientProjectTypeId());
        
      }
	  

      //If clientInitialProject is the service project, check the initialInitialProject
      //to see if it is valid. 
      ValidationUtils vu = new ValidationUtils();
      if (vu.isProjectServiceProject(clientInitialProject_, wsdlURI_, parser_))
      {            
        clientRuntimeJ2EEType_ = getClientRuntimeAndJ2EEFromProject(initialInitialProject_, initialInitialComponentName_);
        if (clientRuntimeJ2EEType_ != null)
        {
          clientJ2EEVersion_ = clientRuntimeJ2EEType_.getJ2eeVersionId();
          setClientRuntimeId(clientRuntimeJ2EEType_.getWsrId());
          setClientProjectType(clientRuntimeJ2EEType_.getClientProjectTypeId());
          //Since the original clientIntialProject was invalid but initialInitialProject is valid,
          //reset clientInitalProject_ to be initialInitialProject for the benefit of
          //downstream project defaulting.
          clientInitialProject_ = initialInitialProject_;
          clientInitialComponentName_ = initialInitialComponentName_;
        }
      }
    
      setClientDefaultProject();
      setClientDefaultEAR();
      IStatus serverStatus = setClientDefaultServer();
      if (serverStatus.getSeverity()== Status.ERROR)
      {
          env.getStatusHandler().reportError(serverStatus);
          return serverStatus;
      }
      updateClientEARs();
      
      //Calculate default IWebServiceClient
      setDefaultsForExtension(env);
      return Status.OK_STATUS;
    } catch (Exception e)
    {
      //Catch all Exceptions in order to give some feedback to the user
      IStatus errorStatus = StatusUtils.errorStatus(msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED",new String[]{e.getMessage()}), e);
      env.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
    
  }
  */

  /**
   * 
   * @param runtimeId
   * @return
   */
  /*
  private SelectionListChoices getClientTypesChoice(String runtimeId)
  {
    String[] clientComponentTypes;
	clientComponentTypes = WebServiceRuntimeExtensionUtils.getClientProjectTypes(clientIds_.getTypeId(), runtimeId);
    SelectionList list = new SelectionList(clientComponentTypes, 0);
    Vector choices = new Vector();
    for (int i = 0; i < clientComponentTypes.length; i++) {
      choices.add(getProjectChoice(clientComponentTypes[i]));
    }
    return new SelectionListChoices(list, choices);
  }
  */  
  /**
   * 
   * @param clientType
   * @return
   * 
   */
  private SelectionListChoices getProjectChoice(String clientType)
  {
    //IProject[] projects = ClientProjectTypeRegistry.getInstance().getProjects(clientType);	  
    //String[] projectNames = new String[projects.length];
	    //for (int i = 0; i < projectNames.length; i++)
		  //    projectNames[i] = projects[i].getName();	  
	String[] projectNames = J2EEUtils.getProjectsContainingComponentOfType(clientType);
    SelectionList list = new SelectionList(projectNames, 0);
    Vector choices = new Vector();
    for (int i = 0; i < projectNames.length; i++)
	{
	  IProject project = ProjectUtilities.getProject(projectNames[i]);
      choices.add(getProjectEARChoice(project));
	}
    return new SelectionListChoices(list, choices, getEARProjects());
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
    /*
    IProject[] earProjects = J2EEUtils.getEARProjects();
    String[] earProjectNames = new String[earProjects.length];
    for (int i=0; i<earProjects.length; i++)
    {
      earProjectNames[i] = earProjects[i].getName();
    }
    */
    String[] flexProjects = getAllFlexibleProjects();
    return new SelectionList(flexProjects, 0);
  }

  // rskreg
  /*
  private String[] gatherAttributeValues(IConfigurationElement[] elements, String key)
  {
    Vector v = new Vector();
    for (int i = 0; i < elements.length; i++)
    {
      String value = elements[i].getAttribute(key);
      if (value != null && value.length() > 0)
      {
        if (!v.contains(value))
          v.add(value);
      }
    }
    return (String [])v.toArray(new String[0]);
  }
  */
  // rskreg
  
  /*
  private void setClientDefaultRuntimeFromPreference()
  {
    PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
    String pRuntimeId = context.getRuntimeId();

    //set the client runtime to be the preferred runtime if the client type allows.
    setClientRuntimeId(pRuntimeId);
  }
  */
  /*
  private void setClientRuntimeId(String id)
  {
    String[] clientRuntimeIds = getRuntime2ClientTypes().getList().getList();
    for (int i=0; i<clientRuntimeIds.length;i++)
    {
    	if(clientRuntimeIds[i].equals(id))
    	{
    	  getClientTypeRuntimeServer().setRuntimeId(id);
		  getRuntime2ClientTypes().getList().setIndex(i);
		  break;
   	    }
    }    
  }
  */
  /*
  private void setClientProjectType(String id)
  {
    String[] clientProjectTypeIds = getRuntime2ClientTypes().getChoice().getList().getList();
    for (int i=0; i<clientProjectTypeIds.length;i++)
    {
    	if(clientProjectTypeIds[i].equals(id))
    	{
    	  getRuntime2ClientTypes().getChoice().getList().setIndex(i);
		  break;
   	    }
    }        
  }
  */
  /*
  protected void setClientDefaultJ2EEVersionFromPreference()
  {
    if (clientIds_ != null)
    {
      String runtimeId = clientIds_.getRuntimeId();
      if (runtimeId != null)
      {
        //IWebServiceRuntime wsr = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceRuntimeById(runtimeId);
        WebServiceRuntimeInfo wsrt = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(runtimeId);
        if (wsrt != null)
        {
          String[] versions = wsrt.getJ2eeLevels();
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
          	      clientJ2EEVersion_ = versions[i];
          		  return;
          		}
          	  }
          	}
          	clientJ2EEVersion_ = versions[0];
          	return;          	
          }
        }
      }
    }  	
  }
  */

  /*
  private WSRuntimeJ2EEType getClientRuntimeAndJ2EEFromProject(IProject project, String componentName)
  {
    WSRuntimeJ2EEType cRJ2EE = null;
    //If there is a valid initial selection, use it to determine
    //reasonable J2EE version and Web service runtime values

    if (project != null && project.exists())
    {   
      boolean isValidComponentType = false;
      if (componentName != null && componentName.length()>0)
      {
        isValidComponentType = J2EEUtils.isWebComponent(project) ||
                                     J2EEUtils.isEJBComponent(project) ||
                                     J2EEUtils.isAppClientComponent(project) ||
                                     J2EEUtils.isJavaComponent(project);
      }
      
      if (isValidComponentType)
      {
        //WebServiceClientTypeRegistry wsctReg = WebServiceClientTypeRegistry.getInstance();
        
        //Get the J2EE level
        String versionString = null;
        if (!J2EEUtils.isJavaComponent(project))
        {
	        int versionId = J2EEUtils.getJ2EEVersion(project);        
	        versionString = String.valueOf(versionId);
        }

        //Get the runtime target of the project
        IRuntime runtimeTarget = ServerSelectionUtils.getRuntimeTarget(project.getName());
        String runtimeTargetId = null;
        if (runtimeTarget != null) 
          runtimeTargetId = runtimeTarget.getRuntimeType().getId();
        
        //Get the client project type
        //String clientProjectTypeId = getClientProjectTypeFromRuntimeId(project, clientIds_.getRuntimeId());
        String clientComponentTypeId = J2EEUtils.getComponentTypeId(project);        
        
        //If the preferred runtime supports this J2EE level and server target, keep it
        if ((versionString == null || WebServiceRuntimeExtensionUtils.doesRuntimeSupportJ2EELevel(versionString, clientIds_.getRuntimeId())) &&
            ((runtimeTarget == null) ||
             ((runtimeTarget != null) && WebServiceRuntimeExtensionUtils.doesRuntimeSupportServerTarget(runtimeTargetId, clientIds_.getRuntimeId()))) &&
             (WebServiceRuntimeExtensionUtils.doesRuntimeSupportComponentType(clientIds_.getTypeId(), clientIds_.getRuntimeId(), clientComponentTypeId))
           )
        {
          //Set the J2EE level and web service runtime to match the project
          cRJ2EE = new WSRuntimeJ2EEType();
          cRJ2EE.setJ2eeVersionId(versionString);
          cRJ2EE.setWsrId(clientIds_.getRuntimeId());
          cRJ2EE.setClientProjectTypeId(clientComponentTypeId);
          return cRJ2EE;
        } else
        {
          //Look for a runtime that matches
          String[] validRuntimes = WebServiceRuntimeExtensionUtils.getRuntimesByClientType(clientIds_.getTypeId());
          for (int i = 0; i < validRuntimes.length; i++)
          {
            //String thisClientProjectTypeId = getClientProjectTypeFromRuntimeId(project, validRuntimes[i]); 
            if ((versionString == null || WebServiceRuntimeExtensionUtils.doesRuntimeSupportJ2EELevel(versionString, validRuntimes[i])) &&
                ((runtimeTarget == null) ||
                 ((runtimeTarget != null) && WebServiceRuntimeExtensionUtils.doesRuntimeSupportServerTarget(runtimeTargetId, validRuntimes[i]))) &&
                 (WebServiceRuntimeExtensionUtils.doesRuntimeSupportComponentType(clientIds_.getTypeId(), validRuntimes[i], clientComponentTypeId))
                )
            {
              cRJ2EE = new WSRuntimeJ2EEType();
              cRJ2EE.setJ2eeVersionId(versionString);
              cRJ2EE.setWsrId(validRuntimes[i]);
              cRJ2EE.setClientProjectTypeId(clientComponentTypeId);
              return cRJ2EE;
            }
          }
        }
      }
    }    
    return cRJ2EE;
  }
  */
  
  /*
  private void setClientComponentType()
  {
	  getRuntime2ClientTypes().getChoice().getList().setIndex(0);	  
  }
  */
  /*
  private void setClientDefaultProjectNew()
  {
    if (clientInitialProject_ != null)
    {
      getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientInitialProject_.getName());
      String moduleName = null;
      if (clientInitialComponentName_!=null && clientInitialComponentName_.length()>0)
      {
        moduleName = clientInitialComponentName_;
      }
      else
      {
        moduleName = J2EEUtils.getFirstWebModuleName(clientInitialProject_);
      }
      clientComponentName_ = moduleName;
      String version = String.valueOf(J2EEUtils.getJ2EEVersion(clientInitialProject_, moduleName));
      String[] validVersions = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(clientIds_.getRuntimeId()).getJ2eeLevels();
      for (int i=0; i< validVersions.length; i++)
      {
        if (validVersions[i].equals(version))
        {
          clientJ2EEVersion_ = validVersions[i];
        }
      }           
    }
    else
    {
      //Pick the first one
      IProject[] projects = WebServiceRuntimeExtensionUtils.getAllProjects();
      if (projects.length>0)
      {
        getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(projects[0].getName());
        String moduleName = J2EEUtils.getFirstWebModuleName(projects[0]);
        clientComponentName_ = moduleName;
        String version = String.valueOf(J2EEUtils.getJ2EEVersion(projects[0], moduleName));
        String[] validVersions = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(clientIds_.getRuntimeId()).getJ2eeLevels();
        for (int i=0; i< validVersions.length; i++)
        {
          if (validVersions[i].equals(version))
          {
            clientJ2EEVersion_ = validVersions[i];
          }
        }        
        
      }
      else
      {
        //there are no projects in the workspace. Pass the default names for new projects.
        getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(ResourceUtils.getDefaultWebProjectName());
        clientComponentName_ = ResourceUtils.getDefaultWebComponentName();
      }      
    }
  }
  */
  
  /*
  private void setClientDefaultProject()
  {    
	//Handle the case where no valid initial selection exists
    if (clientInitialProject_ == null || (clientInitialProject_!=null && clientRuntimeJ2EEType_==null))
    {
      //Select the first existing project that is valid.
      setClientProjectToFirstValid();
      return;
    }    

    ValidationUtils vu = new ValidationUtils();
    if (!vu.isProjectServiceProject(clientInitialProject_, wsdlURI_, parser_))
    {
      getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientInitialProject_.getName());
      clientComponentName_ = clientInitialComponentName_;
    }
    else
    {
      setClientProjectToFirstValid();
    }

  }
  */

  /*
  private void setClientProjectToFirstValid()
  {
    //WebServiceClientTypeRegistry wsctReg = WebServiceClientTypeRegistry.getInstance();
    ValidationUtils vu = new ValidationUtils();
    String[] projectNames = getRuntime2ClientTypes().getChoice().getChoice().getList().getList();
 
    for (int i=0;i<projectNames.length; i++)
    {
      IProject project = ProjectUtilities.getProject(projectNames[i]);
      IVirtualComponent[] vcs = J2EEUtils.getComponentsByType(project, getClientComponentType());
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
        

        
          if (clientJ2EEVersion_ != null && clientJ2EEVersion_.length()>0 && clientJ2EEVersion_.equals(versionString))
          {
            if (WebServiceRuntimeExtensionUtils.doesRuntimeSupportJ2EELevel(versionString, clientIds_.getRuntimeId()) &&
               ((runtimeTarget == null) || 
               ((runtimeTarget!=null) && WebServiceRuntimeExtensionUtils.doesRuntimeSupportServerTarget(runtimeTargetId, clientIds_.getRuntimeId()))) 
               )
            {
              if (!vu.isProjectServiceProject(project, wsdlURI_, parser_))
              {        	
                getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(projectNames[i]);
                clientComponentName_ = vcs[j].getName();
                return;
              }
            }
          }
        }
      }
    }
    
    //No valid project was found. Enter a new project name.
    getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(ResourceUtils.getDefaultWebProjectName());
    clientComponentName_ = ResourceUtils.getDefaultWebProjectName();
  }
  */
  
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
  private void setClientDefaultEAR()
  {
    //Client-side
    String initialClientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection(); 
    IProject initialClientProject = ProjectUtilities.getProject(initialClientProjectName);    
    //IProject defaultClientEAR = getDefaultEARFromClientProject(initialClientProject);
    String[] clientEARInfo = getDefaultEARFromClientProject(initialClientProject, clientComponentName_);
    
    getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(clientEARInfo[0]);
    clientEarComponentName_ = clientEARInfo[1];
  }
  */
  /*
  private void setClientDefaultEARNew()
  {    
    String initialClientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection(); 
    IProject initialClientProject =   (IProject)((new StringToIProjectTransformer()).transform(initialClientProjectName));
    IVirtualComponent[] earComps = J2EEUtils.getReferencingEARComponents(initialClientProject, clientComponentName_);
    if (earComps.length>0)
    {
      //Pick the first one
      IVirtualComponent earComp = earComps[0];
      String earProjectName = earComp.getProject().getName();
      String earComponentName = earComp.getName();
      getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(earProjectName);
      clientEarComponentName_ = earComponentName;      
    }
    else
    {
      //Component is not associated with any EARs, so pick the first EAR you see with the correct J2EE version.
      IVirtualComponent[] allEarComps = J2EEUtils.getAllEARComponents();
      if (allEarComps.length>0)
      {
        for (int i=0; i<allEarComps.length; i++)
        {
          if (clientJ2EEVersion_.equals(String.valueOf(J2EEUtils.getJ2EEVersion(allEarComps[i]))))
          {
            String earProjectName = allEarComps[i].getProject().getName();
            getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(earProjectName);
            clientEarComponentName_ = allEarComps[i].getName();
            
          }
            
        }
      }
      else
      {
        //there are no Ears.
        getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(ResourceUtils.getDefaultClientEARProjectName());
        clientEarComponentName_ = ResourceUtils.getDefaultClientEARComponentName();
      }
      
      
    }
  }
  */
  /**
   * 
   * @param project
   * @return
   * 
   *  
   */
  
  
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
    
    
  /*
  private IStatus setClientDefaultServer()
  {
	  IStatus status = Status.OK_STATUS;
    //Calculate reasonable default server based on initial project selection. 
    String initialClientProjectName = runtimeClientTypes_.getChoice().getChoice().getList().getSelection(); 
    IProject initialClientProject = ProjectUtilities.getProject(initialClientProjectName);
    if (initialClientProject.exists())
    {
      String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialClientProject, clientComponentName_, clientIds_.getRuntimeId(), true);
      if (serverInfo!=null)
      {
        if (serverInfo[0]!=null && serverInfo[0].length()>0)
        {
          clientIds_.setServerId(serverInfo[0]);
        }
        if (serverInfo[1]!=null && serverInfo[1].length()>0)
        {
          clientIds_.setServerInstanceId(serverInfo[1]);
        }        
      }      
    }
    else //the project does not exist.
    {
      //Does the EAR exist?
      String initialClientEARProjectName = runtimeClientTypes_.getChoice().getChoice().getChoice().getList().getSelection();
      IProject initialClientEARProject = ProjectUtilities.getProject(initialClientEARProjectName);
      if (initialClientEARProject.exists())
      {
        String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialClientEARProject, clientEarComponentName_, clientIds_.getRuntimeId(), false);
        if (serverInfo!=null)
        {
          if (serverInfo[0]!=null && serverInfo[0].length()>0)
          {
            clientIds_.setServerId(serverInfo[0]);
          }
          if (serverInfo[1]!=null && serverInfo[1].length()>0)
          {
            clientIds_.setServerInstanceId(serverInfo[1]);
          }        
        }              
      }
      else
      {
        String[] serverInfo = ServerSelectionUtils.getServerFromWebServceRuntimeAndJ2EE(clientIds_.getRuntimeId(), clientJ2EEVersion_);
        if (serverInfo!=null)
        {
          if (serverInfo[0]!=null && serverInfo[0].length()>0)
          {
            clientIds_.setServerId(serverInfo[0]);
          }
          if (serverInfo[1]!=null && serverInfo[1].length()>0)
          {
            clientIds_.setServerInstanceId(serverInfo[1]);
          }        
        }
        else
        {
        	//Since the project and the EAR are both new, try changing the J2EE level
        	boolean foundServer = false;
        	WebServiceRuntimeInfo wsrt = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(clientIds_.getRuntimeId());
            if (wsrt != null)
            {
              String[] versions = wsrt.getJ2eeLevels();
              if (versions != null && versions.length > 0)
              {
            	  for (int k=0; k<versions.length; k++)
            	  {
            		  //If this J2EE version is different from the current one, see if there is
            		  //a server available.
            		  if (clientJ2EEVersion_!=versions[k])
            		  {
            			  String[] si = ServerSelectionUtils.getServerFromWebServceRuntimeAndJ2EE(clientIds_.getRuntimeId(), versions[k]);
             		      if (si!=null)
            		      {
            		        if (si[0]!=null && si[0].length()>0)
            		        {
            		          clientIds_.setServerId(si[0]);
            		        }
            		        if (si[1]!=null && si[1].length()>0)
            		        {
            		          clientIds_.setServerInstanceId(si[1]);
            		        }             
            		        clientJ2EEVersion_ = versions[k];
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
              String runtimeLabel = WebServiceRuntimeExtensionUtils.getRuntimeLabelById(clientIds_.getRuntimeId());
              String serverLabels = getServerLabels(clientIds_.getRuntimeId());
        	  status = StatusUtils.errorStatus(msgUtils_.getMessage("MSG_ERROR_NO_SERVER_RUNTIME", new String[]{runtimeLabel, serverLabels}) );
            }
        	
        }
      }
      
    }    
    
    return status;
  }  
  */
  protected String getServerLabels(String clientRuntimeId)
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
    
  }
  
  
  /*
  protected void updateClientEARs()
  {
  	//Set EAR selection to null if the project/server defaults imply an EAR should not be created
    String clientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection();
  	IProject clientProject = ProjectUtilities.getProject(clientProjectName);
  	if (clientProject != null && clientProject.exists())
  	{
  	  //Get the runtime target on the serviceProject
  	  IRuntime clientTarget = ServerSelectionUtils.getRuntimeTarget(clientProjectName);
  	  String j2eeVersion = String.valueOf(J2EEUtils.getJ2EEVersion(clientProject));
  	  if (clientTarget != null)
  	  {
  	  	if (!ServerUtils.isTargetValidForEAR(clientTarget.getRuntimeType().getId(),j2eeVersion))
  	  	{
  	      //Default the EAR selection to be empty
  	  	  getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setIndex(-1);
          clientEarComponentName_="";
  	  	  clientNeedEAR_ = false;
  	  	}  	  		
  	  }
  	  
  	}
  	else
  	{
      String serverId = clientIds_.getServerId();
      if (serverId != null)
      {
  		//Use the server type
  		String clientServerTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverId);
  		if (clientServerTargetId!=null && clientServerTargetId.length()>0)
  		{
  		  if (!ServerUtils.isTargetValidForEAR(clientServerTargetId,clientJ2EEVersion_))
  	  	  {
  	        //Default the EAR selection to be empty
  	  	    getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setIndex(-1);
            clientEarComponentName_="";
  	  	    clientNeedEAR_ = false;
  	  	  }
  		}
      }
  	}  	
  }  
  */
  
  private void setDefaultsForExtension(IEnvironment env)
  {
    IWebServiceRuntime wsrt = WebServiceRuntimeExtensionUtils2.getClientRuntime(clientRuntimeId_);
    if (wsrt != null)
    {
      WebServiceClientInfo wsInfo = new WebServiceClientInfo();

      wsInfo.setJ2eeLevel("14"); //rm j2ee
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
    initialInitialProject_ = getProjectFromInitialSelection(initialInitialSelection);
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
  
  private String getComponentNameFromInitialSelection(IStructuredSelection selection)
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
          
          IVirtualComponent comp = ResourceUtils.getComponentOf(resource);
          if (comp!=null)
          {
            return comp.getName();
          }
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;
  }

  /*
  private String getClientProjectTypeFromRuntimeId(IProject p, String runtimeId)
  {
    //Navigate the runtimeClientTypes to see if we can navigate from the provided
    //runtime to the provided project's name.
    String pName = p.getName();
    String[] runtimeIds = getRuntime2ClientTypes().getList().getList();
    int numberOfRuntimes = runtimeIds.length;
    //Get the index of the runtimeId we are interested in
    for (int i=0; i<numberOfRuntimes; i++)
    {
      if (runtimeIds[i].equals(runtimeId))
      {
        //Get the list of client project types for this runtimeId
        SelectionListChoices clientProjectTypesToProjects= getRuntime2ClientTypes().getChoice(i);
        String[] clientProjectTypes = clientProjectTypesToProjects.getList().getList();
        for (int j=0; j<clientProjectTypes.length; j++)
        {
          //Get the list of projects for this clientProjectType. If pName
          //is in this list, we know the runtimeId supports this project
          //and we know the client project type.
          String[] clientProjects = clientProjectTypesToProjects.getChoice(j).getList().getList();
          for (int k=0; k<clientProjects.length; k++)
          {
            if (clientProjects[k].equals(pName))
            {
              //Found the project!!
              return clientProjectTypes[j];
            }
          }
        }
        
      }
    }

    //We didn't find the project under any of this runtimes client project types.
    //This means that this runtime does not support that client type. Return null
    return null;
  }
  */
  
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