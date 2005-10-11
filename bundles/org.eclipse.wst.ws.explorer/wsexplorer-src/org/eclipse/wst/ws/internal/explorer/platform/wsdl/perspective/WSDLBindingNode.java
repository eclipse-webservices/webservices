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

// WSDL binding node for the WSDL Navigator Tree View
public class WSDLBindingNode extends WSDLNavigatorNode
{
  public WSDLBindingNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"wsdl/images/wsdl_binding_node.gif");
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(WSDLModelConstants.REL_WSDL_OPERATION)) {
          WSDLOperationNode wsdlOperationNode = new WSDLOperationNode((TreeElement)event.getParentElement(), nodeManager_, nodeDepth_ + 1);
          addChild(wsdlOperationNode);
        }
      }

      public void relRemoved(RelRemoveEvent event)
      {
        TreeElement childElement = null;
        if (event.getInBoundRelName().equals(WSDLModelConstants.REL_WSDL_OPERATION)) {
          childElement = (TreeElement)event.getInboundElement();
        }
        if (event.getOutBoundRelName().equals(WSDLModelConstants.REL_WSDL_OPERATION)) {
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
    new WSDLBindingDetailsTool(toolManager_, wsdlPerspective.getMessage("ALT_WSDL_BINDING_DETAILS"));
  }

}
