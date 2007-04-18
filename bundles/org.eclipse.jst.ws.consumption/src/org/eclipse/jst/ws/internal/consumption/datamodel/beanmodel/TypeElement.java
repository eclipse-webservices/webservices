/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070410   180952 makandre@ca.ibm.com - Andrew Mak, Sample JSP generator chokes on interfaces and abstract classes
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.BasicElement;
import org.eclipse.wst.ws.internal.datamodel.Element;
import org.eclipse.wst.ws.internal.datamodel.Model;

/**
* TypeElement is the base class for Java bean features that have types.
*/
public abstract class TypeElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  public static final String NON_INSTANTIABLE = "nonInstantiable";

  public static int BEAN = 0;
  public static int SIMPLE = 1;
  public static int ARRAY = 2;

  private int fType;
  protected int fOwnerType;
  private boolean fPrimitive = false;

  /**
  * Constructor. Automatically adds this object to the given model.
  * @param name The name of the element
  * @param model The model to own this object.
  * @param type The type represented by this object, one of
  * <code>BEAN</code>, <code>SIMPLE</code> or <code>ARRAY</code>.
  */
  protected TypeElement ( String name, Model model, int type )
  {
    super(name,model);
    fType = type;
  }

  /**
  */
  protected TypeElement ( String name, Element element, String outboundRelName, String inboundRelName, int type )
  {
    super(name,element,outboundRelName,inboundRelName);
    fType = type;
  }

  /*
  *This will tell you wether the type is a bean
  *@param Boolean returns true if this is bean
  **/
  public boolean isBean()
  {
    if (fType == BEAN) return true;
    return false;
  }

  /*
  *This will tell you wether the type is a bean
  *@param Boolean returns true if this is bean
  **/
  public boolean isPrimitive()
  {
    return fPrimitive;
  }

  /*
  *This will tell you wether the type is a bean
  *@param Boolean returns true if this is bean
  **/
  protected void setPrimitive(boolean prim)
  {
    fPrimitive = prim; 
  }


  /*
  * This is mainly needed for arrays as with all types the
  * names come out as java.util.type, but with an array it 
  * isnt explicit in telling us its an array
  */
  public String getTypeName()
  {
    return getName();
  }
  
  /*
  *This will tell you wether the type is a simple
  *@param Boolean returns true if this is simple
  **/
  public boolean isSimple()
  {
    if (fType == SIMPLE) return true;
    return false;
  }

  /*
  *This will tell you wether the type is a array
  *@param Boolean returns true if this is array
  **/
  public boolean isArray()
  {
    if (fType == ARRAY) return true;
    return false;
  }

  public static String REL_ATTRIBUTES = "attributes";
  public static String REL_FIELDS = "fields";
  public static final String REL_TYPE = "type";
  public static final String REL_OWNER = "owner";

  public static int PARAMETER_OWNER = 0;
  public static int ATTRIBUTE_OWNER = 1;
  public static int FIELD_OWNER = 2;
  public static int ROOT = 3;

  /*
  * This could be The root test bean
  * @return boolean true if this is root bean
  */
  public boolean isRoot()
  {
    if(fOwnerType == ROOT) return true;
    return false;
  }

  /*
  * This Type could be owned by a Parameter or an attribute
  * @return boolean true if this is owned by a parameter
  */
  public boolean isOwnerParameter()
  {
    if(fOwnerType == PARAMETER_OWNER) return true;
    return false;
  }

  /*
  * This Type could be owned by a Parameter or an attribute
  * @return boolean true if this is owned by an attribute 
  */
  public boolean isOwnerAttribute()
  {
    if(fOwnerType == ATTRIBUTE_OWNER) return true;
    return false;
  }

  /*
  * This Type could be owned by a Parameter or an attribute
  * @return boolean true if this is owned by an attribute 
  */
  public boolean isOwnerField()
  {
    if(fOwnerType == FIELD_OWNER) return true;
    return false;
  }

  /**
  * This Type may be owned by a parameter or an attribute 
  * Use this method in conjunction with isOwnerParamter, isOwner
  * @return BasicElement the element that owns this attribute.
  */
  public BasicElement getOwningElement ()
  {
    if (isOwnerParameter()){
      Enumeration e = getElements(REL_OWNER);
      return e.hasMoreElements() ? (BasicElement)e.nextElement() : null;
    }
    else if(isOwnerAttribute()){
      Enumeration e = getElements(REL_OWNER);
      return e.hasMoreElements() ? (BasicElement)e.nextElement() : null;
    }
    else if(isOwnerField()){
      Enumeration e = getElements(REL_OWNER);
      return e.hasMoreElements() ? (BasicElement)e.nextElement() : null;
    }

    return null;
  }

  
}

