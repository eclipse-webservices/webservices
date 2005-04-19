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

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;

public class QueryParentNode extends UDDINavigatorFolderNode
{
  public QueryParentNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth);
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(UDDIModelConstants.REL_QUERIES))
        {
          QueryElement queryElement = (QueryElement)event.getParentElement();
          createChildNode(queryElement);
        }
      }

      public void relRemoved(RelRemoveEvent event)
      {
        QueryElement queryElement = null;
        if (event.getInBoundRelName().equals(UDDIModelConstants.REL_QUERIES))
          queryElement = (QueryElement)event.getInboundElement();
        else if (event.getOutBoundRelName().equals(UDDIModelConstants.REL_QUERIES))
          queryElement = (QueryElement)event.getOutBoundElement();

        if (queryElement != null)
          removeChildNode(queryElement);
      }
    });
  }

  private final void createChildNode(QueryElement element)
  {
    QueryNode queryNode = new QueryNode(element,nodeManager_,nodeDepth_+1);
    // Add registry authentication properties.
    RegistryElement regElement = (RegistryElement)(getParent().getTreeElement());
    queryNode.addAuthenticationProperties(regElement);
    addChild(queryNode);
  }
  
  protected final void initTools()
  {
    UDDIPerspective uddiPerspective = nodeManager_.getController().getUDDIPerspective();
    new ItemsSummaryTool(toolManager_,"uddi/images/queries_closed_enabled.gif","uddi/images/queries_closed_highlighted.gif",uddiPerspective.getMessage("ALT_SUMMARY_QUERIES"),UDDIActionInputs.QUERY_ITEM_QUERIES);
    new ItemsSummaryTool(toolManager_,"uddi/images/businesses_closed_enabled.gif","uddi/images/businesses_closed_highlighted.gif",uddiPerspective.getMessage("ALT_SUMMARY_BUSINESSES"),UDDIActionInputs.QUERY_ITEM_BUSINESSES);
    new ItemsSummaryTool(toolManager_,"uddi/images/services_closed_enabled.gif","uddi/images/services_closed_highlighted.gif",uddiPerspective.getMessage("ALT_SUMMARY_SERVICES"),UDDIActionInputs.QUERY_ITEM_SERVICES);
    new ItemsSummaryTool(toolManager_,"uddi/images/service_interfaces_closed_enabled.gif","uddi/images/service_interfaces_closed_highlighted.gif",uddiPerspective.getMessage("ALT_SUMMARY_SERVICE_INTERFACES"),UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES);
  }
  
  public String getOpenImagePath()
  {
    return "uddi/images/queries_open_highlighted.gif";
  }
  
  public String getClosedImagePath()
  {
    return "uddi/images/queries_closed_highlighted.gif";
  }
}
