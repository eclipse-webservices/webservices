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

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.UDDIMainElement;

// Root node for the UDDI Navigator Tree View
public class UDDIMainNode extends UDDINavigatorNode
{
  public UDDIMainNode(TreeElement element,NodeManager nodeManager)
  {
    super(element,nodeManager,1,"images/root_main.gif");
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(UDDIModelConstants.REL_REGISTRIES))
        {
          RegistryElement registryElement = (RegistryElement)event.getParentElement();
          UDDIMainElement uddiMainElement = (UDDIMainElement)element_;
          uddiMainElement.addRegistryName(registryElement.getName());
          createChildNode(registryElement);
        }
      }

      public void relRemoved(RelRemoveEvent event)
      {
        RegistryElement childElement = null;
        if (event.getInBoundRelName().equals(UDDIModelConstants.REL_REGISTRIES))
          childElement = (RegistryElement)event.getInboundElement();
        else if (event.getOutBoundRelName().equals(UDDIModelConstants.REL_REGISTRIES))
          childElement = (RegistryElement)event.getOutBoundElement();

        if (childElement != null)
        {
          UDDIMainElement uddiMainElement = (UDDIMainElement)element_;
          uddiMainElement.removeRegistryName(childElement.getName());
          removeChildNode(childElement);
        }
      }
    });
  }

  private final void createChildNode(TreeElement element)
  {
    RegistryElement regElement = (RegistryElement)element;
    Enumeration userDefinedCategories = regElement.getUserDefinedCategories();
    String imagePath;
    if (userDefinedCategories != null && userDefinedCategories.hasMoreElements())
      imagePath = RegistryNode.IMAGE_PATH_WITH_USER_DEFINED_CATEGORIES;
    else
      imagePath = RegistryNode.IMAGE_PATH_STANDARD;
    
    RegistryNode regNode = new RegistryNode(element,nodeManager_,nodeDepth_+1,imagePath);
    addChild(regNode);
  }

  protected final void initTools()
  {
    Controller controller = nodeManager_.getController();
    UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
    new OpenRegistryTool(toolManager_,uddiPerspective.getMessage("ALT_OPEN_REGISTRY"));
  }
  
  // Given a node under a particular registry node, obtain the registry node.
  public final RegistryNode getRegistryNode(Node currentNode)
  {
    RegistryNode regNode = null;
    if (currentNode instanceof RegistryNode)
    {
      regNode = (RegistryNode)currentNode;
    }
    else if (currentNode instanceof QueryNode)
    {
      // Query -> Query folder -> Registry
      regNode = (RegistryNode)(currentNode.getParent().getParent());
    }
    else if (currentNode instanceof QueryParentNode || currentNode instanceof PublishedItemsNode)
    {
      // Query folder -> Registry
      regNode = (RegistryNode)(currentNode.getParent());
    }
    else if (currentNode instanceof BusinessNode || currentNode instanceof ServiceNode || currentNode instanceof ServiceInterfaceNode)
    {
      // itemNode -> Published Items folder -> Registry
      // OR:
      // itemNode -> QueryNode -> Query folder -> Registry
      Node parentNode = currentNode.getParent();
      if (parentNode instanceof QueryNode)
        regNode = (RegistryNode)(parentNode.getParent().getParent());
      else
        regNode = (RegistryNode)parentNode.getParent();
    }
    return regNode;    
  }
}
