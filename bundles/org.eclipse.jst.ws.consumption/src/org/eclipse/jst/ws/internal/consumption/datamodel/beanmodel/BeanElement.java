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

import org.eclipse.jst.ws.internal.datamodel.BasicModel;
import org.eclipse.jst.ws.internal.datamodel.Model;


/**
* Objects of this class represent a Java bean.
* Nearest moral equivalents: java.lang.Class, java.beans.BeanDescriptor.
*/
public class BeanElement extends TypeElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  public static String STATELESS_BEAN = "10101010statelessbean10101010";
  public static String REL_METHODS = "relmethods";
 
  /**
  * Constructor 
  * @param projectElement The project this Bean belongs to.
  */
  public BeanElement (String name)
  {
    this(name,new BasicModel("Project"),true);
  }

  /**
  * Constructor 
  * @param parameterElement The parameter this Bean belongs to.
  */
  public BeanElement (String name, Model model,boolean isRoot)
  {
    super(name,model,TypeElement.BEAN);
    if(isRoot) model.setRootElement(this);
    fOwnerType = TypeElement.ROOT;
  }

  /**
  * Constructor for the case when this is not the root object
  * here it represents complex types
  * @param ParameterElement The Parameter that owns this bean type.
  * @param String Name of the bean element.
  */
  public BeanElement ( ParameterElement parameterElement, String name)
  {
    super(name,parameterElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.BEAN);
    fOwnerType = TypeElement.PARAMETER_OWNER;
  }

  /**
  * Constructor for the case when this is not the root object
  * here it represents complex types
  * @param ParameterElement The Parameter that owns this bean type.
  * @param String Name of the ParameterElement.
  */
  public BeanElement ( AttributeElement attributeElement, String name)
  {
    super(name,attributeElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.BEAN);
    fOwnerType = TypeElement.ATTRIBUTE_OWNER;
  
  }

   /**
  * Constructor for the case when this is not the root object
  * here it represents complex types
  * @param ParameterElement The Parameter that owns this bean type.
  * @param String Name of the ParameterElement.
  */
  public BeanElement ( FieldElement fieldElement, String name)
  {
    super(name,fieldElement,TypeElement.REL_OWNER,TypeElement.REL_TYPE,TypeElement.BEAN);
    fOwnerType = TypeElement.FIELD_OWNER;
  
  }

  /**
  * Returns an enumeration of all Method objects of this Bean.
  * @return Enumeration All Method objects of this Bean.
  */
  public Enumeration getMethods ()
  {
    return getElements(REL_METHODS);
  }

  /**
  * Returns the number of Method objects of this Bean.
  * @return int The number of Method objects of this Bean.
  */
  public int getNumberOfMethodElements ()
  {
    return getNumberOfElements(REL_METHODS);
  }

  public String getTypeName()
  {
    if(isStateLess()) return STATELESS_BEAN + getName();
    return getName();
  }

  public boolean isStateLess()
  {
    Enumeration ea = getElements(TypeElement.REL_ATTRIBUTES);
    Enumeration ef = getElements(TypeElement.REL_FIELDS);
    if(ea.hasMoreElements() || ef.hasMoreElements()) return false;
    else return true;
  }

  
}

