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

package org.eclipse.jst.ws.internal.consumption.sampleapp.common;

import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.Method;


/**
* Objects of this class represent a PropertyDescriptor.
* */
public class SamplePropertyDescriptor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  private Method fReadMethod;
  private Method fWriteMethod;
  private String fPropertyName;
  private JavaHelpers fPropertyType; 
  private boolean fStatic;
  
  /*
  * Constructor
  **/
  public SamplePropertyDescriptor(String name)
  {
    fPropertyName = name; 
  }

  /*
  * set the ReadMethod
  */
  public void setReadMethod(Method readMethod)
  {
    fReadMethod = readMethod;
  }

   /*
  * get the ReadMethod 
  */
  public Method getReadMethod()
  {
    return fReadMethod;
  }

  /*
  * set the writeMethod
  */
  public void setWriteMethod(Method writeMethod)
  {
    fWriteMethod = writeMethod;
  }

   /*
  * get the writeMethod 
  */
  public Method getWriteMethod()
  {
    return fWriteMethod;
  }

  /*
  * set the propertyName
  */
  public void setName(String name)
  {
    fPropertyName= name;
  }

   /*
  * get the propertyName
  */
  public String getName()
  {
    return fPropertyName;
  }

  /*
  * set the PropertyType
  */
  public void setPropertyType(JavaHelpers propertyType)
  {
    fPropertyType = propertyType;
  }

   /*
  * get the PropertyType 
  */
  public JavaHelpers getPropertyType()
  {
    return fPropertyType;
  }
      
  public boolean isfStatic()
  {
  	return fStatic;
  }

  public void setfStatic(boolean fStatic)
  {
    this.fStatic = fStatic;	
  }
}
