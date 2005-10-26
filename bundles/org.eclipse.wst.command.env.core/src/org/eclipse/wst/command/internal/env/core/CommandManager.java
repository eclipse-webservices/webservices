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
package org.eclipse.wst.command.internal.env.core;

import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;

/**
 * CommandManagers help manage the lifecycle of Commands.
 */
public interface CommandManager
{
  /**
   * Returns true if this CommandManager is capable of undoing
   * Commands. Some CommandManagers and Environments may be of
   * a sort that will never undo() Commands. Command.execute()
   * methods should take advantage of this method to optimize
   * out any caching logic whenever this method returns false.
   */
  public boolean isUndoEnabled ();

  // There's probably more, like factory methods for creating
  // Undo/Redo stacks and stuff like that.
  
  public DataMappingRegistry getMappingRegistry();
}
