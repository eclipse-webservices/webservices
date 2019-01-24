/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ProxyLoadPageAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public class RegistryDetailsTool extends DetailsTool
{
  public RegistryDetailsTool(ToolManager toolManager,String alt)
  {
    super(toolManager,alt,ProxyLoadPageAction.getActionLink("uddi/forms/RegDetailsForm.jsp"));
  }

  public final void initDefaultProperties()
  {
    clearPropertyTable();
    toolManager_.getNode().getNodeManager().getController();
    RegistryElement regElement = (RegistryElement)(toolManager_.getNode().getTreeElement());
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME,regElement.getName());
  }
}
