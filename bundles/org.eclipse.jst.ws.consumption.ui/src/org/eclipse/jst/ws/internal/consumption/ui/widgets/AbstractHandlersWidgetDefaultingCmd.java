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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


/**
 * AbstractHandlersWidgetDefaultingCmd
 *
 * Initialize and load the handlers data
 */
public abstract class AbstractHandlersWidgetDefaultingCmd extends EnvironmentalOperation 
{
  
  private IStructuredSelection initialSelection_; 
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils_ = new MessageUtils( pluginId + ".plugin", this );    
    Status status = new SimpleStatus("");
    
    IStructuredSelection selection = initialSelection_;
    if (selection == null) {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED"), Status.ERROR, null);
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
