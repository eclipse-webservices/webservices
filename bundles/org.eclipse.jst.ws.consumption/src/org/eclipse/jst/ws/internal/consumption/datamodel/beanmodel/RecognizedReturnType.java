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
* objects of this class represent a recognized return type
* 
*/
public class RecognizedReturnType implements DataType
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  protected String fType;
  protected String fUniqueName;

  /**
  *Constructor
  *
  */
  public RecognizedReturnType(String type)
  {
    fType = type;
    fUniqueName = "";
  }

  /**
  * not relevant at this point 
  */
  public String inputForm(String name)
  {
     return "";
  }

   /**
  * again not relevant
  */
  public String getRequestCode(String name, String id)
  {
     return "";
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
    return "";
  }

  /**
  * This is basically the function that dictates how we want to display this type 
  * upon receiving it as a return from a proxy method
  * @param String the name of the type
  * @return String The display string
  */
  public String TypeConversion(String name)
  {
    String nonBean =   Generator.DOUBLE_TAB + "if(" + name + "!= null){" + StringUtils.NEWLINE   
    	             + Generator.DOUBLE_TAB + "String temp" + getUniqueName()+" = " + name + ".toString();" + StringUtils.NEWLINE 
                     + Generator.DOUBLE_TAB + "%>" + StringUtils.NEWLINE  
    	             + Generator.DOUBLE_TAB +  "<%=temp" + getUniqueName() +"%>" + StringUtils.NEWLINE
                     + Generator.DOUBLE_TAB +  "<%" + StringUtils.NEWLINE
                     + Generator.DOUBLE_TAB + "}";
    return nonBean;
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

