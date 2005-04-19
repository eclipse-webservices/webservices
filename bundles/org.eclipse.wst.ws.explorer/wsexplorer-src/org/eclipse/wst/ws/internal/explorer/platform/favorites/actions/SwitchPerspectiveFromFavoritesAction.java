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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

public final class SwitchPerspectiveFromFavoritesAction extends ShowPerspectiveAction
{
  public SwitchPerspectiveFromFavoritesAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String perspectiveContentFramesetCols = parser.getParameter(FavoritesActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = parser.getParameter(FavoritesActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    if (perspectiveContentFramesetCols != null && actionsContainerFramesetRows != null)
    {
      propertyTable_.put(FavoritesActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT,perspectiveContentFramesetCols);
      propertyTable_.put(FavoritesActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER,actionsContainerFramesetRows);
      return true;
    }
    return false;
  }
  
  public final boolean run()
  {
    String perspectiveContentFramesetCols = (String)propertyTable_.get(FavoritesActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(FavoritesActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    
    // Save the frameset sizes iff no frame is maximized.
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    if (!perspectiveContentFramesetCols.startsWith("100%") && !perspectiveContentFramesetCols.endsWith("100%"))
      favPerspective.setPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
      
    if (!actionsContainerFramesetRows.startsWith("100%") && !actionsContainerFramesetRows.endsWith("100%"))
      favPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      
    return super.run();
  }
  
  public static final String getFormActionLink(int targetPerspectiveId,boolean forHistory)
  {
    StringBuffer formLink = new StringBuffer("wsdl/actions/SwitchPerspectiveFromWSDLActionJSP.jsp?");
    formLink.append(ActionInputs.PERSPECTIVE).append('=').append(targetPerspectiveId);
    if (forHistory)
      formLink.append('&').append(ActionInputs.ISHISTORY).append("=1");    
    return formLink.toString();
  }
}
