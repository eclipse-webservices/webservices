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
package org.eclipse.wst.ws.internal.explorer.platform.engine.transformer;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;

public class CurrentNodeSelectionTransformer implements ITransformer
{
  protected Controller controller;
  protected String key;

  public CurrentNodeSelectionTransformer(Controller controller)
  {
    this(controller, ActionInputs.NODEID);
  }

  public CurrentNodeSelectionTransformer(Controller controller, String key)
  {
    this.controller = controller;
    this.key = key;
  }

  public Hashtable normalize(Hashtable properties)
  {
    return properties;
  }
  
  public Hashtable deNormalize(Hashtable properties)
  {
    Node currNode = controller.getCurrentPerspective().getNodeManager().getSelectedNode();
    if (currNode != null)
      properties.put(key, String.valueOf(currNode.getNodeId()));
    return properties;
  }
}