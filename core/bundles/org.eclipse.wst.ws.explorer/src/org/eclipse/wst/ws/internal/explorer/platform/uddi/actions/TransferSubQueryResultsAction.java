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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.MultipleFormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryTransferTarget;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class TransferSubQueryResultsAction extends UDDIPropertiesFormAction
{
  private Node targetNode_;
  public TransferSubQueryResultsAction(Controller controller)
  {
    super(controller);
    targetNode_ = null;
  }

  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String[] selectedNodeIds = parser.getParameterValues(ActionInputs.NODEID);
    // The client Javascript enforces the rule that at least one item was selected before the form can be submitted.
    propertyTable_.put(UDDIActionInputs.SELECTED_NODEIDS,selectedNodeIds);
    return true;
  }

  public static final String getActionLinkForReturn()
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/TransferSubQueryResultsActionJSP.jsp?");
    actionLink.append(UDDIActionInputs.SHOW_RESULTS_TARGET).append("=1");
    return actionLink.toString();
  }

  public final void enableShowResultsTarget()
  {
    // This should only be called if the user clicks on the target link on the query results page. Insert a zero element
    // array so that no results are transferred but the jump back to the correct query and its info occurs.
    propertyTable_.put(UDDIActionInputs.SELECTED_NODEIDS,new String[0]);
  }

  public final boolean run()
  {
    // Transfer the results in this Query node into the property expected by the originating node.
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    uddiPerspective.getMessageQueue();

    String[] selectedNodeIds = (String[])propertyTable_.get(UDDIActionInputs.SELECTED_NODEIDS);
    Node currentNode = getSelectedNavigatorNode();
    TreeElement currentElement = currentNode.getTreeElement();
    SubQueryTransferTarget target = (SubQueryTransferTarget)(currentElement.getPropertyAsObject(UDDIModelConstants.SUBQUERY_TRANSFER_TARGET));
    String subQueryKey = target.getSubQueryKey();
    String targetQueryKey;

    int lastSeparatorPos = subQueryKey.lastIndexOf(':');
    if (lastSeparatorPos == -1)
      targetQueryKey = "";
    else
      targetQueryKey = subQueryKey.substring(0,lastSeparatorPos);

    String targetProperty = subQueryKey.substring(lastSeparatorPos+1,subQueryKey.length());
    FormTool targetFormTool = target.getTargetFormTool();
    ((MultipleFormToolPropertiesInterface)targetFormTool).getFormToolProperties(subQueryKey);
    FormToolPropertiesInterface targetQueryFormToolPI = ((MultipleFormToolPropertiesInterface)targetFormTool).getFormToolProperties(targetQueryKey);
    Vector targetList = (Vector)targetQueryFormToolPI.getProperty(targetProperty);
    if (targetList == null)
      targetList = new Vector();

    NodeManager nodeManager = currentNode.getNodeManager();
    for (int i=0;i<selectedNodeIds.length;i++)
    {
      int selectedNodeId = Integer.parseInt(selectedNodeIds[i]);
      Node selectedNode = nodeManager.getNode(selectedNodeId);
      TreeElement treeElement = selectedNode.getTreeElement();
      Object object;
      if (treeElement instanceof BusinessElement)
        object = ((BusinessElement)treeElement).getBusinessEntity();
      else if (treeElement instanceof ServiceElement)
        object = ((ServiceElement)treeElement).getBusinessService();
      else
      {
        // Service interface
        object = ((ServiceInterfaceElement)treeElement).getTModel();
      }
      ListElement targetListElement = new ListElement(object);
      targetListElement.setTargetViewToolInfo(selectedNodeId,selectedNode.getToolManager().getSelectedToolId(),selectedNode.getViewId());
      if (targetProperty.equals(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS) || targetProperty.equals(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS) || targetProperty.equals(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE))
      {
        // Clear the manager so a replacement occurs.
        targetList.removeAllElements();
      }
      targetList.addElement(targetListElement);
    }
    targetQueryFormToolPI.setProperty(targetProperty,targetList);
    Hashtable savedParentQueryProperties = target.getParentQueryData();
    Enumeration e = savedParentQueryProperties.keys();
    while (e.hasMoreElements())
    {
      String key = (String)e.nextElement();
      targetQueryFormToolPI.setProperty(key,savedParentQueryProperties.get(key));
    }

    // Select the node and the datastructure which is the target of the subquery.
    int targetNodeId = target.getNodeId();
    int targetToolId = target.getToolId();
    int targetViewId = target.getViewId();
    int targetViewToolId = target.getViewToolId();
    nodeManager.setSelectedNodeId(targetNodeId);
    targetNode_ = nodeManager.getSelectedNode();
    targetNode_.getToolManager().setSelectedToolId(targetToolId);
    targetNode_.setViewId(targetViewId);
    if (targetViewId != ActionInputs.VIEWID_DEFAULT)
      targetNode_.getViewToolManager().setSelectedToolId(targetViewToolId);
    targetFormTool.setProperty(UDDIActionInputs.SUBQUERY_KEY,targetQueryKey);
    addToHistory(ActionInputs.PERSPECTIVE_UDDI,SelectFindToolAction.getActionLink(targetNodeId,targetToolId,targetViewId,targetViewToolId,targetQueryKey,true));
    return true;
  }

  public final Node getTargetNode()
  {
    return targetNode_;
  }
}
