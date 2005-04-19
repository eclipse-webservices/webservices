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
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

import java.util.Hashtable;

public class AddBusinessToFavoritesAction extends AddToFavoritesAction
{
  public AddBusinessToFavoritesAction(Controller controller)
  {
    super(controller);
  }

  public boolean favoriteExists()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    int viewID = Integer.parseInt((String) propertyTable_.get(ActionInputs.VIEWID));
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    Node selectedNode = nodeManager_.getNode(nodeID);
    WsilElement selectedElement = (WsilElement) selectedNode.getTreeElement();
    Object obj = selectedElement.getAllUDDILinks().getElementWithViewId(viewID).getObject();
    WsilUddiBusinessElement wsilUddiBusinessElement = (WsilUddiBusinessElement) obj;
    String businessName = wsilUddiBusinessElement.getName();
    String inquiryAPI = wsilUddiBusinessElement.getUDDILinkInquiryAPI();
    String businessKey = wsilUddiBusinessElement.getUDDILinkBusinessKey();
    if (businessName == null)
      businessName = businessKey;
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_NAME, businessName);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_INQUIRY_API, inquiryAPI);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_KEY, businessKey);
    return favoriteExists(table, FavoritesModelConstants.REL_UDDI_BUSINESS_FOLDER_NODE);
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
    Node selectedNode = nodeManager_.getNode(nodeID);
    WsilElement selectedElement = (WsilElement) selectedNode.getTreeElement();
    Object obj = selectedElement.getAllUDDILinks().getElementWithViewId(viewID).getObject();
    WsilUddiBusinessElement wsilUddiBusinessElement = (WsilUddiBusinessElement) obj;
    String businessName = wsilUddiBusinessElement.getName();
    String inquiryAPI = wsilUddiBusinessElement.getUDDILinkInquiryAPI();
    String businessKey = wsilUddiBusinessElement.getUDDILinkBusinessKey();
    if (businessName == null)
      businessName = businessKey;
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_NAME, businessName);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_INQUIRY_API, inquiryAPI);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_KEY, businessKey);
    if (isMultipleLinkAction() && favoriteExists(table, FavoritesModelConstants.REL_UDDI_BUSINESS_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_FAVORITES_ALREADY_EXISTS", businessName));
      return false;
    }
    if (addToFavorites(table, FavoritesModelConstants.REL_UDDI_BUSINESS_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_ADD_TO_FAVORITES_SUCCESSFUL", businessName));
      return true;
    }
    else
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_ADD_TO_FAVORITES", businessName));
      return false;
    }
  }
}
