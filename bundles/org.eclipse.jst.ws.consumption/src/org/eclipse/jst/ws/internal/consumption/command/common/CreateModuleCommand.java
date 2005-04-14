package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;

public class CreateModuleCommand extends SimpleCommand
{
	
	public static int WEB = 0;
	public static int EJB = 1;
	public static int APPCLIENT = 2;
	public static int EAR = 3;
	
	private String   projectName;
	private String   moduleName;
	private int      moduleType;;
	private String   j2eeLevel;
	private String   serverFactoryId;
	
	public Status execute(Environment env)
	{
		//rsk todo
		return new SimpleStatus("");
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}	

	public int getModuleType()
	{
		return moduleType;
	}

	public void setModuleType(int moduleType)
	{
		this.moduleType = moduleType;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public void setJ2eeLevel(String level)
	{
		j2eeLevel = level;
	}

	public void setServerFactoryId(String serverFactoryId)
	{
		this.serverFactoryId = serverFactoryId;
	}

}
