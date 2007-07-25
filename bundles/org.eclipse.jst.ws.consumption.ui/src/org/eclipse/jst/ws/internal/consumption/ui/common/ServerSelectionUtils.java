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
 * 20060324   116750 rsinha@ca.ibm.com - Rupam Kuehner
 * 20070119   159458 mahutch@ca.ibm.com - Mark Hutchinson
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.internal.consumption.common.IServerDefaulter;
import org.eclipse.jst.ws.internal.consumption.common.ServerInfo;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;


public class ServerSelectionUtils
{

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
    IRuntimeType runtimeType = runtime.getRuntimeType();
    if (runtimeType != null)
    {
    	String runtimeId = runtimeType.getId();
		for (int i=0; i<servers.length; i++)
		{
		  IServer server = (IServer)servers[i];
		  IRuntimeType runtimeType2 = server.getRuntime().getRuntimeType();
		  if (runtimeType2 != null)
		  {
			  String serverRuntimeId = runtimeType2.getId();
			  if (serverRuntimeId.equals(runtimeId))
			    compatibleServersList.add(server);
		  }
		  
		}
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
}
