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
* Objects of this class represent a Java bean method parameter.
* Nearest moral equivalents: java.beans.ParameterDescriptor.
*/
public interface AttributeElementType 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  public void setSetterMethod(String setterMethod);
  public String getSetterMethod();
  public String getGetterMethod();
  public void setGetterMethod(String getterMethod);
  public BeanElement getOwningBeanElement ();
  public TypeElement getTypeElement ();
  /**
  * Return the signature with this value as the parmeter
  *
  */
  public String getSetterSignature(String attribute);
  
}

