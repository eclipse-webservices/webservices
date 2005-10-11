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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ShowPerspectiveAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public final class SwitchPerspectiveFromWSDLAction extends ShowPerspectiveAction
{
  public SwitchPerspectiveFromWSDLAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
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
    String perspectiveContentFramesetCols = (String)propertyTable_.get(WSDLActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT);
    String actionsContainerFramesetRows = (String)propertyTable_.get(WSDLActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER);
    
    // Save the frameset sizes iff no frame is maximized.
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    if (!perspectiveContentFramesetCols.startsWith("100%") && !perspectiveContentFramesetCols.endsWith("100%"))
      wsdlPerspective.setPerspectiveContentFramesetCols(perspectiveContentFramesetCols);
      
    if (!actionsContainerFramesetRows.startsWith("100%") && !actionsContainerFramesetRows.endsWith("100%"))
      wsdlPerspective.setActionsContainerFramesetRows(actionsContainerFramesetRows);
      
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
