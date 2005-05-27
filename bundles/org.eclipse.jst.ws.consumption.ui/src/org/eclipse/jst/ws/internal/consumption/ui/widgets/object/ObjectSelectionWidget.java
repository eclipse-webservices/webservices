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
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceImpl;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;


public class ObjectSelectionWidget extends AbstractObjectSelectionWidget implements IObjectSelectionWidget
{
  private IConfigurationElement[] elements;
  private Composite parent;
  private Listener statusListener;
  private Composite composite;
  private TypeRuntimeServer typeRuntimeServer;
  private IStructuredSelection initialSelection;
  private IProject project;
  private String componentName;
  private IObjectSelectionWidget child;
  
  public WidgetDataEvents addControls(Composite parent, Listener statusListener)
  {
	  System.out.println("ObjectSelectionWidget - - 1");	  
    elements = ObjectSelectionRegistry.getInstance().getConfigurationElements();
    this.parent = parent;
    this.statusListener = statusListener;
    composite = null;
	System.out.println("ObjectSelectionWidget - - 2");		
    return this;
  }
  
  /**
   * @param typeRuntimeServer The typeRuntimeServer to set.
   */
  public void setTypeRuntimeServer(TypeRuntimeServer typeRuntimeServer)
  {
    this.typeRuntimeServer = typeRuntimeServer;
    if (composite != null)
    {
      composite.dispose();
      child = null;
    }
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

        if (objectSelectionWidgetId != null && objectSelectionWidgetId.length() > 0)
        {
          for (int i = 0; i < elements.length; i++)
          {
            if (objectSelectionWidgetId.equals(elements[i].getAttribute("id")))
            {
              try
              {
                Object object = elements[i].createExecutableExtension("class");
                if (object instanceof IObjectSelectionWidget)
                {
                  Control shell = parent.getShell();
                  int x = shell.getSize().x;
                  composite = new Composite(parent, SWT.NONE);
                  GridLayout gl = new GridLayout();
                  gl.marginHeight = 0;
                  gl.marginWidth = 0;
                  GridData gd = new GridData(GridData.FILL_BOTH);
                  composite.setLayout(gl);
                  composite.setLayoutData(gd);
                  child = (IObjectSelectionWidget)object;
                  child.addControls(composite, statusListener);
                  child.setInitialSelection(initialSelection);
                  composite.setSize(x-20, composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
                  parent.setSize(x-10, parent.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
                  shell.setSize(x, shell.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
                  shell.setRedraw(true);
                  shell.redraw();
                  shell.update();
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
  
  public Status getStatus()
  {
    return (child != null) ? child.getStatus() : new SimpleStatus("");
  }

  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    this.initialSelection = initialSelection;
    project = getProjectFromInitialSelection(initialSelection);
    componentName = getComponentNameFromInitialSelection(initialSelection);
    if (child != null)
      child.setInitialSelection(initialSelection);
  }
    
  public IStructuredSelection getObjectSelection()
  {
    return (child != null) ? child.getObjectSelection() : null;
  }
  
  public Status validateSelection(IStructuredSelection objectSelection)
  {
    return (child != null) ? child.validateSelection(objectSelection) : new SimpleStatus("");
  }
  
  public IProject getProject()
  {
    if (child != null)
    {
      IProject p = child.getProject();
      if (p != null)
        return p;
      else
        return project;
    }
    else
    {
      return null;
    }
  }
  
  public String getComponentName()
  {
    if (child != null)
    {
      String cname = child.getComponentName();
      if (cname != null && cname.length()>0)
        return cname;
      else
        return componentName;
    }
    else
    {
      return null;
    }    
	 
  }
  
  private IProject getProjectFromInitialSelection(IStructuredSelection selection)
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
  
  private String getComponentNameFromInitialSelection(IStructuredSelection selection)
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
          IVirtualComponent comp = ResourceUtils.getComponentOf(resource.getFullPath());
          if (comp != null)
          {
            return comp.getName();  
          }          
        } catch(CoreException e)
        {
          return null;
        }        
      }
    }
    return null;    
  }
}