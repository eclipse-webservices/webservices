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

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.OptionVector;

public class XSDSimpleAtomicEnumRangeFragment extends XSDSimpleAtomicEnumFragment {
  public XSDSimpleAtomicEnumRangeFragment(String id, String name, XSDToFragmentConfiguration config, EList elist) {
    super(id, name, config, elist);
  }
  
  public XSDSimpleAtomicEnumRangeFragment(String id, String name, XSDToFragmentConfiguration config, OptionVector v)
  {
    super(id, name, config, v);
  }

  public String getInformationFragment() {
    return "/wsdl/fragment/XSDSimpleAtomicInfoFragmentJSP.jsp";
  }

  public String getReadFragment() {
    return "/wsdl/fragment/XSDSimpleAtomicRFragmentJSP.jsp";
  }

  public String getWriteFragment() {
    return "/wsdl/fragment/XSDSimpleAtomicEnumRangeWFragmentJSP.jsp";
  }
}
