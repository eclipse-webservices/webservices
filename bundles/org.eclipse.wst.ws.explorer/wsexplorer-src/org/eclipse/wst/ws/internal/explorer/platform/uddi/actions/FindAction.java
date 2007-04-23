/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.MultipartFormDataParserTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.QueryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.QueryParentElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.MultipleFormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.QueryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryTransferTarget;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;

public abstract class FindAction extends UDDIPropertiesFormAction
{
  protected QueryElement queryElement_;
  protected String subQueryKey_;
  protected boolean isSubQueryGet_;
  protected String newSubQuery_;
  protected String newSubQueryItem_;
  protected boolean subQueryInitiated_;
  protected RegistryNode regNode_;
  private boolean isRefreshAction_;

  public FindAction(Controller controller)
  {
    super(controller);
    queryElement_ = null;
    subQueryKey_ = null;
    isSubQueryGet_ = false;
    subQueryInitiated_ = false;
    regNode_ = getRegistryNode();
    isRefreshAction_ = false;
  }

  protected abstract boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException;

  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    // Setup for MultipartFormDataParserTransformer
    propertyTable_.put(ActionInputs.MULTIPART_FORM_DATA_PARSER, parser);

    newSubQuery_ = parser.getParameter(UDDIActionInputs.NEW_SUBQUERY_INITIATED);
    String isSubQueryGetString = parser.getParameter(UDDIActionInputs.SUBQUERY_GET);
    newSubQueryItem_ = parser.getParameter(UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM);
    subQueryKey_ = parser.getParameter(UDDIActionInputs.SUBQUERY_KEY);
    String queryName = parser.getParameter(UDDIActionInputs.QUERY_NAME);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    FormTool formTool = getSelectedFormTool();
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey_);
    formToolPI.clearErrors();

    if (newSubQuery_ != null && newSubQuery_.length() > 0)
      subQueryInitiated_ = true;
    else
      subQueryInitiated_ = false;
      
    if (isSubQueryGetString != null)
      isSubQueryGet_ = Boolean.valueOf(isSubQueryGetString).booleanValue();

    if (queryName != null)
      propertyTable_.put(UDDIActionInputs.QUERY_NAME,queryName);

    if (!Validator.validateString(queryName) && !subQueryInitiated_)
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_NAME);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_QUERY_NAME"));
    }

    if (!processOthers(parser,formToolPI) && !subQueryInitiated_)
      inputsValid = false;
      
    for (int i=0;i<removedProperties_.size();i++)
      formToolPI.removeProperty(removedProperties_.elementAt(i));
    formToolPI.updatePropertyTable(propertyTable_);
      
    // Process authentication information. If valid, these should be set in the formTool (not the formToolPI).
    String ownedChecked = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED);
    String publishURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
    String userId = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
    String password = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();

    boolean validateAuthentication = (ownedChecked != null) && (!regElement.isLoggedIn()) && !overrideAuthenticationValidation();

    if (publishURL != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
      formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
    }      

    if (!subQueryInitiated_ && validateAuthentication && !Validator.validateString(publishURL))
    {
      inputsValid = false;
      formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_PUBLISH_URL"));
    }

    if (userId != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
      formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
    }

    if (!subQueryInitiated_ && validateAuthentication && !Validator.validateString(userId))
    {
      inputsValid = false;
      formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_USERID"));
    }

    if (password != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
      formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
    }        
      
    return inputsValid;
  }

  protected boolean overrideAuthenticationValidation()
  {
    return false;
  }
  
  public final boolean isSubQueryInitiated()
  {
    return subQueryInitiated_;
  }
  
  public final boolean isSubQueryGet()
  {
    return isSubQueryGet_;
  }    

  public final String getSubQueryKey()
  {
    return subQueryKey_;
  }

  public final String getNewSubQuery()
  {
    return newSubQuery_;
  }

  public final String getNewSubQueryItem()
  {
    return newSubQueryItem_;
  }
  
  public final void setRefreshAction(boolean isRefreshAction)
  {
    isRefreshAction_ = isRefreshAction;
  }
  
  public final boolean isRefreshAction()
  {
    return isRefreshAction_;
  }

  protected final void addQueryNode()
  {
    // If the queryElement already exists, remove it first.
    String queryName = queryElement_.getName();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    QueryParentElement queryParentElement = regElement.getQueryParentElement();
    Enumeration e = queryParentElement.getQueries();
    while (e.hasMoreElements())
    {
      QueryElement qe = (QueryElement)e.nextElement();
      if (qe.getName().equals(queryName))
      {
        SubQueryTransferTarget subQueryTransferTarget = (SubQueryTransferTarget)qe.getPropertyAsObject(UDDIModelConstants.SUBQUERY_TRANSFER_TARGET);
        if (subQueryTransferTarget != null && (qe.getQueryType() == queryElement_.getQueryType()))
          queryElement_.setPropertyAsObject(UDDIModelConstants.SUBQUERY_TRANSFER_TARGET,subQueryTransferTarget);
        qe.disconnectAll();
        break;
      }
    }
    if (!isRefreshAction_)
    {
      // We can make safe assumptions about the node that is currently selected.
      if (subQueryKey_ != null && subQueryKey_.length() > 0)
      {
        String parentQueryKey;
        int lastSeparatorPos = subQueryKey_.lastIndexOf(':');
        if (lastSeparatorPos == -1)
          parentQueryKey = "";
        else
          parentQueryKey = subQueryKey_.substring(0,lastSeparatorPos);
        FormToolPropertiesInterface parentFormToolPI = ((MultipleFormToolPropertiesInterface)getSelectedFormTool()).getFormToolProperties(parentQueryKey);
        Object queryItem = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_ITEM);
        Object queryStyleBus = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_BUSINESSES);
        Object queryStyleSer = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICES);
        Object queryStyleSerInt = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES);
        Hashtable parentQueryData = new Hashtable();
        if (queryItem != null)
          parentQueryData.put(UDDIActionInputs.QUERY_ITEM, queryItem);
        if (queryStyleBus != null)
          parentQueryData.put(UDDIActionInputs.QUERY_STYLE_BUSINESSES, queryStyleBus);
        if (queryStyleSer != null)
          parentQueryData.put(UDDIActionInputs.QUERY_STYLE_SERVICES, queryStyleSer);
        if (queryStyleSerInt != null)
          parentQueryData.put(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES, queryStyleSerInt);
        queryElement_.setPropertyAsObject(UDDIModelConstants.SUBQUERY_TRANSFER_TARGET,new SubQueryTransferTarget(getSelectedNavigatorNode(),subQueryKey_,parentQueryData));
      }
    }
    queryParentElement.connect(queryElement_,UDDIModelConstants.REL_QUERIES,ModelConstants.REL_OWNER);
    QueryNode queryNode = (QueryNode)(regNode_.getChildNode(queryParentElement).getChildNode(queryElement_));
    int newSelectedNodeId;
    // If there is only 1 result, select the result. Otherwise, select the query node to present a summary.
    if (queryNode.getChildNodes().size() == 1 && (subQueryKey_ == null || subQueryKey_.length() == 0))
      newSelectedNodeId = ((Node)(queryNode.getChildNodes().elementAt(0))).getNodeId();
    else
      newSelectedNodeId = queryNode.getNodeId();
    NodeManager nodeManager = regNode_.getNodeManager();
    nodeManager.setSelectedNodeId(newSelectedNodeId);
    queryNode.setFindToolProperties(this);
    Node selectedNode = nodeManager.getSelectedNode();
    ToolManager toolManager = selectedNode.getCurrentToolManager();
    Tool selectedTool = toolManager.getSelectedTool();
    addToHistory(ActionInputs.PERSPECTIVE_UDDI,selectedTool.getSelectToolActionHref(true));
  }
  
  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length + 1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length - 1] = new MultipartFormDataParserTransformer(controller_);
    return transformers;
  }
}
