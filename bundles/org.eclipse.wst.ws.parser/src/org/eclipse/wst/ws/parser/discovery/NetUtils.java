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

package org.eclipse.wst.ws.parser.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Provider;
import java.security.Security;

import org.apache.soap.transport.http.SOAPHTTPConnection;

import sun.misc.BASE64Encoder;

import com.ibm.jsse.JSSEProvider;
import com.ibm.net.ssl.internal.www.protocol.https.Handler;

public final class NetUtils
{
  // Initialize the SSL provider subsystem.
  static
  {
    Security.addProvider(new JSSEProvider());
    System.setProperty("java.protocol.handler.pkgs","com.ibm.net.ssl.internal.www.protocol");
  }

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
  
  public static final void adjustSecurityProviderList()
  {
    // Ensure that the JSSE provider is first. This worksaround a WAS V502 issue where the IBMJSSEFIPS is added first.
    Provider jsseProvider = Security.getProvider("JSSE");
    Security.removeProvider("JSSE");
    if (jsseProvider == null)
    {
      jsseProvider = new JSSEProvider();
      System.setProperty("java.protocol.handler.pkgs","com.ibm.net.ssl.internal.www.protocol");
    }
    Security.insertProviderAt(jsseProvider,1);
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
   * Create a org.apache.soap.transport.http.SOAPHTTPConnection that accounts for WSAD proxy
   * information.
   * @return SOAPHTTPConnection org.apache.soap.transport.http.SOAPHTTPConnection
   */
  public static final SOAPHTTPConnection createSOAPHTTPConnection()
  {
    SOAPHTTPConnection soapHttpConnection = new SOAPHTTPConnection();
    String proxyHost = System.getProperty("http.proxyHost");
    String proxyPort = System.getProperty("http.proxyPort");
    String proxyUserName = System.getProperty("http.proxyUserName");
    String proxyPassword = System.getProperty("http.proxyPassword");
    if (proxyHost != null && proxyPort != null)
    {
      soapHttpConnection.setProxyHost(proxyHost);
      soapHttpConnection.setProxyPort(Integer.parseInt(proxyPort));
    }
    if (proxyUserName != null)
      soapHttpConnection.setProxyUserName(proxyUserName);
    if (proxyPassword != null)
      soapHttpConnection.setProxyPassword(proxyPassword);
    return soapHttpConnection;
  }

  /**
   * Create a URL from a string and provide an SSL handler if applicable.
   * @param urlString String representing the URL.
   * @return URL java.lang.URL representation of the URL.
   * @throws MalformedURLException
   */
  public static final URL createURL(String urlString) throws MalformedURLException
  {
    URL url;
    if (urlString.startsWith("https"))
    {
      adjustSecurityProviderList();
      url = new URL(null,urlString,new Handler());
    }
    else
      url = new URL(urlString);
    return url;
  }
}