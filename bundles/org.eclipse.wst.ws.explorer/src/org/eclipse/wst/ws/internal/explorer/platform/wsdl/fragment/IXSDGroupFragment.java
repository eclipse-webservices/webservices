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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.xsd.XSDModelGroup;

public interface IXSDGroupFragment extends IXSDMapFragment {
  public void setXSDModelGroup(XSDModelGroup xsdModelGroup);
  public XSDModelGroup getXSDModelGroup();

  public String[] getGroupIDs();
  public String[] getGroupMemberIDs(String groupID);
  public IXSDFragment[] getGroupMemberFragments(String groupID);
}
