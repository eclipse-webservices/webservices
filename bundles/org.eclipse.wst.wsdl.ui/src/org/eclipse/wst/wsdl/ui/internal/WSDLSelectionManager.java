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
package org.eclipse.wst.wsdl.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class WSDLSelectionManager implements ISelectionProvider, ISelectionChangedListener
{       
  protected List listenerList = new ArrayList();
  protected ISelection currentSelection;
  protected boolean enableNotify = true;

  public void setSelection(ISelection selection, ISelectionProvider source)
  {  
    if (enableNotify)
    {
      currentSelection = selection;
      enableNotify = false;
      try
      {
        SelectionChangedEvent event = new SelectionChangedEvent(source, selection);
        List copyOfListenerList = new ArrayList(listenerList);
        for (Iterator i = copyOfListenerList.iterator(); i.hasNext(); )
        {
          ISelectionChangedListener listener = (ISelectionChangedListener)i.next();
          listener.selectionChanged(event);
        }
      }
      finally
      {
        enableNotify = true;
      }
    }
  }      

  //  implements ISelectionProvider
  //
  public void setSelection(ISelection selection)
  {
    setSelection(selection, this);
  }

  public ISelection getSelection() 
  {
    return currentSelection;
  }

  public void removeSelectionChangedListener(ISelectionChangedListener listener) 
  {
    listenerList.remove(listener);
  }
  
  public void addSelectionChangedListener(ISelectionChangedListener listener) 
  {
    listenerList.add(listener);
  } 
  
  // implements ISelectionChangedListener
  //
  public void selectionChanged(SelectionChangedEvent event)  
  {                                                                                         
    if (enableNotify)
    {
      setSelection(event.getSelection(), event.getSelectionProvider());
    }
  }
}                      