/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060612   142290 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.util;

public class Validator
{
  public static final boolean validateString(String input)
  {
    return ((input != null) && (input.trim().length() > 0));
  }

  public static final boolean validateURL(String input)
  {
	  return (input != null && input.matches("[a-zA-Z\\+\\-\\.]++:.*"));
  }
  
  public static final boolean validateInteger(String input)
  {
    try
    {
      Integer.parseInt(input);
      return true;
    }
    catch (NumberFormatException e)
    {
      return false;
    }
  }
}
