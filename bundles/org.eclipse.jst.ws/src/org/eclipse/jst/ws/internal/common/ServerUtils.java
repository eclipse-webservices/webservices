/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.common;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jst.j2ee.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.servertarget.IServerTargetConstants;
import org.eclipse.jst.j2ee.internal.servertarget.ServerTargetHelper;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IProjectProperties;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.model.IModule;
import org.eclipse.wst.server.core.model.IProjectModule;
import org.eclipse.wst.server.core.model.IRunningActionServer;
import org.eclipse.wst.server.core.model.IServerDelegate;

/**
 * This class contains useful methods for working with Server plugin functions
 */
public final class ServerUtils {

  private MessageUtils msgUtils_;		
  private Hashtable serverIdToLabel_;
  private Hashtable serverLabelToId_;
  private static ServerUtils instance_;

  public static ServerUtils getInstance() {
    if (instance_ == null) {
      instance_ = new ServerUtils();
    }
    return instance_;

  }

  public ServerUtils(){
    String pluginId = "org.eclipse.jst.ws";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);  	
  }  
  
  // Gets the Server labels given the server factory id
  public void getServerLabelsAndIds() {
    serverIdToLabel_ = new Hashtable();
    serverLabelToId_ = new Hashtable();
    List serverTypes = ServerCore.getServerTypes();
    for (int idx = 0; idx < serverTypes.size(); idx++) {

      IServerType serverType = (IServerType) serverTypes.get(idx);

      String id = serverType.getId();
      String serverLabel = serverType.getName();
      if (!(id == null) && !(serverLabel == null)) {
        serverIdToLabel_.put(id, serverLabel);
        serverLabelToId_.put(serverLabel, id);
      }
    }
  }

  public String getServerLabelForId(String factoryId) {
    if (serverIdToLabel_ == null)
      this.getServerLabelsAndIds();
    return (String) serverIdToLabel_.get(factoryId);
  }

  public String getServerIdForLabel(String factoryLabel) {
    if (serverLabelToId_ == null)
      this.getServerLabelsAndIds();
    return (String) serverLabelToId_.get(factoryLabel);
  }
  
  public Status modifyModules(Environment env, IServer server, IModule module, boolean add, IProgressMonitor monitor) {

  	IServerWorkingCopy wc = null;
  	Status status = new SimpleStatus("");
    try {

      if (module==null || !module.exists()) {
    	return status;
      }

      // check if module is a true Java project
  	  if (module instanceof IProjectModule){
  	  	IProjectModule pm = (IProjectModule)module;
  	  	if (pm!=null){
  	  		IProject project = pm.getProject();
  	  		if (project==null || ResourceUtils.isTrueJavaProject(project)) {
  				return status;
  	  		}
  	  	}
  	  }
       
      wc = server.getWorkingCopy();     
      if (wc!=null){
      	IServerDelegate delegate = wc.getDelegate();
        if (delegate!=null && delegate instanceof IRunningActionServer) {
         byte state = wc.getServerState();
         if (state == IServer.SERVER_STOPPED || state == IServer.SERVER_UNKNOWN) {
           String mode = ILaunchManager.RUN_MODE;
           wc.synchronousStart(mode, monitor);
         }
        }
     
      List list = Arrays.asList(server.getModules());
      if (add) {
        if (!list.contains(module)) {
          ServerUtil.modifyModules(wc, new IModule[] { module}, new IModule[0], monitor);
        }
      }
      else { // removes module
        if (list.contains(module)) {
          ServerUtil.modifyModules(wc, new IModule[0], new IModule[] { module}, monitor);
        }
      }
      }
      else {
      	// handle case of Null WC; non-issue for now
      }
    }
    catch (CoreException ce) {
        status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SERVER"), Status.ERROR, ce);
        env.getStatusHandler().reportError(status);
        return status;
    }
    finally {
        if (wc != null) {
          // Always saveAll and release the serverWorkingCopy
          try {
          	wc.saveAll(monitor);
          	wc.release();
          }
          catch(CoreException cex){
            status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SERVER"), Status.ERROR, cex);
            env.getStatusHandler().reportError(status);
            return status;
          }
        }
      }
    return status;
  }  

  
  public static void modifyModules(IServer server, IModule module, boolean add, IProgressMonitor monitor) {

  	IServerWorkingCopy wc = null;  	
    try {
      wc = server.getWorkingCopy();
      if (wc!=null){
      	IServerDelegate delegate = wc.getDelegate();      	
        if (delegate!=null && delegate instanceof IRunningActionServer) {
            byte state = wc.getServerState();
            if (state == IServer.SERVER_STOPPED || state == IServer.SERVER_UNKNOWN) {
              String mode = ILaunchManager.RUN_MODE;
              wc.synchronousStart(mode, monitor);
            }
        }
        
      	List list = Arrays.asList(server.getModules());
      	if (add) {
      		if (!list.contains(module)) {
      			ServerUtil.modifyModules(wc, new IModule[] { module}, new IModule[0], monitor);
      		}
      	}
      	else { // removes module
      		if (list.contains(module)) {
      			ServerUtil.modifyModules(wc, new IModule[0], new IModule[] { module}, monitor);
      		}
      	}
      }
    }
    catch (CoreException ce) {
      // handle error
    }
    finally{
    	try{
    		if (wc!=null){
    			wc.saveAll(monitor);
    			wc.release();
    		}
    	}
        catch(CoreException ce){
        	//handle error
        }
    }

  }

  public static IServer getServerForModule(IModule module, String serverTypeId, IServer server, boolean create, IProgressMonitor monitor) {
    if (server != null)
      return server;
    else
      return getServerForModule(module, serverTypeId, create, monitor);
  }

  public static IServer getServerForModule(IModule module, String serverTypeId, boolean create, IProgressMonitor monitor) {
    try {

      IServer[] servers = ServerUtil.getServersByModule(module);

      if (servers != null && servers.length > 0) {
        if (serverTypeId == null || serverTypeId.length() == 0)
          return servers[0]; // WSAD v4 behavior

        for (int i = 0; i < servers.length; i++) {
          if (servers[i].getServerType().getId().equalsIgnoreCase(serverTypeId))
            return servers[i];
        }
      }

      return createServer(module, serverTypeId, create, monitor);

    }
    catch (Exception e) {
      return null;
    }
  }

  public static IServer getServerForModule(IModule module) {
    try {
      IServer[] servers = ServerUtil.getServersByModule(module);
      return ((servers != null && servers.length > 0) ? servers[0] : null);
    }
    catch (Exception e) {
      return null;
    }
  }

  public IServer createServer(Environment env, IModule module, String serverTypeId, boolean create, IProgressMonitor monitor){
  	IServerWorkingCopy server = null;
  	try {
      IServerType serverType = ServerCore.getServerType(serverTypeId);
      server = serverType.createServer(serverTypeId, null, monitor);
      if (server != null) {
      	IServerDelegate delegate = server.getDelegate();
        if (delegate!=null && delegate instanceof IRunningActionServer) {
         byte state = server.getServerState();
         if (state == IServer.SERVER_STOPPED || state == IServer.SERVER_UNKNOWN) {
           String mode = ILaunchManager.RUN_MODE;
           server.synchronousStart(mode, monitor);
         }
        }  
        
        if (module != null && module.exists()) {
          List parentModules = server.getParentModules(module);
          if (parentModules!=null && !parentModules.isEmpty()) {
          	module = (IModule)parentModules.get(0);
          }
          server.modifyModules(new IModule[] { module}, new IModule[0], monitor);
        }
      }

      return server;
    }
    catch (Exception e) {
        Status status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SERVER"), Status.ERROR, e);
        env.getStatusHandler().reportError(status);
    	return null;
    }
    finally{
    	try{
    		if (server!=null){
    			server.saveAll(monitor);
    			server.release();
    		}
    	}
    	catch(CoreException ce){
            Status status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SERVER"), Status.ERROR, ce);
            env.getStatusHandler().reportError(status);    		
    		return null;
    		//handler core exception
    	}
    }
  }  
  
  public static IServer createServer(IModule module, String serverTypeId, boolean create, IProgressMonitor monitor) {
  	IServerWorkingCopy server = null;
  	try {
      IServerType serverType = ServerCore.getServerType(serverTypeId);
      server = serverType.createServer(serverTypeId, null, monitor);
      if (server != null) {
      	IServerDelegate delegate = server.getDelegate();
        if (delegate!=null && delegate instanceof IRunningActionServer) {
         byte state = server.getServerState();
         if (state == IServer.SERVER_STOPPED || state == IServer.SERVER_UNKNOWN) {
           String mode = ILaunchManager.RUN_MODE;
           server.synchronousStart(mode, monitor);
         }
        }  
        
        if (module != null) {
          List parentModules = server.getParentModules(module);
          if (parentModules!=null && !parentModules.isEmpty()) {
          	module = (IModule)parentModules.get(0);
          }
          server.modifyModules(new IModule[] { module}, new IModule[0], monitor);
        }
      }

      return server;
    }
    catch (Exception e) {
      return null;
    }
    finally{
    	try{
    		if (server!=null){
    			server.saveAll(monitor);
    			server.release();
    		}
    	}
    	catch(CoreException ce){
    		return null;
    		//handler core exception
    	}
    }
  }

  public static String[] getServerTypeIdsByModule(IProject project) {
    Vector serverIds = new Vector();
    if (project != null) {
      IServer[] servers = ServerUtil.getServersByModule(ResourceUtils.getModule(project));
      if (servers != null && servers.length > 0) {
        for (int i = 0; i < servers.length; i++) {
          serverIds.add(servers[i].getId());
        }
      }
    }
    return (String[]) serverIds.toArray(new String[serverIds.size()]);
  }

  public static IServer getDefaultExistingServer(IProject project) {
    String defaultServerName;

    //IServerPreferences serverPreferences = ServerCore.getServerPreferences();
    IProjectProperties props = ServerCore.getProjectProperties(project);
    IServer preferredServer = props.getDefaultServer();
    //IServer preferredServer = serverPreferences.getDeployableServerPreference(ResourceUtils.getModule(project));
    if (preferredServer != null)
      return preferredServer;

    IServer[] configuredServers = ServerUtil.getServersByModule(ResourceUtils.getModule(project));
    if (configuredServers != null && configuredServers.length > 0) { return configuredServers[0]; }

    IServer[] nonConfiguredServers = ServerUtil.getAvailableServersForModule(ResourceUtils.getModule(project), false);
    if (nonConfiguredServers != null && nonConfiguredServers.length > 0) { return nonConfiguredServers[0]; }
    return null;
  }
  
  
  /*
   * @param moduleType - ad defined in IServerTargetConstants (i.e. EAR_TYPE, WEB_TYPE, etc.)
   * @param j2eeVersion String representation of the int values in J2EEVersionConstants
   * i.e. "12" or "13" or "14"
   * @return String the id of the server target - to be used in project creation operations.
   */
  public static String getServerTargetIdFromFactoryId(String serverFactoryId, String moduleType, String j2eeVersion)
  {
    IServerType serverType = ServerCore.getServerType(serverFactoryId);
    if (serverType == null)
      return null;
    
    String serverRuntimeTypeId = serverType.getRuntimeType().getId();
    
    String stJ2EEVersion = ServerUtils.getServerTargetJ2EEVersion(j2eeVersion);
    List runtimes = ServerTargetHelper.getServerTargets(moduleType, stJ2EEVersion);
    for (int i=0; i<runtimes.size(); i++)
    {
      IRuntime runtime = (IRuntime)runtimes.get(i);
      String thisRuntimeTypeId = runtime.getRuntimeType().getId();
      if (thisRuntimeTypeId.equals(serverRuntimeTypeId))
      {
        return runtime.getId();
      }
    }
    
    return null;    
  }
  
  /*
   * @param serverFactoryId the server's factory id
   * @returns the runtime type id given the server's factory id. Returns a blank String if the no ServerType exists for the given factory id.
   */
  public static String getRuntimeTargetIdFromFactoryId(String serverFactoryId)
  {
    IServerType serverType = ServerCore.getServerType(serverFactoryId);
    if (serverType!=null)
    {
      String serverRuntimeId = serverType.getRuntimeType().getId();
      return serverRuntimeId;
    }
    else
      return "";
  }
  
  /*
   * @param serverFactoryId the server's factory id
   * @returns the server type id given the server's factory id. Returns a blank String if the no ServerType exists for the given factory id.
   */  
  public static String getServerTypeIdFromFactoryId(String serverFactoryId)
  {
  	IServerType serverType = ServerCore.getServerType(serverFactoryId);
  	if (serverType!=null)
  	{
  	  String serverTypeId = serverType.getId();
  	  return serverTypeId;
  	}
  	else
  	  return "";
  }

  /*
   * @param j2eeVersion String representation of the int values in J2EEVersionConstants
   * i.e. "12" or "13" or "14"
   */
  public static boolean isTargetValidForEAR(String runtimeTargetId, String j2eeVersion)
  {
  	if (runtimeTargetId == null)
  		return false;
  	
  	String earModuleType = IServerTargetConstants.EAR_TYPE;
	String stJ2EEVersion = ServerUtils.getServerTargetJ2EEVersion(j2eeVersion);
  	List runtimes = ServerTargetHelper.getServerTargets(earModuleType, stJ2EEVersion);
    for (int i=0; i<runtimes.size(); i++ )
    {
      IRuntime runtime = (IRuntime)runtimes.get(i);
      String thisId = runtime.getRuntimeType().getId();
      if (thisId.equals(runtimeTargetId))
      	return true;
    }
    
    return false;
  }
  /*
   * @param j2eeVersion String representation of the int values in J2EEVersionConstants
   * i.e. "12" or "13" or "14"
   * @param the project type from IServerTargetConstants
   */
  public static boolean isTargetValidForProjectType(String runtimeTargetId, String j2eeVersion, String projectType)
  {
  	if (runtimeTargetId == null)
  		return false;
  	
  	if (projectType==null || projectType.length()==0)
  	  return false;
  	
	String stJ2EEVersion = ServerUtils.getServerTargetJ2EEVersion(j2eeVersion);
  	List runtimes = ServerTargetHelper.getServerTargets(projectType, stJ2EEVersion);
    for (int i=0; i<runtimes.size(); i++ )
    {
      IRuntime runtime = (IRuntime)runtimes.get(i);
      String thisId = runtime.getRuntimeType().getId();
      if (thisId.equals(runtimeTargetId))
      	return true;
    }
    
    return false;
  }
  
  public static String getServerTargetJ2EEVersion(String j2eeVersion)
  {
    String stJ2EEVersion = null;
    if (j2eeVersion==null || j2eeVersion.length()==0)
      return null;
    
    int j2eeVersionInt = Integer.parseInt(j2eeVersion);
    switch (j2eeVersionInt)
    {
      case (J2EEVersionConstants.J2EE_1_2_ID):
        return IServerTargetConstants.J2EE_12;
      case (J2EEVersionConstants.J2EE_1_3_ID):
        return IServerTargetConstants.J2EE_13;
      case (J2EEVersionConstants.J2EE_1_4_ID):
        return IServerTargetConstants.J2EE_14;
      default:
        return null;
    }
  }
}
