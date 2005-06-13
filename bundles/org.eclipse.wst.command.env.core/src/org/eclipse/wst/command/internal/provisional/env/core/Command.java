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
package org.eclipse.wst.command.internal.provisional.env.core;

import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

/**
 * Commands are executable, undoable, redoable objects.
 * Every Command has a name and a description.
 */
public interface Command
{
  /**
   * 
   * @return Returns a unique ID for this Command.
   */
  public String getId();
  
  /**
   * Returns the name of the Command, a short translatable string
   * indicating what the Command does. Can be null.
   * @return The name of the Command. Can be null.
   */
  public String getName ();

  /**
   * Returns the description of the Command. Can be null.
   * @return The description of the Command. Can be null.
   */
  public String getDescription ();
  
  /**
   * Executes the Command.
   * @param environment The environment.
   * Must not be null.
   * @return A <code>Status</code> object indicating the degree
   * to which the <code>execute</code> method was successful.
   * A valud of <code>null</code>, or a Status with a severity
   * of less than <code>Status.ERROR</code> signifies success.
   */
  public Status execute ( Environment environment );

  /**
   * Returns <code>true</code> if, and only if, the Command's
   * <code>undo</code> method is supported.
   * @return True if the Command supports being undone.
   */
  public boolean isUndoable ();

  /**
   * Undoes the Command.
   * @param environment The environment.
   * Must not be null.
   * @return A <code>Status</code> object indicating the degree
   * to which the <code>undo</code> method was successful.
   * A valud of <code>null</code>, or a Status with a severity
   * of less than <code>Status.ERROR</code> signifies success.
   */
  public Status undo ( Environment environment );

  /**
   * Returns <code>true</code> if, and only if, the Command's
   * <code>redo</code> method is supported.
   * @return True if the Command supports being redone.
   */
  public boolean isRedoable ();

  /**
   * Re-executes the Command.
   * @param environment The environment.
   * Must not be null.
   * @return A <code>Status</code> object indicating the degree
   * to which the <code>redo</code> method was successful.
   * A value of <code>null</code>, or a Status with a severity
   * of less then <code>Status.ERROR</code> signifies success.
   */
  public Status redo ( Environment environment );
}
