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
package org.eclipse.jst.ws.internal.axis.consumption.ui.env;

import org.eclipse.wst.command.env.core.CommandManager;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;

/**
 * 
 */
public class J2EECommandManager implements CommandManager
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.CommandManager#isUndoEnabled()
   */
  public boolean isUndoEnabled()
  {
    return false;
  }

public DataMappingRegistry getMappingRegistry() {
	return null;
}
  
}
