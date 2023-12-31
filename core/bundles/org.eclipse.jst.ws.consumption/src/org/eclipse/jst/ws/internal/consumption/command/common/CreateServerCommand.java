/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 * 20071210   203826 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class CreateServerCommand extends AbstractDataModelOperation
{
	private String serverFactoryId;
	private String serverRuntimeId;
	private String serverInstanceId;
	
	/**
	 * Creates a server of the factory id using the server runtime Id if provided
	 * Note1: Checking for server instance == null is done in the PreService/ClientInstallCommands
	 * Note2: Reporting of errors is done in PreService/ClientInstallCommands; simply return the status.
	 */
	public CreateServerCommand(){
	}
	
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
		IStatus status = Status.OK_STATUS;
		
		if (serverFactoryId==null){
			status = StatusUtils.errorStatus(ConsumptionMessages.MSG_ERROR_CREATE_SERVER );
			return status;			
		}
		
		IServerWorkingCopy serverWC = null;
		IServer server = null;
		try {
			IServerType serverType = ServerCore.findServerType(serverFactoryId);
			if (serverType!=null) {
				
				IRuntime serverRuntime = null;
				if (serverRuntimeId != null) {
					serverRuntime = ServerCore.findRuntime(serverRuntimeId);  // may return null if no runtime is found
				}
				
				if (serverRuntime == null) { // either serverRuntimeId is null or did not find a runtime with that Id
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

					if (nonStubRuntime==null)
					{					
						status = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_STUB_ONLY,new String[]{serverFactoryId}) );
						return status;					
					}

					serverRuntime = nonStubRuntime;
				} 
				
				if (env!=null)
				{
					serverWC = serverType.createServer(null, null, serverRuntime, monitor );
				}
				else
				{					
					serverWC = serverType.createServer(null, null, serverRuntime, null);
				}
				
				if (serverWC != null) {
					if (env!=null)
						server = serverWC.saveAll(true, monitor );
					else
						server = serverWC.saveAll(true, null);
				}
			}
		} catch (CoreException ce) {
			status = StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_CREATE_SERVER, ce);
			return status;
		}
		
		// set serverInstanceId
		if (server!=null)
			serverInstanceId = server.getId();
		else {
			status = StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_CREATE_SERVER );			
		}
		
		return status;
	}

	public void setServerFactoryid(String serverFactoryId)
	{
		this.serverFactoryId = serverFactoryId;
	}

	public void setServerRuntimeId(String serverRuntimeId)
	{
		this.serverRuntimeId = serverRuntimeId;
	}
	
	public String getServerInstanceId()
	{
		return serverInstanceId;
	}
	
	
}
