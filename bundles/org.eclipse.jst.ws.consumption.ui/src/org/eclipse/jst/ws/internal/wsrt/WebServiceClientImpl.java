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
package org.eclipse.jst.ws.internal.wsrt;

import org.eclipse.core.runtime.IConfigurationElement;

public class WebServiceClientImpl {

  private IConfigurationElement elem_; 
	private String id;
	private String label;
  
  public WebServiceClientImpl(IConfigurationElement elem_)
  {
    super();
    this.elem_ = elem_;
  }

  public String getId()
  {
    if (id==null)
    {
      id = elem_.getAttribute("id");
    }
    return id;
  }
  

  public String getLabel()
  {
    if (label==null)
    {
      label = elem_.getAttribute("label");
    }
    return label;
  }	
}
