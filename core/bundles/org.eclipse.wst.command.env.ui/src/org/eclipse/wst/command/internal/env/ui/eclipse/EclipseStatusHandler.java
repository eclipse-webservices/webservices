/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
 * 20070502   162287 makandre@ca.ibm.com - Andrew Mak, Server publish cancel button unavailable
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.eclipse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.core.EnvironmentCoreMessages;
import org.eclipse.wst.command.internal.env.eclipse.BaseStatusHandler;
import org.eclipse.wst.command.internal.env.ui.dialog.MessageDialog;
import org.eclipse.wst.command.internal.env.ui.dialog.StatusDialogConstants;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.StatusException;


/**
 * This is the Eclipse UI version of the IStatusHandler
 */
public class EclipseStatusHandler extends BaseStatusHandler
{
  private Shell   shell       = null;
    
  public EclipseStatusHandler()
  {  
  }
  
  public EclipseStatusHandler( Shell theShell )
  {
    shell = theShell;  
  }
    
  /**
   * @see org.eclipse.env.common.IStatusHandler#report(org.eclipse.env.common.Status, org.eclipse.env.common.Choice[])
   */
  public Choice report( final IStatus status, final Choice[] choices) 
  {
    final int[] result = new int[1];
    
    checkStatus( status );
    
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        result[0] =
          MessageDialog.openMessage( getShell(),
                                     EnvironmentCoreMessages.TITLE_WARNING, //TODO: Should be inferred from status' severity.
				                             null,
				                             status,
				                             choices);
      }
    };
    
    Display.getDefault().syncExec( runnable );
    
    for (int i = 0; i < choices.length; i++)
     {
      if (choices[i].getShortcut() == result[0] )
        return choices[i];
    }
    
    return null;
  }

  /**
   * @see org.eclipse.env.common.IStatusHandler#report(org.eclipse.env.common.Status)
   */
  public void report(IStatus status) throws StatusException
  {
    boolean userOk = false;
    
    checkStatus( status );
    
    switch (status.getSeverity())
    {
      // an error has been reported and we need to stop executing the comming
      // commands
      case Status.ERROR :
      {
        userOk = reportErrorStatus(status);
        break;
      }
      case Status.WARNING :
      {
        userOk = reportWarning(status);
        break;
      }     
      case Status.INFO :
      {
        userOk = true;
        reportInfo(status);
        break;
      }
    }
    
    if( !userOk ) throw new StatusException( status );
  }
  
  private boolean reportWarning( final IStatus status )
  {
    final int userResponse[] = new int[1];
    
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        userResponse[0] =
          MessageDialog.openMessage( getShell(),
                                     EnvironmentCoreMessages.TITLE_WARNING,
                                     null,
                                     status);
      }
    };
    
    Display.getDefault().syncExec( runnable );
    
    return (userResponse[0] == StatusDialogConstants.OK_ID);
  }

  private boolean reportErrorStatus(final IStatus status)
  {
	// no need to report an empty message with no exception
	if ("".equals(status.getMessage()) && status.getException() == null)
		return false;
	  
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        MessageDialog.openMessage( getShell(),
                                   EnvironmentCoreMessages.TITLE_ERROR,
                                   null,
                                   status);
      }
    };
    
    Display.getDefault().syncExec( runnable );
    
    return false;
  }
    
  /**
   * @see org.eclipse.wst.command.internal.env.core.common.IStatusHandler#reportError(org.eclipse.wst.command.internal.env.core.common.Status)
   */
  public void reportError(IStatus status)
  {
    checkStatus( status );
    reportErrorStatus( status );
  }
  
  /**
   * @see org.eclipse.wst.command.internal.env.core.common.IStatusHandler#reportInfo(org.eclipse.wst.command.internal.env.core.common.Status)
   */
  public void reportInfo(final IStatus status)
  {
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        MessageDialog.openMessage( getShell(),
                                   EnvironmentCoreMessages.TITLE_INFO,
                                   null,
                                   status);
      }
    };
    
    Display.getDefault().syncExec( runnable );
  }
  
  private Shell getShell()
  {
    if( shell != null ) return shell;
    
    Display display = Display.getDefault();
    
    return display == null ? null : display.getActiveShell();
  }
}
