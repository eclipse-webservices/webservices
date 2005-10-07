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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceImpl;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;


public class ObjectSelectionOutputCommand extends AbstractDataModelOperation
{
  private String                 objectSelectionWidgetId_;
  private IStructuredSelection   objectSelection_;
  private IObjectSelectionWidget objectSelectionWidget_;
  private IProject               project_;
  private String                 componentName_;
  private WebServicesParser      parser_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    
    // Transformation
    if (objectSelectionWidgetId_ != null && objectSelectionWidgetId_.length() > 0)
    {
      IConfigurationElement[] elements = ObjectSelectionRegistry.getInstance().getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        if (objectSelectionWidgetId_.equals(elements[i].getAttribute("id")))
        {
          String transformerId = elements[i].getAttribute("transformer");
          if (transformerId != null && transformerId.length() > 0)
          {
            try
            {
              Object transformer = elements[i].createExecutableExtension("transformer");
              if (transformer instanceof Transformer)
              {
                Object transformedSelection = ((Transformer)transformer).transform(objectSelection_);
                if (transformedSelection instanceof IStructuredSelection)
                  objectSelection_ = (IStructuredSelection)transformedSelection;
              }
            }
            catch (CoreException ce)
            {
            }
          }
        }
      }
    }
	
    // Validation
    IStatus status = (objectSelectionWidget_ != null) ? objectSelectionWidget_.validateSelection(getObjectSelection()) : Status.OK_STATUS;
    if (status.getSeverity() != Status.OK)
    {
      try
      {
        env.getStatusHandler().report(status);
      }
      catch (StatusException se)
      {
      }
    }
	
    return status;
  }

  /**
   * @param typeRuntimeServer The typeRuntimeServer to set.
   */
  public void setTypeRuntimeServer(TypeRuntimeServer typeRuntimeServer)
  {
    if (typeRuntimeServer != null)
    {
      String wst = typeRuntimeServer.getTypeId();

      int scenario = WebServiceRuntimeExtensionUtils.getScenarioFromTypeId(wst);
      String implId = WebServiceRuntimeExtensionUtils.getImplIdFromTypeId(wst);

      WebServiceImpl wsimpl = WebServiceRuntimeExtensionUtils.getWebServiceImplById(implId);
	    //IWebServiceType wst = WebServiceRuntimeExtensionUtils.getWebServiceTypeById(typeRuntimeServer.getTypeId());

      if (wsimpl != null)
      {
        String objectSelectionWidgetId = null;
        if (scenario == WebServiceScenario.TOPDOWN)
        {
          objectSelectionWidgetId = "org.eclipse.jst.ws.internal.consumption.ui.widgets.object.WSDLSelectionWidget";
        }
        else
        {
          objectSelectionWidgetId = wsimpl.getObjectSelectionWidget();
        }        
        
		objectSelectionWidgetId_ = objectSelectionWidgetId;
		
        if (objectSelectionWidgetId_ != null && objectSelectionWidgetId_.length() > 0)
        {
          IConfigurationElement[] elements = ObjectSelectionRegistry.getInstance().getConfigurationElements();
          for (int i = 0; i < elements.length; i++)
          {
            if (objectSelectionWidgetId_.equals(elements[i].getAttribute("id")))
            {
              try
              {
                Object object = elements[i].createExecutableExtension("class");
                if (object instanceof IObjectSelectionWidget)
                {
                  objectSelectionWidget_ = (IObjectSelectionWidget)object;
                  return;
                }
              }
              catch (CoreException ce)
              {
              }
            }
          }
        }
      }
    }
  }

  /**
   * @return Returns the selection.
   */
  public IStructuredSelection getObjectSelection()
  {
    return objectSelection_;
  }
  /**
   * @param selection The selection to set.
   */
  public void setObjectSelection(IStructuredSelection objectSelection)
  {
    objectSelection_ = objectSelection;
    
    //Set the project if you can
    if (project_==null)
    {
      project_ = getProjectFromObjectSelection(objectSelection);
    }
    
    //Set the componentName if you can
    if (componentName_ == null)
    {
      componentName_ = getComponentNameFromObjectSelection(objectSelection);
    }
    
    // Check if this is a WSDL selection object.  If it is we need
    // to unwrap it.
    if( objectSelection != null && !objectSelection.isEmpty() )
    {
      Object object = objectSelection.getFirstElement();
      
      if( object instanceof WSDLSelectionWrapper )
      {
        WSDLSelectionWrapper wsdlWrapper = (WSDLSelectionWrapper)object;
        
        objectSelection_ = wsdlWrapper.wsdlSelection;
        parser_          = wsdlWrapper.parser;
      }
    }
  }
  
  public IProject getProject()
  {
    return project_;
  }
  
  public void setProject(IProject project)
  {
    this.project_ = project;
  }  
  
  public String getComponentName()
  {
    return componentName_;
  }

  public void setComponentName(String componentName)
  {
    this.componentName_ = componentName;
  }

  /**
   * @return Returns the parser_.
   */
  public WebServicesParser getWebServicesParser()
  {
    return parser_;
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
			e.printStackTrace();
          return null;
        }        
      }
    }
    return null;
  }
  
  private String getComponentNameFromObjectSelection(IStructuredSelection selection)
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
     
          IVirtualComponent comp = ResourceUtils.getComponentOf(resource);
     
          if (comp!=null)
          {
            return comp.getName();
          }
        } catch(CoreException e)
        {
      e.printStackTrace();
          return null;
        }        
      }
    }
    return null;
  }  
}