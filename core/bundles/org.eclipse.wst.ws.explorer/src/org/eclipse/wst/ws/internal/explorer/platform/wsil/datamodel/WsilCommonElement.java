/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import org.eclipse.wst.ws.internal.datamodel.BasicElement;
import org.eclipse.wst.ws.internal.datamodel.Model;

public class WsilCommonElement extends BasicElement
{
  private String baseWsilURL;
  private String relativeURIBase;

  public WsilCommonElement(String name, Model model)
  {
    super(name, model);
  }
  
  public String getBaseWsilURL()
  {
    return baseWsilURL;
  }
  
  public void setBaseWsilURL(String baseWsilURL)
  {
    this.baseWsilURL = baseWsilURL;
  }
  
  private final char FWD_SLASH = '/';
  private final char BWD_SLASH = '\\';
  private final char PROTOCOL_INDICATOR = ':';
  
  public String makeAbsolute(String uri)
  {
    if (baseWsilURL != null && uri != null && uri.indexOf(PROTOCOL_INDICATOR) == -1)
    {
      if (relativeURIBase == null)
      {
        relativeURIBase = baseWsilURL.replace(BWD_SLASH, FWD_SLASH);
        int index = relativeURIBase.lastIndexOf(FWD_SLASH);
        relativeURIBase = baseWsilURL.substring(0, index + 1);
      }
      StringBuffer sb = new StringBuffer(relativeURIBase);
      sb.append(uri);
      return sb.toString();
    }
    return uri;
  }
}
