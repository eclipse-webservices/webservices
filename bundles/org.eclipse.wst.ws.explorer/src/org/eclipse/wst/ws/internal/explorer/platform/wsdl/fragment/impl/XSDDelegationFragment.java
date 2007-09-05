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

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDDelegationFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XSDDelegationFragment extends XSDFragment implements IXSDDelegationFragment
{
  private IXSDFragment xsdFragment_;

  public XSDDelegationFragment(String id, String name, XSDToFragmentConfiguration config)
  {
    super(id, name, config);
    xsdFragment_ = null;
  }

  public void setName(String name) {
    super.setName(name);
    if (xsdFragment_ != null)
      xsdFragment_.setName(name);
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    return xsdFragment_.processParameterValues(parser);
  }

  public void setParameterValues(String paramKey, String[] params)
  {
    xsdFragment_.setParameterValues(paramKey, params);
  }

  public void setParameterValues(String paramKey, Vector params)
  {
    xsdFragment_.setParameterValues(paramKey, params);
  }

  public String[] getParameterValues(String paramKey)
  {
    return xsdFragment_.getParameterValues(paramKey);
  }

  public String getParameterValue(String paramKey, int paramIndex)
  {
    return xsdFragment_.getParameterValue(paramKey, paramIndex);
  }

  public boolean validateAllParameterValues()
  {
    return xsdFragment_.validateAllParameterValues();
  }

  public boolean validateParameterValues(String paramKey)
  {
    return xsdFragment_.validateParameterValues(paramKey);
  }

  public boolean validateParameterValue(String paramKey, int paramIndex)
  {
    return xsdFragment_.validateParameterValue(paramKey, paramIndex);
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments)
  {
    return xsdFragment_.setParameterValuesFromInstanceDocuments(instanceDocuments);
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc)
  {
    return xsdFragment_.genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
  }

  public String getInformationFragment()
  {
    return "/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp";
  }

  public String getReadFragment()
  {
    return "/wsdl/fragment/XSDDelegationRFragmentJSP.jsp";
  }

  public String getWriteFragment()
  {
    return "/wsdl/fragment/XSDDelegationWFragmentJSP.jsp";
  }

  public void setXSDDelegationFragment(IXSDFragment xsdFragment)
  {
    xsdFragment_ = xsdFragment;
  }

  public IXSDFragment getXSDDelegationFragment()
  {
    return xsdFragment_;
  }
}
