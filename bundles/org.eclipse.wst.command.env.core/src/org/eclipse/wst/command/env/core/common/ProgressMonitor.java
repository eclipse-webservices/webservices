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
package org.eclipse.wst.command.env.core.common;

public interface ProgressMonitor
{
  /**
   * Reports progress.
   */
  public void report ( String progress );

  /**
   * Returns a new progress monitor that will record progress
   * messages as "children" of the message most recently reported
   * thru the parent monitor.
   */
  public ProgressMonitor getChildProgressMonitor ();

  /**
   * Returns true if the environment has requested cancellation
   * of the operation in progress. It is entirely up to a Command
   * to decide if, and when, to consult this method.
   */
  public boolean isCancelRequested ();
}
