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

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public interface IFragment {
  public void setID(String id);
  public String getID();

  public void setName(String name);
  public String getName();

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException;

  public void setParameterValues(String paramKey, String[] params);
    public void setParameterValues(String paramKey, Vector params);
  public String[] getParameterValues(String paramKey);
  public String getParameterValue(String paramKey, int paramIndex);

  public boolean validateAllParameterValues();
  public boolean validateParameterValues(String paramKey);
  public boolean validateParameterValue(String paramKey, int paramIndex);

  public String getInformationFragment();
  public String getWriteFragment();
  public String getReadFragment();
}
