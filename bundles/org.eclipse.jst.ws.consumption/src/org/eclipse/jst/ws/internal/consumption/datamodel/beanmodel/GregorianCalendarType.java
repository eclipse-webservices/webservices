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
public class GregorianCalendarType extends SimpleType 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  *Constructor
  *
  */
  public GregorianCalendarType()
  {
    super(TypeFactory.GREGORIAN_CALENDAR_NAME);
  }


  /**
  * inputForm returns the user input html
  * The simple form is just a text input box
  * This has been specialized to show an example
  * @param String name The name of the input element
  * @return String The form used to collect the data
  */
  public String inputForm(String name)
  {
     String inputForm = "<TD ALIGN=\"left\"><INPUT TYPE=\"TEXT\" NAME=\"" 
                         + name + "\" SIZE=20></TD>" + StringUtils.NEWLINE 
                         + "<%" + StringUtils.NEWLINE
                         + "java.text.DateFormat dateFormat" + getUniqueName()+ " = java.text.DateFormat.getDateInstance();" + StringUtils.NEWLINE   
                         + "java.util.GregorianCalendar gcExamp" + getUniqueName()+ "  = new java.util.GregorianCalendar();" + StringUtils.NEWLINE 
                         + "java.util.Date date" + getUniqueName()+ " = gcExamp" + getUniqueName()+ ".getTime();" + StringUtils.NEWLINE                 
                         + "String tempResult" + getUniqueName()+ " = dateFormat" + getUniqueName()+ ".format(date" + getUniqueName()+ ");" + StringUtils.NEWLINE  
                         + "%>" + StringUtils.NEWLINE
          
                          
                         + "<TD ALIGN=\"left\">" + StringUtils.NEWLINE  
                         + "</TR>" + StringUtils.NEWLINE
                         + "<TR>"  + StringUtils.NEWLINE
                         + "<TD> </TD>" + StringUtils.NEWLINE
                         + "<TD ALIGN=\"left\"> eg. <%= tempResult" + getUniqueName()+ " %> </TD>" + StringUtils.NEWLINE
                         + "</TR>" + StringUtils.NEWLINE;
                       
                         

     return inputForm;
  }
 
 
  /*
  * This function needs to be specialized
  * We must use the date function to help
  * us convert back and forth
  */
   public String stringConversion(String typeName, String nodeName, String attributeName)
  {
     String conversion =  Generator.DOUBLE_TAB + "java.text.DateFormat dateFormat" + getUniqueName()+ " = java.text.DateFormat.getDateInstance();"  
                         + StringUtils.NEWLINE + Generator.DOUBLE_TAB +  "java.util.Date dateTemp" + getUniqueName()+ "  = dateFormat" + getUniqueName()+ ".parse(" + attributeName + ");"                         
                         + StringUtils.NEWLINE + Generator.DOUBLE_TAB + Generator.SPACE + nodeName + " = " + "new java.util.GregorianCalendar();" 
                         + StringUtils.NEWLINE + Generator.DOUBLE_TAB + nodeName + ".setTime(dateTemp" + getUniqueName()+ ");"
                         + "" + StringUtils.NEWLINE;
     return conversion;
  }

  /**
  * this is needed for most case 
  * but it is peripheral as the stringconversion needed 
  * to be overridden
  */
  public String StringToType(String name)
  {
    return "";
  }

  /**
  * converts the type returned from the proxy 
  * back to a string 
  * @return String convert the type to string.
  */
  public String TypeConversion(String name)
  {

    String conversion = Generator.DOUBLE_TAB + "java.text.DateFormat dateFormat" + getUniqueName()+ " = java.text.DateFormat.getDateInstance();" + StringUtils.NEWLINE 
    	               + Generator.DOUBLE_TAB + "java.util.Date date" + getUniqueName()+ " = " + name + ".getTime();" + StringUtils.NEWLINE 
    	               + Generator.DOUBLE_TAB + "String tempResult" + getUniqueName()+ " = org.eclipse.jst.ws.util.JspUtils.markup(dateFormat" + getUniqueName()+ ".format(date" + getUniqueName() + "));" + StringUtils.NEWLINE 
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
  public String TypeToString(String name)
  {
    return "";
  }
 
 }

