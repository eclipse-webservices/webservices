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
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.common.StatusException;
import org.eclipse.wst.command.env.core.data.Transformer;


public class ObjectSelectionOutputCommand extends SimpleCommand
{
  private String objectSelectionWidgetId;
  private IStructuredSelection objectSelection;
  private IObjectSelectionWidget objectSelectionWidget;
  private IProject project;

  public Status execute(Environment env)
  {
    // Transformation
    if (objectSelectionWidgetId != null && objectSelectionWidgetId.length() > 0)
    {
      IConfigurationElement[] elements = ObjectSelectionRegistry.getInstance().getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        if (objectSelectionWidgetId.equals(elements[i].getAttribute("id")))
        {
          String transformerId = elements[i].getAttribute("transformer");
          if (transformerId != null && transformerId.length() > 0)
          {
            try
            {
              Object transformer = elements[i].createExecutableExtension("transformer");
              if (transformer instanceof Transformer)
              {
                Object transformedSelection = ((Transformer)transformer).transform(objectSelection);
                if (transformedSelection instanceof IStructuredSelection)
                  objectSelection = (IStructuredSelection)transformedSelection;
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
    Status status = (objectSelectionWidget != null) ? objectSelectionWidget.validateSelection(getObjectSelection()) : new SimpleStatus("");
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
      IWebServiceType wst = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceTypeById(typeRuntimeServer.getTypeId());
      if (wst != null)
      {
        objectSelectionWidgetId = wst.getObjectSelectionWidget();
        if (objectSelectionWidgetId != null && objectSelectionWidgetId.length() > 0)
        {
          IConfigurationElement[] elements = ObjectSelectionRegistry.getInstance().getConfigurationElements();
          for (int i = 0; i < elements.length; i++)
          {
            if (objectSelectionWidgetId.equals(elements[i].getAttribute("id")))
            {
              try
              {
                Object object = elements[i].createExecutableExtension("class");
                if (object instanceof IObjectSelectionWidget)
                {
                  objectSelectionWidget = (IObjectSelectionWidget)object;
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
    return objectSelection;
  }
  /**
   * @param selection The selection to set.
   */
  public void setObjectSelection(IStructuredSelection objectSelection)
  {
    this.objectSelection = objectSelection;
    //Set the project if you can
    if (project==null)
    {
      project = getProjectFromObjectSelection(objectSelection);
    }
  }
  
  public IProject getProject()
  {
    return project;
  }
  
  public void setProject(IProject project)
  {
    this.project = project;
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
}