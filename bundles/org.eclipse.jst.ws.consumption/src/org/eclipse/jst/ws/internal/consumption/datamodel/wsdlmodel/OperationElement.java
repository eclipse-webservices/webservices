/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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


public class OperationElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  public static String REL_BINDING = "binding";
  public static String REL_MESSAGES = "messages";

  public OperationElement (BindingElement bindingElement,String name)
  {
    super(name,bindingElement,REL_BINDING,BindingElement.REL_OPERATIONS);
  }

  public Enumeration getBinding()
  {
    return getElements(REL_BINDING);
  }

  public Enumeration getMessages()
  {
    return getElements(REL_MESSAGES);
  }

  public int getNumberOfMessages()
  {
    return getNumberOfElements(REL_MESSAGES);
  }
}

