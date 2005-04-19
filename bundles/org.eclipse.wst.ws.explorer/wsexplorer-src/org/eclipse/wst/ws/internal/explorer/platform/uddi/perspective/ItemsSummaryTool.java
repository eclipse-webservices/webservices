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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ProxyLoadPageAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectPropertiesToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;

public class ItemsSummaryTool extends FormTool
{
  private String formLink_;

  public ItemsSummaryTool(ToolManager toolManager,String enabledImage,String highlightedImage,String alt,int queryItem)
  {
    super(toolManager,enabledImage,highlightedImage,alt);
    StringBuffer formLink = new StringBuffer("uddi/forms/ItemsSummaryForm.jsp?");
    formLink.append(UDDIActionInputs.QUERY_ITEM).append('=').append(queryItem);
	String encodedFormLink = URLUtils.encode(formLink.toString());
    formLink_ = ProxyLoadPageAction.getActionLink(encodedFormLink);
  }

  public final void initDefaultProperties()
  {
  }

  public String getSelectToolActionHref(boolean forHistory)
  {
    Node node = toolManager_.getNode();
    return SelectPropertiesToolAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId(),forHistory);
  }

  public String getFormLink()
  {
    return formLink_;
  }
}
