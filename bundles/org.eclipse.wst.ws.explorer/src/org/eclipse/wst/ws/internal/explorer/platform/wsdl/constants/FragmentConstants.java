/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants;

public class FragmentConstants
{
  // XSD minOccurs and maxOccurs
  public static final int DEFAULT_MIN_OCCURS = 1;
  public static final int DEFAULT_MAX_OCCURS = 1;
  public static final int UNBOUNDED = -1;

  // WSDL style
  public static final int STYLE_DOCUMENT = 0;
  public static final int STYLE_RPC = 1;

  // WSDL Encoding styles
  public static final int ENCODING_LITERAL = 0;
  public static final int ENCODING_SOAP = 1;
  public static final int ENCODING_URL = 2; // HTTP Get/Post

  // IDs used by the fragment model
  public static final String ID_SEPERATOR = "::";
  public static final String INPUT_ID = "::input";
  public static final String OUTPUT_ID = "::output";
  public static final String TABLE_ID = "::tableID";
  public static final String FRAGMENT_ID = "::fragmentID";
  public static final String NAME_ANCHOR_ID = "::nameAnchorID";
  public static final String FRAGMENT_VIEW_ID = "::fragmentViewID";
  public static final String XSD_ATOMIC_ENUM_ID = "::xsdAtomicEnumID";
  public static final String XSD_ALL_GROUP_ID = "::xsdAllGroupID";
  public static final String PART_TOKEN = "^";

  // Fragment view IDs
  public static final String FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE = "::fragmentViewSwitchFormToSource";
  public static final String FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM = "::fragmentViewSwitchSourceToForm";
  public static final String SOURCE_CONTENT_HEADER = "::sourceContentHeader";
  public static final String SOURCE_CONTENT = "::sourceContent";
  public static final String SOURCE_CONTENT_NAMESPACE = "::sourceContentNS";

  // Action input constants
  public static final String NAME_ANCHOR = "nameAnchor";

  // Namespaces contants
  public static final String URI_XSD = "http://www.w3.org/2001/XMLSchema";
  public static final String URI_SOAP = "http://schemas.xmlsoap.org/soap/encoding/";
  public static final String URI_SOAP_ENV = "http://schemas.xmlsoap.org/soap/envelope/";
  public static final String URI_WSDL = "http://schemas.xmlsoap.org/wsdl/";
  public static final String URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String SOAP_ENC_ARRAY_TYPE = "arrayType";
  public static final String XSI_TYPE = "type";
  public static final String QNAME_PREFIX = "q";
  public static final String QNAME_LOCAL_NAME_ARRAY_TYPE = "arrayType";
  public static final String QNAME_LOCAL_NAME_ARRAY = "Array";
  public static final String COLON = ":";
  public static final String QNAME_LOCAL_NAME_HEADER = "Header";  
  public static final String QNAME_LOCAL_NAME_BODY = "Body";
  public static final String QNAME_LOCAL_NAME_FAULT = "Fault";

  // List
  public static final String LIST_SEPERATOR = " ";

  // SOAP-ENC:Array
  public static final String LEFT_SQUARE_BRACKET = "[";
  public static final String RIGHT_SQUARE_BRACKET = "]";

  // XSDDefaultFragment
  public static final String ROOT_ELEMENT_START_TAG = "<root>";
  public static final String ROOT_ELEMENT_END_TAG = "</root>";

  // Namespace URIs.
  public static final String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";
  public static final String NS_URI_SOAP_ENV = "http://schemas.xmlsoap.org/soap/envelope/";
  public static final String NS_URI_SOAP_ENC = "http://schemas.xmlsoap.org/soap/encoding/";
  public static final String NS_URI_1999_SCHEMA_XSI = "http://www.w3.org/1999/XMLSchema-instance";
  public static final String NS_URI_1999_SCHEMA_XSD = "http://www.w3.org/1999/XMLSchema";
  public static final String NS_URI_2000_SCHEMA_XSI = "http://www.w3.org/2000/10/XMLSchema-instance";
  public static final String NS_URI_2000_SCHEMA_XSD = "http://www.w3.org/2000/10/XMLSchema";
  public static final String NS_URI_2001_SCHEMA_XSI = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String NS_URI_2001_SCHEMA_XSD = "http://www.w3.org/2001/XMLSchema";
  public static final String NS_URI_CURRENT_SCHEMA_XSI = NS_URI_2001_SCHEMA_XSI;
  public static final String NS_URI_CURRENT_SCHEMA_XSD = NS_URI_2001_SCHEMA_XSD;
  public static final String NS_URI_XML_SOAP = "http://xml.apache.org/xml-soap";
  public static final String NS_URI_XML_SOAP_DEPLOYMENT = "http://xml.apache.org/xml-soap/deployment";
  public static final String NS_URI_LITERAL_XML = "http://xml.apache.org/xml-soap/literalxml";
  public static final String NS_URI_XMI_ENC = "http://www.ibm.com/namespaces/xmi";
}