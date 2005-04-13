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

import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
import org.eclipse.jst.ws.internal.consumption.ui.selection.SelectionTransformer;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
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
  private String ID_WEB = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Web";
  private String ID_EJB = "org.eclipse.jst.ws.consumption.ui.clientProjectType.EJB";
  private String ID_APP_CLIENT = "org.eclipse.jst.ws.consumption.ui.clientProjectType.AppClient";
  private String ID_JAVA = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Containerless";
  
  private TypeRuntimeServer typeRuntimeServer_;
  private Environment       environment_;
  private IContext          context_;
  private ISelection        selection_;
  private String            project_;
  private String            module_;
  private String            moduleType_;
  private String            earProject_;
  private String            ear_;
  private IWebServiceClient webServiceClient_;
  private String            j2eeLevel_;
  private ResourceContext   resourceContext_;
	private boolean						test_;
  private String            wsdlURI_;

  public Status execute(Environment environment)
  {
    IWebServiceRuntime wsrt = WebServiceRuntimeExtensionUtils
        .getWebServiceRuntime(typeRuntimeServer_.getRuntimeId());
    WebServiceClientInfo wsInfo = new WebServiceClientInfo();

    System.out.println("In Pre client develop command.");

    wsInfo.setJ2eeLevel(j2eeLevel_);
    wsInfo.setServerFactoryId(typeRuntimeServer_.getServerId());
    wsInfo.setServerInstanceId(typeRuntimeServer_.getServerInstanceId());
    wsInfo.setState(WebServiceState.UNKNOWN_LITERAL);
    wsInfo.setWebServiceRuntimeId(typeRuntimeServer_.getRuntimeId());
    wsInfo.setWsdlURL(wsdlURI_);
    
    environment_ = environment;
    webServiceClient_ = wsrt.getWebServiceClient(wsInfo);
    WebServiceScenario scenario = WebServiceScenario.CLIENT_LITERAL;
    context_ = new SimpleContext(true, true, true, true, true, true, test_,
        false, scenario, resourceContext_.isOverwriteFilesEnabled(),
        resourceContext_.isCreateFoldersEnabled(), resourceContext_
            .isCheckoutFilesEnabled());

    // Create the client module
    CreateModuleCommand command = new CreateModuleCommand();
    command.setProjectName(project_);
    command.setModuleName(module_);
    // rsk todo -- once the clientProjectType extension is gone, determination
    // of what type of module to create will have to be done.
    if (moduleType_.equals(ID_WEB)) command.setModuleType(CreateModuleCommand.WEB);
    if (moduleType_.equals(ID_EJB)) command.setModuleType(CreateModuleCommand.EJB);
    if (moduleType_.equals(ID_APP_CLIENT)) command.setModuleType(CreateModuleCommand.APPCLIENT);
    
    command.setServerFactoryId(typeRuntimeServer_.getServerId());
    command.setJ2eeLevel(j2eeLevel_);
    Status status = command.execute(environment);

    if (status.getSeverity() == Status.ERROR)
    {
      environment.getStatusHandler().reportError(status);
    }
    return status;
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer typeRuntimeServer )
  {
	typeRuntimeServer_ = typeRuntimeServer;  
  }
  
  public void setClientJ2EEVersion( String j2eeLevel )
  {
	j2eeLevel_ = j2eeLevel;  
  }
  
  public String getJ2eeLevel()
  {
    return j2eeLevel_;  
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

  public String getProject()
  {
    return project_;    
  }
  
  public void setProject( String project )
  {
    project_ = project;
  }
  
  public String getModule()
  {
    return module_;	  
  }
  
  public void setModule( String module )
  {
	module_ = module;
  }
  
  public void setModuleType( String moduleType)
  {
    moduleType_ = moduleType;
  }

  public String getEarProject()
  {
    return earProject_;   
  }
  
  public void setEarProject( String earProject )
  {
    earProject_ = earProject;
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
  
  public void setWsdlURI(String uri)
  {
    wsdlURI_ = uri;
  }

}
