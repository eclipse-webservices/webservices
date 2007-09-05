/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.CurrentNodeSelectionTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ViewTool;

public abstract class SelectNodeToolAction extends SelectNodeAction
{
  protected NodeManager nodeManager_;
  protected Tool selectedTool_;

  public SelectNodeToolAction(Controller controller,NodeManager nodeManager)
  {
    super(controller,nodeManager);
    nodeManager_ = nodeManager;
    selectedTool_ = null;
  }

  public final NodeManager getNodeManager()
  {
    return nodeManager_;
  }

  // ...jsp?nodeId=...&toolId=...<&viewId=...&viewToolId=...><&isHistory=1>
  protected boolean processLinkParameters(HttpServletRequest request)
  {
    boolean result = super.processLinkParameters(request);
    String toolIdString = request.getParameter(ActionInputs.TOOLID);
    String viewIdString = request.getParameter(ActionInputs.VIEWID);
    String viewToolIdString = request.getParameter(ActionInputs.VIEWTOOLID);

    // Perform data validation.
    try
    {
      Integer.parseInt(toolIdString);
    }
    catch (NumberFormatException e)
    {
      // Validation failed.
      return false;
    }
    propertyTable_.put(ActionInputs.TOOLID,toolIdString);

    // Optional properties
    try
    {
      int viewId = Integer.parseInt(viewIdString);
      propertyTable_.put(ActionInputs.VIEWID,viewIdString);
      if (viewId != ActionInputs.VIEWID_DEFAULT)
      {
        Integer.parseInt(viewToolIdString);
        propertyTable_.put(ActionInputs.VIEWTOOLID,viewToolIdString);
      }
    }
    catch (NumberFormatException e)
    {
    }
    return result;
  }

  public boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    if (!isStaleNode(nodeId))
    {
      // Ensure that the node is visible.
      makeNodeVisible(nodeManager_.getNode(nodeId));
      setSelectedNodeId(nodeId);
      selectedNode_ = nodeManager_.getSelectedNode();
      int toolId = Integer.parseInt((String)propertyTable_.get(ActionInputs.TOOLID));
      ToolManager toolManager = selectedNode_.getToolManager();
      toolManager.setSelectedToolId(toolId);
      selectedTool_ = toolManager.getSelectedTool();
      String viewIdString = ((String)propertyTable_.get(ActionInputs.VIEWID));
      if (viewIdString != null)
      {
        int viewId = Integer.parseInt(viewIdString);
        setSelectedViewId(viewId);
        if (viewId != ActionInputs.VIEWID_DEFAULT)
        {
          String viewToolIdString = ((String)propertyTable_.get(ActionInputs.VIEWTOOLID));
          ViewTool viewTool = (ViewTool)selectedTool_;
          if (viewToolIdString != null)
          {
            int viewToolId = Integer.parseInt(viewToolIdString);
            ToolManager viewToolManager = selectedNode_.getViewToolManager();
            if (viewToolManager == null)
            {
              if (!isHistoryAction())
              {
                viewToolManager = viewTool.createToolManager(viewId);
                viewToolId = viewToolManager.getSelectedToolId();
              }
              else
              {
                setStaleBreadCrumb();
                return false;
              }
            }
            viewToolManager.setSelectedToolId(viewToolId);
          }
        }
      }
      else
        setSelectedViewId(ActionInputs.VIEWID_DEFAULT);
      addToHistory(getPerspectiveId(),getActionLinkForHistory());
      return true;
    }
    setStaleBreadCrumb();
    return false;
  }

  public ITransformer[] getTransformers()
  {
    return new ITransformer[] {new CurrentNodeSelectionTransformer(controller_)};
  }

  public final Tool getSelectedViewTool()
  {
    if (propertyTable_.get(ActionInputs.VIEWID) != null)
      return selectedNode_.getViewToolManager().getSelectedTool();
    return null;
  }

  public final Tool getSelectedTool()
  {
    return selectedTool_;
  }

  // Selecting a node tool updates the properties content. Also, in the event
  // of a history switch, nodes may need to be selected resulting in changes to
  // both the treeview content and the properties container. These requirements are
  // covered through the base class.
  public abstract String getPropertiesContentVar();
  public abstract String getPropertiesContentPage();
  public String getStatusContentVar()
  {
    return null;
  }

  public String getStatusContentPage()
  {
    return null;
  }
  
  public abstract int getPerspectiveId();
}
