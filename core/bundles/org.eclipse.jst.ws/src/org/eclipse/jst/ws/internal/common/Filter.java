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

package org.eclipse.jst.ws.internal.common;

import org.eclipse.core.runtime.IStatus;

/**
* This is the interface for a class of objects that filter
* other objects for inclusion or exclusion by some consumer.
*/
public interface Filter
{

  /**
  * Returns the locale-specific name of this filter.
  * @return The locale-specific name of this filter.
  */
  public String getName ();

  /**
  * Returns the locale-specific description of this filter.
  * @return The locale-specific description of this filter.
  */
  public String getDescription ();

  /**
  * Returns an {@link org.eclipse.core.runtime.IStatus}
  * describing the <code>Filter</code>'s assessment of
  * the given <code>object</code>.
  * The severity of the
  * returned <code>IStatus</code> is as follows:
  * <ul>
  * <li>
  * {@link org.eclipse.core.runtime.IStatus#OK}
  * if the <code>Filter</code> accepts the <code>object</code>
  * with no further information for the caller (meaning the
  * {@link org.eclipse.core.runtime.IStatus#getMessage} and
  * {@link org.eclipse.core.runtime.IStatus#getException} methods
  * are ignored).
  * <li>
  * {@link org.eclipse.core.runtime.IStatus#INFO}
  * if the <code>Filter</code> accepts the <code>object</code>
  * Additional information may be available from the
  * {@link org.eclipse.core.runtime.IStatus#getMessage} and
  * {@link org.eclipse.core.runtime.IStatus#getException} methods.
  * <li>
  * {@link org.eclipse.core.runtime.IStatus#WARNING}
  * if the <code>Filter</code> accepts the <code>object</code>.
  * Additional information may be available from the
  * {@link org.eclipse.core.runtime.IStatus#getMessage} and
  * {@link org.eclipse.core.runtime.IStatus#getException} methods.
  * <li>
  * {@link org.eclipse.core.runtime.IStatus#ERROR}
  * if the <code>Filter</code> does not accept the <code>object</code>.
  * Additional information may be available from the
  * {@link org.eclipse.core.runtime.IStatus#getMessage} and
  * {@link org.eclipse.core.runtime.IStatus#getException} methods.
  * </ul>
  * @param object The object to filter.
  * @return An {@link org.eclipse.core.runtime.IStatus}
  * describing the <code>Filter</code>'s assessment of
  * the given <code>object</code>.
  */
  public IStatus statusOf ( Object object );

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
  public boolean accepts ( Object object );
}
