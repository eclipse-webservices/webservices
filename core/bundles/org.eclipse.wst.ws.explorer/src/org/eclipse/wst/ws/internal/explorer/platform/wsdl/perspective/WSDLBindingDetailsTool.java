/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;

public class WSDLBindingDetailsTool extends DetailsTool {

  public WSDLBindingDetailsTool(ToolManager toolManager, String alt) {
    super(toolManager, alt, "wsdl/forms/WSDLBindingDetailsForm.jsp");
  }
}
