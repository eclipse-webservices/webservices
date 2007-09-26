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

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

// Abstract class representing the action of clicking a link. These actions
// may change the history list.
public abstract class LinkAction extends Action
{
  private boolean isAddedToHistory_;
  private boolean isStaleBreadCrumb_;

  public LinkAction(Controller controller)
  {
    super(controller);
    isAddedToHistory_ = false;
    isStaleBreadCrumb_ = false;
  }

  // Determine if the action was successfully added to this history list.
  // This should be called after the run() method.
  public final boolean isAddedToHistory()
  {
    return isAddedToHistory_;
  }

  protected abstract boolean processLinkParameters(HttpServletRequest request);

  public final boolean populatePropertyTable(HttpServletRequest request)
  {
    String isHistoryString = request.getParameter(ActionInputs.ISHISTORY);
    if (isHistoryString != null)
      propertyTable_.put(ActionInputs.ISHISTORY,isHistoryString);
    return processLinkParameters(request);
  }

  // Add the action's URL to the controller's history list.
  protected final void addToHistory(int perspectiveId,String url)
  {
    if (!isHistoryAction())
      isAddedToHistory_ = controller_.addToHistory(perspectiveId,url);
  }

  public final boolean isHistoryAction()
  {
    return (propertyTable_.get(ActionInputs.ISHISTORY) != null);
  }

  public final void setStaleBreadCrumb()
  {
    if (isHistoryAction())
    {
      isStaleBreadCrumb_ = true;
      controller_.processStaleBreadCrumb();
    }
  }

  public final boolean isStaleBreadCrumb()
  {
    return isStaleBreadCrumb_;
  }

  // Call this when a stale breadcrumb is encountered while traversing history chain.
  public final String getNextHistoryActionLink()
  {
    return RetrieveHistoryAction.getActionLink(controller_.getHistoryDirection());
  }
}
