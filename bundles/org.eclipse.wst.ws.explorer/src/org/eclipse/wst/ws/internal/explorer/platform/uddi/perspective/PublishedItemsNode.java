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

import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.PublishedItemsElement;

public class PublishedItemsNode extends UDDINavigatorFolderNode
{
  public PublishedItemsNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth);
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(UDDIModelConstants.REL_PUBLISHED_ITEMS))
        {
          TreeElement treeElement = (TreeElement)event.getParentElement();
          createChildNode(treeElement);
        }
      }

      public void relRemoved(RelRemoveEvent event)
      {
        TreeElement treeElement = null;
        if (event.getInBoundRelName().equals(UDDIModelConstants.REL_PUBLISHED_ITEMS))
          treeElement = (TreeElement)event.getInboundElement();
        else if (event.getOutBoundRelName().equals(UDDIModelConstants.REL_PUBLISHED_ITEMS))
          treeElement = (TreeElement)event.getOutBoundElement();

        if (treeElement != null)
          removeChildNode(treeElement);
      }
    });
  }

  private final void createChildNode(TreeElement element)
  {
    PublishedItemsElement publishedItemsElement = (PublishedItemsElement)element_;
    Node childNode = null;
    switch (publishedItemsElement.getType())
    {
      case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
        childNode = new BusinessNode(element,nodeManager_,nodeDepth_+1);
        break;
      case UDDIActionInputs.QUERY_ITEM_SERVICES:
        childNode = new ServiceNode(element,nodeManager_,nodeDepth_+1);
        break;
      case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
        childNode = new ServiceInterfaceNode(element,nodeManager_,nodeDepth_+1);
    }
    if (childNode != null)
      addChild(childNode);
  }
  
  protected final void initTools()
  {
    PublishedItemsElement publishedItemsElement = (PublishedItemsElement)element_;
    UDDIPerspective uddiPerspective = nodeManager_.getController().getUDDIPerspective();
    int publishedItemsType = publishedItemsElement.getType();
    switch (publishedItemsType)
    {
      case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
        new ItemsSummaryTool(toolManager_,"uddi/images/businesses_closed_enabled.gif","uddi/images/businesses_closed_highlighted.gif",uddiPerspective.getMessage("ALT_SUMMARY_BUSINESSES"),publishedItemsType);
        break;
      case UDDIActionInputs.QUERY_ITEM_SERVICES:
        new ItemsSummaryTool(toolManager_,"uddi/images/services_closed_enabled.gif","uddi/images/services_closed_highlighted.gif",uddiPerspective.getMessage("ALT_SUMMARY_SERVICES"),publishedItemsType);
        break;
      case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
      default:
        new ItemsSummaryTool(toolManager_,"uddi/images/service_interfaces_closed_enabled.gif","uddi/images/service_interfaces_closed_highlighted.gif",uddiPerspective.getMessage("ALT_SUMMARY_SERVICE_INTERFACES"),publishedItemsType);
    }
  }
  
  public String getOpenImagePath()
  {
    PublishedItemsElement publishedItemsElement = (PublishedItemsElement)element_;
    int publishedItemsType = publishedItemsElement.getType();
    switch (publishedItemsType)
    {
      case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
        return "uddi/images/businesses_open_highlighted.gif";
      case UDDIActionInputs.QUERY_ITEM_SERVICES:
        return "uddi/images/services_open_highlighted.gif";
      case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
        return "uddi/images/service_interfaces_open_highlighted.gif";
    }
    return super.getOpenImagePath();
  }
  
  public String getClosedImagePath()
  {
    PublishedItemsElement publishedItemsElement = (PublishedItemsElement)element_;
    int publishedItemsType = publishedItemsElement.getType();
    switch (publishedItemsType)
    {
      case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
        return "uddi/images/businesses_closed_highlighted.gif";
      case UDDIActionInputs.QUERY_ITEM_SERVICES:
        return "uddi/images/services_closed_highlighted.gif";
      case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
        return "uddi/images/service_interfaces_closed_highlighted.gif";
    }
    return super.getClosedImagePath();
  }
}
