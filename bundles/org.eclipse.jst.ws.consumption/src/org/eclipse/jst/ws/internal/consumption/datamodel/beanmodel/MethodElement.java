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

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.BasicElement;


/**
* Objects of this class represent a Java bean method.
* Nearest moral equivalents: java.lang.reflect.Method, java.beans.MethodDescriptor.
*/
public class MethodElement extends BasicElement
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  public static final String REL_BEAN = "bean";
  public static final String REL_PARAMETERS = "parameters";
  public static final String REL_RETURN_PARAMETERS = "returnparameters";
  private String fDisplayName;
  private String fName;
  private boolean fMethodOmitted;

  /**
  * Constructor.
  * @param bean The Bean that owns this method.
  * @param name The name of the method.
  * @param type The type of the method.
  */
  public MethodElement ( BeanElement beanElement, String name, String displayName)
  {
    super(name,beanElement,REL_BEAN,BeanElement.REL_METHODS);
    fDisplayName = displayName;
    fName = name;
    fMethodOmitted = false;
    
  }

  /*
  * In the event that there is an unsupported type
  * we will set the ommission boolean variable
  * @return boolean true if this method is to be ommited
  */
  public boolean getMethodOmmission()
  {
    return fMethodOmitted;
  }

  /*
  * In the event that there is an unsupported type
  * we will get the ommission boolean variable
  * @param boolean true if this method is to be ommited
  */
  public void setMethodOmmission(boolean bool)
  {
    fMethodOmitted = bool;
  }


  public String getName()
  {
    return fName;
  }

  public String getDisplayName()
  {
    return fDisplayName;
  }

  /**
  * Returns the Bean that owns this method.
  * @return Bean The Bean that owns this method.
  */
  public BeanElement getBeanElement ()
  {
    Enumeration e = getElements(REL_BEAN);
    return e.hasMoreElements() ? (BeanElement)e.nextElement() : null;
  }

  /**
  * Returns an enumeration of the return parameter of this method.
  * @return Enumeration holds the return parameter of this method.
  */
  public Enumeration getReturnParameterEnum ()
  {
    return getElements(REL_RETURN_PARAMETERS);
  }

  /**
  * Returns an enumeration of the return parameter of this method.
  * @return Enumeration holds the return parameter of this method.
  */
  public ParameterElement getReturnParameterElement ()
  {
    Enumeration e = getReturnParameterEnum();
    return e.hasMoreElements() ? (ParameterElement)e.nextElement() : null;
  }

  /**
  * Returns an enumeration of all Parameter objects of this method.
  * @return Enumeration All Parameter objects of this method.
  */
  public Enumeration getParameterElements ()
  {
    return getElements(REL_PARAMETERS);
  }

  /**
  * Returns the number of Parameter objects of this method.
  * @return int The number of Parameter objects of this method.
  */
  public int getNumberOfParameterElements ()
  {
    return getNumberOfElements(REL_PARAMETERS);
  }
}

