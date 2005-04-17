package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

public class AddModuleToServerCommand extends SimpleCommand
{
  private String serverInstanceId;
	private String project;
	private String module;
	
	public Status execute(Environment env)
	{
		return new SimpleStatus("");
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
