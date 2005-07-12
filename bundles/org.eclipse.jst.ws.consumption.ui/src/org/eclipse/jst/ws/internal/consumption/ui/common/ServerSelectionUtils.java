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

package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.j2ee.internal.servertarget.IServerTargetConstants;
import org.eclipse.jst.j2ee.internal.servertarget.ServerTargetHelper;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.IServerDefaulter;
import org.eclipse.jst.ws.internal.consumption.common.ServerInfo;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeInfo;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;


public class ServerSelectionUtils
{
  /*
   * Returns reasonable defaults for server factory id and instance id based on 
   * an existing project
   * index [0] contains factoryId, index[1] contains instance Id.
   * @deprecated
   */
  public static String[] getServerInfoFromExistingProject(IProject project, String componentName, String runtimeId, boolean askExtenders)
  {
    String[] serverInfo = new String[2]; //serverInfp[0] contains factoryId, serverInfo[1] contains instance Id

    // If the project has been added to an existing server, pick that server
    //IServer[] configuredServers = ServerUtil.getServersByModule(ResourceUtils.getModule(project), null);
    IServer[] configuredServers = ServerUtil.getServersByModule(ServerUtils.getModule(project, componentName), null);
    IServer firstSupportedServer = getFirstSupportedServer(configuredServers, runtimeId );
    if (firstSupportedServer != null)
    {
      serverInfo[0] = firstSupportedServer.getServerType().getId();
      serverInfo[1] = firstSupportedServer.getId();
      return serverInfo;
      	
    }
    
    
    //Does the project have a runtime-target?
    IRuntime runtimeTarget = getRuntimeTarget(project.getName());
    if (runtimeTarget != null)
    {
      //Look for an existing server which is compatible with the runtime-target
      IServer[] compatibleServers = getCompatibleExistingServers(runtimeTarget);
      if (compatibleServers!=null && compatibleServers.length > 0)
      {
        IServer firstSupportedCompatServer = getFirstSupportedServer(compatibleServers, runtimeId);
        if (firstSupportedCompatServer != null)
        {
          serverInfo[0] = firstSupportedCompatServer.getServerType().getId();
          serverInfo[1] = firstSupportedCompatServer.getId();
          return serverInfo;
          
        }  
      }
      
      //No existing compatible server was found. Choose a compatible server type.
      
      //If the preferred server is compatible with the project's runtime target, use it.
      PersistentServerRuntimeContext ctx = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
      String pFactoryId = ctx.getServerFactoryId();
      IServerType serverType = ServerCore.findServerType(pFactoryId);
      if (serverType!=null){
      	String serverRuntimeId = serverType.getRuntimeType().getId();
      	if (serverRuntimeId.equals(runtimeTarget.getRuntimeType().getId()))
      	{
      	  serverInfo[0] = pFactoryId;
      	  return serverInfo;
      	}
      }     
      
      //The preferred server was not compatible. Pick the first valid compatible server type.
      String factoryId = getFirstSupportedServerType(runtimeTarget, runtimeId);
      if (factoryId != null) serverInfo[0] = factoryId;
      return serverInfo;

    }
    
    if (askExtenders)
    {
      ServerInfo recommendedServerInfo = getExtenderRecommendation(project);
      if (recommendedServerInfo!=null)
      {
        if (recommendedServerInfo.getServerFactoryId()!=null && recommendedServerInfo.getServerFactoryId().length()>0)
          serverInfo[0] = recommendedServerInfo.getServerFactoryId();
        
        if (recommendedServerInfo.getServerInstanceId()!=null && recommendedServerInfo.getServerInstanceId().length()>0)
          serverInfo[1] = recommendedServerInfo.getServerInstanceId(); 

        return serverInfo;
      }

    }
    
    //Use ServerTargetHelper to get a list of valid runtime-targets and use these to determine a default server type.
    String[] projectAttrs = ServerTargetHelper.getProjectTypeAndJ2EELevel(project);
    List runtimes = ServerTargetHelper.getServerTargets(projectAttrs[0], projectAttrs[1]);
    String[] compatServerInfo = getCompatibleExistingServer(runtimes,runtimeId); 
    if ( compatServerInfo != null)
    {
      return compatServerInfo;
    }
    
    //No existing compatible server, pick a type.
    String factoryId = getFirstSupportedServerType(runtimes, runtimeId);
    if (factoryId != null) serverInfo[0] = factoryId; 
    return serverInfo;   
    
  }

  /*
   * Returns reasonable defaults for server factory id and instance id based on 
   * an existing project
   * index [0] contains factoryId, index[1] contains instance Id.
   */
  /*
  public static String[] getServerInfoFromExistingProject(IProject project, String runtimeId, boolean askExtenders)
  {
    String[] serverInfo = new String[2]; //serverInfp[0] contains factoryId, serverInfo[1] contains instance Id

    // If the project has been added to an existing server, pick that server
    IServer[] configuredServers = ServerUtil.getServersByModule(ResourceUtils.getModule(project), null);
    IServer firstSupportedServer = getFirstSupportedServer(configuredServers, runtimeId );
    if (firstSupportedServer != null)
    {
      serverInfo[0] = firstSupportedServer.getServerType().getId();
      serverInfo[1] = firstSupportedServer.getId();
      return serverInfo;
      	
    }
    
    
    //Does the project have a runtime-target?
    IRuntime runtimeTarget = getRuntimeTarget(project.getName());
    if (runtimeTarget != null)
    {
      //Look for an existing server which is compatible with the runtime-target
      IServer[] compatibleServers = getCompatibleExistingServers(runtimeTarget);
      if (compatibleServers!=null && compatibleServers.length > 0)
      {
        IServer firstSupportedCompatServer = getFirstSupportedServer(compatibleServers, runtimeId);
        if (firstSupportedCompatServer != null)
        {
          serverInfo[0] = firstSupportedCompatServer.getServerType().getId();
          serverInfo[1] = firstSupportedCompatServer.getId();
          return serverInfo;
          
        }  
      }
      
      //No existing compatible server was found. Choose a compatible server type.
      
      //If the preferred server is compatible with the project's runtime target, use it.
      PersistentServerRuntimeContext ctx = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
      String pFactoryId = ctx.getServerFactoryId();
      IServerType serverType = ServerCore.findServerType(pFactoryId);
      if (serverType!=null){
      	String serverRuntimeId = serverType.getRuntimeType().getId();
      	if (serverRuntimeId.equals(runtimeTarget.getRuntimeType().getId()))
      	{
      	  serverInfo[0] = pFactoryId;
      	  return serverInfo;
      	}
      }     
      
      //The preferred server was not compatible. Pick the first valid compatible server type.
      String factoryId = getFirstSupportedServerType(runtimeTarget, typeId);
      if (factoryId != null) serverInfo[0] = factoryId;
      return serverInfo;

    }
    
    if (askExtenders)
    {
      ServerInfo recommendedServerInfo = getExtenderRecommendation(project);
      if (recommendedServerInfo!=null)
      {
        if (recommendedServerInfo.getServerFactoryId()!=null && recommendedServerInfo.getServerFactoryId().length()>0)
          serverInfo[0] = recommendedServerInfo.getServerFactoryId();
        
        if (recommendedServerInfo.getServerInstanceId()!=null && recommendedServerInfo.getServerInstanceId().length()>0)
          serverInfo[1] = recommendedServerInfo.getServerInstanceId(); 

        return serverInfo;
      }

    }
    
    //Use ServerTargetHelper to get a list of valid runtime-targets and use these to determine a default server type.
    String[] projectAttrs = ServerTargetHelper.getProjectTypeAndJ2EELevel(project);
    List runtimes = ServerTargetHelper.getServerTargets(projectAttrs[0], projectAttrs[1]);
    List suppRuntimes = getRuntimeTargetsSupportedByWSRuntime(runtimes, runtimeId);
    String[] compatServerInfo = getCompatibleExistingServer(suppRuntimes,typeId); 
    if ( compatServerInfo != null)
    {
      return compatServerInfo;
    }
    
    //No existing compatible server, pick a type.
    String factoryId = getFirstSupportedServerType(suppRuntimes, typeId);
    if (factoryId != null) serverInfo[0] = factoryId; 
    return serverInfo;   
    
  }
  */
  
  /*
   * Given a list of existing servers, this returns the first one that is supported
   * by the given Web service runtime id. 
   * Returns null of there are no supported servers in the array.
   */
  public static IServer getFirstSupportedServer(IServer[] servers, String webServiceRuntimeId)
  {
    //WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
    if (servers != null && servers.length > 0) {
      for (int i = 0; i < servers.length; i++)
      {
        String serverFactoryId = servers[i].getServerType().getId();
        if (WebServiceRuntimeExtensionUtils.doesRuntimeSupportServer(webServiceRuntimeId, serverFactoryId))
        {
          return servers[i];
        }
      }
    }
    return null;
  }
  
  /*
   * Given a list of existing servers, this returns the first one that is supported
   * by the given Web service type id. 
   * Returns null of there are no supported servers in the array.
   * @deprecated
   */
  /*
  public static IServer getFirstSupportedServer(IServer[] servers, String typeId)
  {
    WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
    if (servers != null && servers.length > 0) {
      for (int i = 0; i < servers.length; i++)
      {
        String serverFactoryId = servers[i].getServerType().getId();
        if (wssrtRegistry.isServerSupportedForChosenType(typeId, serverFactoryId))
        {
          return servers[i];
        }
      }
    }
    return null;
  }  
  */
  
  /*
   * Returns the factory id of a server type compatible with the Web service type and the runtime target.
   * Returns null if there are none.
   */
  public static String getFirstSupportedServerType(IRuntime runtimeTarget, String webServiceRuntimeId)
  {
    String runtimeId = runtimeTarget.getRuntimeType().getId();
    //WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
    //String[] serverFactoryIds = wssrtRegistry.getServerFactoryIdsByType(typeId);
    WebServiceRuntimeInfo wsrt = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(webServiceRuntimeId);
    String[] serverFactoryIds = wsrt.getServerFactoryIds();
    for (int i=0; i<serverFactoryIds.length; i++)
    {
      IServerType serverType = ServerCore.findServerType(serverFactoryIds[i]);
      if (serverType!=null){
        String serverRuntimeId = serverType.getRuntimeType().getId();
        if (serverRuntimeId.equals(runtimeId))
        {
          return serverFactoryIds[i];
        }
      }
    }
    return null;
  }
  
  /*
   * Returns the factory id of a server type compatible with the Web service type and the runtime target.
   * Returns null if there are none.
   * @deprecated
   */
  /*
  public static String getFirstSupportedServerType(IRuntime runtimeTarget, String typeId)
  {
    String runtimeId = runtimeTarget.getRuntimeType().getId();
    WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
    String[] serverFactoryIds = wssrtRegistry.getServerFactoryIdsByType(typeId);
    for (int i=0; i<serverFactoryIds.length; i++)
    {
      IServerType serverType = ServerCore.findServerType(serverFactoryIds[i]);
      if (serverType!=null){
      	String serverRuntimeId = serverType.getRuntimeType().getId();
      	if (serverRuntimeId.equals(runtimeId))
      	{
      		return serverFactoryIds[i];
      	}
      }
    }
    return null;
  }
  */

  /*
   * Return the factory id of the first server type compatible with the runtimeTargets and webServiceRuntimeId.
   * Returns null if there are none.
   */
  public static String getFirstSupportedServerType(List runtimeTargets, String webServiceRuntimeId)
  {
    for (int i=0; i<runtimeTargets.size(); i++ )
    {
      IRuntime runtimeTarget = (IRuntime)runtimeTargets.get(i);
      String factoryId = getFirstSupportedServerType(runtimeTarget, webServiceRuntimeId);
      if (factoryId != null && factoryId.length()>0)
        return factoryId;
    }
    
    return null;
  }  
  /*
   * Return the factory id of the first server type compatible with the runtimeTargets and typeId.
   * Returns null if there are none.
   * @deprecated
   */
  /*
  public static String getFirstSupportedServerType(List runtimeTargets, String typeId)
  {
    for (int i=0; i<runtimeTargets.size(); i++ )
    {
      IRuntime runtimeTarget = (IRuntime)runtimeTargets.get(i);
      String factoryId = getFirstSupportedServerType(runtimeTarget, typeId);
      if (factoryId != null && factoryId.length()>0)
        return factoryId;
    }
    
    return null;
  }
  */
  public static IRuntime getRuntimeTarget(String projectName)
  {
  	if( projectName != null && projectName.length() > 0 ){ //$NON-NLS-1$
  		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
  		if( project != null && project.exists() ){
  			return ServerCore.getProjectProperties(project).getRuntimeTarget(); 
  		}
  	}
  	return null;
  }
  
  public static IServer getFirstExistingServerFromFactoryId(String factoryId)
  {
  	IServer[] servers = ServerCore.getServers();
  	if (servers==null || servers.length!=0)
        return null;
  	else
  	{
  	  for (int i=0; i<servers.length; i++)
  	  {
  	    IServer server = (IServer)servers[i];
  	    if (server.getServerType().getId().equals(factoryId))
  	    {
  	      return server;
  	    }
  	  }
  	}
    return null;
  }
  
  public static IServer[] getCompatibleExistingServers(IRuntime runtime)
  {
    if (runtime == null)
      return null;
    
    IServer[] servers = ServerCore.getServers();
    if (servers==null || servers.length==0)
      return null;
    
    ArrayList compatibleServersList = new ArrayList();
    String runtimeId = runtime.getRuntimeType().getId();
    for (int i=0; i<servers.length; i++)
    {
      IServer server = (IServer)servers[i];
      String serverRuntimeId = server.getRuntime().getRuntimeType().getId();
      if (serverRuntimeId.equals(runtimeId))
        compatibleServersList.add(server);
      
    }
    if (compatibleServersList.size()<1)
      return null;
    
    
    Object[] compatibleServersArray = compatibleServersList.toArray();
    IServer[] compatibleServers = new IServer[compatibleServersArray.length];
    for (int j=0; j<compatibleServersArray.length; j++)
    {
      compatibleServers[j] = (IServer)compatibleServersArray[j];
    }
    
    return compatibleServers;
  }
  

  /*
   * Returns the factory Id and instance id of the first exiting server compatible
   * with the runtime targets and typeId
   * Returns null if there are none.
   * The item at [0] is the factory id.
   * The item at [1] is the instance id.
   * @deprecated
   */
  public static String[] getCompatibleExistingServer(List runtimeTargets, String webServiceRuntimeId)
  {
    String[] serverInfo = new String[2];
    for (int i=0; i<runtimeTargets.size(); i++ )
    {
      IRuntime runtimeTarget = (IRuntime)runtimeTargets.get(i);
      IServer[] existingCompatServers = getCompatibleExistingServers(runtimeTarget);
      IServer compatServer = getFirstSupportedServer(existingCompatServers, webServiceRuntimeId);
      if (compatServer != null)
      {
        serverInfo[0] = compatServer.getServerType().getId();
        serverInfo[1] = compatServer.getId();
        return serverInfo;
      }
    }
    return null;
  }
  
  /*
   * Returns the factory Id and instance id of the first exiting server compatible
   * with the runtime targets and typeId
   * Returns null if there are none.
   * The item at [0] is the factory id.
   * The item at [1] is the instance id.
   * @deprecated
   */
  /*
  public static String[] getCompatibleExistingServer(List runtimeTargets, String typeId)
  {
    String[] serverInfo = new String[2];
    for (int i=0; i<runtimeTargets.size(); i++ )
    {
      IRuntime runtimeTarget = (IRuntime)runtimeTargets.get(i);
      IServer[] existingCompatServers = getCompatibleExistingServers(runtimeTarget);
      IServer compatServer = getFirstSupportedServer(existingCompatServers, typeId);
      if (compatServer != null)
      {
        serverInfo[0] = compatServer.getServerType().getId();
        serverInfo[1] = compatServer.getId();
        return serverInfo;
      }
    }
    return null;
  }
  */
  
  public static ServerInfo getExtenderRecommendation(IProject project)
  {
    try
    {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.eclipse.jst.ws.consumption", "serverDefaulter");
    for (int i=0; i<elements.length; i++)
    {
      Object serverDefaulterObject = elements[i].createExecutableExtension("class");
      if (serverDefaulterObject instanceof IServerDefaulter)
      {
        IServerDefaulter serverDefaulter = (IServerDefaulter)serverDefaulterObject;
        ServerInfo serverInfo = serverDefaulter.recommendDefaultServer(project);
        if (serverInfo != null)
          return serverInfo;
      }
      
    }
    } catch (CoreException ce)
    {
      return null;
    }
    
    return null;
  }
  
  /**
   * 
   * @param runtimeTargets
   * @param webServiceRuntimeId
   * @return
   * @deprecated
   */
  private static List getRuntimeTargetsSupportedByWSRuntime(List runtimeTargets, String webServiceRuntimeId)
  {
    ArrayList suppRuntimeTargets = new ArrayList();
    WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
    for (int i=0; i<runtimeTargets.size(); i++)
    {
      IRuntime runtimeTarget = (IRuntime)runtimeTargets.get(i);
      String rtId = runtimeTarget.getRuntimeType().getId();
      if (wssrtReg.doesRuntimeSupportServerTarget(rtId, webServiceRuntimeId));
      {
        suppRuntimeTargets.add(runtimeTarget);
      }
    }
    return suppRuntimeTargets;
  }  
  
  /**
   * Use this method to get a server factory id and instance id that is compatible with the given Web
   * service runtime.
   * @param webServiceRuntimeId
   * @param j2eeVersion String representation of the int values in J2EEVersionConstants i.e. "12", "13", "14"
   * @return String[] index [0] contains factoryId, index[1] contains instance Id.
   */
  public static String[] getServerFromWebServceRuntimeAndJ2EE(String webServiceRuntimeId, String j2eeVersion)
  {
    String[] serverInfo = new String[2];
    //WebServiceServerRuntimeTypeRegistry wssrtReg = WebServiceServerRuntimeTypeRegistry.getInstance();
    
    //Get all possible valid servers. If there are none, we can't default intelligently, return null.
    //String[] validServerFactoryIds = wssrtReg.getServerFactoryIDByRuntimeID(webServiceRuntimeId);
    WebServiceRuntimeInfo wsrt = WebServiceRuntimeExtensionUtils.getWebServiceRuntimeById(webServiceRuntimeId);
    String[] validServerFactoryIds = wsrt.getServerFactoryIds();
    if (validServerFactoryIds==null || validServerFactoryIds.length<1)
      return null;
    
    //Get all existing servers
    IServer[] servers = ServerCore.getServers(); //Get all existing servers
    
    //Get the preferred server.
  	PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
  	String prefServerFactoryId = context.getServerFactoryId();
  	
    boolean preferredIsValid =  containsString(validServerFactoryIds, prefServerFactoryId)
                                && isServerValid(prefServerFactoryId, j2eeVersion);
    if (preferredIsValid)
    {
      for (int i=0; i<servers.length; i++)
      {
        IServer server = (IServer)servers[i];
        String thisFactoryId = server.getServerType().getId();
        
        if (thisFactoryId.equals(prefServerFactoryId))
        {
          serverInfo[0] = prefServerFactoryId;
          serverInfo[1] = server.getId();
          return serverInfo;
        }                
      }      
    }
    
    //Either the preferred server was not valid or it was valid but does not exist.
    //Check the existing servers for validity
    for (int i=0; i<servers.length; i++)
    {
      IServer server = (IServer)servers[i];
      String thisFactoryId = server.getServerType().getId();
      
      boolean thisServerValid = containsString(validServerFactoryIds, thisFactoryId) 
                                && isServerValid(thisFactoryId, j2eeVersion);
      if (thisServerValid)
      {
        serverInfo[0] = thisFactoryId;
        serverInfo[1] = server.getId();
        return serverInfo;        
      }
    }          

    //None of the existing servers is valid. Pick the preferred one if valid. Otherwise, pick the
    //first valid server type.
    if (preferredIsValid)
    {
      serverInfo[0] = prefServerFactoryId;
      return serverInfo;
    }
    else
    {
      for (int i=0; i<validServerFactoryIds.length; i++ )
      {
        boolean isValid = isServerValid(validServerFactoryIds[i], j2eeVersion);
        if (isValid)
        {
          serverInfo[0] = validServerFactoryIds[i];
          return serverInfo;
        }
      }      
    }
    
    //We can't determine a valid server selection. Return null.
    return null;
  }
  
  private static boolean isServerValid(String serverFactoryId, String j2eeVersion)
  {
   if (serverFactoryId==null || serverFactoryId.length()==0 || j2eeVersion==null || j2eeVersion.length()==0)
     return true;
   
   String runtimeTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverFactoryId);
   if (runtimeTargetId == null || runtimeTargetId.length()==0)
     return false;
   String webModuleType = IServerTargetConstants.WEB_TYPE;
   boolean isValid = ServerUtils.isTargetValidForProjectType(runtimeTargetId, j2eeVersion, webModuleType);
   return isValid;
  }
  /*
   * Returns true is a contains b, false otherwise.
   */
  private static boolean containsString(String[] a, String b)
  {
    if (a==null || a.length<0 || b == null || b.length()==0 )
      return false;
    
    for (int i=0; i<a.length; i++)
    {
      String s = a[i];
      if (s.equals(b))
      {
        return true;
      }
    }
    return false;
  }
}
