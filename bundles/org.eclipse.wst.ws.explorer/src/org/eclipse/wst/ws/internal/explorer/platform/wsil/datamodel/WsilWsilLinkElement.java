/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import org.apache.wsil.Link;
import org.eclipse.wst.ws.internal.datamodel.Model;

/**
* The data model element that represents 
* a WSIL link in a WSIL document
*/
public class WsilWsilLinkElement extends WsilLinkElement
{
  public WsilWsilLinkElement(String name, Model model, Link link)
  {
    super(name, model, link);
  }

  public String getWSILLinkLocation()
  {
    return makeAbsolute(link_.getLocation());
  }

  public boolean validateWSILLink()
  {
    return (link_.getLocation() != null);
  }

  public String toString()
  {
    return getWSILLinkLocation();
  }
}
