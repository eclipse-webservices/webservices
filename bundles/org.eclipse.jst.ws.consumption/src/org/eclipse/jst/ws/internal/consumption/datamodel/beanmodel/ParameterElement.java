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

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;

import java.util.Enumeration;

import org.eclipse.wst.ws.internal.datamodel.BasicElement;


/**
* Objects of this class represent a Java bean method parameter.
* Nearest moral equivalents: java.beans.ParameterDescriptor.
*/
public class ParameterElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

 
  public static final String REL_METHOD = "method";
  public static final String REL_TYPE = "type";

  public boolean fReturnParameter=false;
  
  /**
  * Constructor.
  * @param method The Method that owns this parameter.
  * @param name The name of the parameter.
  * @param type The type of the parameter.
  */
  public ParameterElement ( MethodElement methodElement, String name, String relType,boolean returnParameter )
  {
    this(name,methodElement,REL_METHOD,relType);
    fReturnParameter = returnParameter;
  }
  
  /**
  * Constructor.
  * @param method The Method that owns this parameter.
  * @param name The name of the parameter.
  * @param type The type of the parameter.
  */
  public ParameterElement (String name, MethodElement methodElement, String outRel,String inRel)
  {
    super(name,methodElement,outRel,inRel);
  }
   
  /**
  * Returns the Method that owns this parameter.
  * @return Method The Method that owns this parameter.
  */
  public MethodElement getMethodElement ()
  {
    Enumeration e = getElements(REL_METHOD);
    return e.hasMoreElements() ? (MethodElement)e.nextElement() : null;
  }

  /**
  * Returns the Method that owns this parameter.
  * @return Method The Method that owns this parameter.
  */
  public TypeElement getTypeElement ()
  {
    Enumeration e = getElements(REL_TYPE);
    return e.hasMoreElements() ? (TypeElement)e.nextElement() : null;
  }

  public boolean isReturn()
  {
    return fReturnParameter;
  }

}

