/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import org.eclipse.core.runtime.IConfigurationElement;

public class RuntimeDescriptor
{

  private IConfigurationElement elem;
  private String id;
  private String label;
  private String serverRequiredString;
  private boolean serverRequired;
  

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
  
  public boolean getServerRequired()
  {
    if (serverRequiredString == null)
    {     
      //Defaults to true if the extension omits this attribute
      serverRequired = true;

      serverRequiredString = elem.getAttribute("serverRequired");
      if (serverRequiredString != null)
      {
        serverRequired = Boolean.valueOf(serverRequiredString).booleanValue();
      }             
      
    }    
    return serverRequired;    
  }
  
}
