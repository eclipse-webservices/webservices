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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel;

public class Endpoint
{
  private String endpoint;
  private boolean requireHTTPBasicAuth;
  private String httpBasicAuthUsername;
  private String httpBasicAuthPassword;
  
  public Endpoint()
  {
    endpoint = null;
    requireHTTPBasicAuth = false;
    httpBasicAuthUsername = null;
    httpBasicAuthPassword = null;
  }

  public String getEndpoint()
  {
    return endpoint;
  }
  
  public void setEndpoint(String endpoint)
  {
    this.endpoint = endpoint;
  }
  
  public boolean isRequireHTTPBasicAuth()
  {
    return requireHTTPBasicAuth;
  }
  
  public void setRequireHTTPBasicAuth(boolean requireHTTPBasicAuth)
  {
    this.requireHTTPBasicAuth = requireHTTPBasicAuth;
  }
  
  public String getHttpBasicAuthUsername()
  {
    return httpBasicAuthUsername;
  }
  
  public void setHttpBasicAuthUsername(String httpBasicAuthUsername)
  {
    this.httpBasicAuthUsername = httpBasicAuthUsername;
  }
  
  public String getHttpBasicAuthPassword()
  {
    return httpBasicAuthPassword;
  }
  
  public void setHttpBasicAuthPassword(String httpBasicAuthPassword)
  {
    this.httpBasicAuthPassword = httpBasicAuthPassword;
  }
}