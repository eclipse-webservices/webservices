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

package org.eclipse.wst.ws.internal.parser.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import sun.misc.BASE64Encoder;

public final class NetUtils
{

  /**
   * Get the java.net.URLConnection given a string representing the URL. This class ensures
   * that proxy settings in WSAD are respected.
   * @param urlString String representing the URL.
   * @return java.net.URLCDonnection URLConnection to the URL.
   */
  public static final URLConnection getURLConnection(String urlString)
  {
    try
    {
      URL url = createURL(urlString);
      URLConnection uc = url.openConnection();
      String proxyUserName = System.getProperty("http.proxyUserName");
      String proxyPassword = System.getProperty("http.proxyPassword");
      if (proxyUserName != null && proxyPassword != null)
      {
        StringBuffer userNamePassword = new StringBuffer(proxyUserName);
        userNamePassword.append(':').append(proxyPassword);
        BASE64Encoder encoder = new BASE64Encoder();
        String encoding = encoder.encode(userNamePassword.toString().getBytes());
        userNamePassword.setLength(0);
        userNamePassword.append("Basic ").append(encoding);
        uc.setRequestProperty("Proxy-authorization",userNamePassword.toString());
      }
      return uc;
    }
    catch (MalformedURLException e)
    {
    }
    catch (IOException e)
    {
    }
    return null;
  }
  
  /**
   * Get the java.io.InputStream for a URL given a string representing the URL. This class
   * ensures that proxy settings in WSAD are respected.
   * @param urlString String representing the URL.
   * @return java.io.InputStream InputStream for reading the URL stream.
   */
  public static final InputStream getURLInputStream(String urlString)
  {
    try
    {
      URLConnection uc = getURLConnection(urlString);
      if (uc != null)
      {
        InputStream is = uc.getInputStream();
        return is;
      }
    }
    catch (IOException e)
    {
    }
    return null;
  }

  /**
   * Create a URL from a string.
   * @param urlString String representing the URL.
   * @return URL java.lang.URL representation of the URL.
   * @throws MalformedURLException
   */
  public static final URL createURL(String urlString) throws MalformedURLException
  {
  	return new URL(urlString);
  }
}