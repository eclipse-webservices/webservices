/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.List;
import java.util.Vector;

public class ToolManager
{
  private Vector tools_;
  private int selectedToolId_;
  private Node node_;

  public ToolManager(Node node)
  {
    tools_ = new Vector();
    selectedToolId_ = 0;
    node_ = node;
  }

  public final int getSelectedToolId()
  {
    return selectedToolId_;
  }

  public final int setSelectedToolId(int toolId)
  {
    if (toolId >= 0 && toolId < tools_.size())
      selectedToolId_ = toolId;
    return selectedToolId_;
  }

  public final int addTool(Tool tool)
  {
    tools_.addElement(tool);
    return tools_.size()-1;
  }

  public final Tool getTool(int toolId)
  {
    if (toolId >=0 && toolId < getNumberOfTools())
      return (Tool)tools_.elementAt(toolId);
    return null;
  }
  
  public final List getTools()
  {
    return tools_;
  }

  public final Tool getSelectedTool()
  {
    if (selectedToolId_ >=0 && selectedToolId_ < getNumberOfTools())
      return (Tool)tools_.elementAt(selectedToolId_);
    return null;
  }

  public final int getNumberOfTools()
  {
    return tools_.size();
  }

  public final Node getNode()
  {
    return node_;
  }
}
