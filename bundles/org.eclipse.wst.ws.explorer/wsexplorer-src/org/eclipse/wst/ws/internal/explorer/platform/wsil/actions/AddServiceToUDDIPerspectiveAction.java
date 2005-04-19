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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.RegFindServiceUUIDAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

import java.util.Hashtable;

public class AddServiceToUDDIPerspectiveAction extends AddToUDDIPerspectiveAction
{
  public AddServiceToUDDIPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length+1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length-1] = new WSILViewSelectionTransformer(controller_, WsilModelConstants.LIST_MANAGER_UDDI_SERVICES, ActionInputs.VIEWID, WSILViewSelectionTransformer.UDDI_SERVICE);
    return transformers;
  }

  public boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    int viewID = Integer.parseInt((String) propertyTable_.get(ActionInputs.VIEWID));
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    Node selectedNode = wsilPerspective.getNodeManager().getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    String inquiryURL = null;
    String serviceKey = null;
    String serviceName = null;
    if (selectedElement instanceof WsilElement)
    {
      ListElement le = ((WsilElement) selectedElement).getAllUDDIServices().getElementWithViewId(viewID);
      WsilUddiServiceElement wsilUddiServiceElement = (WsilUddiServiceElement) le.getObject();
      inquiryURL = wsilUddiServiceElement.getUDDIServiceInquiryAPI();
      serviceKey = wsilUddiServiceElement.getUDDIServiceKey();
      serviceName = wsilUddiServiceElement.getName();
      if (serviceName == null)
        serviceName = serviceKey;
    }
    else
      return false;
    // return false if unable to find or create a registry node/element in the
    // UDDI perspective
    if (!createRegistryInUDDIPerspective(inquiryURL, null, inquiryURL))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_ADD_TO_UDDI_PERSPECTIVE", serviceName));
      return false;
    }
    // prepare the action
    RegFindServiceUUIDAction action = new RegFindServiceUUIDAction(controller_);
    // populate property table
    Hashtable propertyTable = action.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY, serviceKey);
    propertyTable.put(UDDIActionInputs.QUERY_NAME, serviceName);
    // run the action
    if (!action.run())
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_SERVICE_NOT_FOUND", serviceName));
      return false;
    }
    else
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_ADD_TO_UDDI_PERSPECTIVE_SUCCESSFUL", serviceName));
      return true;
    }
  }
}
