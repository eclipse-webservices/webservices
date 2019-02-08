/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.WSDLSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.WSDLSelectionWrapper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

public class WSDLSelectionWidgetWrapper extends SimpleWidgetDataContributor
{
  private WSDLSelectionWidget wsdlSelectionWidget;
  private IProject project;
  private String componentName;
  
  public WSDLSelectionWidgetWrapper()
  {
	  wsdlSelectionWidget = new WSDLSelectionWidget();
  }
  
  public WidgetDataEvents addControls(Composite parent, Listener statusListener)
  {    
    wsdlSelectionWidget.addControls(parent, statusListener);
    return this;
  }
  
  public IStatus getStatus()
  {
    return wsdlSelectionWidget.getStatus();
  }
  
  public void setWebServiceURI(String wsUri)
  {
    IStructuredSelection sel;
    if (wsUri != null)
      sel = new StructuredSelection(wsUri);
    else
      sel = new StructuredSelection();
    wsdlSelectionWidget.setInitialSelection(sel);
  }
  
  public String getWebServiceURI()
  {
    IStructuredSelection sel    = wsdlSelectionWidget.getObjectSelection();
    Object               object = sel.getFirstElement();
    String               result = null;
    
    if (object != null )
    {
      if( object instanceof WSDLSelectionWrapper )
      {    
        // Get at the inner structured selection object.
        WSDLSelectionWrapper wrapper        = (WSDLSelectionWrapper)object;
        IStructuredSelection innerSelection = wrapper.wsdlSelection;
        Object               innerObject    = innerSelection.getFirstElement();
        
        result = innerObject == null ? null : innerObject.toString();
      }
      else
      {
        result = object.toString();
      }
    }
    
    return result;
  }
  
  public String getWsdlURI()
  {
  	return getWebServiceURI();
  }
  
  public WebServicesParser getWebServicesParser()
  {
    return wsdlSelectionWidget.getWebServicesParser();
  }
  
  public void setProject(IProject project)
  {
  	this.project = project;
  }
  
  public IProject getProject()
  {
  	IProject p = wsdlSelectionWidget.getProject();
  	if (p==null)
  	{
  	  return project;
  	}
  	else
  	  return p;
  }

  public String getComponentName()
  {
    String cname = wsdlSelectionWidget.getComponentName();
    if (cname==null)
    {
      return componentName;
    }
    else
      return cname;
  }

  public void setComponentName(String componentName)
  {
    this.componentName = componentName;
  }
  
  public String getObjectSelectionDisplayableString() {
	  if (wsdlSelectionWidget != null)
	  {
		  return wsdlSelectionWidget.getObjectSelectionDisplayableString();  
	  }
	  else return ""; 
  }
  
  public IStructuredSelection getObjectSelection() {
	  return wsdlSelectionWidget.getObjectSelection();
  }
  
  public boolean validate(String s)
  {
	  return wsdlSelectionWidget.validate(s);
  }
}
