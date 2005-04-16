/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.wst.ws.internal.parser.wsil;

import java.util.ArrayList;
import java.util.List;

public class WebServiceEntity
{
  public static final int TYPE_UNKNOWN = -1;
  public static final int TYPE_HTML = 0;
  public static final int TYPE_WSIL = 1;
  public static final int TYPE_WSDL = 2;
  public static final int TYPE_UDDI_SERVICE = 3;
  public static final int TYPE_DISCO = 4;

  private Object parent_;
  private List children_;
  private int type_;
  private String uri_;
  private byte[] bytes_;
  private String httpUsername_;
  private String httpPassword_;
  private String documentation_;
  private Object model_;

  public WebServiceEntity()
  {
    parent_ = null;
    children_ = new ArrayList();
    type_ = TYPE_UNKNOWN;
    uri_ = null;
    bytes_ = null;
    httpUsername_ = null;
    httpPassword_ = null;
    documentation_ = null;
    model_ = null;
  }

  public Object getParent()
  {
    return parent_;
  }

  public void setParent(Object parent)
  {
    parent_ = parent;
  }

  public List getChildren()
  {
    return children_;
  }

  public void addChild(Object child)
  {
    children_.add(child);
  }

  public void removeChild(Object child)
  {
    children_.remove(child);
  }

  public int getType()
  {
    return type_;
  }

  public void setType(int type)
  {
    type_ = type;
  }

  public String getURI()
  {
    return uri_;
  }

  public void setURI(String uri)
  {
    uri_ = uri;
  }

  public byte[] getBytes()
  {
    return bytes_;
  }

  public void setBytes(byte[] bytes)
  {
    bytes_ = bytes;
  }

  public String getHTTPUsername()
  {
    return httpUsername_;
  }

  public void setHTTPUsername(String httpUsername)
  {
    httpUsername_ = httpUsername;
  }

  public String getHTTPPassword()
  {
    return httpPassword_;
  }

  public void setHTTPPassword(String httpPassword)
  {
    httpPassword_ = httpPassword;
  }
  
  public String getDocumentation()
  {
    return documentation_;
  }
  
  public void setDocumentation(String documentation)
  {
    documentation_ = documentation;
  }
  
  public Object getModel()
  {
    return model_;
  }
  
  public void setModel(Object model)
  {
    model_ = model;
  }

  public boolean isEntityResolved()
  {
    return (bytes_ != null);
  }
}