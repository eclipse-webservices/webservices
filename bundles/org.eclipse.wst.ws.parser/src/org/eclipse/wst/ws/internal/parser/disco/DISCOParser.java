/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.disco;

import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * See http://msdn.microsoft.com/msdnmag/issues/02/02/xml/default.aspx for more
 * details on DISCO.
 */
public class DISCOParser
{
  private DocumentBuilder parser_;
  private final String NS_DISCO = "http://schemas.xmlsoap.org/disco/";
  private final String DISCOVERY = "discovery";
  private final String DISCOVERY_REF = "discoveryRef";
  private final String NS_CONTRACT_REF = "http://schemas.xmlsoap.org/disco/scl/";
  private final String CONTRACT_REF = "contractRef";
  private final String REF = "ref";
  private final String DOC_REF = "docRef";

  public DISCOParser()
  {
    try
    {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      docBuilderFactory.setNamespaceAware(true);
      parser_ = docBuilderFactory.newDocumentBuilder();
    }
    catch (ParserConfigurationException pce)
    {
      parser_ = null;
    }
  }

  public DISCOReference[] parse(String discoURI, InputSource source) throws Exception
  {
    if (parser_ != null)
    {
      Document doc = parser_.parse(source);
      Element rootElement = doc.getDocumentElement();
      // Root element must by <disco:discovery
      // xmlns:disco="http://schemas.xmlsoap.org/disco/">
      if (rootElement != null && rootElement.getNamespaceURI().equals(NS_DISCO) && rootElement.getLocalName().equals(DISCOVERY))
      {
        NodeList childNodes = rootElement.getChildNodes();
        Vector discoReferences = new Vector();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
          Node childNode = childNodes.item(i);
          if (childNode instanceof Element)
          {
            Element element = (Element)childNode;
            String localName = element.getLocalName();
            String nsURI = element.getNamespaceURI();
            if (nsURI.equals(NS_DISCO) && localName.equals(DISCOVERY_REF))
            {
              // DISCO link.
              String ref = convertToAbsoluteURI(discoURI, element.getAttribute(REF));
              discoReferences.addElement(new DISCOReference(ref));
            }
            else if (nsURI.equals(NS_CONTRACT_REF) && localName.equals(CONTRACT_REF))
            {
              // WSDL link.
              String ref = convertToAbsoluteURI(discoURI, element.getAttribute(REF));
              String docRef = convertToAbsoluteURI(discoURI, element.getAttribute(DOC_REF));
              discoReferences.addElement(new DISCOContractReference(ref, docRef));
            }
          }
        }
        int numberOfDISCOReferences = discoReferences.size();
        if (numberOfDISCOReferences > 0)
        {
          DISCOReference[] references = new DISCOReference[numberOfDISCOReferences];
          discoReferences.copyInto(references);
          return references;
        }
      }
      else
      {
        // The document is not a valid DISCO document.
        throw new Exception();
      }
    }
    return null;
  }

  private String convertToAbsoluteURI(String discoURI, String refURI)
  {
    if (refURI != null)
    {
      if (refURI.indexOf("://") > -1)
      {
        // refURI is already absolute.
        return refURI;
      }
      else
      {
        StringBuffer absoluteURI = new StringBuffer(discoURI.substring(0, Math.max(discoURI.lastIndexOf('\\'), discoURI.lastIndexOf('/') + 1)));
        absoluteURI.append(refURI);
        return absoluteURI.toString();
      }
    }
    return null;
  }
}
