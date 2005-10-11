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

package org.eclipse.wst.ws.internal.explorer.platform.datamodel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ListManager
{
  private int nextViewId_;
  private Vector itemsList_;
  private Hashtable itemsTable_;

  public ListManager()
  {
    nextViewId_ = 0;
    itemsList_ = new Vector();
    itemsTable_ = new Hashtable();
  }

  private final void updateManager(ListElement element)
  {
    itemsTable_.put(String.valueOf(nextViewId_),element);
    element.setViewId(nextViewId_);
    nextViewId_++;
  }

  public final void add(ListElement element)
  {
    itemsList_.addElement(element);
    updateManager(element);
  }

  public final void addWithCurrentViewId(ListElement element)
  {
    itemsList_.addElement(element);
    itemsTable_.put(String.valueOf(element.getViewId()),element);
  }

  public final ListElement elementAt(int index)
  {
    return (ListElement)itemsList_.elementAt(index);
  }

  public final ListElement getElementWithViewId(int viewId)
  {
    return (ListElement)itemsTable_.get(String.valueOf(viewId));
  }

  public final void removeElementWithViewId(int viewId)
  {
    itemsTable_.remove(String.valueOf(viewId));
    for (int i=0;i<itemsList_.size();i++)
    {
      ListElement listElement = elementAt(i);
      if (listElement.getViewId() == viewId)
      {
        itemsList_.removeElementAt(i);
        break;
      }
    }
  }

  public final void removeElementAt(int index)
  {
    ListElement listElement = (ListElement)elementAt(index);
    itemsList_.removeElementAt(index);
    itemsTable_.remove(String.valueOf(listElement.getViewId()));
  }

  public final void insertElementAt(ListElement element,int index)
  {
    itemsList_.insertElementAt(element,index);
    updateManager(element);
  }

  public final Enumeration getListElements()
  {
    return itemsList_.elements();
  }

  public final void clear()
  {
    itemsList_.removeAllElements();
    itemsTable_.clear();
  }

  public final ListManager copy(ListManager newListManager)
  {
    newListManager.clear();
    for (int i=0;i<itemsList_.size();i++)
    {
      ListElement listElement = (ListElement)elementAt(i);
      ListElement newListElement = new ListElement(listElement.getObject());
      newListElement.setViewId(listElement.getViewId());
      newListElement.setTargetViewToolInfo(listElement.getTargetNodeId(),listElement.getTargetToolId(),listElement.getTargetViewId());
      newListManager.addWithCurrentViewId(newListElement);
    }
    newListManager.setNextViewId(nextViewId_);
    return newListManager;
  }

  public final void setNextViewId(int nextViewId)
  {
    nextViewId_ = nextViewId;
  }

  public final int getNextViewId()
  {
    return nextViewId_;
  }
}
