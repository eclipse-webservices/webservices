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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;

public class XSDSimpleListRangeFragment extends XSDSimpleListFragment {
  public XSDSimpleListRangeFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller) {
    super(id, name, config, controller);
  }

  public String getInformationFragment() {
    return "/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp";
  }

  public String getReadFragment() {
    return "/wsdl/fragment/XSDSimpleListRFragmentJSP.jsp";
  }

  public String getWriteFragment() {
    return "/wsdl/fragment/XSDSimpleListRangeWFragmentJSP.jsp";
  }

}
