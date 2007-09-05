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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ShowPerspectiveAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;

public final class SwitchPerspectiveFromWSILAction extends ShowPerspectiveAction
{
  public SwitchPerspectiveFromWSILAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String perspectiveContentFramesetCols = parser.getParameter(WsilActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = parser.getParameter(WsilActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    if (perspectiveContentFramesetCols != null && actionsContainerFramesetRows != null)
    {
      propertyTable_.put(WsilActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT,perspectiveContentFramesetCols);
      propertyTable_.put(WsilActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER,actionsContainerFramesetRows);
      return true;
    }
    return false;
  }
  
  public final boolean run()
  {
    String perspectiveContentFramesetCols = (String)propertyTable_.get(WsilActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(WsilActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    
    // Save the frameset sizes iff no frame is maximized.
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    if (!perspectiveContentFramesetCols.startsWith("100%") && !perspectiveContentFramesetCols.endsWith("100%"))
      wsilPerspective.setPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
      
    if (!actionsContainerFramesetRows.startsWith("100%") && !actionsContainerFramesetRows.endsWith("100%"))
      wsilPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      
    return super.run();
  }
  
  public static final String getFormActionLink(int targetPerspectiveId,boolean forHistory)
  {
    StringBuffer formLink = new StringBuffer("wsil/actions/SwitchPerspectiveFromWSILActionJSP.jsp?");
    formLink.append(ActionInputs.PERSPECTIVE).append('=').append(targetPerspectiveId);
    if (forHistory)
      formLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
    return formLink.toString();
  }
}
