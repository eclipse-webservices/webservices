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
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 * 20080220   219537 makandre@ca.ibm.com - Andrew Mak
 * 20080523   233764 makandre@ca.ibm.com - Andrew Mak, Top down EJB preference not respected
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.ws.internal.ui.utils.AdapterUtils;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class ServerWizardWidgetDefaultingCommand extends ClientWizardWidgetDefaultingCommand
{    
  private TypeRuntimeServer typeRuntimeServer_;
  private IStructuredSelection initialSelection_;
  private int serviceGeneration_;
  private boolean developService_;
  private boolean assembleService_;
  private boolean deployService_;
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
  
    String type      = getScenarioContext().getWebServiceType();
    String runtime   = WebServiceRuntimeExtensionUtils2.getDefaultRuntimeValueFor( type );
	
    String factoryID = WebServiceRuntimeExtensionUtils2.getDefaultServerValueFor(type);    
    typeRuntimeServer_ = new TypeRuntimeServer();
    
    typeRuntimeServer_.setTypeId( type );
    typeRuntimeServer_.setRuntimeId( runtime );
    typeRuntimeServer_.setServerId( factoryID );
    
    //Default the typeId from the initial selection
    String[] typeIds = WebServiceRuntimeExtensionUtils2.getWebServiceTypeBySelection(initialSelection_);

    if (typeIds!=null && typeIds.length>0)
    {
      String preferred = typeIds[0];	// default to first entry initially
      for (String typeId : typeIds) {
    	  if (typeId.equals(type)) {
    		  preferred = typeId;
    		  break;
    	  }
      }
      typeRuntimeServer_.setTypeId(preferred);
  } else {
	  if (initialSelection_ != null && !initialSelection_.isEmpty())
	  { 
		  Object initialObject = initialSelection_.getFirstElement(); 
		  // initialObject would not be null since !initialSelection_.isEmpty()
		  boolean hasAdapter = AdapterUtils.hasAdapter(initialObject);
		  if (hasAdapter) {
			  String entry = null;
			  String prefix = WebServiceScenario.TOPDOWN + "/";
			  if (type.startsWith(prefix))
				  entry = type;
			  else {
				  StringBuffer entrybuff = new StringBuffer();
				  entrybuff.append(prefix);
				  entrybuff.append("org.eclipse.jst.ws.wsImpl.java");
				  entry = entrybuff.toString();
			  }
			  typeRuntimeServer_.setTypeId(entry);
		  }
	  }
  }
    
    serviceGeneration_ = getScenarioContext().getGenerateWebService();
    	
	developService_ = serviceGeneration_ <= ScenarioContext.WS_DEVELOP;
	assembleService_ = serviceGeneration_ <= ScenarioContext.WS_ASSEMBLE;
	deployService_ = serviceGeneration_ <= ScenarioContext.WS_DEPLOY;
    
	defaultClientScale();
	
    return Status.OK_STATUS;
  }
  
  public void setInitialSelection(IStructuredSelection selection)
  {
    initialSelection_ = selection;
  }
  
  public IStructuredSelection getInitialSelection()
  {
    return initialSelection_ ;
  }
  
  IProject project_;  
  
  public IProject getInitialProject()
  {
	  if (project_==null)
	  {
	    project_ = getProjectFromObjectSelection(initialSelection_);
	  }  
	  
	  return project_;
  }
  
  private IProject getProjectFromObjectSelection(IStructuredSelection selection)
  {
    if (selection != null && selection.size() == 1)
    {
      Object obj = selection.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = ResourceUtils.getResourceFromSelection(obj);
          if (resource==null) 
            return null;
          IProject p = ResourceUtils.getProjectOf(resource.getFullPath());
          return p;
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;
  }
  
  public TypeRuntimeServer getServiceTypeRuntimeServer()
  { 
    return typeRuntimeServer_;
  }

  public Boolean getInstallService()
  {
    return new Boolean( getScenarioContext().getInstallWebService() );  
  }
  
  public Boolean getStartService()
  {
    return new Boolean( getScenarioContext().getStartWebService() );  
  }
  
  public Boolean getTestService()
  {
    return new Boolean( getScenarioContext().getTestWebService() );  
  }
  
  public Boolean getPublishService()
  {
    return new Boolean( getScenarioContext().getLaunchWebServiceExplorer() );
  }

  public Boolean getGenerateProxy()
  {
    return new Boolean( getScenarioContext().getGenerateProxy() );  
  }
  
  public boolean getRunTestClient()
  {
    return  getScenarioContext().isLaunchSampleEnabled();  
  }
  
  public Boolean getMonitorService()
  {
    return new Boolean(getScenarioContext().getMonitorWebService());
  }
  
  public int getServiceGeneration()
  {
	  return serviceGeneration_;
  }
  
  public int getClientGeneration()
  {
	  return getScenarioContext().getGenerateClient();
  }
  
  public boolean getDevelopService() {
	  return developService_;
  }
  
  public boolean getAssembleService() {
	  return assembleService_;
  }
  
  public boolean getDeployService() {
	  return deployService_;
  }
  
}
