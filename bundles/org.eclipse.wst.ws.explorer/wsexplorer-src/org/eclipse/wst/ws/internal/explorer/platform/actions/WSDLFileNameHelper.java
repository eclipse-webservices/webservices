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

package org.eclipse.wst.ws.internal.explorer.platform.actions;

public class WSDLFileNameHelper
{
  private static final String default_ = "temp.wsdl";
  
  /**
  * Given a wsdl path (file, URL etc.), guess at the file name by looking for the .wsdl extension at the end of the
  * path and the file that it belongs to. If this is not possible, return the default defined in this class.
  * @param String The path (file, URL etc.) of this WSDL file.
  * @return String The WSDL file name determined by this routine.
  */
  public static final String getWSDLFileName(String wsdlPathname)
  {
    if (wsdlPathname != null)
    {
      if (wsdlPathname.endsWith(".wsdl"))
      {
        int lastSeparatorPos = Math.max(wsdlPathname.lastIndexOf("/"),wsdlPathname.lastIndexOf("\\"));
        if (lastSeparatorPos > 0)
          return wsdlPathname.substring(lastSeparatorPos+1,wsdlPathname.length());
      }
    }
    return default_;
  }
}
