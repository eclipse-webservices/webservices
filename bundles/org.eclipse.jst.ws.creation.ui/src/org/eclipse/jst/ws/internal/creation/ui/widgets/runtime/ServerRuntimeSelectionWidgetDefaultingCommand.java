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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.ServerSelectionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.IWebServiceRuntime;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.context.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.selection.SelectionList;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;

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
  private IStructuredSelection initialSelection_;
  private IProject initialProject_;
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
     webServiceRuntimeJ2EEType = getWSRuntimeAndJ2EEFromProject(initialProject_);
     if (webServiceRuntimeJ2EEType != null)
     {
       serviceJ2EEVersion_ = webServiceRuntimeJ2EEType.getJ2eeVersionId();
       serviceIds_.setRuntimeId(webServiceRuntimeJ2EEType.getWsrId()); 
     }
   
     // Set the default client type to a web client type.
     setWebClientType();
     
     // Default projects EARs and servers.
     setDefaultProjects();
     setDefaultEARs();
     setDefaultServer();
     updateServiceEARs();
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

  private void setWebClientType()
  {
    SelectionListChoices choices = getRuntime2ClientTypes();
    String               webId   = "com.ibm.etools.webservice.consumption.ui.clientProjectType.Web";
    
    if( choices != null )
    {
      choices.getChoice().getList().setSelectionValue( webId );   
    }
  }
  
  private WSRuntimeJ2EEType getWSRuntimeAndJ2EEFromProject(IProject project)
  {
    WSRuntimeJ2EEType wsrJ2EE = null;
    //If there is a valid initial selection, use it to determine
    //reasonable J2EE version and Web service runtime values

    if (project != null && project.exists())
    {
      if (ResourceUtils.isWebProject(project) || ResourceUtils.isEJBProject(project))
      {
        WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
        
        //Get the J2EE level
        int versionId = J2EEUtils.getJ2EEVersion(project);
        String versionString = String.valueOf(versionId);
        
        //Get the runtime target of the project
        IRuntime runtimeTarget = ServerSelectionUtils.getRuntimeTarget(project.getName());
        String runtimeTargetId = null;
        if (runtimeTarget != null) 
          runtimeTargetId = runtimeTarget.getRuntimeType().getId();
        
        //If the preferred Web service runtime supports this J2EE level and this server target, keep it
        if (wssrtReg.doesRuntimeSupportJ2EELevel(versionString, serviceIds_.getRuntimeId()) &&
            ((runtimeTarget == null) || 
             (runtimeTarget != null && wssrtReg.doesRuntimeSupportServerTarget(runtimeTargetId, serviceIds_.getRuntimeId()))
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
          String[] validRuntimes = wssrtReg.getRuntimesByType(serviceIds_.getTypeId());
          for (int i = 0; i < validRuntimes.length; i++)
          {
            if (wssrtReg.doesRuntimeSupportJ2EELevel(versionString, validRuntimes[i]) &&
                ((runtimeTarget == null) ||
                 (runtimeTarget!=null && wssrtReg.doesRuntimeSupportServerTarget(runtimeTargetId, validRuntimes[i]))
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
    WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
    //if the preferred runtime in compatible with the type, select it.
    if (wssrtReg.isRuntimeSupportedForType(serviceIds_.getTypeId(), pRuntimeId))
    {
      serviceIds_.setRuntimeId(pRuntimeId);
    }
    else
    {
      //Set the Web service runtime to one that is supported by the selected type.
      String[] validRuntimes = wssrtReg.getRuntimesByType(serviceIds_.getTypeId());
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
        IWebServiceRuntime wsr = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceRuntimeById(runtimeId);
        if (wsr != null)
        {
          String[] versions = wsr.getJ2EEVersions();          
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
  private void setDefaultProjects()
  {
	
	//Handle the case where no valid initial selection exists
    if (initialProject_== null || (initialProject_!=null && webServiceRuntimeJ2EEType==null))
    {
      //Select the first existing project that is valid.

      setServiceProjectToFirstValid();
      String serviceProjectName = getServiceProject2EARProject().getList().getSelection();
     
      //Select a client project with "client" added to the above project name.
      String clientProjectName = ResourceUtils.getClientWebProjectName(serviceProjectName, serviceIds_.getTypeId() );
      getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientProjectName);
      return;
    }    
	    


	//Set the service project selection to this initialProject
	getServiceProject2EARProject().getList().setSelectionValue(initialProject_.getName());
	String clientProjectName = ResourceUtils.getClientWebProjectName(initialProject_.getName(), serviceIds_.getTypeId() ); 
	//Set the client project selection to clientProject
	getRuntime2ClientTypes().getChoice().getChoice().getList().setSelectionValue(clientProjectName);
		      	    
  }
  
  private void setServiceProjectToFirstValid()
  {
    WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
    String[] projectNames = getServiceProject2EARProject().getList().getList();
    for (int i=0;i<projectNames.length; i++)
    {
      IProject project = (IProject)((new StringToIProjectTransformer().transform(projectNames[i])));
      if (project.isOpen() && (ResourceUtils.isWebProject(project) || ResourceUtils.isEJBProject(project)))
      {
        //Get the J2EE level
        int versionId = J2EEUtils.getJ2EEVersion(project);
        String versionString = String.valueOf(versionId);
        
        //Get the runtime target of the project
        IRuntime runtimeTarget = ServerSelectionUtils.getRuntimeTarget(project.getName());
        String runtimeTargetId = null;
        if (runtimeTarget != null) 
          runtimeTargetId = runtimeTarget.getRuntimeType().getId();
        
        if (serviceJ2EEVersion_!=null && serviceJ2EEVersion_.length()>0 && serviceJ2EEVersion_.equals(versionString))
        {
          if (wssrtReg.doesRuntimeSupportJ2EELevel(versionString, serviceIds_.getRuntimeId()) &&
             ((runtimeTarget == null) || 
             ((runtimeTarget!=null) && wssrtReg.doesRuntimeSupportServerTarget(runtimeTargetId, serviceIds_.getRuntimeId()))) 
             )
          {
            getServiceProject2EARProject().getList().setSelectionValue(projectNames[i]);
            return;
          }
        }
      }
    }
    
    //Didn't find a single project that worked. Default to a new project name.
    getServiceProject2EARProject().getList().setSelectionValue(ResourceUtils.getDefaultWebProjectName());
  }
  
  
  private void setDefaultEARs()
  {

  	//Service-side
    String initialProjectName = getServiceProject2EARProject().getList().getSelection(); 
    IProject initialProject = (IProject)((new StringToIProjectTransformer()).transform(initialProjectName));  	
  	IProject defaultServiceEAR = getDefaultEARFromServiceProject(initialProject);
  	getServiceProject2EARProject().getChoice().getList().setSelectionValue(defaultServiceEAR.getName());
  	
  	//Client-side
    String initialClientProjectName = getRuntime2ClientTypes().getChoice().getChoice().getList().getSelection(); 
    IProject initialClientProject = (IProject)((new StringToIProjectTransformer()).transform(initialClientProjectName));  	
  	IProject defaultClientEAR = getDefaultEARFromClientProject(initialClientProject);
  	
  	//If the client project exists and the default EAR is the same as that of the service project, 
  	//pick the defaultClientEAR
  	if(initialClientProject!=null && initialClientProject.exists() 
  	   && defaultClientEAR.getName().equalsIgnoreCase(defaultServiceEAR.getName()))
  	{
  		getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(defaultClientEAR.getName());
  	}
  	else
  	{
      ProjectTopologyContext ptc= WebServicePlugin.getInstance().getProjectTopologyContext();
      if (!ptc.isUseTwoEARs()) {
      	getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(defaultServiceEAR.getName());
      }
      else {
        IProject proxyEARProject = getUniqueClientEAR(defaultClientEAR.getName(), defaultServiceEAR.getName(), initialClientProjectName);
        getRuntime2ClientTypes().getChoice().getChoice().getChoice().getList().setSelectionValue(proxyEARProject.getName());
      }  		
  	}
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
  
  private void setDefaultServer()
  {
    //Calculate reasonable default server based on the default project selection. 

    String initialProjectName = getServiceProject2EARProject().getList().getSelection(); 
    IProject initialProject = (IProject)((new StringToIProjectTransformer()).transform(initialProjectName));
    if (initialProject.exists())
    {
      String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialProject, serviceIds_.getTypeId(), serviceIds_.getRuntimeId(), true);
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
      IProject initialEARProject = (IProject)((new StringToIProjectTransformer()).transform(initialEARProjectName));
      if (initialEARProject.exists())
      {
        String[] serverInfo = ServerSelectionUtils.getServerInfoFromExistingProject(initialEARProject, serviceIds_.getTypeId(), serviceIds_.getRuntimeId(), false);
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
      }
      
    }
    
    
    //Default the client server    
  }
  
  private void updateServiceEARs()
  {
  	//Set EAR selection to null if the project/server defaults imply an EAR should not be created
  	String serviceProjectName = getServiceProject2EARProject().getList().getSelection();
  	IProject serviceProject = (IProject)((new StringToIProjectTransformer()).transform(serviceProjectName));
  	if (serviceProject != null && serviceProject.exists())
  	{
  	  //Get the runtime target on the serviceProject
  	  IRuntime serviceTarget = ServerSelectionUtils.getRuntimeTarget(serviceProjectName);
  	  String j2eeVersion = String.valueOf(J2EEUtils.getJ2EEVersion(serviceProject));
  	  if (serviceTarget != null)
  	  {
  	  	if (!ServerUtils.isTargetValidForEAR(serviceTarget.getRuntimeType().getId(),j2eeVersion))
  	  	{
  	      //Default the EAR selection to be empty
  	  	  getServiceProject2EARProject().getChoice().getList().setIndex(-1);
  	  	  serviceNeedEAR_ = false;
  	  	}
  	  		
  	  }
  	}
  	else
  	{
  		//Use the server type
  		String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serviceIds_.getServerId());
  		if (serverTargetId!=null && serverTargetId.length()>0)
  		{
  		  if (!ServerUtils.isTargetValidForEAR(serverTargetId,serviceJ2EEVersion_))
  	  	  {
  	        //Default the EAR selection to be empty
  	  	    getServiceProject2EARProject().getChoice().getList().setIndex(-1);
  	  	    serviceNeedEAR_ = false;
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
      IProject[] projects = WebServiceServerRuntimeTypeRegistry.getInstance().getProjectsByWebServiceType(serviceIds_.getTypeId());
	  
	  String[] projectNames = new String[projects.length];
	  for (int i = 0; i < projects.length; i++) {
  		projectNames[i] = projects[i].getName();
	  }
	  SelectionList list = new SelectionList(projectNames, 0);
	  Vector choices = new Vector();
	  for (int i = 0; i < projects.length; i++)
	    choices.add(getProjectEARChoice(projects[i]));
	    
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
  
  public boolean getServiceNeedEAR()
  {
    return serviceNeedEAR_;
  }
  
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
  
  
}