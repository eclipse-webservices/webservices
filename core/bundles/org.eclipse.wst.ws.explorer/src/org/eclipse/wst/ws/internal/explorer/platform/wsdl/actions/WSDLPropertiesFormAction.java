/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.FormAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public abstract class WSDLPropertiesFormAction extends FormAction
{
  protected NodeManager navigatorManager_;
  public WSDLPropertiesFormAction(Controller controller)
  {
    super(controller);
    navigatorManager_ = controller.getWSDLPerspective().getNodeManager();
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String[] keys = parser.getParameterNames();
    for (int i = 0; i < keys.length; i++)
    {
      String[] values = parser.getParameterValues(keys[i]);
      if (values != null)
      {
        if (values.length == 1)
          propertyTable_.put(keys[i], values[0]);
        else if (values.length > 1)
          propertyTable_.put(keys[i], values);
      }
    }
    return true;
  }

  public Node getSelectedNavigatorNode()
  {
    return navigatorManager_.getSelectedNode();
  }

  public FormTool getSelectedFormTool()
  {
    return (FormTool)(getSelectedNavigatorNode().getCurrentToolManager().getSelectedTool());
  }
}
