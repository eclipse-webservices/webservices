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

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

public abstract class AddToWSDLPerspectiveTool extends ActionTool
{
  public AddToWSDLPerspectiveTool(ToolManager toolManager,String alt)
  {
    super(toolManager,"images/add_to_wsdl_perspective_enabled.gif","images/add_to_wsdl_perspective_highlighted.gif",alt);
  }
}
