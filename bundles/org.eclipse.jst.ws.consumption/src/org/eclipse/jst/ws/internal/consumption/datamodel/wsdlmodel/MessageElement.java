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


public class MessageElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  public static String REL_OPERATION = "operation";
  public static String REL_PARTS = "parts";
  
  public MessageElement (OperationElement operationElement,String name)
  {
    super(name,operationElement,REL_OPERATION,OperationElement.REL_MESSAGES);
  }

  public Enumeration getOperation()
  {
    return getElements(REL_OPERATION);
  }

  public Enumeration getParts()
  {
    return getElements(REL_PARTS);
  }

  public int getNumberOfParts()
  {
    return getNumberOfElements(REL_PARTS);
  }
}

