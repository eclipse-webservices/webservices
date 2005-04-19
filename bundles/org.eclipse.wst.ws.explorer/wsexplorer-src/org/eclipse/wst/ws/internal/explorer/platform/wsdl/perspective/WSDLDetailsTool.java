/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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

public class WSDLDetailsTool extends DetailsTool {

  public static final int FORM_VIEW_ID = 0;
  public static final int SOURCE_VIEW_ID = 1;

  private int viewId_;

  public WSDLDetailsTool(ToolManager toolManager, String alt) {
    super(toolManager, alt, "wsdl/forms/WSDLDetailsForm.jsp");
    viewId_ = FORM_VIEW_ID;
  }

  public int getViewId() {
    return viewId_;
  }

  public void setViewId(int viewId) {
    if (viewId == FORM_VIEW_ID || viewId == SOURCE_VIEW_ID)
      viewId_ = viewId;
  }

  public void toggleViewId() {
    if (viewId_ == FORM_VIEW_ID)
      viewId_ = SOURCE_VIEW_ID;
    else
      viewId_ = FORM_VIEW_ID;
  }
}
