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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;

public class XSDToFragmentController {
  private WSDLPartsToXSDTypeMapper wsdlToXSDMapper_;
  private XSDLiteralXMLEncodingToFragmentMapper xsdLiteralMapper_;
  private XSDSOAPEncodingToFragmentMapper xsdSoapMapper_;
  private Hashtable xsdToFragmentCache_;

  public XSDToFragmentController() {
    wsdlToXSDMapper_ = null;
    xsdLiteralMapper_ = null;
    xsdSoapMapper_ = null;
    xsdToFragmentCache_ = new Hashtable();
  }

  public void setWSDLPartsToXSDTypeMapper(WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    wsdlToXSDMapper_ = wsdlToXSDMapper;
  }

  public WSDLPartsToXSDTypeMapper getWSDLPartsToXSDTypeMapper() {
    return wsdlToXSDMapper_;
  }

  private XSDLiteralXMLEncodingToFragmentMapper getXSDLiteralMapper() {
    if (xsdLiteralMapper_ == null)
      xsdLiteralMapper_ = new XSDLiteralXMLEncodingToFragmentMapper(this, wsdlToXSDMapper_);
    return xsdLiteralMapper_;
  }

  private XSDSOAPEncodingToFragmentMapper getXSDSOAPMapper() {
    if (xsdSoapMapper_ == null)
      xsdSoapMapper_ = new XSDSOAPEncodingToFragmentMapper(this, wsdlToXSDMapper_);
    return xsdSoapMapper_;
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    IXSDFragment frag = getCachedFragment(id);
    if (frag != null)
      return frag;
    frag = getNewFragment(config, id, name);
    if (frag != null)
      addToCache(id, frag);
    return frag;
  }

  public IXSDFragment getCachedFragment(String id) {
    return (IXSDFragment)xsdToFragmentCache_.get(id);
  }

  public IXSDFragment getNewFragment(XSDToFragmentConfiguration config, String id, String name) {
    switch (config.getPartEncoding())
    {
      case FragmentConstants.ENCODING_SOAP:
        return getXSDSOAPMapper().getFragment(config,id,name);      
      case FragmentConstants.ENCODING_LITERAL:
      case FragmentConstants.ENCODING_URL:
      default:
        return getXSDLiteralMapper().getFragment(config,id,name);
    }
  }

  public void addToCache(String id, IXSDFragment frag) {
    xsdToFragmentCache_.put(id, frag);
  }

  public void removeFromCache(String id) {
    xsdToFragmentCache_.remove(id);
  }

  public void emptyCache() {
    xsdToFragmentCache_.clear();
  }
}
