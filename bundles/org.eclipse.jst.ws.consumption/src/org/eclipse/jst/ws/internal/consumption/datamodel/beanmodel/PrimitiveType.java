/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071110   209087 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;

import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;

/**
* objects of this class represent a type
* 
*/
public abstract class PrimitiveType extends SimpleType 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  *Constructor
  *
  */
  public PrimitiveType(String type)
  {
    super(type);
  }
 
 /**
  * The stringConversion function nails out specific conversion methods used among simple types 
  * This method is to be implemented by SimpleType subclasses
  * @param String the name of string after the request call
  * @return String the actual conversion string containing the name.
  */
  public String stringConversion(String typeName, String nodeName, String attributeName)
  {
     String conversion = Generator.DOUBLE_TAB + typeName + Generator.SPACE + nodeName 
     	                 + Generator.SPACE + " = " + StringToType(attributeName) 
     	                 + "" + StringUtils.NEWLINE;
     return conversion;
  }
  
  public String TypeToString(String name)
  {
    return "String.valueOf(" + name + ")";
  }
  
  
}

