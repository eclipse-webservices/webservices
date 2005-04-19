/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;

import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

import javax.servlet.http.*;

import java.util.*;

public class SelectSubQueryItemAction extends SelectPropertiesToolAction
{
  private boolean requiresTreeViewRefresh_;
  private boolean requiresViewSelection_;
  private boolean newUUIDQueryCreated_;
  private String itemName_;
  public SelectSubQueryItemAction(Controller controller)
  {
    super(controller);
    requiresTreeViewRefresh_ = false;
    requiresViewSelection_ = false;
    newUUIDQueryCreated_ = false;
    itemName_ = null;
  }

  // /uddi/actions/SelectSubQueryItemAction.jsp?nodeId=...&toolId=...<&viewId=...>&subQueryKey=...&subQueryListKey=...&subQueryListItemId=...&queryItem=...<&isHistory=1>
  public static final String getActionLink(int nodeId,int toolId,int viewId,String subQueryKey,String subQueryListKey,int subQueryListItemId,int queryItem,boolean forHistory)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/SelectSubQueryItemActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    actionLink.append('&').append(ActionInputs.TOOLID).append('=').append(toolId);
    if (viewId != ActionInputs.VIEWID_DEFAULT)
      actionLink.append('&').append(ActionInputs.VIEWID).append('=').append(viewId);
    actionLink.append('&').append(UDDIActionInputs.SUBQUERY_KEY).append('=').append(subQueryKey);
    actionLink.append('&').append(UDDIActionInputs.SUBQUERY_LIST_KEY).append('=').append(subQueryListKey);
    actionLink.append('&').append(UDDIActionInputs.SUBQUERY_LIST_ITEMID).append('=').append(subQueryListItemId);
    actionLink.append('&').append(UDDIActionInputs.QUERY_ITEM).append('=').append(queryItem);

    if (forHistory)
      actionLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
    return actionLink.toString();
  }

  public boolean processLinkParameters(HttpServletRequest request)
  {
    boolean result = super.processLinkParameters(request);
    String subQueryKey = request.getParameter(UDDIActionInputs.SUBQUERY_KEY);
    String subQueryListKey = request.getParameter(UDDIActionInputs.SUBQUERY_LIST_KEY);
    String subQueryListItemId = request.getParameter(UDDIActionInputs.SUBQUERY_LIST_ITEMID);
    String queryItemString = request.getParameter(UDDIActionInputs.QUERY_ITEM);

    if (subQueryKey != null)
      propertyTable_.put(UDDIActionInputs.SUBQUERY_KEY,subQueryKey);

    if (subQueryListKey != null)
      propertyTable_.put(UDDIActionInputs.SUBQUERY_LIST_KEY,subQueryListKey);

    if (subQueryListItemId != null)
      propertyTable_.put(UDDIActionInputs.SUBQUERY_LIST_ITEMID,subQueryListItemId);

    if (queryItemString != null)
      propertyTable_.put(UDDIActionInputs.QUERY_ITEM,queryItemString);
    return result;
  }

  private final boolean findByUUID()
  {
    Node currentNode = nodeManager_.getSelectedNode();
    FormTool formTool = (FormTool)(currentNode.getCurrentToolManager().getSelectedTool());
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties((String)propertyTable_.get(UDDIActionInputs.SUBQUERY_KEY));
    Vector list = (Vector)formToolPI.getProperty((String)propertyTable_.get(UDDIActionInputs.SUBQUERY_LIST_KEY));
    ListElement listElement = (ListElement)list.elementAt(Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.SUBQUERY_LIST_ITEMID)));
    int queryItem = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.QUERY_ITEM));
    FindAction findAction = null;
    String uuidKey = null;
    String uuidKeyValue = null;
    switch (queryItem)
    {
      case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
        findAction = new RegFindBusinessUUIDAction(controller_);
        uuidKey = UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY;
        BusinessEntity sp = (BusinessEntity)listElement.getObject();
        uuidKeyValue = sp.getBusinessKey();
        itemName_ = sp.getDefaultNameString();
        break;
      case UDDIActionInputs.QUERY_ITEM_SERVICES:
        findAction = new RegFindServiceUUIDAction(controller_);
        uuidKey = UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY;
        BusinessService bs = (BusinessService)listElement.getObject();
        uuidKeyValue = bs.getServiceKey();
        itemName_ = bs.getDefaultNameString();
        break;
      case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
      default:
        findAction = new RegFindServiceInterfaceUUIDAction(controller_);
        uuidKey = UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY;
        TModel tModel = (TModel)listElement.getObject();
        uuidKeyValue = tModel.getTModelKey();
        itemName_ = tModel.getNameString();
    }
    Hashtable propertyTable = findAction.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_NAME,uuidKeyValue);
    propertyTable.put(uuidKey,uuidKeyValue);
    boolean result = findAction.run();
    if (result)
    {
      Node itemNode = nodeManager_.getSelectedNode();
      int nodeId = itemNode.getNodeId();
      int toolId = itemNode.getToolManager().getSelectedToolId();
      int viewId = ActionInputs.VIEWID_DEFAULT;
      int viewToolId = ActionInputs.VIEWTOOLID_DEFAULT;
      propertyTable_.put(ActionInputs.NODEID,String.valueOf(nodeId));
      propertyTable_.put(ActionInputs.TOOLID,String.valueOf(toolId));
      propertyTable_.put(ActionInputs.VIEWID,String.valueOf(viewId));
      propertyTable_.put(ActionInputs.VIEWTOOLID,String.valueOf(viewToolId));
      super.run();
      requiresTreeViewRefresh_ = true;
      requiresViewSelection_ = true;
      newUUIDQueryCreated_ = true;
      listElement.setTargetViewToolInfo(nodeId,toolId,viewId);
      return true;
    }
    else
    {
      // The item is no longer in the registry.
      return false;
    }
  }

  public final String getItemName()
  {
    return itemName_;
  }
  
  public boolean run()
  {
    // Given the nodeId, toolId and viewId, check if the node or view is stale.
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    int toolId = Integer.parseInt((String)propertyTable_.get(ActionInputs.TOOLID));
    int viewId = ActionInputs.VIEWID_DEFAULT;
    try
    {
      viewId = Integer.parseInt((String)propertyTable_.get(ActionInputs.VIEWID));
    }
    catch (NumberFormatException e)
    {
    }
    int viewToolId = 0;
    if (!isStaleNode(nodeId))
    {
      // Enusre the node is visible.
      requiresTreeViewRefresh_ = makeNodeVisible(nodeManager_.getNode(nodeId));

      Node node = nodeManager_.getNode(nodeId);
      if (viewId != ActionInputs.VIEWID_DEFAULT)
      {
        ViewTool viewTool = (ViewTool)(node.getToolManager().getTool(toolId));
        ToolManager elementToolManager = viewTool.getToolManager(viewId);
        if (elementToolManager != null)
          viewToolId = elementToolManager.getSelectedToolId();
        else
        {
          // Stale view
          return findByUUID();
        }
      }
      propertyTable_.put(ActionInputs.VIEWTOOLID,String.valueOf(viewToolId));
      return super.run();
    }
    else
    {
      // Stale node
      return findByUUID();
    }
  }

  protected String getActionLinkForHistory()
  {
    int nodeId = selectedNode_.getNodeId();
    int toolId = selectedTool_.getToolId();
    int viewId = selectedNode_.getViewId();
    int viewToolId = selectedNode_.getViewToolId();
    return SelectPropertiesToolAction.getActionLink(nodeId,toolId,viewId,viewToolId,true);
  }

  public boolean requiresTreeViewRefresh()
  {
    return super.requiresTreeViewRefresh()|requiresTreeViewRefresh_;
  }

  public boolean requiresViewSelection()
  {
    return super.requiresViewSelection()|requiresViewSelection_;
  }

  public boolean requiresStatusUpdate()
  {
    return newUUIDQueryCreated_;
  }

  public final String getStatusContentVar()
  {
    return "statusContent";
  }

  public final String getStatusContentPage()
  {
    return "uddi/status_content.jsp";
  }
}
