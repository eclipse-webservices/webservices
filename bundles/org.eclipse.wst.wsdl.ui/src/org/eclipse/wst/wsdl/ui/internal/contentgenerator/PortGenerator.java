/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.contentgenerator;

import java.util.Iterator;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class PortGenerator extends AbstractGenerator
{
  protected Service service;
  protected String bindingName;

  public PortGenerator(Service service)
  {
    this.service = service;
  }

  public int getType()
  {
    return PORT_GENERATOR;
  }

  public Definition getDefinition()
  {
    return service.getEnclosingDefinition();
  }

  public Service getService()
  {
    return service;
  }

  public Node getParentNode()
  {
    return WSDLEditorUtil.getInstance().getElementForObject(service);
  }

  protected String getUndoDescription()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_PORT");
  }

  public void setRefName(String refName)
  {
    setBindingName(refName);
  }

  public String getRefName()
  {
    return bindingName;
  }

  public void setBindingName(String bindingName)
  {
    this.bindingName = bindingName;
  }

  public void generateContent()
  {
    Element portElement = null;
    for (Iterator i = service.getEPorts().iterator(); i.hasNext();)
    {
      Port port = (Port) i.next();
      if (port.getName().equals(name))
      {
        portElement = WSDLEditorUtil.getInstance().getElementForObject(port);
      }
    }

    boolean doGenerateContent = false;
    if (portElement == null)
    {
      Element serviceElement = WSDLEditorUtil.getInstance().getElementForObject(service);
      portElement = createWSDLElement(serviceElement, "port");
      portElement.setAttribute("name", name);
      doGenerateContent = true;
    }
    else
    {
      doGenerateContent = overwrite;
    }

    portElement.setAttribute("binding", bindingName != null ? bindingName : "");
    Port port = (Port) WSDLEditorUtil.getInstance().findModelObjectForElement(getDefinition(), portElement);
    newComponent = port;

    //portElement.
    bindingContentGenerator.generatePortContent(portElement, port);
  }
}