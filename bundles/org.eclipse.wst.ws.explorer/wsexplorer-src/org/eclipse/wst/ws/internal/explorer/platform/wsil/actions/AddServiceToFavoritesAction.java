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

public class AddServiceToFavoritesAction extends AddToFavoritesAction
{
  public AddServiceToFavoritesAction(Controller controller)
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
    Object obj = selectedElement.getAllUDDIServices().getElementWithViewId(viewID).getObject();
    WsilUddiServiceElement wsilUddiServiceElement = (WsilUddiServiceElement) obj;
    String serviceName = wsilUddiServiceElement.getName();
    String inquiryAPI = wsilUddiServiceElement.getUDDIServiceInquiryAPI();
    String serviceKey = wsilUddiServiceElement.getUDDIServiceKey();
    if (serviceName == null)
      serviceName = serviceKey;
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_NAME, serviceName);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INQUIRY_API, inquiryAPI);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_KEY, serviceKey);
    return favoriteExists(table, FavoritesModelConstants.REL_UDDI_SERVICE_FOLDER_NODE);
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
    Node selectedNode = nodeManager_.getNode(nodeID);
    WsilElement selectedElement = (WsilElement) selectedNode.getTreeElement();
    Object obj = selectedElement.getAllUDDIServices().getElementWithViewId(viewID).getObject();
    WsilUddiServiceElement wsilUddiServiceElement = (WsilUddiServiceElement) obj;
    String serviceName = wsilUddiServiceElement.getName();
    String inquiryAPI = wsilUddiServiceElement.getUDDIServiceInquiryAPI();
    String serviceKey = wsilUddiServiceElement.getUDDIServiceKey();
    if (serviceName == null)
      serviceName = serviceKey;
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_NAME, serviceName);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INQUIRY_API, inquiryAPI);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_KEY, serviceKey);
    if (isMultipleLinkAction() && favoriteExists(table, FavoritesModelConstants.REL_UDDI_SERVICE_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_FAVORITES_ALREADY_EXISTS", serviceName));
      return false;
    }
    if (addToFavorites(table, FavoritesModelConstants.REL_UDDI_SERVICE_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_ADD_TO_FAVORITES_SUCCESSFUL", serviceName));
      return true;
    }
    else
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_ADD_TO_FAVORITES", serviceName));
      return false;
    }
  }
}
