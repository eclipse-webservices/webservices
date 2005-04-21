package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class AddModuleToServerCommand extends SimpleCommand
{
	  private java.lang.String DESCRIPTION = "(Re)Start and publish the server";
	  private java.lang.String LABEL = "StartServerCommand";
	  private MessageUtils msgUtils_;
	  
  private String serverInstanceId;
	private String project;
	private String module;
	
	public AddModuleToServerCommand()
	{
	  String pluginId = "org.eclipse.jst.ws.consumption";
	  msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	  setDescription(DESCRIPTION);
	  setName(LABEL);		
	}
	
	public Status execute(Environment env)
	{
	    Status status = new SimpleStatus("");	    
	    
	    IServer server = ServerCore.findServer(serverInstanceId);
	    if (server == null)
	    {
	      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_INSTANCE_NOT_FOUND"), Status.ERROR);
	      env.getStatusHandler().reportError(status);
	      return status;
	    }
	   
	    IServerWorkingCopy serverwc = null;
	    
	    try
	    {
	    //Ensure the module is not a Java utility
	    IProject iproject = (IProject)(new StringToIProjectTransformer()).transform(project);
	    if (!J2EEUtils.isJavaComponent(iproject, module))
	    {      
	      IModule imodule = ServerUtils.getModule(iproject, module);
	      if (!ServerUtil.containsModule(server, imodule, null))
	      {
	        IModule[] imodules = new IModule[]{imodule};
	        serverwc = server.createWorkingCopy();
	        ServerUtil.modifyModules(serverwc, imodules, null, null);
	      }

	    }
	    } catch (CoreException e)
	    {
	      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_ADD_MODULE", new String[]{module}), Status.ERROR);
	      env.getStatusHandler().reportError(status);
	      return status;      
	    } finally
	    {
	      try
	      {
	        if (serverwc != null)
	        {
	          serverwc.save(true, null);
	        }
	      } catch (CoreException ce)
	      {
	        status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_ADD_MODULE", new String[] { module }), Status.ERROR);
	        env.getStatusHandler().reportError(status);
	        return status;
	      }      
	    }
	    
		return status;
	    
		
	}

	public void setModule(String module)
	{
		this.module = module;
	}

	public void setProject(String project)
	{
		this.project = project;
	}

	public void setServerInstanceId(String serverInstanceId)
	{
		this.serverInstanceId = serverInstanceId;
	}	

	
}
