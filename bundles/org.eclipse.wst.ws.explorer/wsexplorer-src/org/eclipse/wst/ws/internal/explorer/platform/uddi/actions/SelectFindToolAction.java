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
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;

public class SelectFindToolAction extends SelectPropertiesToolAction
{
  protected String subQueryKey_;
  protected boolean toolLinkChanged_;

  public SelectFindToolAction(Controller controller)
  {
    super(controller);
    subQueryKey_ = null;
    toolLinkChanged_ = false;
  }

  public boolean processLinkParameters(HttpServletRequest request)
  {
    boolean result = super.processLinkParameters(request);
    subQueryKey_ = request.getParameter(UDDIActionInputs.SUBQUERY_KEY);
    return result;
  }

  // /uddi/actions/SelectFindToolAction.jsp?nodeId=...&toolId=...<&viewId=...&viewToolId=...>&subQueryKey=...<&isHistory=1>
  public static final String getActionLink(int nodeId,int toolId,int viewId,int viewToolId,String subQueryKey,boolean forHistory)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/SelectFindToolActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    actionLink.append('&').append(ActionInputs.TOOLID).append('=').append(toolId);
    if (viewId != ActionInputs.VIEWID_DEFAULT)
    {
      actionLink.append('&').append(ActionInputs.VIEWID).append('=').append(viewId);
      actionLink.append('&').append(ActionInputs.VIEWTOOLID).append('=').append(viewToolId);
    }
    actionLink.append('&').append(UDDIActionInputs.SUBQUERY_KEY).append('=').append(subQueryKey);

    if (forHistory)
      actionLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
    return actionLink.toString();
  }

  public boolean run()
  {
    if (!super.run())
      return false;
    FormTool formTool = (FormTool)(selectedNode_.getCurrentToolManager().getSelectedTool());
    String currentSubQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
    if (subQueryKey_ != null && !subQueryKey_.equals(currentSubQueryKey))
    {
      toolLinkChanged_ = true;
      formTool.setProperty(UDDIActionInputs.SUBQUERY_KEY,subQueryKey_);
    }
    return true;
  }

  public boolean requiresViewSelection()
  {
    return toolLinkChanged_;
  }

  protected String getActionLinkForHistory()
  {
    int nodeId = selectedNode_.getNodeId();
    int toolId = selectedTool_.getToolId();
    int viewId = selectedNode_.getViewId();
    int viewToolId = selectedNode_.getViewToolId();
    return getActionLink(nodeId,toolId,viewId,viewToolId,subQueryKey_,true);
  }
}
