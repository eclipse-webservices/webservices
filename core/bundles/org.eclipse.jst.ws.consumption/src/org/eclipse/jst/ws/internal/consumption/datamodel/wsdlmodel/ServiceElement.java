/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.datamodel.wsdlmodel;

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.BasicElement;


public class ServiceElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  public static String REL_PORTS = "ports";
  public static String REL_DEFINITION = "definition";
  
  public ServiceElement (DefinitionElement definitionElement,String name)
  {
    super(name,definitionElement,REL_DEFINITION,DefinitionElement.REL_SERVICES);
  }

  public Enumeration getDefinition()
  {
    return getElements(REL_DEFINITION);
  }

  public Enumeration getPorts()
  {
    return getElements(REL_PORTS);
  }

  public int getNumberOfPorts()
  {
    return getNumberOfElements(REL_PORTS);
  }
}

