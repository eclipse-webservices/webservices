/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class ClearNavigatorNodesAction extends UDDIPropertiesFormAction
{
  public ClearNavigatorNodesAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String[] selectedNodeIds = parser.getParameterValues(ActionInputs.NODEID);
    if (selectedNodeIds != null)
      propertyTable_.put(UDDIActionInputs.SELECTED_NODEIDS,selectedNodeIds);
    else
      removeProperty(UDDIActionInputs.SELECTED_NODEIDS);
    return true;
  }
  
  public final boolean run()
  {
    String[] selectedNodeIds = (String[])propertyTable_.get(UDDIActionInputs.SELECTED_NODEIDS);
    if (selectedNodeIds != null)
    {
      ClearNavigatorNodeAction action = new ClearNavigatorNodeAction(controller_);
      Hashtable propertyTable = action.getPropertyTable();
      for (int i=0;i<selectedNodeIds.length;i++)
      {
        propertyTable.put(ActionInputs.NODEID,selectedNodeIds[i]);
        action.run();
      }
    }
    return true;
  }
}
