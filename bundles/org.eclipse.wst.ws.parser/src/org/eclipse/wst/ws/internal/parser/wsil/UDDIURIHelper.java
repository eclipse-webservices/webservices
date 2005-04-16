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

package org.eclipse.wst.ws.internal.parser.wsil;

import java.text.MessageFormat;
import java.text.ParseException;

public class UDDIURIHelper
{
  private static final String QUERYSERVICE_URI_TEMPLATE = "uddiservice:query:{0}:query:{1}";
  private static final String SERVICEKEY_URI_TEMPLATE = "uddiservice:serviceKey:{0}:serviceKey:{1}";
  
  public static final String getQueryServiceURI(String query,String inquiryURL)
  {
    String[] uriParams = {query,inquiryURL};
    return MessageFormat.format(QUERYSERVICE_URI_TEMPLATE,uriParams);
  }
  
  public static final String getServiceKeyURI(String serviceKey,String inquiryURL)
  {
    String[] uriParams = {serviceKey,inquiryURL};
    return MessageFormat.format(SERVICEKEY_URI_TEMPLATE,uriParams);
  }
  
  private static final String[] parseURI(String pattern,String uri)
  {
    try
    {
      MessageFormat mf = new MessageFormat(pattern);
      Object[] parsedResults = mf.parse(uri);
      if (parsedResults != null && parsedResults.length > 0)
      {
        String[] results = new String[parsedResults.length];
        for (int i=0;i<parsedResults.length;i++)
          results[i] = (String)parsedResults[i];
        return results;
      }        
    }
    catch (ParseException e)
    {
    }
    return null;
  }
  
  public static final String[] parseServiceKeyURI(String uri)
  {
    return parseURI(SERVICEKEY_URI_TEMPLATE,uri);
  }
  
  public static final String[] parseQueryServiceURI(String uri)
  {
    return parseURI(QUERYSERVICE_URI_TEMPLATE,uri);
  }
}