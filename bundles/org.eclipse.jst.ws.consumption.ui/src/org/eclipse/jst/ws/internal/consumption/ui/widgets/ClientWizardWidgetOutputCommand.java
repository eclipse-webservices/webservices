/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class ClientWizardWidgetOutputCommand extends AbstractDataModelOperation
{    
  private boolean testService_;
  private boolean monitorService;
  private ResourceContext   resourceContext_;
  private TypeRuntimeServer clientIds_;
  private boolean installClient_;
  
  private String webServiceURI_;
  private IProject project_;
  private String componentName_;
  private boolean runTestClient_;
  private boolean developClient_;
  private boolean assembleClient_;
  private boolean deployClient_;
  private boolean startClient_;
  private boolean testClient_; 
  
  public boolean getTestService()
  {
    return testService_;
  }

  public void setTestService( boolean testService )
  {
    testService_ = testService;  
  }
  
  public boolean getRunTestClient()
  {
    return runTestClient_;
  }

  public void setRunTestClient( boolean runTestClient )
  {
    runTestClient_ = runTestClient;  
  }
  
  public void setInstallClient( boolean installClient)
  {
    installClient_ = installClient;  
  }
  
  public boolean getInstallClient()
  {
	return installClient_;  
  }
  
  public boolean getDevelopClient() {
		return developClient_;
	}

	public void setDevelopClient(boolean developClient) {
		this.developClient_ = developClient;
	}	
	
	public boolean getAssembleClient() {
		return assembleClient_;
	}

	public void setAssembleClient(boolean assembleClient) {
		this.assembleClient_ = assembleClient;
	}
	
	public boolean getDeployClient() {
		return deployClient_;
	}

	public void setDeployClient(boolean deployClient) {
		this.deployClient_ = deployClient;
	}
	
	public boolean getStartClient() {
		return startClient_;
	}

	public void setStartClient(boolean startClient) {
		this.startClient_ = startClient;
	}
	
	public boolean getTestClient() {
		return testClient_;
	}

	public void setTestClient(boolean testClient) {
		this.testClient_ = testClient;
	}
  public boolean getMonitorService()
  {
    return monitorService;
  }
  
  public void setMonitorService(boolean monitorService)
  {
    this.monitorService = monitorService;
  }
  
  public ResourceContext getResourceContext()
  {
    
    return resourceContext_;
  }
  
  public void setResourceContext( ResourceContext context )
  {
    resourceContext_ = context;  
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {    
    return clientIds_;  
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer clientIds )
  {
    clientIds_ = clientIds;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return Status.OK_STATUS;
  }
  
  public String getWsdlURI()
  {
      return webServiceURI_ ;
  }
  
  public void setWsdlURI(String uri)
  {
      webServiceURI_ = uri;
  }
  
  public void setProject(IProject project)
  {
      project_ = project;
  }
  
  public IProject getProject()
  {
      return project_;
  }
  
  public void setComponentName(String name)
  {
      componentName_ = name;
  } 
  
  public String getComponentName()
  {
      return componentName_ ;
  }
}
