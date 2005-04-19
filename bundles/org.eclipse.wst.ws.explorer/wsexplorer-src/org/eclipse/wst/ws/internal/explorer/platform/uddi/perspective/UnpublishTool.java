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

import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;

public class UnpublishTool extends FormTool
{
  public UnpublishTool(ToolManager toolManager, String enableImage, String highlightImage, String alt)
  {
    super(toolManager, enableImage, highlightImage, alt);
  }

  public String getSelectToolActionHref(boolean forHistory)
  {
    Node node = toolManager_.getNode();
    return SelectPropertiesToolAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId(),forHistory);
  }

  public void addAuthenticationProperties(RegistryElement regElement)
  {
    String publishURL = regElement.getPublishURL();
    String userId = regElement.getUserId();
    String password = regElement.getCred();

    if (publishURL == null)
      publishURL = "";
    if (userId == null)
      userId = "";
    if (password == null)
      password = "";

    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
  }

  public String getFormLink()
  {
    return "uddi/forms/UnpublishForm.jsp";
  }
}
