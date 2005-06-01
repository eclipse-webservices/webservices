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

import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
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
	private String						project_;
  private String            module_;
	private String						earProject_;
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
    // Split up the project and module
    int index = module_.indexOf("/");
    if (index!=-1){
      project_ = module_.substring(0,index);
      module_ = module_.substring(index+1);
    }

    if (ear_!=null && ear_.length()>0)
    {
      int earIndex = ear_.indexOf("/");
      if (earIndex!=-1) {
        earProject_ = ear_.substring(0,earIndex);
        ear_ = ear_.substring(earIndex+1);
      }
    }
    
    
	  IWebServiceRuntime wsrt   = WebServiceRuntimeExtensionUtils.getWebServiceRuntime( typeRuntimeServer_.getRuntimeId() );
	  WebServiceInfo     wsInfo = new WebServiceInfo();

	  System.out.println( "In Pre service develop command." );
	
	  wsInfo.setJ2eeLevel( j2eeLevel_ );
		wsInfo.setServerFactoryId( typeRuntimeServer_.getServerId() );
    wsInfo.setServerInstanceId( typeRuntimeServer_.getServerInstanceId());
		wsInfo.setState( WebServiceState.UNKNOWN_LITERAL );
		wsInfo.setWebServiceRuntimeId( typeRuntimeServer_.getRuntimeId() );
    

		environment_ = environment;
		webService_  = wsrt.getWebService( wsInfo );
	
		//Set up the IContext
		WebServiceScenario scenario = null;
    int scenarioInt = WebServiceRuntimeExtensionUtils.getScenarioFromTypeId(typeRuntimeServer_.getTypeId());
    if (scenarioInt == WebServiceScenario.BOTTOMUP)
		{
			scenario = WebServiceScenario.BOTTOMUP_LITERAL;
      String impl = (String)(selection_.getSelection())[0];
      wsInfo.setImplURL(impl);
		}
    else if (scenarioInt == WebServiceScenario.TOPDOWN)
		{
		  scenario = WebServiceScenario.TOPDOWN_LITERAL;
      String wsdlURL = (String)(selection_.getSelection())[0];
      wsInfo.setWsdlURL(wsdlURL);      
		}
	
		context_     = new SimpleContext(true, true, true, true, run_, client_, test_, publish_, 
																		scenario, 
																		resourceContext_.isOverwriteFilesEnabled(),
																		resourceContext_.isCreateFoldersEnabled(),
																		resourceContext_.isCheckoutFilesEnabled());

    //Create the service module
		CreateModuleCommand command = new CreateModuleCommand();
		command.setProjectName(project_);
		command.setModuleName(module_);
		//rsk todo -- pick the correct module type based on the Web service type, it's hard coded to WEB for now.
		command.setModuleType(CreateModuleCommand.WEB);
		command.setServerFactoryId(typeRuntimeServer_.getServerId());
		command.setJ2eeLevel(j2eeLevel_);
		Status status = command.execute(environment);
		
		if (status.getSeverity()==Status.ERROR)
		{
			environment.getStatusHandler().reportError(status);
		}			
	  return status;				
  }
  
  public void setServiceTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
  {
	  typeRuntimeServer_ = typeRuntimeServer;  
  }
	
  public void setJ2eeLevel( String j2eeLevel )
  {
	j2eeLevel_ = j2eeLevel;  
  }
  
  public String getJ2eeLevel()
  {
	  return j2eeLevel_;  
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
  
  public String getProject()
  {
    return project_;	  
  }
	 
  public String getModule()
  {
    return module_;	  
  }
	
  public void setModule( String module )
  {
	  module_ = module;
  }
	
  public String getEarProject()
  {
    return earProject_;	  
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
