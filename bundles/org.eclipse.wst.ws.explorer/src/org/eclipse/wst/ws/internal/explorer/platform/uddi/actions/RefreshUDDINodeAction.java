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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.AddRegistryToUDDIPerspectiveAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.BusinessNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.PublishedItemsNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.QueryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.QueryParentNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.ServiceInterfaceNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.ServiceNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

public class RefreshUDDINodeAction extends UDDINodeAction
{
  private Vector staleNodes_;
  public RefreshUDDINodeAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(ActionInputs.NODEID,String.valueOf(controller.getUDDIPerspective().getNavigatorManager().getSelectedNodeId()));
    staleNodes_ = new Vector();
  }

  // uddi/actions/RefreshUDDINodeActionJSP.jsp?nodeId=...
  public static String getActionLink(int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/RefreshUDDINodeActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }

  public final boolean verifyNodeData()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    Node node = nodeManager_.getNode(nodeId);
    if (node instanceof BusinessNode)
      return refreshBusinessElement((BusinessNode)node);
    else if (node instanceof ServiceNode)
      return refreshServiceElement((ServiceNode)node);
    else if (node instanceof ServiceInterfaceNode)
      return refreshServiceInterfaceElement((ServiceInterfaceNode)node);
    else if (node instanceof QueryNode)
    {
      // Nothing to verify
      return true;
    }
    else if (node != null)
    {
      // RegistryNode
      if (node instanceof RegistryNode)
        refreshRegistryElement((RegistryNode)node);
        
      // FolderNodes
      Vector childNodes = node.getChildNodes();
      Vector refreshActions = new Vector();
      for (int i=0;i<childNodes.size();i++)
      {
        Node childNode = (Node)childNodes.elementAt(i);
        RefreshUDDINodeAction action = new RefreshUDDINodeAction(controller_);
        action.setStaleNodes(staleNodes_);
        Hashtable propertyTable = action.getPropertyTable();
        String childNodeIdString = String.valueOf(childNode.getNodeId());
        propertyTable.put(ActionInputs.NODEID,childNodeIdString);
        if (!action.verifyNodeData())
          staleNodes_.addElement(childNode);
        else
          refreshActions.addElement(action);
      }
      propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,refreshActions);
      return true;
    }
    return true;
  }
  
  private final void refreshRegistryElement(RegistryNode regNode)
  {
    RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
    Hashtable userDefinedCategories = null;
    if (regElement.getCheckForUserDefinedCategories())
    {
      userDefinedCategories = new Hashtable();
      OpenRegistryAction openRegAction = new OpenRegistryAction(controller_);
      openRegAction.gatherWSUserDefinedCategories(regElement.getProxy(),userDefinedCategories);
      if (!userDefinedCategories.keys().hasMoreElements())
      {
        userDefinedCategories = null;
        regNode.setImagePath(RegistryNode.IMAGE_PATH_STANDARD);
      }
      else
      {
        String categoriesDirectory = regElement.getCategoriesDirectory();
        AddRegistryToUDDIPerspectiveAction addAction = new AddRegistryToUDDIPerspectiveAction(controller_);
        if (categoriesDirectory != null)
          addAction.linkCategoryModelsWithSavedData(userDefinedCategories.elements(),categoriesDirectory);
        else
          addAction.linkCategoryModelsWithSavedData(regElement.getName(),userDefinedCategories.elements());
        regNode.setImagePath(RegistryNode.IMAGE_PATH_WITH_USER_DEFINED_CATEGORIES);
      }
    }
    regElement.setUserDefinedCategories(userDefinedCategories);
  }

  private final boolean refreshBusinessElement(BusinessNode busNode)
  {
    BusinessElement busElement = (BusinessElement)busNode.getTreeElement();
    String uuidKey = busElement.getBusinessEntity().getBusinessKey();
    RegFindBusinessUUIDAction verifyAction = new RegFindBusinessUUIDAction(controller_);
    Hashtable propertyTable = verifyAction.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,uuidKey);
    propertyTable.put(UDDIActionInputs.REFRESH_NODE,busNode);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE,Boolean.TRUE);
    boolean verifyResult = verifyAction.run();
    if (verifyResult)
      propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,new BusinessElement((BusinessEntity)propertyTable.get(UDDIActionInputs.LATEST_OBJECT),busElement.getModel()));
    return verifyResult;
  }

  private final boolean refreshServiceElement(ServiceNode serviceNode)
  {
    ServiceElement serviceElement = (ServiceElement)serviceNode.getTreeElement();
    String uuidKey = serviceElement.getBusinessService().getServiceKey();
    RegFindServiceUUIDAction verifyAction = new RegFindServiceUUIDAction(controller_);
    Hashtable propertyTable = verifyAction.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY,uuidKey);
    propertyTable.put(UDDIActionInputs.REFRESH_NODE,serviceNode);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE,Boolean.TRUE);
    boolean verifyResult = verifyAction.run();
    if (verifyResult)
      propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,new ServiceElement((BusinessService)propertyTable.get(UDDIActionInputs.LATEST_OBJECT),serviceElement.getModel()));
    return verifyResult;
  }

  private final boolean refreshServiceInterfaceElement(ServiceInterfaceNode siNode)
  {
    ServiceInterfaceElement siElement = (ServiceInterfaceElement)siNode.getTreeElement();
    String uuidKey = siElement.getTModel().getTModelKey();
    RegFindServiceInterfaceUUIDAction verifyAction = new RegFindServiceInterfaceUUIDAction(controller_);
    Hashtable propertyTable = verifyAction.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY,uuidKey);
    propertyTable.put(UDDIActionInputs.REFRESH_NODE,siNode);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE,Boolean.TRUE);
    boolean verifyResult = verifyAction.run();
    if (verifyResult)
      propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,new ServiceInterfaceElement((TModel)propertyTable.get(UDDIActionInputs.LATEST_OBJECT),siElement.getModel()));
    return verifyResult;
  }

  public final void connectElements(Node parentNode,TreeElement newElement)
  {
    TreeElement parentElement = parentNode.getTreeElement();
    String rel = null;
    if (parentNode instanceof PublishedItemsNode)
      rel = UDDIModelConstants.REL_PUBLISHED_ITEMS;
    else if (parentNode instanceof QueryNode)
      rel = UDDIModelConstants.REL_QUERY_RESULTS;
    else if (parentNode instanceof QueryParentNode)
      rel = UDDIModelConstants.REL_QUERIES;
    parentElement.connect(newElement,rel,ModelConstants.REL_OWNER);
    Node newNode = parentNode.getChildNode(newElement);
    int newNodeId = newNode.getNodeId();
    nodeManager_.setSelectedNodeId(newNodeId);
    ToolManager toolManager = newNode.getToolManager();
    Tool selectedTool = toolManager.getSelectedTool();
    addToHistory(ActionInputs.PERSPECTIVE_UDDI,selectedTool.getSelectToolActionHref(true));
  }

  public final String getActionLinkForHistory()
  {
    return null;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    if (!isStaleNode(nodeId))
    {
      Node node = nodeManager_.getNode(nodeId);
      Object latestObject = propertyTable_.get(UDDIActionInputs.LATEST_OBJECT);
      if (latestObject != null)
      {
        if (latestObject instanceof Vector)
        {
          Vector refreshActions = (Vector)propertyTable_.get(UDDIActionInputs.LATEST_OBJECT);
          for (int i=0;i<refreshActions.size();i++)
          {
            RefreshUDDINodeAction refreshAction = (RefreshUDDINodeAction)refreshActions.elementAt(i);
            refreshAction.run();
          }
          nodeManager_.setSelectedNodeId(nodeId);        
        }
        else
        {
          TreeElement oldElement = node.getTreeElement();
          oldElement.disconnectAll();
          connectElements(node.getParent(),(TreeElement)propertyTable_.get(UDDIActionInputs.LATEST_OBJECT));
        }
      }
      else
      {
        // QueryNodes
        QueryNode queryNode = (QueryNode)node;
        FindAction findAction = queryNode.getFindAction();
        findAction.setRefreshAction(true);
        if (!findAction.run())
          queryNode.removeChildNodes(true);
        findAction.setRefreshAction(false);
      }
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_NODE_REFRESHED",node.getNodeName()));
      return true;
    }
    return false;
  }
  
  public final Vector getStaleNodes()
  {
    return staleNodes_;
  }
  
  public final void setStaleNodes(Vector staleNodes)
  {
    staleNodes_ = staleNodes;
  }
}
