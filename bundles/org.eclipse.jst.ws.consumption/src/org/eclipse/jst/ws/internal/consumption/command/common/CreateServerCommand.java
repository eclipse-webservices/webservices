package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class CreateServerCommand extends SimpleCommand
{
	private String serverFactoryId;
	private String serverInstanceId;
	
	private MessageUtils msgUtils;
	
	/**
	 * Creates a server of the factory id
	 * Note1: Checking for server instance == null is done in the PreService/ClientInstallCommands
	 * Note2: Reporting of errors is done in PreService/ClientInstallCommands; simply return the status.
	 */
	public CreateServerCommand(){
		msgUtils = new MessageUtils(WebServiceConsumptionPlugin.ID + ".plugin", this);
		
	}
	
	public Status execute(Environment env)
	{
		Status status = new SimpleStatus("");
		
		if (serverFactoryId==null){
			status = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_CREATE_SERVER"), Status.ERROR, null);
			return status;			
		}
		
		IServerWorkingCopy serverWC = null;
		IServer server = null;
		try {
			IServerType serverType = ServerCore.findServerType(serverFactoryId);
			if (serverType!=null) {
				
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
					status = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_STUB_ONLY",new String[]{serverFactoryId}), Status.ERROR);
					return status;					
				}
				
				if (env!=null)
				{
					serverWC = serverType.createServer(null, null, nonStubRuntime, EnvironmentUtils.getIProgressMonitor(env));
				}
				else
				{					
					serverWC = serverType.createServer(null, null, nonStubRuntime, null);
				}
				
				if (serverWC != null) {
					if (env!=null)
						server = serverWC.saveAll(true, EnvironmentUtils.getIProgressMonitor(env));
					else
						server = serverWC.saveAll(true, null);
				}
			}
		} catch (CoreException ce) {
			status = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_CREATE_SERVER"), Status.ERROR, ce);
			return status;
		}
		
		// set serverInstanceId
		if (server!=null)
			serverInstanceId = server.getId();
		else {
			status = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_CREATE_SERVER"), Status.ERROR, null);			
		}
		
		return status;
	}

	public void setServerFactoryid(String serverFactoryId)
	{
		this.serverFactoryId = serverFactoryId;
	}

	public String getServerInstanceId()
	{
		return serverInstanceId;
	}
	
	
}
