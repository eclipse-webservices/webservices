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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*;

public class ResizeWSILFramesAction extends ResizeFramesAction
{
  public ResizeWSILFramesAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processOthers(MultipartFormDataParser parser) throws MultipartFormDataException
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
    // Save the current frameset sizes.
    String frameName = (String)propertyTable_.get(ActionInputs.FRAME_NAME);
    String perspectiveContentFramesetCols = (String)propertyTable_.get(WsilActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(WsilActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    
    // Set the new frameset sizes.
    boolean saveFrameSettings = false;
    if (frameName.equals(WsilFrameNames.WSIL_NAVIGATOR_CONTAINER))
    {
      wsilPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      wsilPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
      if (perspectiveContentFramesetCols.startsWith("100%"))
      {
        // Restore.
        wsilPerspective.setPerspectiveContentFramesetCols(wsilPerspective.getSavedPerspectiveContentFramesetCols());
      }
      else
      {
        // Maximize.
        wsilPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
        wsilPerspective.setPerspectiveContentFramesetCols("100%,0%");
      }
    }
    else if (frameName.equals(WsilFrameNames.WSIL_PROPERTIES_CONTAINER))
    {
      if (actionsContainerFramesetRows.startsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          wsilPerspective.setPerspectiveContentFramesetCols(wsilPerspective.getSavedPerspectiveContentFramesetCols());
          wsilPerspective.setActionsContainerFramesetRows(wsilPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          wsilPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsilPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          wsilPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        wsilPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        wsilPerspective.setActionsContainerFramesetRows("100%,0%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          wsilPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsilPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    else if (frameName.equals(WsilFrameNames.WSIL_STATUS_CONTAINER))
    {
      if (actionsContainerFramesetRows.endsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          wsilPerspective.setPerspectiveContentFramesetCols(wsilPerspective.getSavedPerspectiveContentFramesetCols());
          wsilPerspective.setActionsContainerFramesetRows(wsilPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          wsilPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsilPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          wsilPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        wsilPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        wsilPerspective.setActionsContainerFramesetRows("0%,100%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          wsilPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsilPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    return true;
  }
}
