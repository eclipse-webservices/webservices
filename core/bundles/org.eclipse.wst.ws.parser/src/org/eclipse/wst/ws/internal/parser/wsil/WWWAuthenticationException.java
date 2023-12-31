/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.wsil;

import java.io.IOException;

public class WWWAuthenticationException extends Exception
{
  private static final long serialVersionUID = -4918211021620316049L;

  private IOException ioe_;
  private String wwwAuthMsg_;
  private String url_;

  public WWWAuthenticationException(IOException ioe, String wwwAuthMsg, String url)
  {
    super(ioe.getMessage());
    ioe_ = ioe;
    wwwAuthMsg_ = wwwAuthMsg;
    url_ = url;
  }

  public IOException getIOException()
  {
    return ioe_;
  }

  public String getWWWAuthenticationMsg()
  {
    return wwwAuthMsg_;
  }

  public String getURL()
  {
    return url_;
  }
}
