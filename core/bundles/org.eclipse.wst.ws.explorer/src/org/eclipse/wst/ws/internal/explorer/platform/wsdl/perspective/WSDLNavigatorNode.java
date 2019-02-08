/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.SelectWSDLNavigatorNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.ToggleWSDLNavigatorNodeAction;

public abstract class WSDLNavigatorNode extends Node
{
  public WSDLNavigatorNode(TreeElement element,NodeManager nodeManager,int nodeDepth,String imagePath)
  {
    super(element,nodeManager,nodeDepth,imagePath);
  }

  // wsdl/actions/ToggleWSDLNavigatorNodeAction.jsp?nodeId=...
  public final String getToggleNodeActionHref()
  {
    return ToggleWSDLNavigatorNodeAction.getActionLink(nodeId_,isOpen_);
  }

  // wsdl/actions/SelectWSDLNavigatorNodeAction.jsp?nodeId=...
  public final String getLinkActionHref()
  {
    return SelectWSDLNavigatorNodeAction.getActionLink(nodeId_,false);
  } 
}
