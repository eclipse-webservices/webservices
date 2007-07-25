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
 * 20060224   129387 pmoogk@ca.ibm.com - Peter Moogk
 * 20060410   135441 joan@ca.ibm.com - Joan Haggarty
 * 20060825   155114 pmoogk@ca.ibm.com - Peter Moogk
 * 20061220   161232 makandre@ca.ibm.com - Andrew Mak, AbstractObjectSelectionWidget.setInitialSelection(IStructuredSelection initialSelection) called twice each time Browse pressed
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceImpl;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;


public class ObjectSelectionWidget extends AbstractObjectSelectionWidget implements IObjectSelectionWidget
{
  private Composite parent;
  private Listener statusListener;
  private Composite composite;
  private IProject project;
  private String componentName;
  private IObjectSelectionWidget child;
  private Point widgetSize_;
  
  public WidgetDataEvents addControls(Composite parentComposite, Listener statListener)
  {
    this.parent = parentComposite;
    this.statusListener = statListener;
    composite = null;
    return this;
  }
  
  public Control getControl(){
	  return composite;
  }
  
  /**
   * @param typeRuntimeServer The typeRuntimeServer to set.
   */
  public void setTypeRuntimeServer(TypeRuntimeServer typeRuntimeServer)
  {
    if (composite != null)
    {
      composite.dispose();
      child = null;
    }
    if (typeRuntimeServer != null)
    {
      String wst = typeRuntimeServer.getTypeId();
      int scenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(wst);
      String implId = WebServiceRuntimeExtensionUtils2.getWebServiceImplIdFromTypeId(wst);
      WebServiceImpl wsimpl = WebServiceRuntimeExtensionUtils2.getWebServiceImplById(implId);
      
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

        Object object = ObjectSelectionRegistry.getInstance().getSelectionWidget( objectSelectionWidgetId );
        
        if( object instanceof IObjectSelectionWidget )
        {
          child = (IObjectSelectionWidget)object;
        }
        
        if( child != null )
        {
          Control shell = parent.getShell();
          composite = new Composite(parent, SWT.NONE);
          GridLayout gl = new GridLayout();
          gl.marginHeight = 0;
          gl.marginWidth = 0;
          GridData gd = new GridData(GridData.FILL_BOTH);
          composite.setLayout(gl);
          composite.setLayoutData(gd);
          child.addControls(composite, statusListener);

          Point origSize = shell.getSize();
          Point compSize = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
          int   newX     = origSize.x;
          int   newY     = origSize.y;
                  
          // Note: we are trying to determine here if the wizard page should
          //       be resized based on the size of the control that is
          //       given to us by the extension.  The hard coded constants
          //       below represent the vertical and horizontal pixels that need
          //       to go around object selection control.  Hopefully, a more
          //       programatic method of doing this can be found in the future.
          if( compSize.x + 20 > origSize.x )
          {
            newX = compSize.x + 20;
          }
                  
          if( compSize.y + 205 > origSize.y )
          {
            newY = compSize.y + 205;
          }
   
          widgetSize_ = new Point(newX, newY);                  
                  
          return;
        }
      }
    }
  }
  
  public IStatus getStatus()
  {
    return (child != null) ? child.getStatus() : Status.OK_STATUS;
  }

  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    project = getProjectFromInitialSelection(initialSelection);
    componentName = getComponentNameFromInitialSelection(initialSelection);
    if (child != null)
      child.setInitialSelection(initialSelection);
  }
    
  public IStructuredSelection getObjectSelection()
  {
    return (child != null) ? child.getObjectSelection() : null;
  }
  
  public IStatus validateSelection(IStructuredSelection objectSelection)
  {
    return (child != null) ? child.validateSelection(objectSelection) : Status.OK_STATUS;
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
          
          IVirtualComponent comp = ResourceUtils.getComponentOf(resource);
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
  
  public String getObjectSelectionDisplayableString() {
	return child.getObjectSelectionDisplayableString();    
  }
  
  public Point getWidgetSize()
  {
	  Point childWidgetSize = child.getWidgetSize();
	  if (childWidgetSize == null)
		  	  return widgetSize_;
	  return childWidgetSize;
  }
}
