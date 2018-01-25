/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.datamodel.wsdlmodel;

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.BasicElement;


public class BindingElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  public static String REL_PORT = "port";
  public static String REL_OPERATIONS = "operations";
  public static String REL_PORT_TYPE = "porttype";
  
  public BindingElement (PortElement portElement,String name)
  {
    super(name,portElement,REL_PORT,PortElement.REL_BINDING);
  }

  public Enumeration getPort()
  {
    return getElements(REL_PORT);
  }

  public Enumeration getPortType()
  {
    return getElements(REL_PORT_TYPE);
  }

  public Enumeration getOperations()
  {
    return getElements(REL_OPERATIONS);
  }

  public int getNumberOfOperations()
  {
    return getNumberOfElements(REL_OPERATIONS);
  }
}

