/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.*;

// A class representing a Vector of breadcrumbs. A position index is used
// to navigate through the data structure.
public class History
{
  private Vector items_;
  private int position_;

  public History()
  {
    items_ = new Vector();
    position_ = -1;
  }

  // Add a new breadcrumb to the history list. When adding a new breadcrumb
  // within the current list, all breadcrumbs after the newly added one are
  // removed.
  public boolean addBreadCrumb(BreadCrumb b)
  {
    int numberOfItems = items_.size();
    if (numberOfItems == 0)
    {
      position_++;
      items_.addElement(b);
      return true;
    }
    else
    {
      BreadCrumb currentBreadCrumb = (BreadCrumb)items_.elementAt(position_);
      if (!currentBreadCrumb.equals(b))
      {
        position_++;
        items_.insertElementAt(b,position_);
        for (int i=items_.size()-1;i>position_;i--)
          items_.removeElementAt(i);
        return true;
      }
    }
    return false;
  }

  // Move forward within the history list and obtain the breadcrumb.
  public BreadCrumb forward()
  {
    if (position_ < items_.size()-1)
    {
      position_++;
      BreadCrumb b = (BreadCrumb)items_.elementAt(position_);
      return b;
    }
    return null;
  }

  // Move back within the history list and obtain the breadcrumb.
  public BreadCrumb back()
  {
    if (position_ > 0)
    {
      position_--;
      BreadCrumb b = (BreadCrumb)items_.elementAt(position_);
      return b;
    }
    return null;
  }

  public void removeCurrentBreadCrumb()
  {
    items_.removeElementAt(position_);
    if (position_ > items_.size()-1)
      position_--;
  }
  
  public void dump()
  {
    for (int i=0;i<items_.size();i++)
    {
      BreadCrumb b = (BreadCrumb)items_.elementAt(i);
    }
  }
}
