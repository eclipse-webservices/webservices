/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.RegFindBusinessUUIDAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilUddiBusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

public class AddBusinessToUDDIPerspectiveAction extends AddToUDDIPerspectiveAction
{
  public AddBusinessToUDDIPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length+1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length-1] = new WSILViewSelectionTransformer(controller_, WsilModelConstants.LIST_MANAGER_UDDI_LINKS, ActionInputs.VIEWID, WSILViewSelectionTransformer.UDDI_BUSINESS);
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
    String businessKey = null;
    String businessName = null;
    if (selectedElement instanceof WsilElement)
    {
      ListElement le = ((WsilElement) selectedElement).getAllUDDILinks().getElementWithViewId(viewID);
      WsilUddiBusinessElement wsilUddiBusinessElement = (WsilUddiBusinessElement) le.getObject();
      inquiryURL = wsilUddiBusinessElement.getUDDILinkInquiryAPI();
      businessKey = wsilUddiBusinessElement.getUDDILinkBusinessKey();
      businessName = wsilUddiBusinessElement.getName();
      if (businessName == null)
        businessName = businessKey;
    }
    else
      return false;
    // return false if unable to find or create a registry node/element in the
    // UDDI perspective
    if (!createRegistryInUDDIPerspective(inquiryURL, null, inquiryURL))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_ADD_TO_UDDI_PERSPECTIVE", businessName));
      return false;
    }
    // prepare the action
    RegFindBusinessUUIDAction action = new RegFindBusinessUUIDAction(controller_);
    // populate property table
    Hashtable propertyTable = action.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY, businessKey);
    propertyTable.put(UDDIActionInputs.QUERY_NAME, businessName);
    // run the action
    if (!action.run())
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_BUSINESS_NOT_FOUND", businessName));
      return false;
    }
    else
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_ADD_TO_UDDI_PERSPECTIVE_SUCCESSFUL", businessName));
      return true;
    }
  }
}
