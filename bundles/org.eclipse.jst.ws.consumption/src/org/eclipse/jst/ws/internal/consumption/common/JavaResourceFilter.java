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
public class JavaResourceFilter extends AnyFilter
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * A <code>JavaResourceFilter</code> constructed with this value will
  * accept "<code>.java</code>", but not "<code>.class</code>", objects.
  */
  public static final byte JAVA_FILES = (byte)1;

  /**
  * A <code>JavaResourceFilter</code> constructed with this value will
  * accept "<code>.class</code>", but not "<code>.java</code>", objects.
  */
  public static final byte CLASS_FILES = (byte)2;

  /**
  * A <code>JavaResourceFilter</code> constructed with this value will
  * accept both "<code>.java</code>" and "<code>.class</code>" objects.
  */
  public static final byte ALL_FILES = (byte)255;

  private byte extensions_;

  /**
  * Constructs a new <code>JavaResourceFilter</code> that filters
  * "<code>.java</code>" and "<code>.class</code>" objects.
  * Equivalent to <code>JavaResourceFilter(ALL_FILES)</code>.
  */
  public JavaResourceFilter ()
  {
    this(ALL_FILES);
  }

  /**
  * Constructs a new <code>JavaResourceFilter</code> that filters
  * "<code>.java</code>" objects, "<code>.class</code>" objects,
  * or both if the value of <code>extensions</code> is
  * {@link #JAVA_FILES}, {@link #CLASS_FILES} or {@link #ALL_FILES}
  * respectively.
  * @param extensions One of
  * {@link #JAVA_FILES}, {@link #CLASS_FILES} or {@link #ALL_FILES}.
  */
  public JavaResourceFilter ( byte extensions )
  {
    extensions_ = extensions;
  }

  /**
  * Returns the locale-specific name of this filter.
  * @return The locale-specific name of this filter.
  */
  public String getName ()
  {
    return WebServiceConsumptionPlugin.getMessage("%JAVA_FILTER_NAME");
  }

  /**
  * Returns the locale-specific description of this filter.
  * @return The locale-specific description of this filter.
  */
  public String getDescription ()
  {
    return WebServiceConsumptionPlugin.getMessage("%JAVA_FILTER_DESC");
  }

  /**
  * Returns an {@link org.eclipse.core.runtime.IStatus}
  * describing the <code>Filter</code>'s assessment of
  * the given <code>object</code>.
  * See {@link Filter#statusOf} for general comments.
  * @param object The object to filter.
  * @return An {@link org.eclipse.core.runtime.IStatus}
  * indicating whether or not if the given <code>object</code>
  * represents a Java resource.
  */
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
      if (resource.getType() != IResource.FILE)
      {
        return new Status(
          IStatus.ERROR,
          WebServiceConsumptionPlugin.ID,
          0,
          WebServiceConsumptionPlugin.getMessage("%FILTER_MSG_ERROR_NOT_FILE",new Object[] {name}),
          null
        );
      }
    }

    if (name == null)
    {
      name = object.toString();
    }

    if (!acceptsName(name))
    {
      return new Status(
        IStatus.ERROR,
        WebServiceConsumptionPlugin.ID,
        0,
        WebServiceConsumptionPlugin.getMessage("%JAVA_FILTER_MSG_ERROR_WRONG_EXTENSION",new Object[] {name}),
        null
      );
    }

    return new Status(IStatus.OK,WebServiceConsumptionPlugin.ID,0,"",null);
  }

  //
  // Checks if the given name is acceptable based upon its extension
  // and the value of the extensions_ bitmask.
  //
  private boolean acceptsName ( String name )
  {
    return ( 
      (name.endsWith(".java") && ((extensions_ & JAVA_FILES) != 0)) ||
      (name.endsWith(".class") && ((extensions_ & CLASS_FILES) != 0))
    );
  }
}
