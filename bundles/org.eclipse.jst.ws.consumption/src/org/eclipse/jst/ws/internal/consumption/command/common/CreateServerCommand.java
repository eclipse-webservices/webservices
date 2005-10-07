package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class CreateServerCommand extends AbstractDataModelOperation
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
	
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    
		IStatus status = Status.OK_STATUS;
		
		if (serverFactoryId==null){
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_CREATE_SERVER") );
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
					status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_STUB_ONLY",new String[]{serverFactoryId}) );
					return status;					
				}
				
				if (env!=null)
				{
					serverWC = serverType.createServer(null, null, nonStubRuntime, monitor );
				}
				else
				{					
					serverWC = serverType.createServer(null, null, nonStubRuntime, null);
				}
				
				if (serverWC != null) {
					if (env!=null)
						server = serverWC.saveAll(true, monitor );
					else
						server = serverWC.saveAll(true, null);
				}
			}
		} catch (CoreException ce) {
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_CREATE_SERVER"), ce);
			return status;
		}
		
		// set serverInstanceId
		if (server!=null)
			serverInstanceId = server.getId();
		else {
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_CREATE_SERVER") );			
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
