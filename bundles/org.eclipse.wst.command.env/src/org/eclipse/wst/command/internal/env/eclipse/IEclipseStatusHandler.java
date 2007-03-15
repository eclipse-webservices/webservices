/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070314   176886 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.eclipse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.common.environment.IStatusHandler;

public interface IEclipseStatusHandler extends IStatusHandler
{
  /**
   * 
   * @return returns the worst status that has been reported.
   */	
  public IStatus getStatus();
  
  /**
   * Resets the worst status reported to be OK.
   *
   */
  public void resetStatus();
}
