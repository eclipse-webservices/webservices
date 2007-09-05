/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDEmptyFragment extends XSDFragment {
  private int occurance_;

  public XSDEmptyFragment(String id, String name, XSDToFragmentConfiguration config) {
    super(id, name, config);
    occurance_ = 0;
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
    String occurance = parser.getParameter(getID());
    try {
      occurance_ = Integer.parseInt(occurance);
      return validateAllParameterValues();
    }
    catch (Throwable t) {
      occurance_ = -1;
      return false;
    }
  }

  public void setParameterValues(String paramKey, String[] params) {
  }

  public void setParameterValues(String paramKey, Vector params) {
  }

  public String[] getParameterValues(String paramKey) {
    if (occurance_ < 0)
      return null;
    else {
      String[] params = new String[occurance_];
      for (int i = 0; i < params.length; i++) {
        params[i] = "";
      }
      return params;
    }
  }

  public String getParameterValue(String paramKey, int paramIndex) {
    if (paramIndex >= 0 && paramIndex < occurance_)
      return "";
    else
      return null;
  }

  public boolean validateAllParameterValues() {
    XSDToFragmentConfiguration xsdConfig = getXSDToFragmentConfiguration();
    int min = xsdConfig.getMinOccurs();
    int max = xsdConfig.getMaxOccurs();
    return (occurance_ >= 0 && occurance_ >= min && (max == FragmentConstants.UNBOUNDED || occurance_ <= max));
  }

  public boolean validateParameterValues(String paramKey) {
    return validateAllParameterValues();
  }

  public boolean validateParameterValue(String paramKey, int paramIndex) {
    return validateAllParameterValues();
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments) {
    Element[] instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocuments, getName());
    boolean paramsValid = true;
    for (int i = 0; i < instanceDocumentsCopy.length; i++) {
      NodeList nodeList = instanceDocumentsCopy[i].getChildNodes();
      if (nodeList.getLength() > 1)
        paramsValid = false;
      else if (nodeList.getLength() != 0) {
        Node node = nodeList.item(0);
        if (node.getNodeType() != Node.TEXT_NODE || !isWhitespace(node.getNodeValue()))
          paramsValid = false;
      }
    }
    occurance_ = instanceDocumentsCopy.length;
    return paramsValid && validateAllParameterValues();
  }

  private boolean isWhitespace(String s) {
    char[] chars = s.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if (!Character.isWhitespace(chars[i]) || !Character.isSpaceChar(chars[i]))
        return false;
    }
    return true;
  }

  public String getInformationFragment() {
    return "/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp";
  }

  public String getWriteFragment() {
    return "/wsdl/fragment/XSDEmptyWFragmentJSP.jsp";
  }

  public String getReadFragment() {
    return "/wsdl/fragment/XSDEmptyRFragmentJSP.jsp";
  }
}
