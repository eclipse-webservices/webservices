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

package org.eclipse.jst.ws.internal.creation.ui.extension;

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceInfo;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceState;
import org.eclipse.wst.ws.internal.wsrt.SimpleContext;

public class PreServiceDevelopCommand extends SimpleCommand 
{
  private TypeRuntimeServer typeRuntimeServer_;
  private Environment       environment_;
  private IContext          context_;
  private ISelection        selection_;
  private String            module_;
  private String            ear_;
  private IWebService       webService_;
  private String            j2eeLevel_;
  private ResourceContext   resourceContext_;
  
  private boolean run_;
  private boolean client_;
  private boolean test_;
  private boolean publish_;

  public Status execute(Environment environment) 
  {
	  IWebServiceRuntime wsrt   = WebServiceRuntimeExtensionUtils.getWebServiceRuntime( typeRuntimeServer_.getRuntimeId() );
	  WebServiceInfo     wsInfo = new WebServiceInfo();

	  System.out.println( "In Pre service develop command." );
	
	  wsInfo.setJ2eeLevel( j2eeLevel_ );
		wsInfo.setServerFactoryId( typeRuntimeServer_.getServerId() );
		wsInfo.setState( WebServiceState.UNKNOWN_LITERAL );
		wsInfo.setWebServiceRuntimeId( typeRuntimeServer_.getRuntimeId() );

		environment_ = environment;
		webService_  = wsrt.getWebService( wsInfo );
	
		//Set up the IContext
		WebServiceScenario scenario = null;
		if (typeRuntimeServer_.getTypeId().equals("org.eclipse.jst.ws.type.java"))
		{
			scenario = WebServiceScenario.BOTTOMUP_LITERAL;
		}
		else if (typeRuntimeServer_.getTypeId().equals("org.eclipse.jst.ws.type.wsdl"))
		{
		  scenario = WebServiceScenario.TOPDOWN_LITERAL;
		}
	
		context_     = new SimpleContext(true, true, true, true, run_, client_, test_, publish_, 
																		scenario, 
																		resourceContext_.isOverwriteFilesEnabled(),
																		resourceContext_.isCreateFoldersEnabled(),
																		resourceContext_.isCheckoutFilesEnabled());

    return new SimpleStatus("");
  }
  
  public void setServiceTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
  {
	typeRuntimeServer_ = typeRuntimeServer;  
  }
  
  public void setJ2eeLevel( String j2eeLevel )
  {
	j2eeLevel_ = j2eeLevel;  
  }
  
  public IWebService getWebService()
  {
	return webService_;  
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
  
	public void setStartService(boolean startService)
	{
		run_ = startService;
	}
	
	public void setTestService(boolean testService)
	{
		test_ = testService;
	}	
	
  public void setPublishService(boolean publishService)
  {
    publish_ = publishService;
  }
	
  public void setGenerateProxy(boolean genProxy)
  {
    client_ = genProxy;  
  }	
	

}
