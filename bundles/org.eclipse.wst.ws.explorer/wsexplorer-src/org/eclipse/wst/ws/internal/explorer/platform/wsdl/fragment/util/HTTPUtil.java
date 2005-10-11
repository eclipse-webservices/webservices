/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util;

import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.Constants;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDSimpleAtomicFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDSimpleListFragment;
import org.w3c.dom.Element;

public class HTTPUtil
{
  private HTTPUtil()
  {
  }

  public static String genURLEncodedParameters(IXSDFragment fragment)
  {
    if (fragment instanceof IXSDSimpleListFragment)
      return genURLEncodedParamsFromListFragment((IXSDSimpleListFragment)fragment);
    else if (fragment instanceof IXSDSimpleAtomicFragment)
      return genURLEncodedParamsFromAtomicFragment((IXSDSimpleAtomicFragment)fragment);
    else
      return genURLEncodedXMLParameters(fragment);
  }

  private static String genURLEncodedParamsFromAtomicFragment(IXSDSimpleAtomicFragment fragment)
  {
    StringBuffer urlEncodedParam = new StringBuffer();
    String[] params = fragment.getParameterValues(fragment.getID());
    for (int i = 0; i < params.length; i++)
    {
      urlEncodedParam.append(URLUtils.encode(fragment.getName()));
      urlEncodedParam.append("=");
      urlEncodedParam.append(URLUtils.encode(params[i]));
      if (i < params.length - 1)
        urlEncodedParam.append("&");
    }
    return urlEncodedParam.toString();
  }

  private static String genURLEncodedParamsFromListFragment(IXSDSimpleListFragment fragment)
  {
    StringBuffer urlEncodedParam = new StringBuffer();
    IXSDFragment[] childFrags = fragment.getAllFragments();
    for (int i = 0; i < childFrags.length; i++)
    {
      urlEncodedParam.append(URLUtils.encode(fragment.getName()));
      urlEncodedParam.append("=");
      String[] params = childFrags[i].getParameterValues(childFrags[i].getID());
      StringBuffer paramBuffer = new StringBuffer();
      for (int j = 0; j < params.length; j++)
      {
        paramBuffer.append(params[j]);
        if (j < params.length - 1)
          paramBuffer.append(FragmentConstants.LIST_SEPERATOR);
      }
      urlEncodedParam.append(URLUtils.encode(paramBuffer.toString()));
      if (i < childFrags.length - 1)
        urlEncodedParam.append("&");
    }
    return urlEncodedParam.toString();
  }

  private static String genURLEncodedXMLParameters(IXSDFragment fragment)
  {
    StringBuffer urlEncodedParam = new StringBuffer();
    Hashtable soapEnvelopeNamespaceTable = new Hashtable();
    soapEnvelopeNamespaceTable.put(Constants.URI_SOAP11_ENV, Constants.NS_PREFIX_SOAP_ENV);
    soapEnvelopeNamespaceTable.put(Constants.URI_2001_SCHEMA_XSI, Constants.NS_PREFIX_SCHEMA_XSI);
    soapEnvelopeNamespaceTable.put(Constants.URI_2001_SCHEMA_XSD, Constants.NS_PREFIX_SCHEMA_XSD);
    Element[] instanceDocuments = new Element[0];
    try
    {
      instanceDocuments = fragment.genInstanceDocumentsFromParameterValues(true, soapEnvelopeNamespaceTable, XMLUtils.createNewDocument(null));
    }
    catch (ParserConfigurationException pce)
    {
    }
    for (int i = 0; i < instanceDocuments.length; i++)
    {
      urlEncodedParam.append(URLUtils.encode(fragment.getName()));
      urlEncodedParam.append("=");
      urlEncodedParam.append(URLUtils.encode(XMLUtils.serialize(instanceDocuments[i], true)));
      if (i < instanceDocuments.length - 1)
        urlEncodedParam.append("&");
    }
    return urlEncodedParam.toString();
  }
}
