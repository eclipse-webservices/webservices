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
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.IWebServiceType;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.common.StatusException;
import org.eclipse.wst.command.env.core.data.Transformer;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ObjectSelectionOutputCommand extends SimpleCommand
{
  private String                 objectSelectionWidgetId_;
  private IStructuredSelection   objectSelection_;
  private IObjectSelectionWidget objectSelectionWidget_;
  private IProject               project_;
  private WebServicesParser      parser_;

  public Status execute(Environment env)
  {
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
    Status status = (objectSelectionWidget_ != null) ? objectSelectionWidget_.validateSelection(getObjectSelection()) : new SimpleStatus("");
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
	  // rskreg
      //IWebServiceType wst = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceTypeById(typeRuntimeServer.getTypeId());
	  IWebServiceType wst = WebServiceRuntimeExtensionUtils.getWebServiceTypeById(typeRuntimeServer.getTypeId());
	  // rskreg
      if (wst != null)
      {
        objectSelectionWidgetId_ = wst.getObjectSelectionWidget();
        
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
		  System.out.println("getProjectFromObjectSelection - resource = "+resource);
          if (resource==null) 
            return null;
          IProject p = ResourceUtils.getProjectOf(resource.getFullPath());
		  System.out.println("ObjectSelection project = "+p);
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
}