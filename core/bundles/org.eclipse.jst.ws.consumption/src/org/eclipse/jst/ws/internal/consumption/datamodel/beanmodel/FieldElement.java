/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.BasicElement;


/**
* Objects of this class represent a Java bean method parameter.
* Nearest moral equivalents: java.beans.ParameterDescriptor.
*/
public class FieldElement extends BasicElement implements AttributeElementType
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private String fSetterMethod;
  private String fGetterMethod;

 
  public static final String REL_TYPE = "type";
  public static final String REL_OWNING_BEAN = "owningbean";
  
  /**
  * Constructor this takes the owning bean.
  * @param name The name of the attribute.
  * @param beanElement the bean that owns this attribute.
  */
  public FieldElement ( BeanElement beanElement, String name)
  {
    super(name,beanElement,REL_OWNING_BEAN,BeanElement.REL_FIELDS);
    fSetterMethod = name;
    fGetterMethod = name;
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
    return fSetterMethod + " = " + attribute + ";";
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

