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
* 1 of three type elements represents primitives 
*/
public class SimpleElement extends TypeElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  * Constructor 
  * @param ParameterElement The Parameter that owns this simple type.
  * @param String Name of the simple element.
  */
  public SimpleElement ( ParameterElement parameterElement, String name, boolean prim)
  {
    super(name,parameterElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.SIMPLE);
    fOwnerType = TypeElement.PARAMETER_OWNER;
    setPrimitive(prim);
  }
  
  /**
  * Constructor 
  * @param ParameterElement The Attribute that owns this simple type.
  * @param String Name of the AttributeElement.
  */
  public SimpleElement ( AttributeElement attributeElement, String name, boolean prim)
  {
    super(name,attributeElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.SIMPLE);
    fOwnerType = TypeElement.ATTRIBUTE_OWNER;
    setPrimitive(prim);
  }

  /**
  * Constructor 
  * @param ParameterElement The Attribute that owns this simple type.
  * @param String Name of the AttributeElement.
  */
  public SimpleElement ( FieldElement fieldElement, String name, boolean prim)
  {
    super(name,fieldElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.SIMPLE);
    fOwnerType = TypeElement.FIELD_OWNER;
    setPrimitive(prim);
  }
  
}

