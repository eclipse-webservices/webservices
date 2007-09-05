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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.NodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIMainNode;

public abstract class UDDINodeAction extends NodeAction
{
  protected RegistryNode regNode_;
  public UDDINodeAction(Controller controller)
  {
    super(controller,controller.getUDDIPerspective().getNavigatorManager());
    regNode_ = getRegistryNode();
  }

  public final Node getSelectedNavigatorNode()
  {
    Node selectedNavigatorNode = nodeManager_.getSelectedNode();
    return selectedNavigatorNode;
  }

  public final RegistryNode getRegistryNode()
  {
    UDDIMainNode uddiMainNode = (UDDIMainNode)nodeManager_.getRootNode();
    return uddiMainNode.getRegistryNode(getSelectedNavigatorNode());
  }
}
