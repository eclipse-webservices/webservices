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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport;

import java.util.Hashtable;
import java.util.Map;

public class HTTPResponse
{
  private boolean statusSet;
  private int statusCode;
  private String statusMessage;
  private Hashtable headers;
  private byte[] payload;

  public HTTPResponse()
  {
    reset();
  }

  public void reset()
  {
    statusSet = false;
    statusCode = -1;
    statusMessage = null;
    if (headers != null)
      headers.clear();
    else
      headers = new Hashtable();
    payload = new byte[0];
  }

  public boolean isStatusSet()
  {
    return statusSet;
  }

  public int getStatusCode()
  {
    return statusCode;
  }

  public void setStatusCode(int statusCode)
  {
    statusSet = true;
    this.statusCode = statusCode;
  }

  public String getStatusMessage()
  {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage)
  {
    this.statusMessage = statusMessage;
  }

  public void addHeader(String key, String value)
  {
    headers.put(key, value);
  }

  public String getHeader(String key)
  {
    return (String) headers.get(key);
  }

  public Map getHeaders()
  {
    return headers;
  }

  public byte[] getPayload()
  {
    return payload;
  }

  public void setPayload(byte[] payload)
  {
    this.payload = payload;
  }
}
