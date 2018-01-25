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
public abstract class SimpleType implements DataType
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  protected String fType;
  protected String fUniqueName;

  /**
  *Constructor
  *
  */
  public SimpleType(String type)
  {
    fType = type;
  }

  /**
  * inputForm returns the user input html
  * The simple form is just a text input box
  * @param String name The name of the input element
  * @return String The form used to collect the data
  */
  public String inputForm(String name)
  {
     String inputForm = "<TD ALIGN=\"left\"><INPUT TYPE=\"TEXT\" NAME=\"" 
                         + name + "\" SIZE=20></TD>" + StringUtils.NEWLINE 
                         + "</TR>" + StringUtils.NEWLINE;
     return inputForm;
  }

   /**
  * This function dictates how you want to process the 
  * incoming string from the input jsp. You may or may not use the markup 
  * function depending on the type. The simple types use it. DomElement does not
  * @param String the name of the attribute
  * @return String the code to be generated
  */
  public String getRequestCode(String name, String id)
  {
     String requestCode = Generator.DOUBLE_TAB + "String" + Generator.SPACE 
     	                   + id + "=  request.getParameter(\""
     	                   + name +"\");" + StringUtils.NEWLINE;
     return requestCode;
   }
 
  /**
  * This function hands back the code required to go from the 
  * string the user entered to the actual type of the element
  * it uses another function that all subclasses of SimpleType are required to implement
  * The StringToType function
  * @param String typeName this is the actual name of the type ie int,boolean
  * @param String nodeName The nodeName is just the attributename + Temp. 
  * @param String the attributeName 
  * @return String convert the string to type .
  */
  public String stringConversion(String typeName, String nodeName, String attributeName)
  {
     String conversion = Generator.DOUBLE_TAB + Generator.SPACE + nodeName 
     	                 + Generator.SPACE + " = " + StringToType(attributeName) 
     	                 + "" + StringUtils.NEWLINE;
     return conversion;
  }

  /**
  * The stringConversion function nails out specific conversion methods used among simple types 
  * This method is to be implemented by SimpleType subclasses
  * @param String the name of string after the request call
  * @return String the actual conversion string containing the name.
  */
  public abstract String StringToType(String name);

  /**
  * converts the type returned from the proxy 
  * back to a string 
  * @return String convert the type to string.
  */
  public String TypeConversion(String name)
  {

    String conversion = Generator.DOUBLE_TAB + "String tempResult" + getUniqueName()+ " = org.eclipse.jst.ws.util.JspUtils.markup(" + TypeToString(name) + ");" + StringUtils.NEWLINE 
                       + Generator.DOUBLE_TAB + "%>" + StringUtils.NEWLINE
                       + Generator.DOUBLE_TAB + "<%= tempResult" + getUniqueName()+ " %>" + StringUtils.NEWLINE
                       + Generator.DOUBLE_TAB + "<%" + StringUtils.NEWLINE;

    return conversion;
  }

   /**
  * The stringConversion function nails out specific conversion methods used among simple types 
  * This method is to be implemented by SimpleType subclasses
  * @param String the name of string after the request call
  * @return String the actual conversion string containing the name.
  */
  public abstract String TypeToString(String name);

  /**
  * return the name of this type
  * @return String the name(including package).
  */
  public String getType()
  {
    return fType;
  }

   public String getUniqueName()
  {
    return fUniqueName;
  }

  public void setUniqueName(String name)
  {
    fUniqueName = name;
  }

}

