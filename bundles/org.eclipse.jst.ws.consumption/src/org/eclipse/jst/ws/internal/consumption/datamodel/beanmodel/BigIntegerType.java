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

/**
* objects of this class represent a type
* 
*/
public class BigIntegerType extends SimpleType 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  *Constructor
  *
  */
  public BigIntegerType()
  {
    super(TypeFactory.BIG_INTEGER_NAME);
  }
 
  /**
  * StringToType gets the string taken by the 
  * user into proxy form
  * @return String convert the string to type .
  */
  public String StringToType(String name)
  {
    return "new java.math.BigInteger(" + name + ");";
  }

  /**
  * The stringConversion function nails out specific conversion methods used among simple types 
  * This method is to be implemented by SimpleType subclasses
  * @param String the name of string after the request call
  * @return String the actual conversion string containing the name.
  */
  public String TypeToString(String name)
  {
    return name + ".toString()";
  }
 
 }

