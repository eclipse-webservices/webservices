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

import org.eclipse.wst.command.env.core.common.Choice;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.common.StatusException;
import org.eclipse.wst.command.env.core.common.StatusHandler;

/**
 * 
 */
public class J2EEStatusHandler implements StatusHandler
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.StatusHandler#report(org.eclipse.wst.command.env.core.common.Status, org.eclipse.wst.command.env.core.common.Choice[])
   */
  public Choice report(Status status, Choice[] choices)
  {
    // TODO Auto-generated method stub
    return null;
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.StatusHandler#report(org.eclipse.wst.command.env.core.common.Status)
   */
  public void report(Status status) throws StatusException
  {
    // TODO Auto-generated method stub
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.StatusHandler#reportError(org.eclipse.wst.command.env.core.common.Status)
   */
  public void reportError(Status status)
  {
    // TODO Auto-generated method stub
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.StatusHandler#reportInfo(org.eclipse.wst.command.env.core.common.Status)
   */
  public void reportInfo(Status status)
  {
    // TODO Auto-generated method stub
  }
}
