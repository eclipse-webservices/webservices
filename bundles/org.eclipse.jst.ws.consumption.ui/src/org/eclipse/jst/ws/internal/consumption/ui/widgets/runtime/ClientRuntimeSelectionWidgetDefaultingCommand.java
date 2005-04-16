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

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.ClientServerSelectionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ServerSelectionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.ClientProjectTypeRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.IWebServiceRuntime;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.selection.SelectionList;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

public class ClientRuntimeSelectionWidgetDefaultingCommand extends SimpleCommand
{   
  protected MessageUtils msgUtils_;
  // clientRuntimeJ2EEType contains the default client runtime and J2EE level based on the initial selection.
  // If the initialSeleciton does not result in a valid client runtime and J2EE level, clientRuntimeJ2EEType
  // should remain null for the life of this instance.  
  private WSRuntimeJ2EEType    clientRuntimeJ2EEType_;
  
  private TypeRuntimeServer    clientIds_;
  private SelectionListChoices runtimeClientTypes_;
  
  //A note on initialSelections ...
  //The difference between clientInitialSelection_ and initialInitialSelection is that
  //clientInitialSelection_ comes from the ObjectSelectionOutputCommand while initialInitialSelection
  //is the actual thing that was selected before the wizard was launched. In the runtime/j2ee/project 
  //defaulting algorithm, clientInitialSelection will be given first priority. If, however, it is 
  //deemed that clientInitialProject is not a valid initial selection, initialInitialSelection
  //will be given second priority. Things that could make an initialSelection invalid include
  //1. The containing project contains the Web service for which we are trying to create a client
  //2. The containing project has a J2EE level, server target, and project type combination which
  //   is not supported by any of the registered Web service runtimes.
  private IStructuredSelection clientInitialSelection_;
  private IProject clientInitialProject_;
  private IStructuredSelection initialInitialSelection_;
  private IProject initialInitialProject_;

  private String clientJ2EEVersion_;
  protected boolean clientNeedEAR_ = true;
  
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
  
  public SelectionListChoices getRuntime2ClientTypes()
  {
    return runtimeClientTypes_;
  }
  
  public String getClientJ2EEVersion()
  {
    return clientJ2EEVersion_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.env.core.common.Environment)
   */
  public Status execute(Environment environment)
  {
    try
    {
	  // rskreg		
      //WebServiceClientTypeRegistry reg = WebServiceClientTypeRegistry.getInstance();

      //String[] runtimeIds = gatherAttributeValues(reg.getConfigurationElement(clientIds_.getTypeId()), "runtime");
	  String[] runtimeIds = WebServiceRuntimeExtensionUtils.getRuntimesByClientType(clientIds_.getTypeId()); 
	  // rskreg
      SelectionList list = new SelectionList(runtimeIds, 0);
      Vector choices = new Vector();
      for (int i = 0; i < runtimeIds.length; i++) {
        choices.add(getClientTypesChoice(runtimeIds[i]));
      }
      runtimeClientTypes_ = new SelectionListChoices(list, choices);
      //setClientDefaultRuntimeFromPreference();
      //setClientDefaultJ2EEVersionFromPreference();
      //clientRuntimeJ2EEType_ = getClientRuntimeAndJ2EEFromProject(clientInitialProject_);
      //if (clientRuntimeJ2EEType_ != null)
      //{
        //clientJ2EEVersion_ = clientRuntimeJ2EEType_.getJ2eeVersionId();
        //setClientRuntimeId(clientRuntimeJ2EEType_.getWsrId());
        //setClientProjectType(clientRuntimeJ2EEType_.getClientProjectTypeId());
      //}
	  setClientRuntimeId((WebServiceRuntimeExtensionUtils.getRuntimesByClientType(clientIds_.getTypeId()))[0]);
	  clientJ2EEVersion_ = (WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(clientIds_.getRuntimeId()).getJ2eeLevels())[0];
	  
      //If clientInitialProject is the service project, check the initialInitialProject
      //to see if it is valid.
	  /*
      ValidationUtils vu = new ValidationUtils();
      if (vu.isProjectServiceProject(clientInitialProject_, wsdlURI_, parser_))
      {            
        clientRuntimeJ2EEType_ = getClientRuntimeAndJ2EEFromProject(initialInitialProject_);
        if (clientRuntimeJ2EEType_ != null)
        {
          clientJ2EEVersion_ = clientRuntimeJ2EEType_.getJ2eeVersionId();
          setClientRuntimeId(clientRuntimeJ2EEType_.getWsrId());
          setClientProjectType(clientRuntimeJ2EEType_.getClientProjectTypeId());
          //Since the original clientIntialProject was invalid but initialInitialProject is valid,
          //reset clientInitalProject_ to be initialInitialProject for the benefit of
          //downstream project defaulting.
          clientInitialProject_ = initialInitialProject_;
        }
      }
      */
      setClientDefaultProject();
      //setClientDefaultEAR();
      setClientDefaultServer();
      updateClientEARs();
      return new SimpleStatus("");
    } catch (Exception e)
    {
      //Catch all Exceptions in order to give some feedback to the user
      Status errorStatus = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED",new String[]{e.getMessage()}),Status.ERROR, e);
      environment.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
    
  }
  

  /**
   * 
   * @param runtimeId
   * @return
   */
  private SelectionListChoices getClientTypesChoice(String runtimeId)
  {
    //WebServiceClientTypeRegistry reg = WebServiceClientTypeRegistry.getInstance();
    ClientProjectTypeRegistry cptr = ClientProjectTypeRegistry.getInstance();
    String[] clientTypes;
	// rskreg
    //String[] clientProjectTypes = gatherAttributeValues(reg.getConfigurationElement(clientIds_.getTypeId(), runtimeId), "clientProjectType");
	clientTypes = WebServiceRuntimeExtensionUtils.getClientProjectTypes(clientIds_.getTypeId(), runtimeId);
	/*
    if (clientProjectTypes!=null && clientProjectTypes.length > 0)
    {
      StringTokenizer st = new StringTokenizer(clientProjectTypes[0]);
      clientTypes = new String[st.countTokens()];
      for (int i = 0; i < clientTypes.length; i++)
        clientTypes[i] = st.nextToken();
    }
    else
      clientTypes = new String[] {cptr.getDefaultElement().getAttribute("id")};
    */
    // rskreg
	// Seng's Note: Check flexible project structure non-support here!!!
    SelectionList list = new SelectionList(clientTypes, 0);
    Vector choices = new Vector();
    for (int i = 0; i < clientTypes.length; i++) {
      choices.add(getProjectChoice(clientTypes[i]));
    }
    return new SelectionListChoices(list, choices);
  }
    
  /**
   * 
   * @param clientType
   * @return
   * 
   */
  private SelectionListChoices getProjectChoice(String clientType)
  {
    IProject[] projects = ClientProjectTypeRegistry.getInstance().getProjects(clientType);
    String[] projectNames = new String[projects.length];
    for (int i = 0; i < projects.length; i++)
      projectNames[i] = projects[i].getName();
    SelectionList list = new SelectionList(projectNames, 0);
    Vector choices = new Vector();
    for (int i = 0; i < projects.length; i++)
      choices.add(getProjectEARChoice(projects[i]));
    return new SelectionListChoices(list, choices, getEARProjects());
  }
  
/**
 * 
 * @param project
 * @return
 */
  protected SelectionListChoices getProjectEARChoice(IProject project)
  {
    Vector v = new Vector();
	// TODO used to be J2EEUtils.getEARProjects(project) -- referenced EARs
    IProject[] earNatures = J2EEUtils.getEARProjects();
    if (earNatures != null){
      for(int i = 0; i < earNatures.length; i++){
        v.add(earNatures[i].getName());
      }
    }
    IProject[] earProjects = J2EEUtils.getEARProjects();
    if (earProjects != null)
    {
    	for (int i = 0; i < earProjects.length; i++)
    	{
    	  String earProjectName = earProjects[i].getName();
    	  if (!v.contains(earProjectName))
    	  	v.add(earProjectName);
    	}
    }
    SelectionList list = new SelectionList((String[])v.toArray(new String[0]), 0);
    return new SelectionListChoices(list, null);
  }
  
  protected SelectionList getEARProjects()
  {
    IProject[] earProjects = J2EEUtils.getEARProjects();
    String[] earProjectNames = new String[earProjects.length];
    for (int i=0; i<earProjects.length; i++)
    {
      earProjectNames[i] = earProjects[i].getName();
    }
    return new SelectionList(earProjectNames, 0);
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
  // rskreg
  
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
  
  // rskreg
  /*
  protected void setClientDefaultJ2EEVersionFromPreference()
  {
    if (clientIds_ != null)
    {
      String runtimeId = clientIds_.getRuntimeId();
      if (runtimeId != null)
      {
        IWebServiceRuntime wsr = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceRuntimeById(runtimeId);
        if (wsr != null)
        {
          String[] versions = wsr.getJ2EEVersions();
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
  // rskreg
  // rskreg
  /*
  private WSRuntimeJ2EEType getClientRuntimeAndJ2EEFromProject(IProject project)
  {
    WSRuntimeJ2EEType cRJ2EE = null;
    //If there is a valid initial selection, use it to determine
    //reasonable J2EE version and Web service runtime values

    if (project != null && project.exists())
    {
      if (ResourceUtils.isWebProject(project) || ResourceUtils.isEJBProject(project) || ResourceUtils.isAppClientProject(project) || ResourceUtils.isTrueJavaProject(project))
      {
        WebServiceClientTypeRegistry wsctReg = WebServiceClientTypeRegistry.getInstance();
        
        //Get the J2EE level
        String versionString = null;
        if (!ResourceUtils.isTrueJavaProject(project))
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
        String clientProjectTypeId = getClientProjectTypeFromRuntimeId(project, clientIds_.getRuntimeId());
        
        //If the preferred runtime supports this J2EE level and server target, keep it
        if ((versionString == null || wsctReg.doesRuntimeSupportJ2EELevel(versionString, clientIds_.getRuntimeId())) &&
            ((runtimeTarget == null) ||
             ((runtimeTarget != null) && wsctReg.doesRuntimeSupportServerTarget(runtimeTargetId, clientIds_.getRuntimeId()))) &&
             (clientProjectTypeId != null)
           )
        {
          //Set the J2EE level and web service runtime to match the project
          cRJ2EE = new WSRuntimeJ2EEType();
          cRJ2EE.setJ2eeVersionId(versionString);
          cRJ2EE.setWsrId(clientIds_.getRuntimeId());
          cRJ2EE.setClientProjectTypeId(clientProjectTypeId);
          return cRJ2EE;
        } else
        {
          //Look for a runtime that matches
          String[] validRuntimes = wsctReg.getRuntimesByType(clientIds_.getTypeId());
          for (int i = 0; i < validRuntimes.length; i++)
          {
            String thisClientProjectTypeId = getClientProjectTypeFromRuntimeId(project, validRuntimes[i]); 
            if ((versionString == null || wsctReg.doesRuntimeSupportJ2EELevel(versionString, validRuntimes[i])) &&
                ((runtimeTarget == null) ||
                 ((runtimeTarget != null) && wsctReg.doesRuntimeSupportServerTarget(runtimeTargetId, validRuntimes[i]))) &&
                 (thisClientProjectTypeId != null)
                )
            {
              cRJ2EE = new WSRuntimeJ2EEType();
              cRJ2EE.setJ2eeVersionId(versionString);
              cRJ2EE.setWsrId(validRuntimes[i]);
              cRJ2EE.setClientProjectTypeId(thisClientProjectTypeId);
              return cRJ2EE;
            }
          }
        }
      }
    }    
    return cRJ2EE;
  }
  */
  // rskreg
  private void setClientDefaultProject()
  {
    if (clientInitialProject_ != null)
    {
      getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientInitialProject_.getName());
      String moduleName = J2EEUtils.getFirstWebModuleName(clientInitialProject_);
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
  }
  // rskreg
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
    WebServiceClientTypeRegistry wsctReg = WebServiceClientTypeRegistry.getInstance();
    ValidationUtils vu = new ValidationUtils();
    String[] projectNames = getRuntime2ClientTypes().getChoice().getChoice().getList().getList();
    for (int i=0;i<projectNames.length; i++)
    {
      IProject project = (IProject)((new StringToIProjectTransformer().transform(projectNames[i])));
      if (project.isOpen() && (ResourceUtils.isWebProject(project) || ResourceUtils.isEJBProject(project) || ResourceUtils.isAppClientProject(project)))
      {
        //Get the J2EE level
        int versionId = J2EEUtils.getJ2EEVersion(project);
        String versionString = String.valueOf(versionId);
        
        //Get the runtime target of the project
        IRuntime runtimeTarget = ServerSelectionUtils.getRuntimeTarget(project.getName());
        String runtimeTargetId = null;
        if (runtimeTarget != null) 
          runtimeTargetId = runtimeTarget.getRuntimeType().getId();
        
        if (clientJ2EEVersion_ != null && clientJ2EEVersion_.length()>0 && clientJ2EEVersion_.equals(versionString))
        {
          if (wsctReg.doesRuntimeSupportJ2EELevel(versionString, clientIds_.getRuntimeId()) &&
             ((runtimeTarget == null) || 
             ((runtimeTarget!=null) && wsctReg.doesRuntimeSupportServerTarget(runtimeTargetId, clientIds_.getRuntimeId()))) 
             )
          {
            if (!vu.isProjectServiceProject(project, wsdlURI_, parser_))
            {        	
              getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(projectNames[i]);
              return;
            }
          }
        }
      }
    }
    
    //No valid project was found. Enter a new project name.
    getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(ResourceUtils.getDefaultWebProjectName());
  }
  */
  // rskreg
  
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
  
  private void setClientDefaultEAR()
  {
  	//Client-side
    String initialClientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection(); 
    IProject initialClientProject = (IProject)((new StringToIProjectTransformer()).transform(initialClientProjectName));  	
  	IProject defaultClientEAR = getDefaultEARFromClientProject(initialClientProject);
  	getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(defaultClientEAR.getName());
  }
  
  /**
   * 
   * @param project
   * @return
   * 
   * @deprecated Needs to be re-written to deal with flexible project structure APIs 
   */
  protected IProject getDefaultEARFromClientProject(IProject project)
  {
    if (project!=null && project.exists()) 
    {
      IServer[] configuredServers = ServerUtil.getServersByModule(ResourceUtils.getModule(project), null);
      IServer firstSupportedServer = ClientServerSelectionUtils.getFirstSupportedServer(configuredServers, clientIds_.getTypeId() );
      
      EARNatureRuntime[] earProjects = J2EEUtils.getEARProjects(project, firstSupportedServer);
      if (earProjects!=null && earProjects[0] instanceof EARNatureRuntime) 
        return earProjects[0].getProject();
    }
    
    int versionId = -1;
    if (clientJ2EEVersion_ != null && clientJ2EEVersion_.length()>0)
    {
      versionId = Integer.parseInt(clientJ2EEVersion_);
    }
    EARNatureRuntime newEAR = J2EEUtils.getEAR(versionId);

    IProject earProject = ResourceUtils.getWorkspaceRoot().getProject(ResourceUtils.getDefaultClientEARProjectName());
      
    if (newEAR!=null)
      earProject = newEAR.getProject();

    return earProject;  	
  }
  
  private void setClientDefaultServer()
  {
    //Temporarily pick the first existing server in the workspace
    IServer[] servers = ServerCore.getServers();
    if (servers.length > 0)
    {
      clientIds_.setServerId(servers[0].getServerType().getId());
      clientIds_.setServerInstanceId(servers[0].getId());
    }
    else
    {
     clientIds_.setServerId((WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(clientIds_.getRuntimeId()).getServerFactoryIds())[0]);
    }    
  }
  /*
  private void setClientDefaultServer()
  {
    //Calculate reasonable default server based on initial project selection. 
    String initialClientProjectName = runtimeClientTypes_.getChoice().getChoice().getList().getSelection(); 
    IProject initialClientProject = (IProject)((new StringToIProjectTransformer()).transform(initialClientProjectName));
    if (initialClientProject.exists())
    {
      String[] serverInfo = ClientServerSelectionUtils.getServerInfoFromExistingProject(initialClientProject, clientIds_.getTypeId(), clientIds_.getRuntimeId(), true);
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
      IProject initialClientEARProject = (IProject)((new StringToIProjectTransformer()).transform(initialClientEARProjectName));
      if (initialClientEARProject.exists())
      {
        String[] serverInfo = ClientServerSelectionUtils.getServerInfoFromExistingProject(initialClientEARProject, clientIds_.getTypeId(), clientIds_.getRuntimeId(), false);
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
        String[] serverInfo = ClientServerSelectionUtils.getServerFromClientRuntimeAndJ2EE(clientIds_.getRuntimeId(), clientJ2EEVersion_);
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
      
    }        
  }  
  */
  
  protected void updateClientEARs()
  {
  	//Set EAR selection to null if the project/server defaults imply an EAR should not be created
    String clientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection();
  	IProject clientProject = (IProject)((new StringToIProjectTransformer()).transform(clientProjectName));
  	if (clientProject != null && clientProject.exists())
  	{
  	  //Get the runtime target on the serviceProject
  	  IRuntime clientTarget = ClientServerSelectionUtils.getRuntimeTarget(clientProjectName);
  	  String j2eeVersion = String.valueOf(J2EEUtils.getJ2EEVersion(clientProject));
  	  if (clientTarget != null)
  	  {
  	  	if (!ServerUtils.isTargetValidForEAR(clientTarget.getRuntimeType().getId(),j2eeVersion))
  	  	{
  	      //Default the EAR selection to be empty
  	  	  getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setIndex(-1);
  	  	  clientNeedEAR_ = false;
  	  	}  	  		
  	  }
  	  
  	}
  	else
  	{
  		//Use the server type
  		String clientServerTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(clientIds_.getServerId());
  		if (clientServerTargetId!=null && clientServerTargetId.length()>0)
  		{
  		  if (!ServerUtils.isTargetValidForEAR(clientServerTargetId,clientJ2EEVersion_))
  	  	  {
  	        //Default the EAR selection to be empty
  	  	    getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setIndex(-1);
  	  	    clientNeedEAR_ = false;
  	  	  }
  		}
  	}  	
  }  
  
  public void setClientInitialSelection(IStructuredSelection selection)
  {
    clientInitialSelection_ = selection;
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
    initialInitialSelection_ = initialInitialSelection;
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