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

package org.eclipse.wst.ws.internal.datamodel;

/**
* Carries data relevant to when a property is removed from the model.
*/
public class PropertyRemoveEvent
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * The model the changed.
  */
  protected BasicElement fElement;

  /**
  * The property that was removed.
  */
  protected Property fProperty;

  /**
   * Constructor.
   * @param model The model that changed.
   * @param property The property that was removed.
   * @param value The property's value.
   */
  public PropertyRemoveEvent ( BasicElement element, Property property)
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
   * Returns the model property that was removed.
   * @return TinyModel The property that was removed.
   */
  public Property getProperty ()
  {
    return fProperty;
  }

}

