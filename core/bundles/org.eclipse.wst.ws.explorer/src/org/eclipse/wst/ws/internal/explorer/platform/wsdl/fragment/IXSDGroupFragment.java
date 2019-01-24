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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.xsd.XSDModelGroup;

public interface IXSDGroupFragment extends IXSDMapFragment {
  public void setXSDModelGroup(XSDModelGroup xsdModelGroup);
  public XSDModelGroup getXSDModelGroup();

  public String[] getGroupIDs();
  public String[] getGroupMemberIDs(String groupID);
  public IXSDFragment[] getGroupMemberFragments(String groupID);
}
