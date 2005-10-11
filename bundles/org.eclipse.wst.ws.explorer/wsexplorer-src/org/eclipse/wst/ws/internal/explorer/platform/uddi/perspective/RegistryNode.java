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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import java.util.Vector;
import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.PublishedItemsElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.QueryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.QueryParentElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public class RegistryNode extends UDDINavigatorNode
{
  private RegistryDetailsTool regDetailsTool_;
  private RegPublishTool regPublishTool_;
  
  public static final String IMAGE_PATH_STANDARD = "uddi/images/registry.gif";
  public static final String IMAGE_PATH_WITH_USER_DEFINED_CATEGORIES = "uddi/images/regstrycat_obj.gif";
  
  public RegistryNode(TreeElement element,NodeManager nodeManager,int nodeDepth,String imagePath)
  {
    super(element,nodeManager,nodeDepth,imagePath);
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(UDDIModelConstants.REL_QUERIES_PARENT))
        {
          QueryParentElement queryParentElement = (QueryParentElement)event.getParentElement();
          createChildNode(queryParentElement);
        }
        else if (isPublishedItemsRel(rel))
        {
          PublishedItemsElement publishedItemsElement = (PublishedItemsElement)event.getParentElement();
          createChildNode(publishedItemsElement);
        }
      }
      public void relRemoved(RelRemoveEvent event)
      {
        TreeElement childElement = null;
        String inBoundRelName = event.getInBoundRelName();
        String outBoundRelName = event.getOutBoundRelName();
        if (inBoundRelName.equals(UDDIModelConstants.REL_QUERIES_PARENT) || isPublishedItemsRel(inBoundRelName))
          childElement = (TreeElement)event.getInboundElement();
        else if (outBoundRelName.equals(UDDIModelConstants.REL_QUERIES_PARENT) || isPublishedItemsRel(outBoundRelName))
          childElement = (TreeElement)event.getOutBoundElement();

        if (childElement != null)
          removeChildNode(childElement);
      }
    });
    UDDIPerspective uddiPerspective = nodeManager_.getController().getUDDIPerspective();
    element.connect(new QueryParentElement(uddiPerspective.getMessage("NODE_NAME_QUERIES_FOLDER"),element_.getModel()),UDDIModelConstants.REL_QUERIES_PARENT,ModelConstants.REL_OWNER);
    element.connect(new PublishedItemsElement(UDDIActionInputs.QUERY_ITEM_BUSINESSES,uddiPerspective.getMessage("NODE_NAME_PUBLISHED_BUSINESSES_FOLDER"),element_.getModel()),UDDIModelConstants.REL_PUBLISHED_BUSINESSES_PARENT,ModelConstants.REL_OWNER);
    element.connect(new PublishedItemsElement(UDDIActionInputs.QUERY_ITEM_SERVICES,uddiPerspective.getMessage("NODE_NAME_PUBLISHED_SERVICES_FOLDER"),element_.getModel()),UDDIModelConstants.REL_PUBLISHED_SERVICES_PARENT,ModelConstants.REL_OWNER);
    element.connect(new PublishedItemsElement(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES,uddiPerspective.getMessage("NODE_NAME_PUBLISHED_SERVICE_INTERFACES_FOLDER"),element_.getModel()),UDDIModelConstants.REL_PUBLISHED_SERVICE_INTERFACES_PARENT,ModelConstants.REL_OWNER);
    setVisibilityOfChildren(false);
  }

  private final boolean isPublishedItemsRel(String rel)
  {
    return rel.equals(UDDIModelConstants.REL_PUBLISHED_BUSINESSES_PARENT) || rel.equals(UDDIModelConstants.REL_PUBLISHED_SERVICES_PARENT) || rel.equals(UDDIModelConstants.REL_PUBLISHED_SERVICE_INTERFACES_PARENT);
  }

  private final void createChildNode(QueryParentElement queryParentElement)
  {
    QueryParentNode queryParentNode = new QueryParentNode(queryParentElement,nodeManager_,nodeDepth_+1);
    addChild(queryParentNode);
  }

  private final void createChildNode(PublishedItemsElement publishedItemsElement)
  {
    PublishedItemsNode publishedItemsNode = new PublishedItemsNode(publishedItemsElement,nodeManager_,nodeDepth_+1);
    addChild(publishedItemsNode);
  }

  protected final void initTools()
  {
    nodeManager_.getController();
    UDDIPerspective uddiPerspective = nodeManager_.getController().getUDDIPerspective();
    regDetailsTool_ = new RegistryDetailsTool(toolManager_ ,uddiPerspective.getMessage("ALT_REGISTRY_DETAILS"));
    RegFindTool regFindTool = new RegFindTool(toolManager_,uddiPerspective.getMessage("ALT_FIND"));
    regPublishTool_ = new RegPublishTool(toolManager_,uddiPerspective.getMessage("ALT_PUBLISH"));
    new AddToFavoritesTool(toolManager_,uddiPerspective.getMessage("ALT_ADD_TO_FAVORITES"));
    regFindTool.addAuthenticationProperties((RegistryElement)element_);
    regPublishTool_.addAuthenticationProperties((RegistryElement)element_);
  }
  
  public final RegistryDetailsTool getRegDetailsTool()
  {
    return regDetailsTool_;
  }

  public final RegPublishTool getRegPublishTool()
  {
    return regPublishTool_;
  }

  private final void addDiscoveredNodes(Vector allNodes,Node queryParentNode,int queryType)
  {
    Vector queryNodes = queryParentNode.getChildNodes();
    for (int i=0;i<queryNodes.size();i++)
    {
      QueryNode queryNode = (QueryNode)queryNodes.elementAt(i);
      QueryElement queryElement = (QueryElement)queryNode.getTreeElement();
      if (queryType == UDDIActionInputs.QUERY_ITEM_QUERIES)
        allNodes.addElement(queryNode);
      else if (queryElement.getQueryType() == queryType)
      {
        Vector discoveredNodes = queryNode.getChildNodes();
        for (int j=0;j<discoveredNodes.size();j++)
          allNodes.addElement(discoveredNodes.elementAt(j));
      }
    }
  }

  private final void addPublishedNodes(Vector allNodes,Node publishedItemsNode)
  {
    Vector itemNodes = publishedItemsNode.getChildNodes();
    for (int i=0;i<itemNodes.size();i++)
      allNodes.addElement(itemNodes.elementAt(i));
  }

  public final void getAllBusinessNodes(Vector allBusinessNodes)
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node queryParentNode = getChildNode(regElement.getQueryParentElement());
    Node publishedBusinessesNode = getChildNode(regElement.getPublishedBusinessesElement());
    addDiscoveredNodes(allBusinessNodes,queryParentNode,UDDIActionInputs.QUERY_ITEM_BUSINESSES);
    addPublishedNodes(allBusinessNodes,publishedBusinessesNode);
  }

  public final void getAllServiceNodes(Vector allServicesNodes)
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node queryParentNode = getChildNode(regElement.getQueryParentElement());
    Node publishedServicesNode = getChildNode(regElement.getPublishedServicesElement());
    addDiscoveredNodes(allServicesNodes,queryParentNode,UDDIActionInputs.QUERY_ITEM_SERVICES);
    addPublishedNodes(allServicesNodes,publishedServicesNode);
  }

  public final void getAllServiceInterfaceNodes(Vector allServiceInterfaceNodes)
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node queryParentNode = getChildNode(regElement.getQueryParentElement());
    Node publishedServiceInterfacesNode = getChildNode(regElement.getPublishedServiceInterfacesElement());
    addDiscoveredNodes(allServiceInterfaceNodes,queryParentNode,UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES);
    addPublishedNodes(allServiceInterfaceNodes,publishedServiceInterfacesNode);
  }

  public final void getPublishedBusinessNodes(Vector publishedBusinessNodes)
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node publishedBusinessesNode = getChildNode(regElement.getPublishedBusinessesElement());
    addPublishedNodes(publishedBusinessNodes,publishedBusinessesNode);
  }

  public final void getPublishedServiceNodes(Vector publishedServiceNodes)
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node publishedServicesNode = getChildNode(regElement.getPublishedServicesElement());
    addPublishedNodes(publishedServiceNodes,publishedServicesNode);
  }

  public final void getPublishedServiceInterfaceNodes(Vector publishedServiceInterfaceNodes)
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node publishedServiceInterfacesNode = getChildNode(regElement.getPublishedServiceInterfacesElement());
    addPublishedNodes(publishedServiceInterfaceNodes,publishedServiceInterfacesNode);
  }

  public final void getDiscoveredNodes(Vector discoveredNodes,int queryItem)
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node queryParentNode = getChildNode(regElement.getQueryParentElement());
    addDiscoveredNodes(discoveredNodes,queryParentNode,queryItem);
  }

  private final boolean hasQueryResults(Node queryParentNode,int queryType)
  {
    Vector queries = queryParentNode.getChildNodes();
    for (int i=0;i<queries.size();i++)
    {
      QueryNode queryNode = (QueryNode)queries.elementAt(i);
      if (queryNode.getChildNodes().size() > 0)
      {
        QueryElement queryElement = (QueryElement)queryNode.getTreeElement();
        if (queryElement.getQueryType() == queryType)
          return true;
      }
    }
    return false;
  }

  public final boolean hasBusiness()
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node publishedBusinessesNode = getChildNode(regElement.getPublishedBusinessesElement());
    if (publishedBusinessesNode.getChildNodes().size() > 0)
      return true;
    return hasQueryResults(getChildNode(regElement.getQueryParentElement()),UDDIActionInputs.QUERY_ITEM_BUSINESSES);
  }

  public final boolean hasService()
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node publishedServicesNode = getChildNode(regElement.getPublishedServicesElement());
    if (publishedServicesNode.getChildNodes().size() > 0)
      return true;
    return hasQueryResults(getChildNode(regElement.getQueryParentElement()),UDDIActionInputs.QUERY_ITEM_SERVICES);
  }

  public final boolean hasServiceInterface()
  {
    RegistryElement regElement = (RegistryElement)element_;
    Node publishedServiceInterfacesNode = getChildNode(regElement.getPublishedServiceInterfacesElement());
    if (publishedServiceInterfacesNode.getChildNodes().size() > 0)
      return true;
    return hasQueryResults(getChildNode(regElement.getQueryParentElement()),UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES);
  }
}
