/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.command;

import java.util.Vector;

import org.eclipse.core.runtime.IStatus;

/**
* A NullStatusRenderer throws away IStatus information.
*/
public class NullStatusMonitor implements StatusMonitor
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  protected boolean result;

  /**
  * Default constructor.
  */
  public NullStatusMonitor ()  {}
  /**
  * Does nothing.
  */
  public int reportStatus(IStatus status, Vector options) { return 0; }
  public boolean reportStatus(IStatus status) 
  { 
    switch (status.getSeverity())
    {
      case IStatus.ERROR:
      case IStatus.WARNING:
      		result = false;
      		break;
	  case IStatus.INFO:
      default: //IStatus.OK
      		result = true; 
      		break;
     }
 	return result;
  }
  public void dumpSavedStatus() { }
  public boolean canContinue() 
  { 
	return result;
  }

  public void reset () {
  		result = true;
  	}
}

