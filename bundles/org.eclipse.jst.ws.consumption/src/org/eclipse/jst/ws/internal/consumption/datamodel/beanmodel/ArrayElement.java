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

import org.eclipse.jst.ws.internal.datamodel.BasicModel;
import org.eclipse.jst.ws.internal.datamodel.Model;

/**
* Objects of this class represent a Java bean.
* Nearest moral equivalents: java.lang.Class, java.beans.BeanDescriptor.
*/
public class ArrayElement extends TypeElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  public static String ARRAY_NAME = "10101010array10101010";
  public static String REL_METHODS = "relmethods";
 
  /**
  * Constructor 
  * @param projectElement The project this Bean belongs to.
  */
  public ArrayElement (String name)
  {
    this(name,new BasicModel("Project"),true);
  }

  /**
  * Constructor 
  * @param parameterElement The parameter this Bean belongs to.
  */
  public ArrayElement (String name, Model model,boolean isRoot)
  {
    super(name,model,TypeElement.ARRAY);
    if(isRoot) model.setRootElement(this);
    fOwnerType = TypeElement.ROOT;
  }
  
  
  /**
  * Constructor for the case when this is not the root object
  * here it represents complex types
  * @param ParameterElement The Parameter that owns this bean type.
  * @param String Name of the bean element.
  */
  public ArrayElement ( ParameterElement parameterElement, String name)
  {
    super(name,parameterElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.ARRAY);
    fOwnerType = TypeElement.PARAMETER_OWNER;
  }

  /**
  * Constructor for the case when this is not the root object
  * here it represents complex types
  * @param ParameterElement The Parameter that owns this bean type.
  * @param String Name of the ParameterElement.
  */
  public ArrayElement ( AttributeElement attributeElement, String name)
  {
    super(name,attributeElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.ARRAY);
    fOwnerType = TypeElement.ATTRIBUTE_OWNER;
  }

  /**
  * Constructor for the case when this is not the root object
  * here it represents complex types
  * @param FieldElement The Parameter that owns this bean type.
  * @param String Name of the ParameterElement.
  */
  public ArrayElement ( FieldElement fieldElement, String name)
  {
    super(name,fieldElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.ARRAY);
    fOwnerType = TypeElement.FIELD_OWNER;
  }


  /*
  * determine wether this is an object array or
  * primitive array
  */
  public String getTypeName()
  {
    return ARRAY_NAME + getName();
  }

 
  
}

