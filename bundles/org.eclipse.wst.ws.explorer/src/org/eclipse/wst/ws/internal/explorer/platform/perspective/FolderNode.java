/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;

public abstract class FolderNode extends Node
{
  public FolderNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"images/folder_closed.gif");
  }

  public String getOpenImagePath()
  {
    return "images/folder_open.gif";
  }
}
