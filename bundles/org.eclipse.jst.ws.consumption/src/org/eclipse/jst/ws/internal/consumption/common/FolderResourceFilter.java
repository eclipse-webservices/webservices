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

package org.eclipse.jst.ws.internal.consumption.common;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.AnyFilter;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;


/**
* This is a kind of {@link Filter} that accepts only objects
* which indicate a Java resource. In particular, any
* {@link org.eclipse.core.resources.IResource} whose
* pathname, or any object whose string representation as
* returned by <code>toString()</code>, ends in
* "<code>.java</code>" or "<code>.class</code>" will be accepted.
* All other objects will be rejected.
*/
public class FolderResourceFilter extends AnyFilter
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Constructs a new <code>JavaResourceFilter</code> that filters
  * "<code>.java</code>" and "<code>.class</code>" objects.
  * Equivalent to <code>JavaResourceFilter(ALL_FILES)</code>.
  */
  public FolderResourceFilter () { }

 /**
  * Returns the locale-specific name of this filter.
  * @return The locale-specific name of this filter.
  */
  public String getName ()
  {
    return WebServiceConsumptionPlugin.getMessage("%FOLDER_FILTER_NAME");
  }

  /**
  * Returns the locale-specific description of this filter.
  * @return The locale-specific description of this filter.
  */
  public String getDescription ()
  {
    return WebServiceConsumptionPlugin.getMessage("%FOLDER_FILTER_DESC");
  }

 public IStatus statusOf ( Object object )
  {
    if (object == null)
    {
      return new Status(
        IStatus.ERROR,
        WebServiceConsumptionPlugin.ID,
        0,
        WebServiceConsumptionPlugin.getMessage("%FILTER_MSG_ERROR_NULL_OBJECT"),
        null
      );
    }

    String name = null;

    if (object instanceof IResource)
    {
      IResource resource = (IResource)object;
      name = resource.getFullPath().toString();
      if (resource.getType() != IResource.FOLDER)
      {
        return new Status(
          IStatus.ERROR,
          WebServiceConsumptionPlugin.ID,
          0,
          WebServiceConsumptionPlugin.getMessage("%FILTER_MSG_ERROR_NOT_FOLDER",new Object[] {name}),
          null
        );
      }
    }
    return new Status(IStatus.OK,WebServiceConsumptionPlugin.ID,0,"",null);
  }
}
