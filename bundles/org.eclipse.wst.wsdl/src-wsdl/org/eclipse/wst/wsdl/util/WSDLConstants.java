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
package org.eclipse.wst.wsdl.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WSDLConstants
{
  public static final int BINDING = 0;
  public static final int DEFINITION = 1;
  public static final int DOCUMENTATION = 2;
  public static final int FAULT = 3;
  public static final int IMPORT = 4;
  public static final int INPUT = 5;
  public static final int MESSAGE = 6;
  public static final int OPERATION = 7;
  public static final int OUTPUT = 8;
  public static final int PART = 9;
  public static final int PORT = 10;
  public static final int PORT_TYPE = 11;
  public static final int SERVICE = 12;
  public static final int TYPES = 13;
  public static final int ELEMENT = 14;
  public static final int TYPE = 15;

  public static final int EXTENSIBILITY_ELEMENT = 24;

  public static final String BINDING_ELEMENT_TAG = "binding";
  public static final String DEFINITION_ELEMENT_TAG = "definitions";
  public static final String DOCUMENTATION_ELEMENT_TAG = "documentation";
  public static final String FAULT_ELEMENT_TAG = "fault";
  public static final String IMPORT_ELEMENT_TAG = "import";
  public static final String INPUT_ELEMENT_TAG = "input";
  public static final String MESSAGE_ELEMENT_TAG = "message";
  public static final String OPERATION_ELEMENT_TAG = "operation";
  public static final String OUTPUT_ELEMENT_TAG = "output";
  public static final String PART_ELEMENT_TAG = "part";
  public static final String PORT_ELEMENT_TAG = "port";
  public static final String PORT_TYPE_ELEMENT_TAG = "portType";
  public static final String SERVICE_ELEMENT_TAG = "service";
  public static final String TYPES_ELEMENT_TAG = "types";

  // common
  public static final String NAME_ATTRIBUTE = "name";
  public static final String MESSAGE_ATTRIBUTE = "message";
  public static final String BINDING_ATTRIBUTE = "binding";
  public static final String TYPE_ATTRIBUTE = "type";

  // definitions
  public final static String ENCODING_ATTRIBUTE = "encoding";
  public final static String TARGETNAMESPACE_ATTRIBUTE = "targetNamespace";

  // binding
  public final static String RESOURCE_URI_ATTRIBUTE = "resourceURI";

  // part
  public final static String ELEMENT_ATTRIBUTE = "element";
  
  // import
  public final static String LOCATION_ATTRIBUTE = "location"; 
  public static final String NAMESPACE_ATTRIBUTE = "namespace";  

  public static final String WSDL_NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/";
  public static final String XSD_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema";

  /**
   * The value <code>http://www.w3.org/1999/XMLSchema"</code>.
   */
  public static final String SCHEMA_FOR_SCHEMA_URI_1999 = "http://www.w3.org/1999/XMLSchema";

  /**
   * The value <code>"http://www.w3.org/2000/10/XMLSchema"</code>.
   */
  public static final String SCHEMA_FOR_SCHEMA_URI_2000_10 = "http://www.w3.org/2000/10/XMLSchema";

  /**
   * The value <code>"http://www.w3.org/2001/XMLSchema"</code>.
   */
  public static final String SCHEMA_FOR_SCHEMA_URI_2001 = "http://www.w3.org/2001/XMLSchema";

  private static final String[] ELEMENT_TAGS =
    new String[] {
      BINDING_ELEMENT_TAG,
      DEFINITION_ELEMENT_TAG,
      DOCUMENTATION_ELEMENT_TAG,
      FAULT_ELEMENT_TAG,
      IMPORT_ELEMENT_TAG,
      INPUT_ELEMENT_TAG,
      MESSAGE_ELEMENT_TAG,
      OPERATION_ELEMENT_TAG,
      OUTPUT_ELEMENT_TAG,
      PART_ELEMENT_TAG,
      PORT_ELEMENT_TAG,
      PORT_TYPE_ELEMENT_TAG,
      SERVICE_ELEMENT_TAG,
      TYPES_ELEMENT_TAG };

  public static final String getElementTag(int nodeType)
  {
    return ELEMENT_TAGS[nodeType];
  }
  
  public static final int nodeType(String localName)
  {
    for (int i = 0; i < ELEMENT_TAGS.length; ++i)
    {
      if (localName.equals(ELEMENT_TAGS[i]))
      {
        return i;
      }
    }
    return -1;
  }

  public static final int nodeType(Node node)
  {
    return isWSDLNamespace(node.getNamespaceURI()) ? nodeType(node.getLocalName()) : -1;
  }

  /**
   * Returns whether the given namespace is the WSDL namespace or not.
   * @param namespace a namespace.
   * @return whether the given namespace is the WSDL namespace or not.
   */
  public static boolean isWSDLNamespace(String namespace)
  {
    return WSDL_NAMESPACE_URI.equals(namespace);
  }

  public static boolean isMatchingNamespace(String namespace1, String namespace2)
  {
    return (namespace1 == null ? namespace2 == null : namespace1.equals(namespace2));
  }

  public static String getAttribute(Element element, String attributeName)
  {
    return element.hasAttribute(attributeName) ? element.getAttribute(attributeName) : null;
  }
}
