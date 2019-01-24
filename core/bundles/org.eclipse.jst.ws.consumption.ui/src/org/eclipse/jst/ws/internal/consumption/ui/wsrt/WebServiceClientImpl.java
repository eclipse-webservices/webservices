/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

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
