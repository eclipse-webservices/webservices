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

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.OptionVector;
import org.eclipse.xsd.XSDEnumerationFacet;

public abstract class XSDSimpleAtomicEnumFragment extends XSDSimpleAtomicFragment {
  private OptionVector optionVector_;
  public XSDSimpleAtomicEnumFragment(String id, String name, XSDToFragmentConfiguration config, EList elist) {
    super(id, name, config);
    optionVector_ = new OptionVector();
    for (int i=0;i<elist.size();i++)
    {
      String enumValue = ((XSDEnumerationFacet)elist.get(i)).getLexicalValue();
      optionVector_.addOption(enumValue,enumValue);
    }
  }
  
  public XSDSimpleAtomicEnumFragment(String id, String name, XSDToFragmentConfiguration config, OptionVector v)
  {
    super(id, name, config);
    optionVector_ = new OptionVector();
    for (int i=0;i<v.size();i++)
      optionVector_.addOption(v.getDisplayValue(i),v.getValue(i));
  }
  
  public final OptionVector getOptionVector()
  {
    return optionVector_;
  }
}
