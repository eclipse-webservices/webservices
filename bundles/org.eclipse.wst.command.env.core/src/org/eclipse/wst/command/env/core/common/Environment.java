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
package org.eclipse.wst.command.env.core.common;

import org.eclipse.wst.command.env.core.CommandManager;
import org.eclipse.wst.command.env.core.uri.URIFactory;


/**
 * An Environment provides the means for a 
 * <ol>
 * <li>A log for writing messages to a logging facility,</li>
 * <li>A progress monitor for receiving progress information,</li>
 * <li>A status handler for receiving and processing status reports,</li>
 * <li>A factory for the handling of URIs (resources).</li>
 * </ol>
 */
public interface Environment
{
  /**
   * Returns a logging facility.
   */
  public Log getLog ();

  /**
   * Returns a progress monitor.
   */
  public ProgressMonitor getProgressMonitor ();

  /**
   * Returns a status handler.
   */
  public StatusHandler getStatusHandler ();

  /**
   * Returns a URI factory.
   * Hint: Implementers should insure that the Factory they return
   * has a reference to this Environment so that URI objects can
   * report progress and announce status.
   */
  public URIFactory getURIFactory ();

  /**
   * Returns a compiler appropriate to the environment.
   */
  public JavaCompiler getJavaCompiler ();

  /**
   * Returns an object that helps manage execution/undoing of Commands.
   */
  public CommandManager getCommandManager ();
}
