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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ResizeFramesAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLFrameNames;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class ResizeWSDLFramesAction extends ResizeFramesAction
{
  public ResizeWSDLFramesAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processOthers(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String perspectiveContentFramesetCols = parser.getParameter(WSDLActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = parser.getParameter(WSDLActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    if (perspectiveContentFramesetCols != null && actionsContainerFramesetRows != null)
    {
      propertyTable_.put(WSDLActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT,perspectiveContentFramesetCols);
      propertyTable_.put(WSDLActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER,actionsContainerFramesetRows);
      return true;
    }
    return false;
  }
  
  public final boolean run()
  {
    // Save the current frameset sizes.
    String frameName = (String)propertyTable_.get(ActionInputs.FRAME_NAME);
    String perspectiveContentFramesetCols = (String)propertyTable_.get(WSDLActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(WSDLActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    
    // Set the new frameset sizes.
    if (frameName.equals(WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER))
    {
      wsdlPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      wsdlPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
      if (perspectiveContentFramesetCols.startsWith("100%"))
      {
        // Restore.
        wsdlPerspective.setPerspectiveContentFramesetCols(wsdlPerspective.getSavedPerspectiveContentFramesetCols());
      }
      else
      {
        // Maximize.
        wsdlPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
        wsdlPerspective.setPerspectiveContentFramesetCols("100%,0%");
      }
    }
    else if (frameName.equals(WSDLFrameNames.WSDL_PROPERTIES_CONTAINER))
    {
      if (actionsContainerFramesetRows.startsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          wsdlPerspective.setPerspectiveContentFramesetCols(wsdlPerspective.getSavedPerspectiveContentFramesetCols());
          wsdlPerspective.setActionsContainerFramesetRows(wsdlPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          wsdlPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsdlPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          wsdlPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        wsdlPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        wsdlPerspective.setActionsContainerFramesetRows("100%,0%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          wsdlPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsdlPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    else if (frameName.equals(WSDLFrameNames.WSDL_STATUS_CONTAINER))
    {
      if (actionsContainerFramesetRows.endsWith("100%"))
      {
        if (perspectiveContentFramesetCols.endsWith("100%"))
        {
          // Restore.
          wsdlPerspective.setPerspectiveContentFramesetCols(wsdlPerspective.getSavedPerspectiveContentFramesetCols());
          wsdlPerspective.setActionsContainerFramesetRows(wsdlPerspective.getSavedActionsContainerFramesetRows());
        }
        else
        {
          // Maximize.
          wsdlPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsdlPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
          wsdlPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
      else
      {
        // Maximize.
        wsdlPerspective.setSavedActionsContainerFramesetRows(actionsContainerFramesetRows);
        wsdlPerspective.setActionsContainerFramesetRows("0%,100%");
        if (!perspectiveContentFramesetCols.endsWith("100%"))
        {
          wsdlPerspective.setSavedPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
          wsdlPerspective.setPerspectiveContentFramesetCols("0%,100%");
        }
      }
    }
    return true;
  }
}
