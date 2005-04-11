/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.extension;

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceState;
import org.eclipse.wst.ws.internal.wsrt.SimpleContext;

public class PreClientDevelopCommand extends SimpleCommand 
{
  private TypeRuntimeServer typeRuntimeServer_;
  private Environment       environment_;
  private IContext          context_;
  private ISelection        selection_;
  private String            module_;
  private String            ear_;
  private IWebServiceClient webServiceClient_;
  private String            j2eeLevel_;
  private ResourceContext   resourceContext_;
	private boolean						test_;

  public Status execute(Environment environment) 
  {
	IWebServiceRuntime   wsrt   = WebServiceRuntimeExtensionUtils.getWebServiceRuntime( typeRuntimeServer_.getRuntimeId() );
	WebServiceClientInfo wsInfo = new WebServiceClientInfo();
 
	System.out.println( "In Pre client develop command." );
	
	wsInfo.setJ2eeLevel( j2eeLevel_ );
	wsInfo.setServerFactoryId( typeRuntimeServer_.getServerId() );
	wsInfo.setState( WebServiceState.UNKNOWN_LITERAL );
	wsInfo.setWebServiceRuntimeId( typeRuntimeServer_.getRuntimeId() );

	environment_       = environment;
	webServiceClient_  = wsrt.getWebServiceClient( wsInfo );
	WebServiceScenario scenario = WebServiceScenario.CLIENT_LITERAL;
	context_           = new SimpleContext(true, true, true, true, true, true, test_, false, 
																					scenario, 
																					resourceContext_.isOverwriteFilesEnabled(), 
																					resourceContext_.isCreateFoldersEnabled(), 
																					resourceContext_.isCheckoutFilesEnabled());

    return new SimpleStatus("");
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
  {
	typeRuntimeServer_ = typeRuntimeServer;  
  }
  
  public void setClientJ2EEVersion( String j2eeLevel )
  {
	j2eeLevel_ = j2eeLevel;  
  }
  
  public IWebServiceClient getWebService()
  {
	return webServiceClient_;  
  }
  
  public Environment getEnvironment()
  {
	return environment_;
  }
  
  public IContext getContext()
  {
    return context_;
  }
  
  public void setResourceContext( ResourceContext resourceContext )
  {
    resourceContext_ = resourceContext;	  
  }
  
  public ISelection getSelection()
  {
    return selection_;	  
  }
  
  public void setSelection( ISelection selection )
  {
	selection_ = selection;  
  }
  
  public String getModule()
  {
    return module_;	  
  }
  
  public void setModule( String module )
  {
	module_ = module;
  }
  
  public String getEar()
  {
	return ear_;  
  }
  
  public void setEar( String ear )
  {
	ear_ = ear;  
  }
	
	public void setTestService(boolean testService)
	{
		test_ = testService;
	}		
}
