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

import java.util.Enumeration;

import org.eclipse.jst.ws.internal.datamodel.BasicElement;


/**
* Objects of this class represent a Java bean method parameter.
* Nearest moral equivalents: java.beans.ParameterDescriptor.
*/
public class AttributeElement extends BasicElement implements AttributeElementType
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

 
  public static final String REL_TYPE = "type";
  public static final String REL_OWNING_BEAN = "owningbean";

  private String fSetterMethod;
  private String fGetterMethod;

  /**
  * Constructor this takes the owning bean.
  * @param name The name of the attribute.
  * @param beanElement the bean that owns this attribute.
  */
  public AttributeElement ( BeanElement beanElement, String name)
  {
    super(name,beanElement,REL_OWNING_BEAN,BeanElement.REL_ATTRIBUTES);
  }
  
  public void setSetterMethod(String setterMethod)
  {
    fSetterMethod = setterMethod;
  }

  public String getSetterMethod()
  {
    return fSetterMethod;
  }

  public String getGetterMethod()
  {
    return fGetterMethod;
  }
 
  public void setGetterMethod(String getterMethod)
  {
    fGetterMethod = getterMethod;
  }

  public String getSetterSignature(String attribute)
  {
    int bracket = fSetterMethod.indexOf("(");
    bracket++;
    String setter = fSetterMethod.substring(0,bracket);
    String fullSetter = setter + attribute + ");";
   
    return fullSetter;
  }

  /**
  * Returns the Bean that owns this Attribute.
  * @return BeanElement The bean that owns this attribute.
  */
  public BeanElement getOwningBeanElement ()
  {
    Enumeration e = getElements(REL_OWNING_BEAN);
    return e.hasMoreElements() ? (BeanElement)e.nextElement() : null;
  }

  /**
  * Returns the type that is owned by this attributte.
  * @return TypeElement The Type that of this attribute.
  */
  public TypeElement getTypeElement ()
  {
    Enumeration e = getElements(REL_TYPE);
    return e.hasMoreElements() ? (TypeElement)e.nextElement() : null;
  }
}

