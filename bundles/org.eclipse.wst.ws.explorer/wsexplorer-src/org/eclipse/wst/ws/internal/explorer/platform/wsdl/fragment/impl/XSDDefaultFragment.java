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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDDefaultFragment extends XSDFragment {
  public XSDDefaultFragment(String id, String name, XSDToFragmentConfiguration config) {
    super(id, name, config);
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
    String[] param = new String[1];
    param[0] = parser.getParameter(getID());
    setParameterValues(getID(), param);
    return isElement(addRootElement(param[0]));
  }

  private String addRootElement(String element) {
    StringBuffer sb = new StringBuffer();
    sb.append(FragmentConstants.ROOT_ELEMENT_START_TAG);
    sb.append(element);
    sb.append(FragmentConstants.ROOT_ELEMENT_END_TAG);
    return sb.toString();
  }

  private boolean isElement(String elementString) {
    try {
      return (XMLUtils.stringToElement(elementString) != null);
    }
    catch (Throwable t) {
      return false;
    }
  }

  public boolean validateAllParameterValues() {
    String param = getParameterValue(getID(), 0);
    return (param == null || isElement(addRootElement(param)));
  }

  public boolean validateParameterValues(String paramKey) {
    String param = getParameterValue(getID(), 0);
    return (param == null || isElement(addRootElement(param)));
  }

  public boolean validateParameterValue(String paramKey, int paramIndex) {
    String param = getParameterValue(getID(), 0);
    return (param == null || isElement(addRootElement(param)));
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments) {
    StringBuffer sb = new StringBuffer();
    boolean paramValid = true;
    for (int i = 0; i < instanceDocuments.length; i++) {
      String instanceString = XMLUtils.serialize(instanceDocuments[i], true);
      if (instanceString != null && instanceString.length() > 0)
        sb.append(instanceString);
      else
        paramValid = false;
    }
    String[] param = {sb.toString()};
    setParameterValues(getID(), param);
    return paramValid;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc) {
    String param = getParameterValue(getID(), 0);
    if (param == null)
      return new Element[0];
    Element root;
    try {
      root = (Element)doc.importNode(XMLUtils.stringToElement(addRootElement(param)), true);
    }
    catch (Throwable t) {
      return new Element[0];
    }
    NodeList nl = root.getChildNodes();
    Vector instanceDocumentVector = new Vector();
    for (int i = 0; i < nl.getLength(); i++) {
      Node node = nl.item(i);
      if (node instanceof Element)
        instanceDocumentVector.add(node);
    }
    Element[] instanceDocuments = new Element[instanceDocumentVector.size()];
    for (int i = 0; i < instanceDocuments.length; i++) {
      instanceDocuments[i] = (Element)instanceDocumentVector.get(i);
    }
    return instanceDocuments;
  }

  public String getInformationFragment() {
    return "/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp";
  }

  public String getReadFragment() {
    return "/wsdl/fragment/XSDDefaultRFragmentJSP.jsp";
  }

  public String getWriteFragment() {
    return "/wsdl/fragment/XSDDefaultWFragmentJSP.jsp";
  }
}
