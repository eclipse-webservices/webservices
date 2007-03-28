/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.util;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.axis.Constants;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SoapHelper
{
  private static Hashtable defaultSoapEnvelopeNamespaces_ = null;
  
  private static final void initDefaultSoapEnvelopeNamespaces()
  {
    defaultSoapEnvelopeNamespaces_ = new Hashtable();
    defaultSoapEnvelopeNamespaces_.put(Constants.URI_SOAP11_ENV,Constants.NS_PREFIX_SOAP_ENV);
    defaultSoapEnvelopeNamespaces_.put(Constants.URI_2001_SCHEMA_XSI,Constants.NS_PREFIX_SCHEMA_XSI);
    defaultSoapEnvelopeNamespaces_.put(Constants.URI_2001_SCHEMA_XSD,Constants.NS_PREFIX_SCHEMA_XSD);
  }
  
  public static final void addDefaultSoapEnvelopeNamespaces(Hashtable soapEnvelopeNamespaces)
  {
    if (defaultSoapEnvelopeNamespaces_ == null)
      initDefaultSoapEnvelopeNamespaces();
    Enumeration defaultSoapEnvelopeNamespaceURIs = defaultSoapEnvelopeNamespaces_.keys();
    while (defaultSoapEnvelopeNamespaceURIs.hasMoreElements())
    {
      String defaultSoapEnvelopeNamespaceURI = (String)defaultSoapEnvelopeNamespaceURIs.nextElement();
      soapEnvelopeNamespaces.put(defaultSoapEnvelopeNamespaceURI,(String)defaultSoapEnvelopeNamespaces_.get(defaultSoapEnvelopeNamespaceURI));
    }
  }
  
  public static final boolean isDefaultSoapEnvelopeNamespace(String namespaceURI,String namespacePrefix)
  {
    if (defaultSoapEnvelopeNamespaces_ == null)
      initDefaultSoapEnvelopeNamespaces();
    if (defaultSoapEnvelopeNamespaces_.get(namespaceURI) != null)
      return true;
    return false;
  }  
  
  public static final Element createSoapEnvelopeElement(Document doc,Hashtable soapEnvelopeNamespaceTable)
  {
    Element soapEnvelopeElement = doc.createElement("soapenv:Envelope");
    Enumeration e = soapEnvelopeNamespaceTable.keys();
    while (e.hasMoreElements())
    {
      String soapEnvelopeNamespaceURI = (String)e.nextElement();
      StringBuffer soapEnvelopeNamespaceAttr = new StringBuffer("xmlns:");
      soapEnvelopeNamespaceAttr.append((String)soapEnvelopeNamespaceTable.get(soapEnvelopeNamespaceURI));
      soapEnvelopeElement.setAttribute(soapEnvelopeNamespaceAttr.toString(),soapEnvelopeNamespaceURI);      
    }    
    return soapEnvelopeElement;
  }
  
  public static final Element createSoapHeaderElement(Document doc)
  {
    return doc.createElement("soapenv:Header");
  }
  
  public static final Element createSoapBodyElement(Document doc)
  {
    return doc.createElement("soapenv:Body");
  }
  
  public static final Element createRPCWrapperElement(Document doc,Hashtable soapEnvelopeNamespaceTable,String encodingNamespaceURI,String operationName, String encodingStyle)
  {
    int nsId = 0;
    StringBuffer wrapperElementName = new StringBuffer();
    String encodingNamespacePrefix = (String)soapEnvelopeNamespaceTable.get(encodingNamespaceURI);
    if (encodingNamespacePrefix != null)
      wrapperElementName.append(encodingNamespacePrefix);
    else
    {
      // Loop until we generate a unique prefix.
      do
      {
        wrapperElementName.setLength(0);
        wrapperElementName.append("ns").append(nsId);
        if (!soapEnvelopeNamespaceTable.containsValue(wrapperElementName.toString()))
          break;
        nsId++;
      } while (true);
    }    
    String wrapperElementNamePrefix = wrapperElementName.toString();
    wrapperElementName.append(':').append(operationName);
    Element wrapperElement = doc.createElement(wrapperElementName.toString());
    StringBuffer namespaceAttrName = new StringBuffer("xmlns:");
    namespaceAttrName.append(wrapperElementNamePrefix);
    wrapperElement.setAttribute(namespaceAttrName.toString(),encodingNamespaceURI);
    if (encodingStyle != null)
      wrapperElement.setAttribute("soapenv:encodingStyle",encodingStyle);
    return wrapperElement;
  }

  public static final String encodeNamespaceDeclaration(String prefix, String uri)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(prefix);
    sb.append(" ");
    sb.append(uri);
    String result = URLUtils.encode(sb.toString());
    return result;
  }

  public static final String[] decodeNamespaceDeclaration(String s)
  {
    String sCopy = URLUtils.decode(s);
    int index = sCopy.indexOf(" ");
    String[] nsDecl = new String[2];
    if (index != -1)
    {
      nsDecl[0] = sCopy.substring(0, index);
      nsDecl[1] = sCopy.substring(index+1, sCopy.length());
    }
    else
    {
      nsDecl[0] = null;
      nsDecl[1] = sCopy;
    }
    return nsDecl;
  }
}
