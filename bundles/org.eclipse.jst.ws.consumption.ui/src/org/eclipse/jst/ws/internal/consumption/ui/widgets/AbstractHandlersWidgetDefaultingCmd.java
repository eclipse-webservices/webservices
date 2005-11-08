/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


/**
 * AbstractHandlersWidgetDefaultingCmd
 *
 * Initialize and load the handlers data
 */
public abstract class AbstractHandlersWidgetDefaultingCmd extends AbstractDataModelOperation 
{
  
  private IStructuredSelection initialSelection_; 
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    IStatus status = Status.OK_STATUS;
    
    IStructuredSelection selection = initialSelection_;
    if (selection == null) {
      status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED );
      env.getStatusHandler().reportError(status);
      return status;         
    }
    
    
    return status;
    
  }
  

  public List getClientHandlers(){
    return null;
  }

  public void setInitialSelection(IStructuredSelection initSelection){
    this.initialSelection_ = initSelection;
  }
  
  public IStructuredSelection getInitialSelection(){
    return this.initialSelection_;
  }
  
  protected IResource getResourceFromInitialSelection()
  {
    if (initialSelection_ != null && initialSelection_.size() == 1)
    {
      Object obj = initialSelection_.getFirstElement();
      if (obj != null) 
      {
        try
        { 
          IResource resource = ResourceUtils.getResourceFromSelection(obj);
          return resource;
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;
  }   
 
  public IProject getProject(){
    
    IResource resource = getResourceFromInitialSelection();
	if (resource != null)
	{
	  return ResourceUtils.getProjectOf( resource.getFullPath() );
	}
	 
	return null;
  }
  
  public String getComponentName(){
    IResource resource = getResourceFromInitialSelection();
    if (resource!=null) {
      IPath absolutePath = resource.getFullPath();
      if (absolutePath.isAbsolute()) {
        return absolutePath.segment(1);
      }
    }
    return null;
  }
  
//  public WebServiceEditModel getWebServiceEditModel() {
//    WebServicesManager wsm = new WebServicesManager();
//    IProject project = getProject();
//    return wsm.getWSEditModel(project);    
//  }
  
}
