package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;

public class StartServerCommand extends SimpleCommand
{
	private String serverInstanceId;
	
	public Status execute(Environment env)
	{
		return new SimpleStatus("");
	}

	public void setServerInstanceId(String serverInstanceId)
	{
		this.serverInstanceId = serverInstanceId;
	}	
}
