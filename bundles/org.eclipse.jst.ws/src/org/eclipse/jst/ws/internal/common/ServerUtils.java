/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060204  124408   rsinha@ca.ibm.com - Rupam Kuehner          
 * 20060330   124667 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.common;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.servertarget.IServerTargetConstants;
import org.eclipse.jst.ws.internal.WSPluginMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.model.IURLProvider;

/**
 * This class contains useful methods for working with Server plugin functions
 */
public final class ServerUtils {

	private Hashtable serverIdToLabel_;

	private Hashtable serverLabelToId_;

	private static ServerUtils instance_;

	public static ServerUtils getInstance() {
		if (instance_ == null) {
			instance_ = new ServerUtils();
		}
		return instance_;

	}

	public ServerUtils() {
	}

	// Gets the Server labels given the server factory id
	public void getServerLabelsAndIds() {
		serverIdToLabel_ = new Hashtable();
		serverLabelToId_ = new Hashtable();
		IServerType[] serverTypes = ServerCore.getServerTypes();
		for (int idx = 0; idx < serverTypes.length; idx++) {

			IServerType serverType = (IServerType) serverTypes[idx];

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

	public IStatus modifyModules(IEnvironment env, IServer server,
			IModule module, boolean add, IProgressMonitor monitor) {

		IServerWorkingCopy wc = null;
		IStatus status = Status.OK_STATUS;
    
		try {

			if (module == null || !module.getProject().exists()) {
				return status;
			}

			// check if module is a true Java project
			if (module instanceof IModule) {
				IModule pm = (IModule) module;
				if (pm != null) {
					IProject project = pm.getProject();
					if (project == null
							|| ResourceUtils.isTrueJavaProject(project)) {
						return status;
					}
				}
			}

			wc = server.createWorkingCopy();
			if (wc != null) {
//				Object x = server.getAdapter(IRunningActionServer.class);
//				if (x != null && x instanceof IRunningActionServer) {
//					int state = server.getServerState();
//					if (state == IServer.STATE_STOPPED
//							|| state == IServer.STATE_UNKNOWN) {
//						String mode = ILaunchManager.RUN_MODE;
//						server.synchronousStart(mode, monitor);
//					}
//				}

				List list = Arrays.asList(server.getModules());
				if (add) {
					if (!list.contains(module)) {
						ServerUtil.modifyModules(wc, new IModule[] { module },
								new IModule[0], monitor);
					}
				} else { // removes module
					if (list.contains(module)) {
						ServerUtil.modifyModules(wc, new IModule[0],
								new IModule[] { module }, monitor);
					}
				}
			} else {
				// handle case of Null WC; non-issue for now
			}
		} 
    catch (CoreException exc ) 
    {
			status = StatusUtils.errorStatus( WSPluginMessages.MSG_ERROR_SERVER, exc );
			env.getStatusHandler().reportError(status);
			return status;
		} finally {
			if (wc != null) {
				// Always saveAll and release the serverWorkingCopy
				try 
        {
					wc.saveAll(true, monitor);
				} 
        catch (CoreException exc ) 
        {
					status = StatusUtils.errorStatus( WSPluginMessages.MSG_ERROR_SERVER, exc );
					env.getStatusHandler().reportError(status);
					return status;
				}
			}
		}
		return status;
	}

	public static IServer getServerForModule(IModule module, String serverTypeId, 
								IServer server, boolean create, IProgressMonitor monitor) {
		if (server != null)
			return server;
		else
			return getServerForModule(module, serverTypeId, create, monitor);
	}

	public static IServer getServerForModule(IModule module,
			String serverTypeId, boolean create, IProgressMonitor monitor) {
		try {

			IServer[] servers = ServerUtil.getServersByModule(module, monitor);

			if (servers != null && servers.length > 0) {
				if (serverTypeId == null || serverTypeId.length() == 0)
					return servers[0]; // WSAD v4 behavior

				for (int i = 0; i < servers.length; i++) {
					if (servers[i].getServerType().getId().equalsIgnoreCase(
							serverTypeId))
						return servers[i];
				}
			}

			return createServer(module, serverTypeId, monitor);

		} catch (Exception e) {
			return null;
		}
	}

	public static IServer getServerForModule(IModule module) {
		try {
			IServer[] servers = ServerUtil.getServersByModule(module, null);
			return ((servers != null && servers.length > 0) ? servers[0] : null);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param env
	 * @param module
	 * @param serverTypeId
	 * @param monitor
	 * @return
	 * 
	 * @deprecated
	 */
	public IServer createServer(IEnvironment env, IModule module,
			String serverTypeId, IProgressMonitor monitor) {
		IServerWorkingCopy serverWC = null;
		IServer server = null;
		try {
			IServerType serverType = ServerCore.findServerType(serverTypeId);
			serverWC = serverType.createServer(serverTypeId, null, monitor);
			try {
				if (serverWC != null) {
					server = serverWC.saveAll(true, monitor);
				}
			} catch (CoreException ce) 
      {
				IStatus status = StatusUtils.errorStatus( WSPluginMessages.MSG_ERROR_SERVER, ce);
				env.getStatusHandler().reportError(status);
				return null;
			}

			if (server != null) {

//				Object x = server.getAdapter(IRunningActionServer.class);
//				if (x != null && x instanceof IRunningActionServer) {
//					int state = server.getServerState();
//					if (state == IServer.STATE_STOPPED
//							|| state == IServer.STATE_UNKNOWN) {
//						String mode = ILaunchManager.RUN_MODE;
//						server.synchronousStart(mode, monitor);
//					}
//				}

				if (module != null && module.getProject().exists()) {
					IModule[] parentModules = server.getRootModules(module,
							monitor);
					if (parentModules != null && parentModules.length != 0) {
						module = (IModule) parentModules[0];
					}
					serverWC.modifyModules(new IModule[] { module },
							new IModule[0], monitor);
				}

			}

			return server;
		} 
    catch (Exception e) 
    {
			IStatus status = StatusUtils.errorStatus( WSPluginMessages.MSG_ERROR_SERVER, e);
			env.getStatusHandler().reportError(status);
			return null;
		} finally {
			try {
				if (serverWC != null) {
					serverWC.saveAll(true, monitor);
				}
			} catch (CoreException ce) {
				IStatus status = StatusUtils.errorStatus( WSPluginMessages.MSG_ERROR_SERVER, ce);
				env.getStatusHandler().reportError(status);
				return null;
			}
		}
	}

	/**
	 * createServer This creates a server but does not report errors. @param
	 * module 
	 * @param serverTypeId 
	 * @param monitor progress monitor 
	 * @return IServer returns null if unsuccessful
	 * 
	 * @deprecated
	 */
	public static IServer createServer(IModule module, String serverTypeId,
			IProgressMonitor monitor) {
		IServerWorkingCopy serverWC = null;
		IServer server = null;
		try {
			IServerType serverType = ServerCore.findServerType(serverTypeId);
			serverWC = serverType.createServer(serverTypeId, null, monitor);

			try {
				if (serverWC != null) {
					server = serverWC.saveAll(true, monitor);
				}
			} catch (CoreException ce) {
				return null;
			}

			if (server != null) {

//				Object x = server.getAdapter(IRunningActionServer.class);
//				if (x != null && x instanceof IRunningActionServer) {
//					int state = server.getServerState();
//					if (state == IServer.STATE_STOPPED
//							|| state == IServer.STATE_UNKNOWN) {
//						String mode = ILaunchManager.RUN_MODE;
//						server.synchronousStart(mode, monitor);
//					}
//				}
				if (module != null) {
					IModule[] parentModules = server.getRootModules(module,
							monitor);
					if (parentModules != null && parentModules.length != 0) {
						module = (IModule) parentModules[0];
					}
					serverWC.modifyModules(new IModule[] { module },
							new IModule[0], monitor);
				}

			}

			return server;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (serverWC != null) {
					serverWC.saveAll(true, monitor);
				}
			} catch (CoreException ce) {
				return null;
				// handler core exception
			}
		}
	}

	public static String[] getServerTypeIdsByModule(IVirtualComponent component) {
		IProject project = component.getProject();
		String[] serverIds = null;

		if (project != null) {
			IServer[] servers = ServerUtil.getServersByModule(getModule(project), null);
			if (servers != null) {
				serverIds = new String[servers.length];

				for (int index = 0; index < servers.length; index++) {
					serverIds[index] = servers[index].getId();

				}
			}
		}

		if (serverIds == null) {
			serverIds = new String[0];
		}

		return serverIds;
	}

	/**
	 * @param project
	 * @return
	 * @deprecated should be using getServerTypeIdsByModule( IVirtualComponent )
	 */
	public static String[] getServerTypeIdsByModule(IProject project) {
		Vector serverIds = new Vector();
		if (project != null) {
			IServer[] servers = ServerUtil.getServersByModule(ResourceUtils
					.getModule(project), null);
			if (servers != null && servers.length > 0) {
				for (int i = 0; i < servers.length; i++) {
					serverIds.add(servers[i].getId());
				}
			}
		}
		return (String[]) serverIds.toArray(new String[serverIds.size()]);
	}

	public static IModule getModule(IProject project) {
	return ServerUtil.getModule(project);
}
	
// Workaround for 113621
//public static IModule getModule(IProject project) {
//	IModule[] modules = ServerUtil.getModules(project);
//	if (modules!=null && modules.length!=0) {
//		return modules[0];
//	}
//	return null;
//}
  
	/**
	 * Returns the URL string corresponding to the web server module root of the
	 * component in a server instance or null if the project has no Web nature
	 * or has no association to a server instance.
	 * 
	 * @param project
	 *            The project.
	 * @return The web server module root URL or null if the project has no Web
	 *         nature or has no association to a server instance.
	 */
	public static String getWebComponentURL(IProject project, String serverFactoryId) {
		String webProjectURL = null;
		IModule module = getModule(project);
		if (module != null) {
			IServer serverInstance = getServerForModule(module);
			if (serverInstance != null) {
				IURLProvider urlProvider = (IURLProvider) serverInstance.loadAdapter(IURLProvider.class, null);
				if (urlProvider!=null) {
					URL url = urlProvider.getModuleRootURL(module);
					if (url != null) {
						String s = url.toString();
						webProjectURL = (s.endsWith("/") ? s.substring(0, s.length() - 1) : s);
					}
				}
			}
			else {
				//IRuntime projectTarget = ServerCore.getProjectProperties(project).getRuntimeTarget();
                IServerType serverType = ServerCore.findServerType(serverFactoryId);               
				if (serverType!=null)
				{
					try {
                        //Choose a Runtime which is not a stub
                        IRuntime nonStubRuntime = null;
                        IRuntime[] runtimes = ServerUtil.getRuntimes(null, null);
                        String serverRuntimeTypeId = serverType.getRuntimeType().getId();
                        for (int i = 0; i < runtimes.length; i++) {
                            IRuntime runtime = runtimes[i];
                            String thisRuntimeTypeId = runtime.getRuntimeType().getId();
                            if (thisRuntimeTypeId.equals(serverRuntimeTypeId) && !runtime.isStub()) {
                                //Found an appropriate IRuntime that is not a stub
                                nonStubRuntime = runtime;
                                break;
                            }
                        }
                        
                        if (nonStubRuntime != null)
                        {
					      IServerWorkingCopy serverWC = serverType.createServer(null, null, nonStubRuntime, null);
						  IURLProvider urlProvider = (IURLProvider) serverWC.loadAdapter(IURLProvider.class, null);
						  if (urlProvider!=null) {
						     URL url = urlProvider.getModuleRootURL(module);							
							 if (url != null) {
								String s = url.toString();
								webProjectURL = (s.endsWith("/") ? s.substring(0, s.length() - 1) : s);
							 }				
						  }
                        }

					} catch(CoreException ce){
                        Logger.getLogger().log(ce);
					}
					
				}
			}
		}
		return webProjectURL;
	}

	/**
	 * Returns the URL string corresponding to the web server module root of the
	 * project in a server instance or null if the project has no Web nature or
	 * has no association to a server instance.
	 * 
	 * @param project
	 *            The project.
	 * @return The web server module root URL or null if the project has no Web
	 *         nature or has no association to a server instance.
	 */
	public static String getWebComponentURL(IProject project,
			String serverFactoryId, IServer server) {

		String webProjectURL = null;
		IModule module = getModule(project);
		if (module != null) {
			IServer serverInstance = ServerUtils.getServerForModule(module, serverFactoryId, server, true, new NullProgressMonitor());
			if (serverInstance != null) {
                IURLProvider urlProvider = (IURLProvider) serverInstance.loadAdapter(IURLProvider.class, null);
                if (urlProvider!=null) {
                    URL url = urlProvider.getModuleRootURL(module);              
					if (url != null) {
					  String s = url.toString();
					  webProjectURL = (s.endsWith("/") ? s.substring(0, s.length() - 1) : s);
                    }
				}
			}
		}
		return webProjectURL;
	}
	
	public static String getEncodedWebComponentURL(IProject project, String serverFactoryId) {
		String url = getWebComponentURL(project, serverFactoryId);
		return encodeURL(url);
	}
	
	public static String getEncodedWebComponentURL(IProject project, String serverFactoryId, IServer server) {
		String url = getWebComponentURL(project, serverFactoryId, server);
		return encodeURL(url);
	}

	public static String encodeURL(String url) {
		if (url != null) {
			int index = url.lastIndexOf('/');
			if (index != -1) {
				StringBuffer encodedURL = new StringBuffer();
				encodedURL.append(url.substring(0, index + 1));
				try {
					String ctxtRoot = URLEncoder.encode(url.substring(index + 1, url.length()), "UTF-8");
					int plusIndex = ctxtRoot.indexOf('+');
					while (plusIndex != -1) {
						StringBuffer sb = new StringBuffer();
						sb.append(ctxtRoot.substring(0, plusIndex));
						sb.append("%20");
						sb.append(ctxtRoot.substring(plusIndex + 1, ctxtRoot
							.length()));
						ctxtRoot = sb.toString();
						plusIndex = ctxtRoot.indexOf('+');
					}
					encodedURL.append(ctxtRoot);
				}catch (IOException io){
					//handler exception
				}				
				url = encodedURL.toString();
			}
		}
		return url;
	}	

	public static IServer getDefaultExistingServer(IVirtualComponent component) {
		IProject project = component.getProject();
		IModule module = getModule(project);
		IServer preferredServer = ServerCore.getDefaultServer(module);
		if (preferredServer != null)
			return preferredServer;
		
		IServer[] configuredServers = ServerUtil.getServersByModule(module,
				null);

		if (configuredServers != null && configuredServers.length > 0) {
			preferredServer = configuredServers[0];
		} else {
			IServer[] nonConfiguredServers = ServerUtil
					.getAvailableServersForModule(module, false, null);

			if (nonConfiguredServers != null
					&& nonConfiguredServers.length > 0) {
				preferredServer = nonConfiguredServers[0];
			}
		}

		return preferredServer;
	}

	/**
	 * @param project
	 * @return
	 * @deprecated should be using getDefaultExistingServer( IVirtualComponent )
	 */
	public static IServer getDefaultExistingServer(IProject project) {
    
      IModule module = ServerUtil.getModule(project);
      IServer preferredServer = null;
      preferredServer = ServerCore.getDefaultServer(module);
		
// Workaround for 113621
//		IModule[] modules = ServerUtil.getModules(project);
//      IServer preferredServer = null;
//      if (modules.length > 0){
//        preferredServer = ServerCore.getDefaultServer(modules[0]);
//      }
  
		if (preferredServer != null)
			return preferredServer;

		IServer[] configuredServers = ServerUtil.getServersByModule(
				ResourceUtils.getModule(project), null);
		if (configuredServers != null && configuredServers.length > 0) {
			return configuredServers[0];
		}

		IServer[] nonConfiguredServers = ServerUtil
				.getAvailableServersForModule(ResourceUtils.getModule(project),
						false, null);
		if (nonConfiguredServers != null && nonConfiguredServers.length > 0) {
			return nonConfiguredServers[0];
		}
		return null;
	}

	/*
	 * @param moduleType - ad defined in IServerTargetConstants (i.e. EAR_TYPE,
	 * WEB_TYPE, etc.) @param j2eeVersion String representation of the int
	 * values in J2EEVersionConstants i.e. "12" or "13" or "14" @return String
	 * the id of the server target - to be used in project creation operations.
	 */
	public static String getServerTargetIdFromFactoryId(String serverFactoryId,
			String moduleType, String j2eeVersion) {
		IServerType serverType = ServerCore.findServerType(serverFactoryId);
		if (serverType == null)
			return null;

		String serverRuntimeTypeId = serverType.getRuntimeType().getId();

		String stJ2EEVersion = ServerUtils.getServerTargetJ2EEVersion(j2eeVersion);
        List runtimes = Arrays.asList(ServerUtil.getRuntimes(moduleType, stJ2EEVersion));    
		for (int i = 0; i < runtimes.size(); i++) {
			IRuntime runtime = (IRuntime) runtimes.get(i);
			String thisRuntimeTypeId = runtime.getRuntimeType().getId();
			if (thisRuntimeTypeId.equals(serverRuntimeTypeId) && !runtime.isStub()) {
				return runtime.getId();
			}
		}

		return null;
	}

	/*
	 * @param serverFactoryId the server's factory id @returns the runtime type
	 * id given the server's factory id. Returns a blank String if the no
	 * ServerType exists for the given factory id.
	 */
	public static String getRuntimeTargetIdFromFactoryId(String serverFactoryId) {
		IServerType serverType = ServerCore.findServerType(serverFactoryId);
		if (serverType != null) {
			String serverRuntimeId = serverType.getRuntimeType().getId();
			return serverRuntimeId;
		} else
			return "";
	}
	
	public static String getFactoryIdFromRuntimeTargetId(String runtimeTargetId){
		IServerType[] serverTypes = ServerCore.getServerTypes();
		for (int i=0;i<serverTypes.length;i++) {
			IRuntimeType runtimeTyp = serverTypes[i].getRuntimeType();
			if (runtimeTyp!=null){
				if (runtimeTyp.getId().equals(runtimeTargetId))
					return serverTypes[i].getId();
			}
		}
		return "";
		
	}
	

	/*
	 * @param serverFactoryId the server's factory id @returns the server type
	 * id given the server's factory id. Returns a blank String if the no
	 * ServerType exists for the given factory id.
	 */
	public static String getServerTypeIdFromFactoryId(String serverFactoryId) {
		IServerType serverType = ServerCore.findServerType(serverFactoryId);
		if (serverType != null) {
			String serverTypeId = serverType.getId();
			return serverTypeId;
		} else
			return "";
	}

	/*
	 * @param j2eeVersion String representation of the int values in
	 * J2EEVersionConstants i.e. "12" or "13" or "14"
	 */
	public static boolean isTargetValidForEAR(String runtimeTargetId,
			String j2eeVersion) {
		if (runtimeTargetId == null)
			return false;

		String earModuleType = IModuleConstants.JST_EAR_MODULE;
		String stJ2EEVersion = ServerUtils.getServerTargetJ2EEVersion(j2eeVersion);
        List runtimes = Arrays.asList(ServerUtil.getRuntimes(earModuleType, stJ2EEVersion));
		for (int i = 0; i < runtimes.size(); i++) {
			IRuntime runtime = (IRuntime) runtimes.get(i);
			String thisId = runtime.getRuntimeType().getId();
			if (thisId.equals(runtimeTargetId))
				return true;
		}

		return false;
	}

	/*
	 * @param j2eeVersion String representation of the int values in
	 * J2EEVersionConstants i.e. "12" or "13" or "14" @param the project type
	 * from IServerTargetConstants
	 */
	public static boolean isTargetValidForProjectType(String runtimeTargetId,
			String j2eeVersion, String projectType) {
		if (runtimeTargetId == null)
			return false;

		if (projectType == null || projectType.length() == 0)
			return false;

		String stJ2EEVersion = ServerUtils.getServerTargetJ2EEVersion(j2eeVersion);
        List runtimes = Arrays.asList(ServerUtil.getRuntimes(projectType, stJ2EEVersion));    
		for (int i = 0; i < runtimes.size(); i++) {
			IRuntime runtime = (IRuntime) runtimes.get(i);
			String thisId = runtime.getRuntimeType().getId();
			if (thisId.equals(runtimeTargetId))
				return true;
		}

		return false;
	}

	public static String getServerTargetJ2EEVersion(String j2eeVersion) {

		if (j2eeVersion == null || j2eeVersion.length() == 0)
			return null;

		int j2eeVersionInt = Integer.parseInt(j2eeVersion);
		switch (j2eeVersionInt) {
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
	
	//Converts a module type from J2EEUtils to a module type like
	//the one in IServerTargetConstants.
	public static String getServerTargetModuleType(int moduleType)
	{
		switch (moduleType)
    {
    case J2EEUtils.WEB :
      return IServerTargetConstants.WEB_TYPE;
    case J2EEUtils.EJB :
      return IServerTargetConstants.EJB_TYPE;
    case J2EEUtils.APPCLIENT :
      return IServerTargetConstants.APP_CLIENT_TYPE;
    case J2EEUtils.EAR :
      return IServerTargetConstants.EAR_TYPE;
    default:
      return null;
      
    }      			
	}
    
  /*
   * Returns a non-stub runtime if one is available for the the given server type.
   * @param serverFactoryId - server type id
   * @returns IRuntime Returns a non-stub runtime if one is available for the the given server type.
   * Returns null otherwise.
   */
  public static IRuntime getNonStubRuntime(String serverFactoryId)
  {
    //Find a Runtime which is not a stub
    IServerType serverType = ServerCore.findServerType(serverFactoryId);
    IRuntime nonStubRuntime = null;
    if (serverType != null)
    {
      //boolean foundNonStubRuntime = false;
      IRuntime[] runtimes = ServerUtil.getRuntimes(null, null);
      String serverRuntimeTypeId = serverType.getRuntimeType().getId();
      for (int i = 0; i < runtimes.length; i++)
      {
        IRuntime runtime = runtimes[i];
        String thisRuntimeTypeId = runtime.getRuntimeType().getId();
        if (thisRuntimeTypeId.equals(serverRuntimeTypeId) && !runtime.isStub())
        {
          // Found an appropriate IRuntime that is not a stub
          //foundNonStubRuntime = true;
          nonStubRuntime = runtime;
          break;
        }
      }
    }
    
    return nonStubRuntime;
  }
}
