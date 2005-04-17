package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

public class AssociateModuleWithEARCommand extends SimpleCommand
{
	private String						project_;
  private String            module_;
	private String						earProject_;
  private String            ear_;
	
	public Status execute(Environment env)
	{
		return new SimpleStatus("");
	}
	
  public void setProject( String project )
  {
	  project_ = project;
  }
	  
  public void setModule( String module )
  {
	  module_ = module;
  }	
	
  public void setEARProject( String earProject )
  {
	  earProject_ = earProject;
  }
  
  public void setEar( String ear )
  {
	  ear_ = ear;  
  }	
}
