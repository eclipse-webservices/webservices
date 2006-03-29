/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;

public final class AddPortCommand extends WSDLElementCommand
{
  private Service service;
  private String name;
  private Port port;
  
  public AddPortCommand
		(Service service,
		 String name)
	{
	  this.service = service;
	  this.name = name;
	}
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public WSDLElement getWSDLElement()
  {
    return port;
  }

  public void run()
  {
    port = WSDLFactory.eINSTANCE.createPort();
    port.setName(name);
    port.setEnclosingDefinition(service.getEnclosingDefinition());
    service.addPort(port);
  }
}
