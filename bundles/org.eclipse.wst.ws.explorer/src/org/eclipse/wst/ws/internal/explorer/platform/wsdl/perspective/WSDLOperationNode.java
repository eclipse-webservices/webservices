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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

// WSDL operation node for the WSDL Navigator Tree View
public class WSDLOperationNode extends WSDLNavigatorNode
{
  public WSDLOperationNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"wsdl/images/wsdl_operation_node.gif");
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
      }

      public void relRemoved(RelRemoveEvent event)
      {
      }
    });
  }

  protected final void initTools()
  {
    Controller controller = nodeManager_.getController();
    WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
    new InvokeWSDLOperationTool(toolManager_, wsdlPerspective.getMessage("ALT_INVOKE_WSDL_OPERATION"));
  }

}
