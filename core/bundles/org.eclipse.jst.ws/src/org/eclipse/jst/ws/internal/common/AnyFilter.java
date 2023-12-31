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

package org.eclipse.jst.ws.internal.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.jst.ws.internal.WSPluginMessages;

/**
* This is a kind of {@link Filter} that accepts all objects,
* and is a suitable base class for new <code>Filters</code>.
*/
public class AnyFilter implements Filter
{

  /**
  * Constructs a new <code>AnyFilter</code>.
  */
  public AnyFilter ()
  {
  }

  /**
  * Returns the locale-specific name of this filter.
  * @return The locale-specific name of this filter.
  */
  public String getName ()
  {
    return WSPluginMessages.ANY_FILTER_NAME;
  }

  /**
  * Returns the locale-specific description of this filter.
  * @return The locale-specific description of this filter.
  */
  public String getDescription ()
  {
    return WSPluginMessages.ANY_FILTER_DESC;
  }

  /**
  * Returns an {@link org.eclipse.core.runtime.IStatus}
  * describing the <code>Filter</code>'s assessment of
  * the given <code>object</code>.
  * See {@link Filter#statusOf} for general comments.
  * The default implementation of <code>AnyFilter</code>
  * returns a status with severity <code>IStatus.OK</code>.
  * Subclasses may override.
  * @param object The object to filter.
  * @return An {@link org.eclipse.core.runtime.IStatus}.
  */
  public IStatus statusOf ( Object object )
  {
    return new Status(IStatus.OK,WebServicePlugin.ID,0,"",null);
  }

  /**
  * Returns true if and only if {@link #statusOf} returns
  * an {@link org.eclipse.core.runtime.IStatus} with a
  * severity of less than <code>IStatus.ERROR</code>.
  * Subclasses may override to provide a higher-performance
  * implementation.
  * @param object The object to filter.
  * @return True if and only if {@link #statusOf} returns
  * an {@link org.eclipse.core.runtime.IStatus} with a
  * severity of less than <code>IStatus.ERROR</code>.
  */
  public boolean accepts ( Object object )
  {
    return !statusOf(object).matches(IStatus.ERROR);
  }
}
