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

import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;


/**
* objects of this class represent a type
* 
*/
public class SOAPElementType implements DataType
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  protected String fType;
  protected String fUniqueName;

  /**
  *Constructor
  *
  */
  public SOAPElementType()
  {
    fType = "javax.xml.soap.SOAPElement";
    fUniqueName = "";
  }

  /**
  * inputForm returns the user input html
  * The DomElement form is a text box
  * @param String name The name of the input element
  * @return String The form used to collect the data
  */
  public String inputForm(String name)
  {
     String inputForm = "<TD ALIGN=\"left\"><TEXTAREA Rows=7 Cols=45 NAME=\"" 
                         + name + "\"></TEXTAREA></TD>" + StringUtils.NEWLINE 
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
  * @param String typeName this is the actual name of the type ie int,boolean
  * @param String nodeName The nodeName is just the attributename + Temp. 
  * @param String the attributeName 
  * @return String convert the string to type .
  */
  public String stringConversion(String typeName, String nodeName, String attributeName)
  {
     String conversion =  Generator.DOUBLE_TAB 
                       + "javax.xml.soap.SOAPElement " + nodeName + "= webserviceutils.org.eclipse.jst.ws.util.SoapElementHelper.createSOAPElementFromXMLString(" + attributeName + ");" 
	                   + StringUtils.NEWLINE;
     return conversion;
  }

  /**
  * This is basically the function that dictates how we want to display this type 
  * upon receiving it as a return from a proxy method
  * @param String the name of the type
  * @return String The display string
  */
  public String TypeConversion(String name)
  {
    String typeToString = Generator.DOUBLE_TAB + "String tempResult" + getUniqueName()+ " = webserviceutils.org.eclipse.jst.ws.util.SoapElementHelper.soapElementWriter(" + name + ", new java.lang.StringBuffer()" + ");" + StringUtils.NEWLINE 
                       + Generator.DOUBLE_TAB + "%>" + StringUtils.NEWLINE
                       + Generator.DOUBLE_TAB + "<%= tempResult" + getUniqueName()+ " %>" + StringUtils.NEWLINE
                       + Generator.DOUBLE_TAB + "<%" + StringUtils.NEWLINE;

    return typeToString;
  }

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

