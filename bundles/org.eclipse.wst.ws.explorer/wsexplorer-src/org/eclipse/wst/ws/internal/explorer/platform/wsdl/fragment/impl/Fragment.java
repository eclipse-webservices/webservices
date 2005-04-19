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

import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IFragment;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public abstract class Fragment implements IFragment {
  private String id_;
  private String name_;
  private Hashtable params_;

  public Fragment(String id, String name) {
    id_ = id;
    name_ = name;
    params_ = new Hashtable();
  }

  public void setID(String id) {
    id_ = id;
  }

  public String getID() {
    return id_;
  }

  public void setName(String name) {
    name_ = name;
  }

  public String getName() {
    return name_;
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
    String[] params = parser.getParameterValues(getID());
    setParameterValues(getID(), params);
    return validateAllParameterValues();
  }

  public void setParameterValues(String paramKey, String[] params) {
    if (params == null)
      params_.remove(paramKey);
    else
      params_.put(paramKey, params);
  }

  public void setParameterValues(String paramKey, Vector params) {
    if (params == null)
      params_.remove(paramKey);
    else {
      String[] paramsArray = new String[params.size()];
      for (int i = 0; i < paramsArray.length; i++) {
        paramsArray[i] = params.get(i).toString();
      }
      setParameterValues(paramKey, paramsArray);
    }
  }

  public String[] getParameterValues(String paramKey) {
    return (String[])params_.get(paramKey);
  }

  public String getParameterValue(String paramKey, int paramIndex) {
    String[] params = getParameterValues(paramKey);
    if (params != null && paramIndex < params.length)
      return params[paramIndex];
    else
      return null;
  }

  public boolean validateAllParameterValues() {
    Enumeration paramKeys = params_.keys();
    while (paramKeys != null && paramKeys.hasMoreElements()) {
      if (!validateParameterValues((String)paramKeys.nextElement()))
        return false;
    }
    return true;
  }

  public boolean validateParameterValues(String paramKey) {
    String[] params = getParameterValues(paramKey);
    for (int i = 0; params != null && i < params.length; i++) {
      if (!validateParameterValue(paramKey, i))
        return false;
    }
    return true;
  }

}
