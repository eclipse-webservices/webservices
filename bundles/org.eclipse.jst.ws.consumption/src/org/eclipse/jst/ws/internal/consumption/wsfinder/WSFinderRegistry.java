/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.wsfinder;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class WSFinderRegistry
{
  private static WSFinderRegistry instance_;
  private Vector wsfinders_;

  private WSFinderRegistry()
  {
    wsfinders_ = new Vector();
  }
  
  /**
  * Returns a singleton instance of this class.
  * @return A singleton WSFinderRegistry object.
  */
  public static WSFinderRegistry getInstance()
  {
    if (instance_ == null)
    {
      instance_ = new WSFinderRegistry();
      instance_.init();
    }
    return instance_;
  }
  
  private void init()
  {
    IExtensionRegistry pluginReg = Platform.getExtensionRegistry();
    IConfigurationElement[] configElements = pluginReg.getConfigurationElementsFor("org.eclipse.jst.ws.consumption", "wsfinder");
    for (int i = 0; i < configElements.length; i++)
    {
      try
      {
        Object object = configElements[i].createExecutableExtension("class");
        if (object instanceof IWSFinder)
        {
          IWSFinder wsfinder = (IWSFinder)object;
          wsfinder.setID(configElements[i].getAttribute("id"));
          wsfinder.setName(configElements[i].getAttribute("name"));
          wsfinder.setDescription(configElements[i].getAttribute("description"));
          wsfinders_.add(wsfinder);
        }
      }
      catch (CoreException ce)
      {
      }
    }
  }

  public List getWSFinders()
  {
    return wsfinders_;
  }
  
  public List getWebServices()
  {
    List ws = new Vector();
    List wsFinders = getWSFinders();
    for (Iterator it = wsFinders.iterator(); it.hasNext();)
    {
      IWSFinder wsFinder = (IWSFinder)it.next();
      ws.addAll(wsFinder.find());
    }
    return ws;
  }
}
