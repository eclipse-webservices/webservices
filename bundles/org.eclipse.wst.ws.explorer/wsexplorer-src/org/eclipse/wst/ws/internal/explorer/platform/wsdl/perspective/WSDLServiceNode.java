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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;

// WSDL service node for the WSDL Navigator Tree View
public class WSDLServiceNode extends WSDLNavigatorNode
{
  public WSDLServiceNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"wsdl/images/wsdl_service_node.gif");
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(WSDLModelConstants.REL_WSDL_BINDING)) {
          WSDLBindingNode wsdlBindingNode = new WSDLBindingNode((TreeElement)event.getParentElement(), nodeManager_, nodeDepth_ + 1);
          addChild(wsdlBindingNode);
        }
      }

      public void relRemoved(RelRemoveEvent event)
      {
        TreeElement childElement = null;
        if (event.getInBoundRelName().equals(WSDLModelConstants.REL_WSDL_BINDING)) {
          childElement = (TreeElement)event.getInboundElement();
        }
        if (event.getOutBoundRelName().equals(WSDLModelConstants.REL_WSDL_BINDING)) {
          childElement = (TreeElement)event.getOutBoundElement();
        }
        removeChildNode(childElement);
      }
    });
    setVisibilityOfChildren(false);
  }

  protected final void initTools()
  {
    Controller controller = nodeManager_.getController();
    WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
    new WSDLServiceDetailsTool(toolManager_, wsdlPerspective.getMessage("ALT_WSDL_SERVICE_DETAILS"));
  }

}
