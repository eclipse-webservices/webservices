/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.common;

/**
* This is the interface for a class of objects that filter
* other objects for inclusion or exclusion by some consumer.
*/
public interface IFilter
{
  /**
  * Returns the locale-specific name of this filter.
  * @return The locale-specific name of this filter.
  */
  public String getName();

  /**
  * Returns the locale-specific description of this filter.
  * @return The locale-specific description of this filter.
  */
  public String getDescription();

  /**
  * Returns true if and only if this <code>Filter</code>
  * accepts the given <code>object</code>. This method
  * must return true if and only if {@link #statusOf}
  * returns an <code>IStatus</code> with a severity of
  * less than <code>IStatus.ERROR</code>.
  * @param object The object to filter.
  * @return True if and only if this <code>Filter</code>
  * accepts the given <code>object</code>.
  */
  public boolean accepts(Object object);
}
