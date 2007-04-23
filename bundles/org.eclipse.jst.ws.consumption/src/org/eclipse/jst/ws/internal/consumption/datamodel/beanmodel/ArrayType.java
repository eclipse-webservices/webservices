/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
public class ArrayType extends RecognizedReturnType
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  *Constructor
  *
  */
  public ArrayType(String typeName)
  {
    this(TypeFactory.ARRAY_NAME,typeName);
  }

  public ArrayType(String name,String typeName)
  {
    super(name);
  } 

 
   /**
  * This is basically the function that dictates how we want to display this type 
  * upon receiving it as a return from a proxy method
  * @param String the name of the type
  * @return String The display string
  */
  public String TypeConversion(String name)
  {
     
    String nonBean = Generator.DOUBLE_TAB + "java.util.List list" + getUniqueName() + "= java.util.Arrays.asList(" + name + ");" +StringUtils.NEWLINE
    	             + Generator.DOUBLE_TAB + "String temp"+ getUniqueName() +" = list" + getUniqueName() + ".toString();" + StringUtils.NEWLINE 
                     + Generator.DOUBLE_TAB + "%>" + StringUtils.NEWLINE  
    	             + Generator.DOUBLE_TAB + "<%=temp"+ getUniqueName() +"%>" + StringUtils.NEWLINE
                     + Generator.DOUBLE_TAB + "<%"+ StringUtils.NEWLINE;

    return nonBean;
  }
}

