/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;

public class XSDSimpleAtomicDateTimeRangeFragment extends XSDSimpleAtomicRangeFragment {
  private int calendarType_;
  public XSDSimpleAtomicDateTimeRangeFragment(String id, String name, XSDToFragmentConfiguration config, int calendarType) {
    super(id, name, config);
    calendarType_ = calendarType;
  }

  public String getWriteFragment() {
    StringBuffer fragmentLink = new StringBuffer("/wsdl/fragment/XSDSimpleAtomicDateTimeRangeWFragmentJSP.jsp?");
    fragmentLink.append(ActionInputs.CALENDAR_TYPE).append('=').append(calendarType_);
    return fragmentLink.toString();
  }
}
