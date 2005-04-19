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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.xsd.XSDComponent;

public class XSDToFragmentConfiguration {
  private XSDComponent component_;
  private int minOccurs_;
  private int maxOccurs_;
  private int style_;
  private int partEncoding_;
  private boolean isWSDLPart_;
  private String wsdlPartName_;

  public XSDToFragmentConfiguration() {
    component_ = null;
    minOccurs_ = FragmentConstants.DEFAULT_MIN_OCCURS;
    maxOccurs_ = FragmentConstants.DEFAULT_MAX_OCCURS;
    style_ = FragmentConstants.STYLE_DOCUMENT;
    partEncoding_ = FragmentConstants.ENCODING_LITERAL;
    isWSDLPart_ = false;
    wsdlPartName_ = null;
  }

  public void setXSDComponent(XSDComponent component) {
    component_ = component;
  }

  public XSDComponent getXSDComponent() {
    return component_;
  }

  public void setMinOccurs(int minOccurs) {
    minOccurs_ = minOccurs;
  }

  public int getMinOccurs() {
    return minOccurs_;
  }

  public void setMaxOccurs(int maxOccurs) {
    maxOccurs_ = maxOccurs;
  }

  public int getMaxOccurs() {
    return maxOccurs_;
  }
  
  public void setStyle(int style) {
    style_ = style;
  }
  
  public int getStyle() {
    return style_;
  }

  public void setPartEncoding(int partEncoding) {
    partEncoding_ = partEncoding;
  }

  public int getPartEncoding() {
    return partEncoding_;
  }

  public void setIsWSDLPart(boolean isWSDLPart) {
    isWSDLPart_ = isWSDLPart;
  }

  public boolean getIsWSDLPart() {
    return isWSDLPart_;
  }
  
  public void setWSDLPartName(String wsdlPartName) {
    wsdlPartName_ = wsdlPartName;
  }
  
  public String getWSDLPartName() {
    return wsdlPartName_;
  }
}
