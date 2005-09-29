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

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;

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
