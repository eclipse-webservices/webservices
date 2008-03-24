/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 2007104   114835 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20070314  176886 pmoogk@ca.ibm.com - Peter Moogk
 * 20070502  185208 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20070509  180567 sengpl@ca.ibm.com - Seng Phung-Lu 
 * 20070705  195553 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20071116  208124 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20080313  126774 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.tests.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.context.PersistentScenarioContext;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.jst.ws.tests.plugin.TestsPlugin;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.common.WaitForAutoBuildCommand;
import org.eclipse.wst.command.internal.env.context.PersistentActionDialogsContext;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.eclipse.AccumulateStatusHandler;
import org.eclipse.wst.command.internal.env.preferences.ActionDialogPreferenceType;
import org.eclipse.wst.command.internal.env.ui.eclipse.EnvironmentUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.validation.internal.operations.ValidatorManager;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;


public class JUnitUtils {
	// Begin Server Utilities
	public static IRuntime createServerRuntime(String runtimeTypeId,String serverInstallPath) throws Exception
	{
		IRuntimeType rt = ServerCore.findRuntimeType(runtimeTypeId);
		String tcversion = runtimeTypeId.substring(runtimeTypeId.lastIndexOf(".")+1);
		IRuntimeWorkingCopy wc = rt.createRuntime(tcversion+"Runtime", null);
		wc.setLocation(new Path(serverInstallPath));
		
		return wc.save(true, null);
	}

	public static IServer createServer(String name,String serverTypeId,IRuntime runtime,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
	  IServerType serverType = ServerCore.findServerType(serverTypeId);
      IServer[] servers = ServerCore.getServers(); 
      for (int i=0;i<servers.length;i++){
        if(servers[i].getServerType().getId().equals(serverType.getId())){
          return servers[i];  
        }
      }

      IServerWorkingCopy serverWc = serverType.createServer(serverTypeId,null, monitor );
	  serverWc.setName(name);
	  serverWc.setRuntime(runtime);
      IServer server = serverWc.saveAll(true, monitor );
      return server;
	}	
	
	/**
	 * createServer and install JRE for Servers requiring a specific JRE
	 * @param javaRuntimePath JRE install location; i.e. E:\\Java141
	 * @param jreID JRE id; i.e. java141
	 * @param name server name
	 * @param serverTypeId server type id
	 * @param runtime IRuntime
	 * @param env IEnvironment
	 * @return  server IServer
	 * @throws Exception
	 * 
	 * @deprecated
	 */
	public static IServer createServer(String javaRuntimePath, String jreID, String name,String serverTypeId,IRuntime runtime,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		IServerType serverType = ServerCore.findServerType(serverTypeId);
    IServer[] servers = ServerCore.getServers(); 
    for (int i=0;i<servers.length;i++){
      if(servers[i].getServerType().getId().equals(serverType.getId())){
        return servers[i];  
      }
    }

		IServerWorkingCopy serverWc = serverType.createServer(serverTypeId,null, monitor );
		serverWc.setName(name);
		serverWc.setRuntime(runtime);
		
		IServer server = serverWc.saveAll(true, monitor );
		
		return server;
	}
	
	public static void startServer(IServer server,IEnvironment env) throws Exception
	{
		final IServer currentServer = server;
		IRunnableWithProgress runnable = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				try
				{
				  currentServer.start(ILaunchManager.RUN_MODE, monitor );
				}
				catch (CoreException e)
				{
					InterruptedException wrapper = new InterruptedException(e.getMessage());
					wrapper.setStackTrace(e.getStackTrace());
					throw wrapper;
				}
			}
		};
		PlatformUI.getWorkbench().getProgressService().run(true,false,runnable);
	}
	
	public static boolean removeEARFromServer(IServer server,IProject earProject,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		int numberOfModules = server.getModules().length;
		if (server != null)
		{
      final IModule earProjectModule = ServerUtil.getModule(earProject);
      final IModule[] modules = {earProjectModule};
			new ServerUtils().modifyModules(env, server,earProjectModule,false, monitor );
		  	final IServer currentServer = server;
		  	IRunnableWithProgress runnable = new IRunnableWithProgress()
			{
		  		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
		  		  	for (int i=0;i<1000;i++)
		  		  	{
		  		  		int moduleState = currentServer.getModuleState(modules);
		  		  		if (moduleState == IServer.STATE_STARTED || moduleState == IServer.STATE_STOPPING)
		  		  		{
		  		  		  try
		  				  {
		  		  		    Thread.sleep(300);
		  				  }
		  		  		  catch (InterruptedException e)
		  				  {
		  		  		      e.printStackTrace(System.err);
		  				  }
		  		  		}
		  		  		else
		  		  			break;
		  		  	}  			  			
				}  		
			};
			PlatformUI.getWorkbench().getProgressService().run(true,false,runnable);
			return (server.getModules().length == (numberOfModules-1));
		}
		return false;
	}
	
	public static boolean removeModuleFromServer(IServer server, IProject webProject, IEnvironment env, IProgressMonitor monitor ) throws Exception {
	  
	  int numberOfModules = server.getModules().length;
		if (server != null)
		{
      final IModule webProjectModule = ServerUtil.getModule(webProject);
      final IModule[] modules = {webProjectModule};

			new ServerUtils().modifyModules(env, server,webProjectModule,false, monitor );
		  	final IServer currentServer = server;
		  	IRunnableWithProgress runnable = new IRunnableWithProgress()
			{
		  		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
		  		  	for (int i=0;i<1000;i++)
		  		  	{
		  		  		int moduleState = currentServer.getModuleState(modules);
		  		  		if (moduleState == IServer.STATE_STARTED || moduleState == IServer.STATE_STOPPING)
		  		  		{
		  		  		  try
		  				  {
		  		  		    Thread.sleep(300);
		  				  }
		  		  		  catch (InterruptedException e)
		  				  {
		  		  		      e.printStackTrace(System.err);
		  				  }
		  		  		}
		  		  		else
		  		  			break;
		  		  	}  			  			
				}  		
			};
			PlatformUI.getWorkbench().getProgressService().run(true,false,runnable);
			//Thread.sleep(6000);
			return (server.getModules().length == (numberOfModules-1));
		}
		return false;
	}
	
	// Begin: General Eclipse Utilities
	
	public static void syncBuildProject(IProject project,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor );
		WaitForAutoBuildCommand cmd = new WaitForAutoBuildCommand();
    cmd.setEnvironment( env );
		cmd.execute( monitor, null );
	}
	
	private static void copyTestFiles(String pathString,int rootSegmentLength,IFolder destFolder,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		Enumeration e = TestsPlugin.getDefault().getBundle().getEntryPaths(pathString);
		while (e.hasMoreElements())
		{
			String filePath = (String)e.nextElement();
			if (filePath.endsWith("/"))
				copyTestFiles(filePath,rootSegmentLength,destFolder,env, monitor );
			else
			{
				IPath fileIPath = new Path(filePath);
				FileResourceUtils.copyFile(EnvironmentUtils.getResourceContext(env),
						                   TestsPlugin.getDefault(),
										   fileIPath.removeLastSegments(fileIPath.segmentCount()-rootSegmentLength), // /data/<subdir>
										   (new Path(filePath)).removeFirstSegments(rootSegmentLength), // files after /data/<subdir>
										   destFolder.getFullPath(),
										   monitor,
										   env.getStatusHandler());
			}
		}
	}
	
	public static void copyTestData(String dataSubdirectory,IFolder destFolder,IEnvironment env, IProgressMonitor monitor ) throws Exception
	{
		String pathString = "/data/"+dataSubdirectory;
		copyTestFiles(pathString,new Path(pathString).segmentCount(),destFolder,env, monitor);
		
	}
	
	// Begin: Web Services Utilities
	public static void hideActionDialogs()
	{
		PersistentActionDialogsContext actionDialogsCtx = PersistentActionDialogsContext.getInstance();
		ActionDialogPreferenceType[] actionDialogPrefs  = actionDialogsCtx.getDialogs();
		for (int i=0;i<actionDialogPrefs.length;i++)
			actionDialogsCtx.setActionDialogEnabled(actionDialogPrefs[i].getId(),true);		
	}
	

	public static boolean enableProxyGeneration(boolean enable)
	{
		ScenarioContext ctx = WebServicePlugin.getInstance().getScenarioContext();
		boolean previousSetting = ctx.getGenerateProxy();
		ctx.setGenerateProxy(enable);
		return previousSetting;
	}
	
	public static boolean enableOverwrite(boolean enable)
	{
		ResourceContext ctx = WebServicePlugin.getInstance().getResourceContext();
		boolean previousSetting = ctx.isOverwriteFilesEnabled();
		ctx.setOverwriteFilesEnabled(enable);
		return previousSetting;
	}
	
	public static void setRuntimePreference(String rt){
		PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
		context.setRuntimeId(rt);
	}
	
	public static void disableWSIDialog(IProject project){
	  
	  PersistentWSIContext ctx = WSPlugin.getInstance().getWSIAPContext();
	  ctx.updateProjectWSICompliances(project, PersistentWSIContext.IGNORE_NON_WSI);
	  
	  PersistentWSIContext ctx2 = WSPlugin.getInstance().getWSISSBPContext();
	  ctx2.updateProjectWSICompliances(project, PersistentWSIContext.IGNORE_NON_WSI);
	  
	}
	
	public static void setWSRuntimeServer(String wsRuntimeId,String serverTypeId)
	{
		PersistentServerRuntimeContext serverRuntimeCtx = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
		serverRuntimeCtx.setRuntimeId(wsRuntimeId);
		serverRuntimeCtx.setServerFactoryId(serverTypeId);		
	}
	

	private static IStatus[] launchWizard(String pluginNS,String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		AccumulateStatusHandler statusHandler = new AccumulateStatusHandler();

        DynamicPopupJUnitWizard wizard = new DynamicPopupJUnitWizard(statusHandler);
        wizard.setInitialData(wizardId);
        ProgressMonitorDialog monitor = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        try {
        	wizard.runHeadLess(initialSelection, monitor);
        } 
        catch (Exception e){
        	e.printStackTrace();
        	return new IStatus[]{StatusUtils.errorStatus(e)};
        }
        return statusHandler.getAllReports();

	}
	
	public static IStatus[] launchCreationWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		return launchWizard("org.eclipse.jst.ws.creation.ui",wizardId,objectClassId,initialSelection);
	}
	
	public static IStatus[] launchConsumptionWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		return launchWizard("org.eclipse.jst.ws.internal.consumption.ui",wizardId,objectClassId,initialSelection);
	}
	
	public static IStatus createWebModule(String webProjectName, String moduleName, String serverInstanceID, String serverFactoryId, String j2eeVersion, IEnvironment env, IProgressMonitor monitor ){

	  IStatus status = Status.OK_STATUS;
	  try{
	    CreateFacetedProjectCommand createWeb = new CreateFacetedProjectCommand();
	    createWeb.setProjectName(webProjectName);
        createWeb.setProjectName(moduleName);
        createWeb.setTemplateId("template.jst.web");
	    createWeb.setServerFactoryId(serverFactoryId);
	    createWeb.setServerInstanceId(serverInstanceID);
	    createWeb.setRequiredFacetVersions(new RequiredFacetVersion[0]);
        createWeb.setEnvironment( env );
	    return createWeb.execute( monitor, null );
	  }
	  catch (Exception e){
	    status = StatusUtils.errorStatus( e );
	  }
	  return status;
	  
	}
	
	public static boolean disableValidation(IProject project){
		
    	// disable validators for project
		try
		{
			ValidatorManager vm = ValidatorManager.getManager();
			vm.disableAllValidators(project, null);			
			
		}
		catch (Exception e)
		{
			System.out.println("Failed to set Validators...");
		}
		
		return true;
	}
	
	public static void setServiceScenarioDefault() {
		PersistentScenarioContext context = (PersistentScenarioContext)WebServicePlugin.getInstance().getScenarioContext();
	      context.setGenerateProxy( false );
	      context.setLaunchWebServiceExplorer( false );
	      context.setInstallWebService( false );
	      context.setInstallClient( false );
	      context.setStartWebService( false );
	      context.setTestWebService( false );
	      context.setMonitorWebService(false);
	      context.setGenerateWebService(ScenarioContext.WS_DEVELOP);    
	}
	
	public static void setClientScenarioDefault() {
		PersistentScenarioContext context = (PersistentScenarioContext)WebServicePlugin.getInstance().getScenarioContext();
	      context.setGenerateProxy( true );
	      context.setLaunchWebServiceExplorer( false );
	      context.setInstallWebService( false );
	      context.setInstallClient( false );
	      context.setStartWebService( false );
	      context.setTestWebService( false );
	      context.setMonitorWebService(false);
	      context.setGenerateClient(ScenarioContext.WS_DEVELOP);
	}
	
}
