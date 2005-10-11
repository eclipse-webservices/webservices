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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ResizeFramesAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesFrameNames;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class ResizeFavoritesFramesAction extends ResizeFramesAction
{
  public ResizeFavoritesFramesAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processOthers(MultipartFormDataParser parser) throws MultipartFormDataException
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
    // Save the current frameset sizes.
    String frameName = (String)propertyTable_.get(ActionInputs.FRAME_NAME);
    String perspectiveContentFramesetCols = (String)propertyTable_.get(FavoritesActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(FavoritesActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    
    // Set the new frameset sizes.
    if (frameName.equals(FavoritesFrameNames.NAVIGATOR_CONTAINER))
    {
      favPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      favPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
      if (perspectiveContentFramesetCols.startsWith("100%"))
      {
        // Restore.
        favPerspective.setPerspectiveContentFramesetCols(favPerspective.getSavedPerspectiveContentFramesetCols());
      }
      else
      {
        // Maximize.
        favPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
        favPerspective.setPerspectiveContentFramesetCols("100%,0%");
      }
    }
    else if (frameName.equals(FavoritesFrameNames.PROPERTIES_CONTAINER))
    {
      if (actionsContainerFramesetRows.startsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          favPerspective.setPerspectiveContentFramesetCols(favPerspective.getSavedPerspectiveContentFramesetCols());
          favPerspective.setActionsContainerFramesetRows(favPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          favPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          favPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          favPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        favPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        favPerspective.setActionsContainerFramesetRows("100%,0%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          favPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          favPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    else if (frameName.equals(FavoritesFrameNames.STATUS_CONTAINER))
    {
      if (actionsContainerFramesetRows.endsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          favPerspective.setPerspectiveContentFramesetCols(favPerspective.getSavedPerspectiveContentFramesetCols());
          favPerspective.setActionsContainerFramesetRows(favPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          favPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          favPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          favPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        favPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        favPerspective.setActionsContainerFramesetRows("0%,100%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          favPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          favPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    return true;
  }
}
