/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class AddItemsToFavoritesAction extends UDDIPropertiesFormAction
{
  public AddItemsToFavoritesAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String[] selectedNodeIds = parser.getParameterValues(ActionInputs.NODEID);
    // The client Javascript enforces the rule that at least one item was selected before the form can be submitted.
    propertyTable_.put(UDDIActionInputs.SELECTED_NODEIDS,selectedNodeIds);
    return true;
  }
  
  public final boolean run()
  {
    String[] selectedNodeIds = (String[])propertyTable_.get(UDDIActionInputs.SELECTED_NODEIDS);
    AddToFavoritesAction action = new AddToFavoritesAction(controller_);
    Hashtable propertyTable = action.getPropertyTable();
    for (int i=0;i<selectedNodeIds.length;i++)
    {
      propertyTable.put(ActionInputs.NODEID,selectedNodeIds[i]);
      if (action.favoriteExists()) {
        UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
        Node node = uddiPerspective.getNavigatorManager().getNode(Integer.parseInt(selectedNodeIds[i]));
        String nodeName = node.getNodeName();
        uddiPerspective.getMessageQueue().addMessage(uddiPerspective.getMessage("MSG_ERROR_FAVORITE_ALREADY_EXISTS",nodeName));
      }
      else
        action.run();
    }
    return true;
  }
}
