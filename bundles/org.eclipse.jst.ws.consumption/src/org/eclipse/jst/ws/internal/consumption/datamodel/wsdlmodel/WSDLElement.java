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

import org.eclipse.jst.ws.internal.datamodel.BasicElement;
import org.eclipse.jst.ws.internal.datamodel.BasicModel;
import org.eclipse.jst.ws.internal.datamodel.Model;


public class WSDLElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  public static String REL_DEFINITIONS = "definitions";
  
  /*
  *
  **/
  public WSDLElement (String name)
  {
    this(name,new BasicModel("WSDLModel"));
  }

  public WSDLElement (String name, Model model)
  {
    super(name,model);
    model.setRootElement(this);
  }

  public Enumeration getDefinitions()
  {
    return getElements(REL_DEFINITIONS);
  }

  public int getNumberOfDefinitions()
  {
    return getNumberOfElements(REL_DEFINITIONS);
  }
}

