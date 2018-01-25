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
* This class is used to simplify the codegen process
* Because codegen may be processed differently depending on the type
* we can handle how each type wants the code to look for a specific task 
* by creating a datatype and calling generic methods that are 
* to be specialized in subclasses
*/
public interface DataType 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  public static String REL_TYPE = "type";

  /**
  * This function hands back the code required to go from the 
  * string the user entered to the actual type of the element
  * @param String typeName this is the actual name of the type ie int,boolean
  * @param String nodeName The nodeName is just the attributename + Temp. 
  * @param String the attributeName 
  * @return String convert the string to type .
  */
  public String stringConversion(String typeName, String nodeName, String attributeName);

  /**
  * Often times in order to convert a type to string, string to type or just plain provide an input form
  * A temporary variable is used. If we run into a sitution where two of the same types are used in the  
  * same method these temporary variables share the same name space. It isnt enough to use a name 
  * because of we may have a nested scenario. Therefore any temporary variable that is used in a dataType
  * must use a unique name apeended to the end. This name must be passed in and it is up to the user of
  * DataType to insure it is unique.
  * @return Unique Name 
  */
  public void setUniqueName(String name);

  /**
  * This is the name each temp variable will use
  * 
  *
  */
  public String getUniqueName();
  
  /**
  * This function dictates how you want to process the 
  * incoming string from the input jsp. You may or may not use the markup 
  * function depending on the type. The simple types use it. DomElement does not
  * @param String the name of the attribute
  * @return String the code to be generated
  */
  public String getRequestCode(String name, String id);


  /**
  * Input form returns the user input html
  * @param String name The name of the input element
  * @return String The form used to collect the data
  */
  public String inputForm(String name);
  
  /**
  * converts the type returned from the proxy 
  * back to a string 
  * @return String convert the type to string.
  */
  public String TypeConversion(String name);

  /**
  * return the type
  * @return String the type(including package).
  */
  public String getType();
}

