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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;

public class XSDSimpleAtomicFixFragment extends XSDSimpleAtomicFragment {
  public XSDSimpleAtomicFixFragment(String id, String name, XSDToFragmentConfiguration config) {
    super(id, name, config);
  }

  public String getInformationFragment() {
    return "/wsdl/fragment/XSDSimpleAtomicInfoFragmentJSP.jsp";
  }

  public String getReadFragment() {
    return "/wsdl/fragment/XSDSimpleAtomicRFragmentJSP.jsp";
  }

  public String getWriteFragment() {
    return "/wsdl/fragment/XSDSimpleAtomicFixWFragmentJSP.jsp";
  }
}
