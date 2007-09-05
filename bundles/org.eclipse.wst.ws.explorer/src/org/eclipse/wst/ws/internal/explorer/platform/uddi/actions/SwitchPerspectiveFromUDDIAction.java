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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ShowPerspectiveAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public final class SwitchPerspectiveFromUDDIAction extends ShowPerspectiveAction
{
  public SwitchPerspectiveFromUDDIAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String perspectiveContentFramesetCols = parser.getParameter(UDDIActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = parser.getParameter(UDDIActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    if (perspectiveContentFramesetCols != null && actionsContainerFramesetRows != null)
    {
      propertyTable_.put(UDDIActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT,perspectiveContentFramesetCols);
      propertyTable_.put(UDDIActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER,actionsContainerFramesetRows);
      return true;
    }
    return false;
  }
  
  public final boolean run()
  {
    String perspectiveContentFramesetCols = (String)propertyTable_.get(UDDIActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(UDDIActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    
    // Save the frameset sizes iff no frame is maximized.
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    if (!perspectiveContentFramesetCols.startsWith("100%") && !perspectiveContentFramesetCols.endsWith("100%"))
      uddiPerspective.setPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
      
    if (!actionsContainerFramesetRows.startsWith("100%") && !actionsContainerFramesetRows.endsWith("100%"))
      uddiPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      
    return super.run();
  }
  
  public static final String getFormActionLink(int targetPerspectiveId,boolean forHistory)
  {
    StringBuffer formLink = new StringBuffer("uddi/actions/SwitchPerspectiveFromUDDIActionJSP.jsp?");
    formLink.append(ActionInputs.PERSPECTIVE).append('=').append(targetPerspectiveId);
    if (forHistory)
      formLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
    return formLink.toString();
  }
}
