/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
 * 20060131   125777 jesper@selskabet.org - Jesper S Moller
 * 20060222   118019 andyzhai@ca.ibm.com - Andy Zhai
 * 20060222   128564 jesper@selskabet.org - Jesper S Moller
 * 20060823    99034 makandre@ca.ibm.com - Andrew Mak, WSE support for basic-authenticating firewalls
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 * 20080728   242091 makandre@ca.ibm.com - Andrew Mak, Web services explorer inserts spurious CRLF in a long authentication header
 * 20080908   121120 mahutch@ca.ibm.com - Mark Hutchinson, Class sun.misc.BASE64Encoder is missing
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Hashtable;
import javax.net.ssl.SSLSocketFactory;

import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.transport.HTTPTransportException;
import org.w3c.dom.Element;
import org.apache.commons.codec.binary.Base64;

public class HTTPTransport
{
  private final String SYS_PROP_HTTPS_PROXY_HOST = "https.proxyHost";
  private final String SYS_PROP_HTTPS_PROXY_PORT = "https.proxyPort";
  private final String SYS_PROP_HTTPS_NON_PROXY_HOSTS = "https.nonProxyHosts";
  private final String SYS_PROP_HTTP_PROXY_HOST = "http.proxyHost";
  private final String SYS_PROP_HTTP_PROXY_PORT = "http.proxyPort";
  private final String SYS_PROP_HTTP_PROXY_USER_NAME = "http.proxyUserName";
  private final String SYS_PROP_HTTP_PROXY_PASSWORD = "http.proxyPassword";
  private final String SYS_PROP_HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts";
  
  private final String HTTP_METHOD = "POST";
  private final String HTTP_CONNECT = "CONNECT";
  private final String HTTP_VERSION_1_0 = "HTTP/1.0";
  private final String HTTP_VERSION = "HTTP/1.1";
  private final String HTTP_HEADER_SOAP_ACTION = "SOAPAction";
  public static final String HTTP_HEADER_AUTH = "Authorization";
  public static final String HTTP_HEADER_WWW_AUTH = "WWW-Authenticate";
  private final String HTTP_HEADER_PROXY_AUTH = "Proxy-authorization";
  private final String HTTP_HEADER_COOKIE = "Cookie";
  private final String HTTP_HEADER_COOKIE2 = "Cookie2";
  private final String HTTP_HEADER_HOST = "Host";
  private final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
  private final String HTTP_HEADER_ACCEPT = "Accept";
  private final String HTTP_HEADER_USER_AGENT = "User-Agent";
  private final String HTTP_HEADER_CACHE_CONTROL = "Cache-Control";
  private final String HTTP_HEADER_PRAGMA = "Pragma";
  private final String HTTP_HEADER_TRANSFER_ENCODEING = "Transfer-Encoding";
  private final String HTTP_HEADER_CONNECTION = "Connection";
  
  private final int HTTP_CODE_CONTINUE = 100;
  private final int HTTP_CODE_OK = 200;
  private final int HTTP_CODE_EXCEPTION = 300;

  private final String IBM_WEB_SERVICES_EXPLORER = "IBM Web Services Explorer";
  private final String TEXT_XML = "text/xml";
  private final String CONTENT_TYPE_VALUE = "text/xml; charset=utf-8";
  private final String ACCEPT_VALUE = "application/soap+xml, application/dime, multipart/related, text/*";
  public static final String BASIC = "Basic";
  private final String NO_CACHE = "no-cache";
  private final String CHUNKED = "chunked";
  private final String CLOSE = "close";
  private final String HTTPS = "https";
  private final char QUOTE = '\"';
  public static final char COLON = ':';
  private final char SPACE = ' ';
  private final char TAB = '\t';
  private final char R = '\r';
  private final char N = '\n';
  private final char ROOT = '/';
  private final String NEW_LINE = "\r\n";
  private final String EMPTY_STRING = "";

  private final int DEFAULT_HTTP_PORT = 80;
  private final int DEFAULT_HTTPS_PORT = 443;
  private final String DEFAULT_SOAP_ENCODING = "UTF-8";
  private final String DEFAULT_HTTP_HEADER_ENCODING = "iso-8859-1";
  private final boolean DEFAULT_CASE_SENSITIVE_FOR_HOST_NAME = false;

  private String httpBasicAuthUsername;
  private String httpBasicAuthPassword;
  private boolean maintainSession = false;
  private String cookie;
  private String cookie2;
  private HTTPResponse httpResponse;

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

  private String getMethod(URL url)
  {
    StringBuffer sb = new StringBuffer(HTTP_METHOD);
    sb.append(SPACE);
    String protocol = url.getProtocol();
    String httpProxyHost = System.getProperty(SYS_PROP_HTTP_PROXY_HOST);
    String httpsProxyHost = System.getProperty(SYS_PROP_HTTPS_PROXY_HOST);
    if (protocol.equalsIgnoreCase("http") && httpProxyHost != null && httpProxyHost.length() > 0)
    {
      sb.append(url.toString());
    }
    else if (protocol.equalsIgnoreCase("https") && httpsProxyHost != null && httpsProxyHost.length() > 0)
    {
      sb.append(url.toString());
    }
    else
    {
      String file = url.getFile();
      if (file != null && file.length() > 0)
        sb.append(file);
      else
        sb.append(ROOT);
    }
    sb.append(SPACE);
    sb.append(HTTP_VERSION);
    sb.append(NEW_LINE);
    return sb.toString();
  }

  private String getHost(URL url)
  {
    StringBuffer sb = new StringBuffer(HTTP_HEADER_HOST);
    sb.append(COLON);
    sb.append(SPACE);
    sb.append(url.getHost());
    sb.append(COLON);
    int port = url.getPort();
    if (port > 0)
      sb.append(String.valueOf(port));
    else if (url.getProtocol().equalsIgnoreCase(HTTPS))
      sb.append(DEFAULT_HTTPS_PORT);
    else
      sb.append(DEFAULT_HTTP_PORT);
    sb.append(NEW_LINE);
    return sb.toString();
  }
  
  private String getContentType()
  {
    return getHTTPHeader(HTTP_HEADER_CONTENT_TYPE, CONTENT_TYPE_VALUE);
  }
  
  private String getContentLength(byte[] payloadAsBytes)
  {
    return getHTTPHeader(HTTP_HEADER_CONTENT_LENGTH, String.valueOf(payloadAsBytes.length));
  }

  private String getSOAPAction(String soapAction)
  {
    StringBuffer sb = new StringBuffer(HTTP_HEADER_SOAP_ACTION);
    sb.append(COLON);
    sb.append(SPACE);
    sb.append(QUOTE);
    if (soapAction != null)
      sb.append(soapAction);
    sb.append(QUOTE);
    sb.append(NEW_LINE);
    return sb.toString();
  }
  
  private String getCookie()
  {
    if (maintainSession)
      return getHTTPHeader(HTTP_HEADER_COOKIE, cookie);
    else
      return EMPTY_STRING;
  }
  
  private String getCookie2()
  {
    if (maintainSession)
      return getHTTPHeader(HTTP_HEADER_COOKIE2, cookie2);
    else
      return EMPTY_STRING;
  }
  
  private String stripNewLine(String input) {
	  return input.replaceAll(System.getProperty("line.separator"), "");
  }
  
  private String getWWWAuthentication()
  {
    if (httpBasicAuthUsername != null && httpBasicAuthPassword != null)
    {
      StringBuffer sb = new StringBuffer(httpBasicAuthUsername);
      sb.append(COLON);
      sb.append(httpBasicAuthPassword);
      Base64 encoder = new Base64();
      String encodedUserNamePassword = new String(encoder.encode(sb.toString().getBytes()));
      sb.setLength(0);
      sb.append(HTTP_HEADER_AUTH);
      sb.append(COLON);
      sb.append(SPACE);
      sb.append(BASIC);
      sb.append(SPACE);
      sb.append(stripNewLine(encodedUserNamePassword));
      sb.append(NEW_LINE);
      return sb.toString();
    }
    else
      return EMPTY_STRING;
  }
  
  private String getProxyAuthentication()
  {
    String proxyUserName = System.getProperty(SYS_PROP_HTTP_PROXY_USER_NAME);
    String proxyPassword = System.getProperty(SYS_PROP_HTTP_PROXY_PASSWORD);
    if (proxyUserName != null && proxyPassword != null)
    {
      StringBuffer sb = new StringBuffer(proxyUserName);
      sb.append(COLON);
      sb.append(proxyPassword);
      Base64 encoder = new Base64();
      String encodedUserNamePassword = new String(encoder.encode(sb.toString().getBytes()));
      sb.setLength(0);
      sb.append(HTTP_HEADER_PROXY_AUTH);
      sb.append(COLON);
      sb.append(SPACE);
      sb.append(BASIC);
      sb.append(SPACE);
      sb.append(stripNewLine(encodedUserNamePassword));
      sb.append(NEW_LINE);
      return sb.toString();
    }
    else
      return EMPTY_STRING;
  }
  
  private String getAccept()
  {
    return getHTTPHeader(HTTP_HEADER_ACCEPT, ACCEPT_VALUE);
  }
  
  private String getUserAgent()
  {
    return getHTTPHeader(HTTP_HEADER_USER_AGENT, IBM_WEB_SERVICES_EXPLORER);
  }
  
  private String getCacheControl()
  {
    return getHTTPHeader(HTTP_HEADER_CACHE_CONTROL, NO_CACHE);
  }
  
  private String getPragma()
  {
    return getHTTPHeader(HTTP_HEADER_PRAGMA, NO_CACHE);
  }
  
  private String getConnection()
  {
    return getHTTPHeader(HTTP_HEADER_CONNECTION, CLOSE);
  }
  
  private String getHTTPHeader(String key, String value)
  {
    if (value != null)
    {
      StringBuffer sb = new StringBuffer(key);
      sb.append(COLON);
      sb.append(SPACE);
      sb.append(value);
      sb.append(NEW_LINE);
      return sb.toString();
    }
    else
      return EMPTY_STRING;
  }

  public void send(URL url, String soapAction, String payload) throws UnknownHostException, IOException
  {
    byte[] payloadAsUTF8 = payload.getBytes(DEFAULT_SOAP_ENCODING);
       
    StringBuffer httpHeader = new StringBuffer();
    httpHeader.append(getMethod(url));
    httpHeader.append(getHost(url));
    httpHeader.append(getContentType());
    httpHeader.append(getContentLength(payloadAsUTF8));
    httpHeader.append(getAccept());
    httpHeader.append(getUserAgent());
    httpHeader.append(getCacheControl());
    httpHeader.append(getPragma());
    httpHeader.append(getSOAPAction(soapAction));
    httpHeader.append(getWWWAuthentication());
    httpHeader.append(getProxyAuthentication());
    httpHeader.append(getCookie());
    httpHeader.append(getCookie2());
    httpHeader.append(getConnection());
    httpHeader.append(NEW_LINE); // new line between the HTTP header and the payload
    Socket socket = buildSocket(url);
    InputStream is = socket.getInputStream();
    OutputStream os = socket.getOutputStream();
    os.write(httpHeader.toString().getBytes(DEFAULT_HTTP_HEADER_ENCODING));
    os.write(payloadAsUTF8);
    os.flush();
    httpResponse = new HTTPResponse();
    readHTTPResponseHeader(is, httpResponse);
    int code = httpResponse.getStatusCode();
    if (code == HTTP_CODE_CONTINUE)
    {
      httpResponse.reset();
      readHTTPResponseHeader(is, httpResponse);
    }
    readHTTPResponsePayload(is, httpResponse);
    os.close();
    is.close();
    socket.close();
    code = httpResponse.getStatusCode();
    String contentType = httpResponse.getHeader(HTTP_HEADER_CONTENT_TYPE.toLowerCase());
    if (code >= HTTP_CODE_EXCEPTION && (contentType == null || contentType.toLowerCase().indexOf(TEXT_XML.toLowerCase()) == -1))
      throw new HTTPTransportException(code, httpResponse.getStatusMessage(), httpResponse.getHeaders());
  }

  private void readHTTPResponseHeader(InputStream is, HTTPResponse resp) throws IOException
  {
    byte b = 0;
    int len = 0;
    int colonIndex = -1;
    String key;
    String value;
    boolean readTooMuch = false;
    ByteArrayOutputStream baos;
    for (baos = new ByteArrayOutputStream(4096);;)
    {
      if (!readTooMuch)
        b = (byte)is.read();
      if (b == -1)
        break;
      readTooMuch = false;
      if ((b != R) && (b != N))
      {
        if ((b == COLON) && (colonIndex == -1))
          colonIndex = len;
        len++;
        baos.write(b);
      }
      else if (b == R)
        continue;
      else // b == N
      {
        if (len == 0)
          break;
        b = (byte)is.read();
        readTooMuch = true;
        // A space or tab at the begining of a line means the header continues.
        if ((b == SPACE) || (b == TAB))
          continue;
        baos.close();
        byte[] bArray = baos.toByteArray();
        baos.reset();
        if (colonIndex != -1)
        {
          key = new String(bArray, 0, colonIndex, DEFAULT_HTTP_HEADER_ENCODING);
          value = new String(bArray, colonIndex + 1, len - 1 - colonIndex, DEFAULT_HTTP_HEADER_ENCODING);
          colonIndex = -1;
        }
        else
        {
          key = new String(bArray, 0, len, DEFAULT_HTTP_HEADER_ENCODING);
          value = EMPTY_STRING;
        }
        if (!resp.isStatusSet())
        {
          // Reader status code
          int start = key.indexOf(SPACE) + 1;
          String s = key.substring(start).trim();
          int end = s.indexOf(SPACE);
          if (end != -1)
            s = s.substring(0, end);
          try
          {
            resp.setStatusCode(Integer.parseInt(s));
          }
          catch (NumberFormatException nfe)
          {
            resp.setStatusCode(-1);
          }
          resp.setStatusMessage(key.substring(start + end + 1));
        }
        else
          resp.addHeader(key.toLowerCase().trim(), value.trim());
        len = 0;
      }
    }
    baos.close();
  }

  private void readHTTPResponsePayload(InputStream is, HTTPResponse resp) throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      byte b = (byte)is.read();
      while (b != -1)
      {
        baos.write(b);
        b = (byte)is.read();
      }
    }
    catch (SocketTimeoutException ste)
    {
    }
    baos.close();
    resp.setPayload(baos.toByteArray());
  }

  /**
   * Receives the bytes from the last web service invocation.  This is used by WSE's default
   * SOAP transport.
   * 
   * @return A byte array.
   * @throws IOException
   */
  byte[] receiveBytes() throws IOException
  {
    if (httpResponse != null)
    {
        byte[] payload = httpResponse.getPayload();
        if (CHUNKED.equalsIgnoreCase(httpResponse.getHeader(HTTP_HEADER_TRANSFER_ENCODEING.toLowerCase())))
        {
          ByteArrayInputStream bais = new ByteArrayInputStream(payload);
          ChunkedInputStream cis = new ChunkedInputStream(bais);
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          byte b;
          while ((b = (byte)cis.read()) != -1)
            baos.write(b);
          baos.close();
          cis.close();
          bais.close();
          return baos.toByteArray();
        }
        else
        {
          return payload;
        }
    }
    return null;
  }
  
  /**
   * This method is deprecated after WSE swtiches to using a pluggable transport via enhancement 176493.
   * The core functionality of this method is refactored to the receiveBytes() method. 
   * 
   * @deprecated
   */
  public BufferedReader receive()
  {
    try
    {
      byte[] payload = receiveBytes();
      if (payload != null) {
    	Element soapEnvelope = XMLUtils.byteArrayToElement(payload, false);
        // remove XML namespace declaration
        if (soapEnvelope != null)
          return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(XMLUtils.serialize(soapEnvelope, true).getBytes(DEFAULT_SOAP_ENCODING)), DEFAULT_SOAP_ENCODING)); 
      }
    }
    catch (Throwable t)
    {
    }
    return null;
  }

  public Hashtable getHeaders()
  {
    Hashtable headers = new Hashtable();
    if (httpResponse != null)
      headers.putAll(httpResponse.getHeaders());
    return headers;
  }

  /**
   * Builds the first line of a connection request to the proxy server
   * 
   * @param url The URL that we want to ultimately connect to.
   * @return A string of the form "CONNECT &lt;hostname&gt;:&lt;port&gt; HTTP/1.0".
   */
  private StringBuffer getConnectMethod(URL url) {
	
	  StringBuffer sb = new StringBuffer(HTTP_CONNECT);
	  sb.append(SPACE);	  
	  sb.append(url.getHost());
	  sb.append(COLON);
	  sb.append(url.getPort());
	  sb.append(SPACE);
	  sb.append(HTTP_VERSION_1_0);
	  sb.append(NEW_LINE);
	  return sb;
  }
  
  /**
   * Construct a socket to the proxy server which be used to tunnel through.
   * 
   * @param url The URL that we want to ultimately connect to.
   * @param proxyHost The proxy host.
   * @param proxyPort The proxy port.
   * @return A connected socket to the proxy server. 
   * @throws IOException 
   */
  private Socket buildTunnelSocket(URL url, String proxyHost, int proxyPort) throws IOException {

	  StringBuffer httpHeader = new StringBuffer();
	  httpHeader.append(getConnectMethod(url));
	  httpHeader.append(getProxyAuthentication());       
	  httpHeader.append(NEW_LINE);
    
	  Socket tunnel = new Socket(proxyHost, proxyPort);
    
	  InputStream  is = tunnel.getInputStream();
	  OutputStream os = tunnel.getOutputStream();
    
	  os.write(httpHeader.toString().getBytes(DEFAULT_HTTP_HEADER_ENCODING));        
	  os.flush();
    
	  HTTPResponse httpResponse = new HTTPResponse();
	  readHTTPResponseHeader(is, httpResponse);
    
	  int code = httpResponse.getStatusCode();

	  // ensure we are successfully connected to the proxy
	  if (code != HTTP_CODE_OK)
		  throw new HTTPTransportException(code, httpResponse.getStatusMessage(), httpResponse.getHeaders());
	  
	  return tunnel;
  }
  
  private Socket buildSocket(URL url) throws UnknownHostException, IOException
  {
    Socket s = null;
    String host = url.getHost();
    int port = url.getPort();
    String proxyHost = System.getProperty(SYS_PROP_HTTP_PROXY_HOST);
    int proxyPort = Integer.getInteger(SYS_PROP_HTTP_PROXY_PORT, DEFAULT_HTTP_PORT).intValue();
    
    String nonProxyHosts = System.getProperty(SYS_PROP_HTTP_NON_PROXY_HOSTS);

    //  String proxyUserName = System.getProperty(SYS_PROP_HTTP_PROXY_USER_NAME);
    //  String proxyPassword = System.getProperty(SYS_PROP_HTTP_PROXY_PASSWORD);
    if (url.getProtocol().equalsIgnoreCase(HTTPS))
    {
      proxyHost = System.getProperty(SYS_PROP_HTTPS_PROXY_HOST);
      proxyPort = Integer.getInteger(SYS_PROP_HTTPS_PROXY_PORT, DEFAULT_HTTPS_PORT).intValue();
      nonProxyHosts = System.getProperty(SYS_PROP_HTTPS_NON_PROXY_HOSTS);

      if (proxyHost != null && proxyHost.length() > 0 && !isHostInNonProxyHosts(host, nonProxyHosts, DEFAULT_CASE_SENSITIVE_FOR_HOST_NAME))
      {
        // SSL with proxy server
        Socket tunnel = buildTunnelSocket(url, proxyHost, proxyPort);       
        s = ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(tunnel, host, port, true);    	  
      }
      else
        s = SSLSocketFactory.getDefault().createSocket(host, (port > 0 ? port : DEFAULT_HTTPS_PORT));
      // Removing dependency on soap.jar
      //  s = SSLUtils.buildSSLSocket(host, (port > 0 ? port : DEFAULT_HTTPS_PORT), proxyHost, proxyPort);
      // TODO:
      // Build an SSL socket that supports proxyUser and proxyPassword,
      // as demonstrated in the following (original) line of code:
      //  s = SSLUtils.buildSSLSocket(host, (port > 0 ? port : DEFAULT_HTTPS_PORT), proxyHost, proxyPort, proxyUserName, proxyPassword);
    }
    else if (proxyHost != null && proxyHost.length() > 0 && !isHostInNonProxyHosts(host, nonProxyHosts, DEFAULT_CASE_SENSITIVE_FOR_HOST_NAME))
      s = new Socket(proxyHost, proxyPort);
    else
      s = new Socket(host, (port > 0 ? port : DEFAULT_HTTP_PORT));
    return s;
  }
  
  private boolean isHostInNonProxyHosts(String host, String nonProxyHosts, boolean caseSensitive)
  {
  	if (caseSensitive) return host.matches(createPatternFromString(nonProxyHosts));
  	else return host.toLowerCase().matches(createPatternFromString(nonProxyHosts.toLowerCase()));  
  }
  
  /*
   * This method is used to generate a valid regular expression for a 
   * normal java String used in the proxy mechanism. 
   * For example, the http.nonProxyHosts contains following String: 
   * "192.168.2.*|localhost|*.ibm.com" . It would be 
   * transformed into: "192\.168\.2\.\w*|localhost|\w*\.ibm\.com"
   * Thus, following host string would match above pattern:
   * 192.168.2.5 192.168.2. 192.168.2.666 192.168.2.w
   * localhost
   * torolab.ibm.com .ibm.com 123.ibm.com
   * 
   * Two transformations are done:
   * 1. replace all "." into "\." As in regular expression, '.' represents 
   *    any charater.  "\.' represents the real character '.'
   * 2. In order to get the real meaning of "*" used in property 
   *    http.nonProxyHosts, "\w*" is used to replace "*"
   *    "\w" represent a word character: [a-zA-Z_0-9]
   *    
   * Restriction:
   * The validity of address is not checked. 
   * (192.168.2.555 and 192.168.2.com are OK)
   * 
   * TODO check whether * occurs in address or hostname.
   * if it occuus in address representation, replace "*" with "\d*"
   * and check: value < 256 ?
   */
  private String createPatternFromString(String str) 
  {
    /* This is the same as following more understandable way:
	 * return str.replace(".", "\\.").replace("*", "\\w*");
	 * But, replace(CharSequence target, CharSequence replacement) can only be 
	 * supported after j2se 1.5, on the other hand, 
	 * replaceAll(String regex, String replacement) can be supported before 
	 * j2se 1.5.
	 */
    return str == null ? null : str.replaceAll("\\.", "\\.").replaceAll("\\*", "\\w*");
  }
}
