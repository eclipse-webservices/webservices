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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDGroupSeqFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.xsd.XSDModelGroup;

public abstract class XSDGroupSeqFragment extends XSDGroupFragment implements IXSDGroupSeqFragment {
  public XSDGroupSeqFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller, XSDModelGroup xsdModelGroup) {
    super(id, name, config, controller, xsdModelGroup);
  }

  public String createGroupSeqInstance() {
    return createGroupInstance();
  }

  public String createInstance() {
    return createGroupSeqInstance();
  }
}
