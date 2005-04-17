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
package org.eclipse.wst.command.internal.env.ui.eclipse;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.ui.dialog.MessageDialog;
import org.eclipse.wst.command.internal.env.ui.dialog.StatusDialogConstants;
import org.eclipse.wst.command.internal.provisional.env.core.common.Choice;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusException;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;


/**
 * This is the Eclipse UI version of the StatusHandler
 */
public class EclipseStatusHandler implements StatusHandler
{
  private Shell        shell_;
  private MessageUtils msg_;
  
  public EclipseStatusHandler()
  {
    this( new Shell() );
  }

  public EclipseStatusHandler(Shell shell)
  {
    msg_   = new MessageUtils( "org.eclipse.wst.command.internal.env.common.environment", this );
    shell_ = shell;
  }
  
  /**
   * @see org.eclipse.env.common.StatusHandler#report(org.eclipse.env.common.Status, org.eclipse.env.common.Choice[])
   */
  public Choice report(Status status, Choice[] choices) 
  {
    int result =
    MessageDialog.openMessage(
        shell_,
        msg_.getMessage("TITLE_WARNING"),
				null,
				status,
				choices);
    for (int i = 0; i < choices.length; i++)
     {
      if (choices[i].getShortcut() == result)
        return choices[i];
    }
    
    return null;
  }

  /**
   * @see org.eclipse.env.common.StatusHandler#report(org.eclipse.env.common.Status)
   */
  public void report(Status status) throws StatusException
  {
    boolean userOk = false;
    
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
  
  private boolean reportWarning(Status status)
  {
    int userResponse =
      MessageDialog.openMessage(
        shell_,
        msg_.getMessage("TITLE_WARNING"),
        null,
        status);
    return (userResponse == StatusDialogConstants.OK_ID);
  }

  private boolean reportErrorStatus(Status status)
  {
    MessageDialog.openMessage(
      shell_,
      msg_.getMessage("TITLE_ERROR"),
      null,
      status);
    return false;
  }
  
  /**
   * @see org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler#reportError(org.eclipse.wst.command.internal.provisional.env.core.common.Status)
   */
  public void reportError(Status status)
  {
    reportErrorStatus( status );
  }
  
  /**
   * @see org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler#reportInfo(org.eclipse.wst.command.internal.provisional.env.core.common.Status)
   */
  public void reportInfo(Status status)
  {
    MessageDialog.openMessage(
        shell_,
        msg_.getMessage("TITLE_INFO"),
        null,
        status);
  }
}
