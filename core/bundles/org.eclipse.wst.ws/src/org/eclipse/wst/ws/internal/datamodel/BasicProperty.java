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

public class BasicProperty implements Property
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private String fName;
  private Object fValue;

  public BasicProperty ( String name )
  {
    fName = name;
  }

  public BasicProperty ( String name, Object value )
  {
    fName = name;
    fValue = value;
  }

  /**
  * Returns a shallow clone of this <code>Property</code>.
  * Property key and value references are cloned,
  * but the value objects themselves are not cloned.
  * @return Property A new Property.
  */
  public Property shallowClone ()
  {
    return new BasicProperty(fName,fValue);
  }

  public String getName ()
  {
    return fName;
  }

  public void setValue ( Object value )
  {
    fValue = value;
  }

  public Object getValue ()
  {
    return fValue;
  }

  public void setValueAsString ( String value )
  {
    fValue = value;
  }

  public String getValueAsString ()
  {
    return (fValue != null ? fValue.toString() : null);
  }
}

