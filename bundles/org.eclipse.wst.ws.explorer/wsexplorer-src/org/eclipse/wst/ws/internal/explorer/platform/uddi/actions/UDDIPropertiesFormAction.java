/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;

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
