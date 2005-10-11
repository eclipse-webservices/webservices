/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ToolTypes;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ViewTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

public class SelectWSILToolAction extends SelectNodeToolAction
{
  public SelectWSILToolAction(Controller controller)
  {
    super(controller, controller.getWSILPerspective().getNodeManager());
  }

  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length + 1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length - 1] = new WSILViewSelectionTransformer(controller_);
    return transformers;
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID, boolean keepHistory)
  {
    StringBuffer actionLink = new StringBuffer("wsil/actions/SelectWSILToolActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID);
    actionLink.append('=');
    actionLink.append(nodeID);
    actionLink.append('&');
    actionLink.append(ActionInputs.TOOLID);
    actionLink.append('=');
    actionLink.append(toolID);
    if (viewID != ActionInputs.VIEWID_DEFAULT)
    {
      actionLink.append('&');
      actionLink.append(ActionInputs.VIEWID);
      actionLink.append('=');
      actionLink.append(viewID);
      actionLink.append('&');
      actionLink.append(ActionInputs.VIEWTOOLID);
      actionLink.append('=');
      actionLink.append(viewToolID);
    }
    if (keepHistory)
    {
      actionLink.append('&');
      actionLink.append(ActionInputs.ISHISTORY);
      actionLink.append("=1");
    }
    return actionLink.toString();
  }

  protected String getActionLinkForHistory()
  {
    boolean keepHistory = false;
    Node selectedNode = getSelectedNode();
    Tool selectedTool = getSelectedTool();
    int toolType = getSelectedTool().getToolType();
    if (toolType == ToolTypes.FORM)
      keepHistory = true;
    else if (toolType == ToolTypes.VIEW)
    {
      int viewID = selectedNode.getViewId();
      if (viewID == ActionInputs.VIEWID_DEFAULT)
        keepHistory = true;
      else
      {
        if (((ViewTool) selectedTool).getToolManager(viewID).getTool(selectedNode.getViewToolId()).getToolType() != ToolTypes.ACTION)
          keepHistory = true;
      }
    }
    if (keepHistory)
    {
      return getActionLink(selectedNode.getNodeId(), selectedTool.getToolId(), selectedNode.getViewId(), selectedNode.getViewToolId(), true);
    }
    else
      return null;
  }

  public final String getTreeContentVar()
  {
    return "wsilNavigatorContent";
  }

  public final String getTreeContentPage()
  {
    return "wsil/wsil_navigator_content.jsp";
  }

  public final String getPropertiesContainerVar()
  {
    return "wsilPropertiesContainer";
  }

  public final String getPropertiesContainerPage()
  {
    return "wsil/wsil_properties_container.jsp";
  }

  public final String getPropertiesContentVar()
  {
    return "wsilPropertiesContent";
  }

  public final String getPropertiesContentPage()
  {
    return "wsil/wsil_properties_content.jsp";
  }

  public final int getPerspectiveId()
  {
    return ActionInputs.PERSPECTIVE_WSIL;
  }
}
