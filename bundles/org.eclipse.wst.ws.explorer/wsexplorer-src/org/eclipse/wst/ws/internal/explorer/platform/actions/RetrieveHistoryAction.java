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

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.BreadCrumb;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class RetrieveHistoryAction extends LinkAction
{
  private BreadCrumb breadCrumb_;

  public RetrieveHistoryAction(Controller controller)
  {
    super(controller);
    breadCrumb_ = null;
  }

  // actions/RetrieveHistoryAction.jsp?jump=1  - forward  (ActionInputs.JUMP_FORWARD)
  // actions/RetrieveHistoryAction.jsp?jump=-1 - backward (ActionInputs.JUMP_BACK)
  public final static String getActionLink(int jump)
  {
    StringBuffer actionLink = new StringBuffer("actions/RetrieveHistoryActionJSP.jsp?");
    actionLink.append(ActionInputs.JUMP).append('=').append(jump);
    return actionLink.toString();
  }

  protected final boolean processLinkParameters(HttpServletRequest request)
  {
    String jumpString = request.getParameter(ActionInputs.JUMP);
    // Perform data validation.
    try
    {
      Integer.parseInt(jumpString);
    }
    catch (NumberFormatException e)
    {
      // Validation failed!
      return false;
    }
    propertyTable_.put(ActionInputs.JUMP,jumpString);
    return true;
  }

  public final boolean run()
  {
    int jump = Integer.parseInt((String)propertyTable_.get(ActionInputs.JUMP));
    switch (jump)
    {
      case ActionInputs.JUMP_FORWARD:
        breadCrumb_ = controller_.forward();
        return true;
      case ActionInputs.JUMP_BACK:
        breadCrumb_ = controller_.back();
        return true;
    }
    return false;
  }

  public final BreadCrumb getBreadCrumb()
  {
    return breadCrumb_;
  }
}
