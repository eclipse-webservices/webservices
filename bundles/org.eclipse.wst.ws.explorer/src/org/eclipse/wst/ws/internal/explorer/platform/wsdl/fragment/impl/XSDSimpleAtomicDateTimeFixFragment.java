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

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;

public class XSDSimpleAtomicDateTimeFixFragment extends XSDSimpleAtomicFixFragment {
  private int calendarType_;
  public XSDSimpleAtomicDateTimeFixFragment(String id, String name, XSDToFragmentConfiguration config, int calendarType) {
    super(id, name, config);
    calendarType_ = calendarType;
  }
  
  public String getWriteFragment() {
    StringBuffer fragmentLink = new StringBuffer("/wsdl/fragment/XSDSimpleAtomicDateTimeFixWFragmentJSP.jsp?");
    fragmentLink.append(ActionInputs.CALENDAR_TYPE).append('=').append(calendarType_);
    return fragmentLink.toString();
  }
}
