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

package org.eclipse.wst.ws.internal.datamodel;

/**
* Carries data relevant to when a property is added to the model.
*/
public class PropertyAddEvent
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * The model the changed.
  */
  protected BasicElement fElement;

  /**
  * The property that was added.
  */
  protected Property fProperty;

 
  /**
   * Constructor.
   * @param model The model that changed.
   * @param property The property that was added.
   * @param value The property's value.
   */
  public PropertyAddEvent ( BasicElement element, Property property)
  {
    fElement = element;
    fProperty = property;
  }

  /**
   * Returns the model that changed (that produced this event).
   * @return TinyModel The model that changed.
   */
  public BasicElement getElement ()
  {
    return fElement;
  }

  /**
   * Returns the model property that was added.
   * @return TinyModel The property that was added.
   */
  public Property getProperty ()
  {
    return fProperty;
  }

  
}

