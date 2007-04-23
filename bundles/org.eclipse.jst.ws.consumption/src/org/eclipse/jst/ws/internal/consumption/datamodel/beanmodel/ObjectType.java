/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060823	  145643 mahutch@ca.ibm.com - Mark Hutchinson (created class)
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;


/**
* objects of this class represent a type
* 
*/
public class ObjectType extends SimpleType 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  *Constructor
  *
  */
  public ObjectType()
  {
    super(TypeFactory.OBJECT_NAME);
  }
 
  /**
  * StringToType gets the string taken by the 
  * user into proxy form
  * @return String convert the string to type .
  */
  public String StringToType(String name)
  { 
	//We can't deserialize a String to an Object
	//this should never actually be called since
	//methods with Objects as input are omitted.
    return "new java.lang.Object();";
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

