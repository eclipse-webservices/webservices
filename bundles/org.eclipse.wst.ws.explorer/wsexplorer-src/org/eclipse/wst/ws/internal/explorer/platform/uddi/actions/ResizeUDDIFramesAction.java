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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ResizeFramesAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIFrameNames;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class ResizeUDDIFramesAction extends ResizeFramesAction
{
  public ResizeUDDIFramesAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processOthers(MultipartFormDataParser parser) throws MultipartFormDataException
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
    // Save the current frameset sizes.
    String frameName = (String)propertyTable_.get(ActionInputs.FRAME_NAME);
    String perspectiveContentFramesetCols = (String)propertyTable_.get(UDDIActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(UDDIActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    
    // Set the new frameset sizes.
    if (frameName.equals(UDDIFrameNames.NAVIGATOR_CONTAINER))
    {
      uddiPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      uddiPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
      if (perspectiveContentFramesetCols.startsWith("100%"))
      {
        // Restore.
        uddiPerspective.setPerspectiveContentFramesetCols(uddiPerspective.getSavedPerspectiveContentFramesetCols());
      }
      else
      {
        // Maximize.
        uddiPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
        uddiPerspective.setPerspectiveContentFramesetCols("100%,0%");
      }
    }
    else if (frameName.equals(UDDIFrameNames.PROPERTIES_CONTAINER))
    {
      if (actionsContainerFramesetRows.startsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          uddiPerspective.setPerspectiveContentFramesetCols(uddiPerspective.getSavedPerspectiveContentFramesetCols());
          uddiPerspective.setActionsContainerFramesetRows(uddiPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          uddiPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          uddiPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          uddiPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        uddiPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        uddiPerspective.setActionsContainerFramesetRows("100%,0%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          uddiPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          uddiPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    else if (frameName.equals(UDDIFrameNames.STATUS_CONTAINER))
    {
      if (actionsContainerFramesetRows.endsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          uddiPerspective.setPerspectiveContentFramesetCols(uddiPerspective.getSavedPerspectiveContentFramesetCols());
          uddiPerspective.setActionsContainerFramesetRows(uddiPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          uddiPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          uddiPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          uddiPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        uddiPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        uddiPerspective.setActionsContainerFramesetRows("0%,100%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          uddiPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          uddiPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    return true;
  }
}
