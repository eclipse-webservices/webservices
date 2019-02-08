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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.FormAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIMainNode;

public abstract class UDDIPropertiesFormAction extends FormAction
{
  protected NodeManager navigatorManager_;
  public UDDIPropertiesFormAction(Controller controller)
  {
    super(controller);
    navigatorManager_ = controller.getUDDIPerspective().getNavigatorManager();
  }

  public final Node getSelectedNavigatorNode()
  {
    return navigatorManager_.getSelectedNode();
  }

  public final FormTool getSelectedFormTool()
  {
    return (FormTool)(getSelectedNavigatorNode().getCurrentToolManager().getSelectedTool());
  }

  public final RegistryNode getRegistryNode()
  {
    return getRegistryNode(getSelectedNavigatorNode());
  }

  public final RegistryNode getRegistryNode(Node registryChildNode)
  {
    UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager_.getRootNode();
    return uddiMainNode.getRegistryNode(registryChildNode);
  }
}
