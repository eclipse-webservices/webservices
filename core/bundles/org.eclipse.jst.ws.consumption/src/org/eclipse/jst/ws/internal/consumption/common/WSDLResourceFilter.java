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

package org.eclipse.jst.ws.internal.consumption.common;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.AnyFilter;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.osgi.util.NLS;


/**
* This is a kind of Filter that accepts only objects
* which indicate a WSDL resource. In particular, any
* {@link org.eclipse.core.resources.IResource} whose
* pathname, or any object whose string representation as
* returned by <code>toString()</code>, ends in
* "<code>.wsdl</code>" will be accepted.
* All other objects will be rejected.
*/
public class WSDLResourceFilter extends AnyFilter
{
  /**
  * Constructs a new <code>WSDLResourceFilter</code>.
  */
  public WSDLResourceFilter ()
  {
  }

  /**
  * Returns the locale-specific name of this filter.
  * @return The locale-specific name of this filter.
  */
  public String getName ()
  {
    return ConsumptionMessages.WSDL_FILTER_NAME;
  }

  /**
  * Returns the locale-specific description of this filter.
  * @return The locale-specific description of this filter.
  */
  public String getDescription ()
  {
    return ConsumptionMessages.WSDL_FILTER_DESC;
  }

  /**
  * Returns an {@link org.eclipse.core.runtime.IStatus}
  * describing the <code>Filter</code>'s assessment of
  * the given <code>object</code>.
  * @param object The object to filter.
  * @return An {@link org.eclipse.core.runtime.IStatus}
  * indicating whether or not if the given <code>object</code>
  * represents a WSDL resource.
  */
  public IStatus statusOf ( Object object )
  {
    if (object == null)
    {
      return new Status(
        IStatus.ERROR,
        WebServiceConsumptionPlugin.ID,
        0,
        ConsumptionMessages.FILTER_MSG_ERROR_NULL_OBJECT,
        null
      );
    }

    String name = null;

    if (object instanceof IResource)
    {
      IResource resource = (IResource)object;
      name = resource.getFullPath().toString();
      if (resource.getType() != IResource.FILE)
      {
        return new Status(
          IStatus.ERROR,
          WebServiceConsumptionPlugin.ID,
          0,
          NLS.bind(ConsumptionMessages.FILTER_MSG_ERROR_NOT_FILE,new Object[] {name}),
          null
        );
      }
    }

    if (name == null)
    {
      name = object.toString();
    }

    if (!name.endsWith(".wsdl"))
    {
      return new Status(
        IStatus.ERROR,
        WebServiceConsumptionPlugin.ID,
        0,
        NLS.bind(ConsumptionMessages.WSDL_FILTER_MSG_ERROR_WRONG_EXTENSION,new Object[] {name}),
        null
      );
    }

    return new Status(IStatus.OK,WebServiceConsumptionPlugin.ID,0,"",null);
  }
}
