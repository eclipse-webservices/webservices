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

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.actions.Action;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.MultipleFormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;

public class RemoveSubQueryItemAction extends Action
{
  private Controller controller_;
  
  public RemoveSubQueryItemAction(Controller controller)
  {
    super();
    controller_ = controller;
  }
  
  // uddi/actions/RemoveSubQueryItemActionJSP.jsp?subQueryKey=...&subQueryListKey=...&subQueryListItemId=...
  public static final String getActionLink(String subQueryKey,String subQueryListKey,int subQueryListItemId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/RemoveSubQueryItemActionJSP.jsp?");
    actionLink.append(UDDIActionInputs.SUBQUERY_KEY).append('=').append(subQueryKey);
    actionLink.append('&').append(UDDIActionInputs.SUBQUERY_LIST_KEY).append('=').append(subQueryListKey);
    actionLink.append('&').append(UDDIActionInputs.SUBQUERY_LIST_ITEMID).append('=').append(subQueryListItemId);
    return actionLink.toString();
  }
  
  public boolean populatePropertyTable(HttpServletRequest request)
  {
    String subQueryKey = request.getParameter(UDDIActionInputs.SUBQUERY_KEY);
    String subQueryListKey = request.getParameter(UDDIActionInputs.SUBQUERY_LIST_KEY);
    String subQueryListItemId = request.getParameter(UDDIActionInputs.SUBQUERY_LIST_ITEMID);
    
    if (subQueryKey != null)
      propertyTable_.put(UDDIActionInputs.SUBQUERY_KEY,subQueryKey);
      
    if (subQueryListKey != null)
      propertyTable_.put(UDDIActionInputs.SUBQUERY_LIST_KEY,subQueryListKey);
      
    if (subQueryListItemId != null)
      propertyTable_.put(UDDIActionInputs.SUBQUERY_LIST_ITEMID,subQueryListItemId);
      
    return true;
  }
  
  public boolean run()
  {
    propertyTable_.get(UDDIActionInputs.SUBQUERY_KEY);
    propertyTable_.get(UDDIActionInputs.SUBQUERY_LIST_KEY);
    int subQueryListItemId = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.SUBQUERY_LIST_ITEMID));
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
    Node currentNode = navigatorManager.getSelectedNode();
    FormTool formTool = (FormTool)(currentNode.getCurrentToolManager().getSelectedTool());
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties((String)propertyTable_.get(UDDIActionInputs.SUBQUERY_KEY));
    Vector list = (Vector)formToolPI.getProperty((String)propertyTable_.get(UDDIActionInputs.SUBQUERY_LIST_KEY));
    list.removeElementAt(subQueryListItemId);
    return true;
  }
}
