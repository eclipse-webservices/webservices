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

package org.eclipse.jst.ws.internal.datamodel;

/**
* Carries data relevant to when a property is changed in the model.
*/
public class PropertyChangeEvent
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * The model the changed.
  */
  protected BasicElement fElement;

  /**
  * The property that changed.
  */
  protected Property fProperty;

  /**
  * The property's old value.
  */
  protected Property fOldProperty;

   /**
   * Constructor.
   * @param model The model that changed.
   * @param property The property that changed.
   * @param oldValue The property's old value.
   * @param newValue The property's new value.
   */
  public PropertyChangeEvent ( BasicElement element , Property property, Property oldProperty )
  {
    fElement = element;
    fProperty = property;
    fOldProperty = oldProperty;
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
   * Returns the model property that changed.
   * @return TinyModel The property that changed.
   */
  public Property getProperty ()
  {
    return fProperty;
  }

  /**
   * Returns the model property's old value.
   * @return TinyModel The property's old value.
   */
  public Property getOldProperty ()
  {
    return fOldProperty;
  }

  
}

