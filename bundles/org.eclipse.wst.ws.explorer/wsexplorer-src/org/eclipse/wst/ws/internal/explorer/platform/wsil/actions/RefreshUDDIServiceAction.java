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
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.RefreshAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

import java.util.Vector;
import java.util.Iterator;

public class RefreshUDDIServiceAction extends RefreshAction
{
  public RefreshUDDIServiceAction(Controller controller)
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

  public boolean run()
  {
    return executeSingleLinkAction();
  }

  protected boolean executeMultipleLinkAction()
  {
    return executeSingleLinkAction();
  }

  public boolean executeSingleLinkAction()
  {
    boolean result = true;
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    MessageQueue messageQueue = wsilPerspective.getMessageQueue();
    if (selectedElement instanceof WsilElement)
    {
      WsilElement wsilElement = (WsilElement) selectedElement;
      ListManager lm = wsilElement.getAllUDDIServices();
      Vector uddiServiceElements = new Vector();
      String[] viewIDs = getViewIds();
      for (int i = 0; i < viewIDs.length; i++)
      {
        int viewID = Integer.parseInt(viewIDs[i]);
        WsilUddiServiceElement uddiServiceElement = (WsilUddiServiceElement) lm.getElementWithViewId(viewID).getObject();
        uddiServiceElements.add(uddiServiceElement);
      }
      wsilElement.refreshServiceDefinitionsFromRegistry(uddiServiceElements);
      for (Iterator it = uddiServiceElements.iterator(); it.hasNext();)
      {
        WsilUddiServiceElement uddiServiceElement = (WsilUddiServiceElement) it.next();
        String name = uddiServiceElement.getName();
        if (name == null || name.length() <= 0)
          name = uddiServiceElement.getUDDIServiceKey();
        if (uddiServiceElement.getServiceDefinition() != null)
          messageQueue.addMessage(wsilPerspective.getMessage("MSG_INFO_REFRESH_SUCCESSFUL", name));
        else
        {
          messageQueue.addMessage(wsilPerspective.getMessage("MSG_ERROR_SERVICE_NOT_FOUND", name));
          result = false;
        }
      }
    }
    else
      result = false;
    return result;
  }
}
