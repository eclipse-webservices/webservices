/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport;

import java.util.Map;

public class HTTPException extends RuntimeException
{
  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256438105900134961L;
private int statusCode;
  private String statusMessage;
  private Map headers;
  
  public HTTPException(int statusCode, String statusMessage, Map headers)
  {
    super(statusMessage);
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
    this.headers = headers;
  }
  
  public int getStatusCode()
  {
    return statusCode;
  }
  
  public String getStatusMessage()
  {
    return statusMessage;
  }
  
  public Map getHeaders()
  {
    return headers;
  }
  
  public String getHeader(String key)
  {
    Object value = headers.get(key);
    if (value != null)
      return value.toString();
    else
      return null;
  }
}