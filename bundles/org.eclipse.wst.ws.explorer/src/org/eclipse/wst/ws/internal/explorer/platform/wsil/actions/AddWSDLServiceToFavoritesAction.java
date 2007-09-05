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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilWsdlServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

public class AddWSDLServiceToFavoritesAction extends AddToFavoritesAction
{
  public AddWSDLServiceToFavoritesAction(Controller controller)
  {
    super(controller);
  }

  public boolean favoriteExists()
  {
    String wsdlURL = getWsdlUrl();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_WSDL_URL, wsdlURL);
    return favoriteExists(table, FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE);
  }

  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length+1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length-1] = new WSILViewSelectionTransformer(controller_, WsilModelConstants.LIST_MANAGER_WSDL_SERVICES, ActionInputs.VIEWID, WSILViewSelectionTransformer.WSDL_SERVICE);
    return transformers;
  }

  public boolean executeSingleLinkAction()
  {
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    String wsdlURL = getWsdlUrl();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_WSDL_URL, wsdlURL);
    if (isMultipleLinkAction() && favoriteExists(table, FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_FAVORITES_ALREADY_EXISTS", wsdlURL));
      return false;
    }
    if (addToFavorites(table, FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_ADD_TO_FAVORITES_SUCCESSFUL", wsdlURL));
      return true;
    }
    else
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_ADD_TO_FAVORITES", wsdlURL));
      return false;
    }
  }

  private String getWsdlUrl()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    int viewID = Integer.parseInt((String) propertyTable_.get(ActionInputs.VIEWID));
    Node selectedNode = nodeManager_.getNode(nodeID);
    WsilElement selectedElement = (WsilElement) selectedNode.getTreeElement();
    WsilWsdlServiceElement wsilWsdlServiceElement;
    Object obj = selectedElement.getAllWSDLServices().getElementWithViewId(viewID).getObject();
    wsilWsdlServiceElement = (WsilWsdlServiceElement) obj;
    return wsilWsdlServiceElement.getWSDLServiceURL();
  }
}
