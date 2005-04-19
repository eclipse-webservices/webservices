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
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.MultipartFormDataParserTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

import java.util.*;

public abstract class PublishAction extends UDDIPropertiesFormAction
{
  protected String subQueryKey_;
  protected boolean subQueryInitiated_;
  protected String newSubQuery_;
  protected boolean isSubQueryGet_;
  protected String newSubQueryItem_;
  protected RegistryNode regNode_;

  public PublishAction(Controller controller)
  {
    super(controller);
    subQueryKey_ = null;
    isSubQueryGet_ = false;
    subQueryInitiated_ = false;
    regNode_ = getRegistryNode();
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

    if (!processOthers(parser,formToolPI) && !subQueryInitiated_)
      inputsValid = false;

    for (int i=0;i<removedProperties_.size();i++)
      formToolPI.removeProperty(removedProperties_.elementAt(i));
    formToolPI.updatePropertyTable(propertyTable_);

    String publishURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
    String userId = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
    String password = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();

    boolean validateAuthentication = !regElement.isLoggedIn();

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

  protected final void addPublishedItemNode(BusinessEntity be,RegistryElement regElement)
  {
    BusinessElement beElement = new BusinessElement(be,regElement.getModel());
    PublishedItemsElement publishedBusinessesElement = regElement.getPublishedBusinessesElement();
    Enumeration e = publishedBusinessesElement.getItems();
    while (e.hasMoreElements())
    {
      BusinessElement element = (BusinessElement)e.nextElement();
      BusinessEntity elementBE = element.getBusinessEntity();
      if (elementBE.getBusinessKey().equals(be.getBusinessKey()))
      {
        element.disconnectAll();
        break;
      }
    }
    connectElements(publishedBusinessesElement,beElement);
  }

  protected final void addPublishedItemNode(BusinessService bs,RegistryElement regElement)
  {
    ServiceElement sElement = new ServiceElement(bs,regElement.getModel());
    PublishedItemsElement publishedServicesElement = regElement.getPublishedServicesElement();
    Enumeration e = publishedServicesElement.getItems();
    while (e.hasMoreElements())
    {
      ServiceElement element = (ServiceElement)e.nextElement();
      BusinessService elementBS = element.getBusinessService();
      if (elementBS.getServiceKey().equals(bs.getServiceKey()))
      {
        element.disconnectAll();
        break;
      }
    }
    connectElements(publishedServicesElement,sElement);
  }

  protected final void addPublishedItemNode(TModel tModel,RegistryElement regElement)
  {
    ServiceInterfaceElement siElement = new ServiceInterfaceElement(tModel,regElement.getModel());
    PublishedItemsElement publishedServiceInterfacesElement = regElement.getPublishedServiceInterfacesElement();
    Enumeration e = publishedServiceInterfacesElement.getItems();
    while (e.hasMoreElements())
    {
      ServiceInterfaceElement element = (ServiceInterfaceElement)e.nextElement();
      TModel elementTModel = element.getTModel();
      if (tModel.getTModelKey().equals(elementTModel.getTModelKey()))
      {
        element.disconnectAll();
        break;
      }
    }
    connectElements(publishedServiceInterfacesElement,siElement);
  }

  private final void connectElements(PublishedItemsElement publishedItemsElement,TreeElement itemElement)
  {
    publishedItemsElement.connect(itemElement,UDDIModelConstants.REL_PUBLISHED_ITEMS,ModelConstants.REL_OWNER);
    int newNodeId = regNode_.getChildNode(publishedItemsElement).getChildNode(itemElement).getNodeId();
    NodeManager nodeManager = regNode_.getNodeManager();
    nodeManager.setSelectedNodeId(newNodeId);
    Node newNode = nodeManager.getSelectedNode();
    ToolManager toolManager = newNode.getToolManager();
    Tool selectedTool = toolManager.getSelectedTool();
    addToHistory(ActionInputs.PERSPECTIVE_UDDI,selectedTool.getSelectToolActionHref(true));
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
  
  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length + 1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length - 1] = new MultipartFormDataParserTransformer(controller_);
    return transformers;
  }
}