/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.xml.schema;

import org.eclipse.wst.wsi.internal.WSIException;
import org.eclipse.wst.wsi.internal.util.TestUtils;
import org.eclipse.wst.wsi.internal.xml.XMLDocumentCache;
import org.eclipse.wst.wsi.internal.xml.XMLDocumentCacheUser;
import org.eclipse.wst.wsi.internal.xml.XMLTags;
import org.eclipse.wst.wsi.internal.xml.XMLUtils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Vector;

/**
 * This abstract class is used to process an XML Schema.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public abstract class XMLSchemaProcessor
  extends XMLDocumentCacheUser
  implements XMLTags
{
  protected List returnList = new Vector();
  protected String context;
  protected boolean throwException = true;
  protected List schemaLocations = new Vector();

  /**
   * Constructor for XMLSchemaProcessor.
   * @param context       document context. 
   * @param documentList  cache of previously parsed documents.
   */
  public XMLSchemaProcessor(String context, XMLDocumentCache documentList)
  {
    this(context, documentList, true);
  }

  /**
   * Constructor for XMLSchemaProcessor.
   * @param context        document context. 
   * @param documentList   cache of previously parsed documents.
   * @param throwException indicates if an exception should be thrown if there is a parsing problem.
   */
  public XMLSchemaProcessor(
    String context,
    XMLDocumentCache documentList,
    boolean throwException)
  {
    super();

    this.context = context;
    this.documentList = documentList;
    this.throwException = throwException;
  }

  /**
   * Search for all schema elements, load the xsd documents, and then process them.
   * @param node  node.
   * @return a list.
   * @throws WSIException if the XML schema file is not found or if it is not formatted correctly.
   */
  public List processAllSchema(Node node) throws WSIException
  {
    return processAllSchema(node, context);
  }

  /**
   * Search for all schema elements, load the xsd documents, and then process them.
   * @param node  node.
   * @return a list.
   * @throws WSIException if the XML schema file is not found or if it is not formatted correctly.
   */
  private List processAllSchema(Node node, String ctx) throws WSIException
  {
    // Process all nodes
    while (node != null)
    {
      // If this is an element node, then continue
      if (Node.ELEMENT_NODE == node.getNodeType())
      {
        // if xsd:schema element is found -> process schema
        if (XMLUtils.equals(node, ELEM_XSD_SCHEMA))
        {
          processSchema((Element) node);

          // Process all children of the schema element
          processAllSchema(node.getFirstChild(), ctx);
        }

        // if xsd:import element is found -> load schema and process schema
        else if (XMLUtils.equals(node, ELEM_XSD_IMPORT))
        {

          Attr schemaLocation =
            XMLUtils.getAttribute((Element) node, ATTR_XSD_SCHEMALOCATION);

          // Try to parse imported XSD
          if (schemaLocation != null && schemaLocation.getValue() != null)
          {
          	if (!schemaLocations.contains(schemaLocation.getValue()))
          	{
          		schemaLocations.add(schemaLocation.getValue());
              try
              {
                // Read and pParse the XML schema document
                Document document =
                  parseXMLDocumentURL(
                    schemaLocation.getValue(),
                    ctx,
                    TestUtils.getXMLSchemaLocation());

                processAllSchema(document.getDocumentElement(),
                    XMLUtils.createURLString(schemaLocation.getValue(), ctx));
              }
              catch (WSIException e)
              {
                if (throwException)
                  throw e;
              }

              catch (Throwable t)
              {
                // NOTE: An exception will occur if the XML schema file is not found or if it is not formatted correctly
                if (throwException)
                  throw new WSIException(t.getMessage(), t);
              }
            }
          }
        }

        else
        {
          // else iterate element recursively
          processAllSchema(node.getFirstChild(), ctx);
        }
      }

      // Get next sibiling
      node = node.getNextSibling();
    }

    // Return list created by the class that extends this class
    return returnList;
  }

  protected abstract void processSchema(Element element);

}
