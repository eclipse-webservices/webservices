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

import java.util.Enumeration;
import java.util.Vector;

public class BasicRel implements Rel
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private String fName;
  private Element fSourceElement;
  private Vector fConnections;

  public BasicRel ( String name, Element sourceElement )
  {
    fName = name;
    fSourceElement = sourceElement;
    fConnections = new Vector();
  }

  public String getName ()
  {
    return fName;
  }

  public Element getSourceElement ()
  {
    return fSourceElement;
  }

  public Enumeration getTargetElements ()
  {
    return new ElementEnumeration(fConnections.elements());
  }

  public int getNumberOfTargetElements ()
  {
    return fConnections.size();
  }

  public void addConnection ( Connection connection )
  {
    fConnections.addElement(connection);
  }

  public boolean removeConnection ( Connection connection )
  {
    return fConnections.removeElement(connection);
  }

  public Connection getConnectionTo ( Element targetElement )
  {
    Enumeration e = fConnections.elements();
    while (e.hasMoreElements())
    {
      Connection c = (Connection)e.nextElement();
      if (c.getElement() == targetElement) return c;
    }
    return null;
  }

  public String toString ()
  {
    return getName();
  }

  private class ElementEnumeration implements Enumeration
  {
    private Enumeration fConnectionEnumeration;

    public ElementEnumeration ( Enumeration connectionEnumeration )
    {
      fConnectionEnumeration = connectionEnumeration;
    }

    public boolean hasMoreElements ()
    {
      return fConnectionEnumeration.hasMoreElements();
    }

    public Object nextElement ()
    {
      return ((Connection)fConnectionEnumeration.nextElement()).getElement();
    }
  }
}

