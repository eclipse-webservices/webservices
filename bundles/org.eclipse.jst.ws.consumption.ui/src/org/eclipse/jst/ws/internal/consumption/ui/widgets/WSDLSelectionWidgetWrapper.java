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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.WSDLSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.WSDLSelectionWrapper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;

public class WSDLSelectionWidgetWrapper extends SimpleWidgetDataContributor
{
  private String pluginId;
  private MessageUtils msgUtils;

  private Composite parent;
  private Listener statusListener;
  
  private WSDLSelectionWidget wsdlSelectionWidget;
  private String webServiceURI;
  private IProject project;
  
  public WSDLSelectionWidgetWrapper()
  {
    pluginId = "org.eclipse.jst.ws.consumption.ui";
    msgUtils = new MessageUtils(pluginId + ".plugin", this);
  }
  
  public WidgetDataEvents addControls(Composite parent, Listener statusListener)
  {
    this.parent = parent;
    this.statusListener = statusListener;
    wsdlSelectionWidget = new WSDLSelectionWidget();
    wsdlSelectionWidget.addControls(parent, statusListener);
    return this;
  }
  
  public Status getStatus()
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
}
