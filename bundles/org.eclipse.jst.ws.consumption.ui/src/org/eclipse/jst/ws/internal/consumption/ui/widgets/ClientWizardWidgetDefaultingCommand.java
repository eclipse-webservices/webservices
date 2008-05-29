/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20060728   150560 kathy@ca.ibm.com - Kathy Chan
 * 20060728   151078 kathy@ca.ibm.com - Kathy Chan
 * 20080402   225378 makandre@ca.ibm.com - Andrew Mak, Client wizard runtime/server defaulting is not respecting the preference
 * 20080528   234487 makandre@ca.ibm.com - Andrew Mak, Performance degradation in Web service client gen
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class ClientWizardWidgetDefaultingCommand extends AbstractDataModelOperation
{    
	private int clientGeneration_;
	private boolean developClient_;
	private boolean assembleClient_;
	private boolean deployClient_;
	private boolean installClient_;
	private boolean startClient_;
	private boolean testClient_;
	private boolean clientOnly_ = false;
	private TypeRuntimeServer clientTypeRuntimeServer_;
	  
	public ClientWizardWidgetDefaultingCommand() {
	}
	
	
	/**
	 * @param clientOnly  Set to true for if called from ClientWidgetWizard
	 */
	public ClientWizardWidgetDefaultingCommand(boolean clientOnly) {
		clientOnly_ = clientOnly;
	}
	
  public Boolean getTestService()
  {
    return new Boolean( getScenarioContext().getTestWebService() );
  }
  
  public Boolean getMonitorService()
  {
    return new Boolean( getScenarioContext().getMonitorWebService());
  }
  
  public boolean getInstallClient()
  {
    return installClient_; 
  }
  
  public boolean getRunTestClient()
  {
    return getScenarioContext().isLaunchSampleEnabled() ;  
  }
  
  public int getClientGeneration()
  {
	  return clientGeneration_;
  }

  public ResourceContext getResourceContext()
  { 
    return WebServicePlugin.getInstance().getResourceContext();
  }
  
  // Current ScenarioContext is used to default the first page of the wizard.  The 
  // properties in ScenarioContext are mapped individual.  Therefore, ScenarioContext
  // should not also be mapped.
  protected ScenarioContext getScenarioContext()
  {
    return WebServicePlugin.getInstance().getScenarioContext();
  }
  
  // TODO Set client name defaults here.  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
	if (clientTypeRuntimeServer_ == null) {
	    String type = getScenarioContext().getClientWebServiceType();
	    String runtime  = WebServiceRuntimeExtensionUtils2.getDefaultClientRuntimeValueFor(type);
	    String factoryID = WebServiceRuntimeExtensionUtils2.getDefaultClientServerValueFor(type);
	    clientTypeRuntimeServer_ = new TypeRuntimeServer();
	    
	    clientTypeRuntimeServer_.setTypeId( type );
	    clientTypeRuntimeServer_.setRuntimeId( runtime );
	    clientTypeRuntimeServer_.setServerId( factoryID );
	}
	
    return clientTypeRuntimeServer_;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
	  defaultClientScale();
	  return Status.OK_STATUS;
  }
  
  protected void defaultClientScale() {
	  clientGeneration_ = getScenarioContext().getGenerateClient();
	  
	  if (clientOnly_ && clientGeneration_ == ScenarioContext.WS_NONE) {
		  developClient_ = true;
		  assembleClient_ = true;
		  deployClient_ = true;
		  clientGeneration_ = ScenarioContext.WS_DEPLOY;
	  } else {
		  developClient_ = clientGeneration_ <= ScenarioContext.WS_DEVELOP;
		  assembleClient_ = clientGeneration_ <= ScenarioContext.WS_ASSEMBLE;
		  deployClient_ = clientGeneration_ <= ScenarioContext.WS_DEPLOY;
	  }
	  installClient_ = clientGeneration_ <= ScenarioContext.WS_INSTALL;
	  startClient_ = clientGeneration_ <= ScenarioContext.WS_START;
	  testClient_ = clientGeneration_ <= ScenarioContext.WS_TEST;
  }
  
  public boolean getDevelopClient() {
	  return developClient_;
  }
  
  public boolean getAssembleClient() {
	  return assembleClient_;
  }
  
  public boolean getDeployClient() {
	  return deployClient_;
  }
  
  public boolean getStartClient() {
	  return startClient_;
  }
  
  public boolean getTestClient() {
	  return testClient_;
  }
  
}
