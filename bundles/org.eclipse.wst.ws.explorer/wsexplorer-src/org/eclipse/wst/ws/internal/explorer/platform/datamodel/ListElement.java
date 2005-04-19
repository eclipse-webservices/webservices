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

package org.eclipse.wst.ws.internal.explorer.platform.datamodel;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;

public class ListElement
{
  private int viewId_;
  private Object object_;
  // For search parameters: info on the Node and ViewTool holding this element.
  private int targetNodeId_;
  private int targetToolId_;
  private int targetViewId_;

  public ListElement(Object object)
  {
    object_ = object;
    viewId_ = ActionInputs.VIEWID_DEFAULT;
    targetNodeId_ = -1;
    targetToolId_ = -1;
    targetViewId_ = ActionInputs.VIEWID_DEFAULT;
  }

  public final Object getObject()
  {
    return object_;
  }

  public final void setViewId(int viewId)
  {
    viewId_ = viewId;
  }

  public final int getViewId()
  {
    return viewId_;
  }

  public final void setTargetViewToolInfo(int nodeId,int toolId,int viewId)
  {
    targetNodeId_ = nodeId;
    targetToolId_ = toolId;
    targetViewId_ = viewId;
  }

  public final int getTargetNodeId()
  {
    return targetNodeId_;
  }

  public final int getTargetToolId()
  {
    return targetToolId_;
  }

  public final int getTargetViewId()
  {
    return targetViewId_;
  }

  public final String toString() {
    return getObject().toString();
  }
}
