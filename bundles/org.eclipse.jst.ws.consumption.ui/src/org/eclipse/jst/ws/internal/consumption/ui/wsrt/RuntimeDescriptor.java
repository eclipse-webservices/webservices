/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import org.eclipse.core.runtime.IConfigurationElement;

public class RuntimeDescriptor
{

  private IConfigurationElement elem;
  private String id;
  private String label;
  

  public RuntimeDescriptor(IConfigurationElement elem)
  {
    this.elem = elem;
  }
  
  public String getId()
  {
    if (id == null)
    {
      id = elem.getAttribute("id");
    }
    return id;
  }
  
  public String getLabel()
  {
    if (label == null)
    {
      label = elem.getAttribute("label");
    }
    return label;
  }
  
  public String[] getJ2eeLevels()
  {
    return new String[]{"13", "14"};
  }
  
}
