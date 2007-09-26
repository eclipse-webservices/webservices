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
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListManager;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ViewTool;

// Abstract action class used for both Link and Form actions.
public abstract class ClearViewElementAction extends LinkAction
{
  private String listManagerRel_;
  private NodeManager nodeManager_;
  public ClearViewElementAction(Controller controller,NodeManager nodeManager,String listManagerRel)
  {
    super(controller);
    nodeManager_  = nodeManager;
    listManagerRel_ = listManagerRel;
  }

  // ...jsp?viewId=...
  protected final boolean processLinkParameters(HttpServletRequest request)
  {
    String viewIdString = request.getParameter(ActionInputs.VIEWID);
    // Perform data validation.
    try
    {
      Integer.parseInt(viewIdString);
    }
    catch (NumberFormatException e)
    {
      // Validation failed!
      return false;
    }
    propertyTable_.put(ActionInputs.VIEWID,viewIdString);
    return true;
  }

  public final boolean run()
  {
    int viewId = Integer.parseInt((String)propertyTable_.get(ActionInputs.VIEWID));
    Node selectedNode = nodeManager_.getSelectedNode();
    ViewTool viewTool = (ViewTool)selectedNode.getToolManager().getSelectedTool();
    if (viewTool.getToolManager(viewId) == null)
    {
      // Stale view.
      return false;
    }
    TreeElement element = selectedNode.getTreeElement();
    ListManager listManager = (ListManager)element.getPropertyAsObject(listManagerRel_);
    listManager.removeElementWithViewId(viewId);
    viewTool.clearViewToolManager(viewId);
    selectedNode.setViewId(ActionInputs.VIEWID_DEFAULT);
    // Do not add this to the history.
    return true;
  }

  public final String getActionLinkForHistory()
  {
    return null;
  }

  public abstract String getPropertiesContainerVar();
  public abstract String getPropertiesContainerPage();
  public abstract String getStatusContentVar();
  public abstract String getStatusContentPage();
}
