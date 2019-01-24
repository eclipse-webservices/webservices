/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.command;

import java.util.Vector;
import org.eclipse.core.runtime.IStatus;

/**
* A StatusMonitor reports IStatus information to a display system.
*/
public interface StatusMonitor
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * reports the contents of the given status object.
  * Subclasses must implement this method to report
  * IStatus information to a given display system,
  * such as SWT, the command line, or a trace file.
  */
  public int reportStatus(IStatus status, Vector options);
  public boolean reportStatus(IStatus status);
  public void dumpSavedStatus();
  public boolean canContinue();
  public void reset();
}
