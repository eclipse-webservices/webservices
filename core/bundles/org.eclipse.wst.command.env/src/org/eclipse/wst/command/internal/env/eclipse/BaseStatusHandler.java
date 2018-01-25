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
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.StatusException;

public class BaseStatusHandler implements IEclipseStatusHandler
{
  private IStatus worstStatus = Status.OK_STATUS;
  
  public IStatus getStatus() 
  {
	return worstStatus;
  }

  public void resetStatus() 
  {
    worstStatus = Status.OK_STATUS;		
  }

  public void report(IStatus status) throws StatusException 
  {
    checkStatus(status);
  }

  public Choice report(IStatus status, Choice[] choices) 
  {
	checkStatus(status);
	return choices == null || choices.length == 0? null : choices[0];
  }

  public void reportError(IStatus status) 
  {
	checkStatus(status);
  }

  public void reportInfo(IStatus status) 
  {
	checkStatus(status);
  }
  
  protected void checkStatus( IStatus status )
  {
    if( status.getSeverity() > worstStatus.getSeverity() )
    {
      worstStatus = status;
    }
  }
}
