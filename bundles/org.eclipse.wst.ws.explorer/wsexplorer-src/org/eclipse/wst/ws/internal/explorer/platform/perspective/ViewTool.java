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

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;

import java.util.*;

public abstract class ViewTool extends FormTool
{
  private Hashtable viewToolManagers_;

  public ViewTool(ToolManager toolManager,String enabledImageLink,String highlightedImageLink,String alt)
  {
    super(toolManager,enabledImageLink,highlightedImageLink,alt);
    toolType_ = ToolTypes.VIEW;
    viewToolManagers_ = new Hashtable();
  }

  protected abstract void addSetDefaultViewTool(ToolManager viewToolManager,int index);
  protected abstract void addTools(ToolManager viewToolManager,int index);

  public final ToolManager createToolManager(int viewId)
  {
    ToolManager viewToolManager = new ToolManager(toolManager_.getNode());
    addTools(viewToolManager,viewId);
    addSetDefaultViewTool(viewToolManager,viewId);
    viewToolManagers_.put(String.valueOf(viewId),viewToolManager);
    return viewToolManager;
  }

  public final ToolManager getToolManager(int viewId)
  {
    return (ToolManager)(viewToolManagers_.get(String.valueOf(viewId)));
  }

  public final void clearViewToolManager(int viewId)
  {
    viewToolManagers_.remove(String.valueOf(viewId));
  }

  public final void clearViewToolManagers()
  {
    viewToolManagers_.clear();
  }
}
