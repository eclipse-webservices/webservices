/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsil;

import java.util.Vector;

public class Arguments
{
  private Vector args_;

  public Arguments()
  {
    args_ = new Vector();
  }

  public void add(String key, Object value)
  {
    args_.add(key);
    if (value != null)
      args_.add(value);
  }

  public void remove(String key)
  {
    int index = args_.indexOf(key);
    if (index >= 0)
    {
      args_.remove(index);
      args_.remove(index+1);
    }
  }

  public Object get(String key)
  {
    int index = args_.indexOf(key);
    if (index >= 0)
      return args_.get(index+1);
    else
      return null;
  }

  public void clear()
  {
    args_.clear();
  }

  public Object[] getArguments()
  {
    Object[] args = new Object[args_.size()];
    args_.copyInto(args);
    return args;
  }

  public String[] getStringArguments()
  {
    String[] args = new String[args_.size()];
    for (int i = 0; i < args.length; i++)
      args[i] = args_.get(i).toString();
    return args;
  }

  public boolean isEmpty()
  {
    return args_.isEmpty();
  }
}
