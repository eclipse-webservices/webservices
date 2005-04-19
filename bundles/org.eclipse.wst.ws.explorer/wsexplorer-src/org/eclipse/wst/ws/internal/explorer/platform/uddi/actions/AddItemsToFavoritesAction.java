/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import java.util.*;

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
