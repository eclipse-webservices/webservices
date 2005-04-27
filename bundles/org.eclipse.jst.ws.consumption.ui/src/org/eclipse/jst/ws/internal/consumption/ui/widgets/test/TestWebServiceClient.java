package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.wst.command.internal.provisional.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.ws.internal.provisional.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;

public class TestWebServiceClient extends AbstractWebServiceClient {
  
  public TestWebServiceClient(WebServiceClientInfo clientInfo){
	super(clientInfo);  
  }
  
  public ICommandFactory assemble(Environment env, IContext ctx, ISelection sel,
      String project, String module, String earProject, String ear){
    return null;	  
  }
			
  public ICommandFactory deploy(Environment env, IContext ctx, ISelection sel,
      String project, String module, String earProject, String ear){
    return null;	  
  }
			

  public ICommandFactory develop(Environment env, IContext ctx, ISelection sel,
		      String project, String module, String earProject, String ear){
    return null;	  
  }
				
  public ICommandFactory install(Environment env, IContext ctx, ISelection sel,
		      String project, String module, String earProject, String ear){
    return null;
  }
			

  public ICommandFactory run(Environment env, IContext ctx, ISelection sel,
		      String project, String module, String earProject, String ear){
    return null;	  
  }
  
}
