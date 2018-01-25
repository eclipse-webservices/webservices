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

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
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
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.PublishedItemsNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

public abstract class UpdateAction extends UDDIPropertiesFormAction
{
  protected RegistryNode regNode_;
  private boolean requiresAuthentication_;
  public UpdateAction(Controller controller,boolean requiresAuthentication)
  {
    super(controller);
    regNode_ = getRegistryNode();
    requiresAuthentication_ = requiresAuthentication;
  }
  
  protected abstract boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException;
  
  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    // Setup for MultipartFormDataParserTransformer
    propertyTable_.put(ActionInputs.MULTIPART_FORM_DATA_PARSER, parser);

    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    FormTool formTool = getSelectedFormTool();
    FormToolPropertiesInterface formToolPI = formTool;
    formToolPI.clearErrors();
    boolean inputsValid = processOthers(parser,formToolPI);
    for (int i=0;i<removedProperties_.size();i++)
      formToolPI.removeProperty(removedProperties_.elementAt(i));
    formToolPI.updatePropertyTable(propertyTable_);
    
    String publishURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
    String userId = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
    String password = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    
    boolean validateAuthentication = !regElement.isLoggedIn() & requiresAuthentication_;
    
    if (publishURL != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
      formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
    }
    
    if (validateAuthentication && !Validator.validateString(publishURL))
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
    
    if (validateAuthentication && !Validator.validateString(userId))
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

  public abstract boolean refreshFromRegistry();
  
  protected final void refreshNode(BusinessEntity be)
  {
    Node node = getSelectedNavigatorNode();
    Node parentNode = node.getParent();
    TreeElement element = node.getTreeElement();
    element.disconnectAll();
    BusinessElement newBusinessElement = new BusinessElement(be,regNode_.getTreeElement().getModel());
    connectElements(parentNode,newBusinessElement);
  }
  
  protected final void refreshNode(BusinessService bs)
  {
    Node node = getSelectedNavigatorNode();
    Node parentNode = node.getParent();
    TreeElement element = node.getTreeElement();
    element.disconnectAll();
    ServiceElement newServiceElement = new ServiceElement(bs,regNode_.getTreeElement().getModel());
    connectElements(parentNode,newServiceElement);
  }
  
  protected final void refreshNode(TModel tModel)
  {
    Node node = getSelectedNavigatorNode();
    Node parentNode = node.getParent();
    TreeElement element = node.getTreeElement();
    element.disconnectAll();
    ServiceInterfaceElement newServiceInterfaceElement = new ServiceInterfaceElement(tModel,regNode_.getTreeElement().getModel());
    connectElements(parentNode,newServiceInterfaceElement);
  }
  
  private final void connectElements(Node parentNode,TreeElement newElement)
  {
    TreeElement parentElement = parentNode.getTreeElement();
    String rel = null;
    if (parentNode instanceof PublishedItemsNode)
      rel = UDDIModelConstants.REL_PUBLISHED_ITEMS;
    else
    {
      // QueryNode
      rel = UDDIModelConstants.REL_QUERY_RESULTS;
    }
    parentElement.connect(newElement,rel,ModelConstants.REL_OWNER);
    Node newNode = parentNode.getChildNode(newElement);
    int newNodeId = newNode.getNodeId();
    NodeManager nodeManager = parentNode.getNodeManager();
    nodeManager.setSelectedNodeId(newNodeId);
    ToolManager toolManager = newNode.getToolManager();
    Tool selectedTool = toolManager.getSelectedTool();
    addToHistory(ActionInputs.PERSPECTIVE_UDDI,selectedTool.getSelectToolActionHref(true));
  }
    
  protected final void reindexListElementVector(Vector v)
  {
    for (int i=0;i<v.size();i++)
    {
      ListElement listElement = (ListElement)v.elementAt(i);
      listElement.setViewId(i);
      // targetViewId determines whether or not a successfully validated list element remains in edit state.
    }
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
