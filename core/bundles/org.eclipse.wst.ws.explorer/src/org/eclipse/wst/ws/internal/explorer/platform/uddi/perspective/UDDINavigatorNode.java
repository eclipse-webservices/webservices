/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectNavigatorNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.ToggleNavigatorNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public abstract class UDDINavigatorNode extends Node
{
  public UDDINavigatorNode(TreeElement element,NodeManager nodeManager,int nodeDepth,String imagePath)
  {
    super(element,nodeManager,nodeDepth,imagePath);
  }

  // uddi/actions/ToggleNavigatorNodeAction.jsp?nodeId=...
  public final String getToggleNodeActionHref()
  {
    return ToggleNavigatorNodeAction.getActionLink(nodeId_,isOpen_);
  }

  // uddi/actions/SelectNavigatorNodeAction.jsp?nodeId=...
  public final String getLinkActionHref()
  {
    return SelectNavigatorNodeAction.getActionLink(nodeId_,false);
  }

  public void addAuthenticationProperties(RegistryElement regElement)
  {
  }
}
