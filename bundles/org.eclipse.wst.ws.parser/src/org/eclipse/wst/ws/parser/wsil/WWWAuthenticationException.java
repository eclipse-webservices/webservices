/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.wst.ws.parser.wsil;

import java.io.IOException;

public class WWWAuthenticationException extends Exception
{
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
