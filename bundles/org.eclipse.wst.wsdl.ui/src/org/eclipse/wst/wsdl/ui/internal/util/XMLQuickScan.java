/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import java.io.InputStream;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
public class XMLQuickScan
{            
  public static String getTargetNamespaceURIForSchema(String uri)
  {
    String result = null;
    try
    {             
      URL url = new URL(uri);
      InputStream inputStream = url.openStream();
      result = XMLQuickScan.getTargetNamespaceURIForSchema(inputStream); 
    }
    catch (Exception e)
    {      
    }  
    return result;
  }

  public static String getTargetNamespaceURIForSchema(InputStream input)
  {  
    TargetNamespaceURIContentHandler handler = new TargetNamespaceURIContentHandler();                                                                  
    ClassLoader prevClassLoader = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(XMLQuickScan.class.getClassLoader());
      Class theClass = Class.forName("org.apache.xerces.parsers.SAXParser");
      XMLReader reader = (XMLReader)theClass.newInstance();
      reader.setContentHandler(handler);
      reader.parse(new InputSource(input));
    }
    catch (Exception e)
    {      
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(prevClassLoader);
    }
    return handler.targetNamespaceURI;
  }  

  protected static class TargetNamespaceURIContentHandler extends DefaultHandler
  {       
    public String targetNamespaceURI;

    public void startElement(String uri, String localName, String qName, Attributes attributes)  throws SAXException
    {            
      if (localName.equals("schema") || localName.equals("definitions"))
      {               
        int nAttributes = attributes.getLength();
        for (int i = 0; i < nAttributes; i++)
        {
          if (attributes.getLocalName(i).equals("targetNamespace"))
          {
            targetNamespaceURI = attributes.getValue(i);
            break;
          }
        }
      }                                    
      // todo there's a ice way to do this I'm sure    
      // here I intentially cause an exception... 
      String x = null;
      x.length();
    }
  }
}