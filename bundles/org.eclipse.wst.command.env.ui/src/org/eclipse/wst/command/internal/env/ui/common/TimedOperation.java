/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060504   119296 pmoogk@ca.ibm.com - Peter Moogk
 * 20060822   154750 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.common;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.ui.EnvironmentUIMessages;

public class TimedOperation implements IUndoableOperation
{
  private AbstractOperation operation;
  private int               timeout;
  private IProgressMonitor  tempMonitor;
  private IAdaptable        tempInfo;
  private IStatus           returnStatus;
  private boolean           operationComplete;
  private String            timeOutMessage;
  
  public TimedOperation( AbstractOperation operation, int timeout, String timeOutMessage )
  {
    this.operation      = operation;
    this.timeout        = timeout;
    this.timeOutMessage = timeOutMessage;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) 
  {
    Thread  executeThread = new Thread( new OperationRunnable() );
    
    returnStatus      = Status.OK_STATUS;
    tempMonitor       = monitor;
    tempInfo          = info;
    operationComplete = false;
    executeThread.start();
    
    synchronized( operation )
    {
      while( !operationComplete )
      {
        try
        {
          operation.wait(timeout);
        }
        catch( InterruptedException exc )
        {
          String message     = exc.getMessage();
          Status errorStatus = new Status( IStatus.ERROR,"id", 0, message == null ? "" : message, exc );
          executeThread.interrupt();
          return errorStatus;
        }
        
        if( !operationComplete )
        {
          // We timed out, since the execution thread hasn't set operationComplete 
          // to true.
          Status  errorStatus = new Status( IStatus.ERROR,"id", 0, EnvironmentUIMessages.MSG_ERROR_OPERATION_TIMED_OUT, null);
          Shell   shell       = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
          boolean waitMore    = MessageDialog.openConfirm(shell, EnvironmentUIMessages.MSG_ERROR_OPERATION_TIMED_OUT, timeOutMessage );
          
          if( !waitMore )
          {
            executeThread.interrupt();
            operationComplete = true;
            return errorStatus;
          }
        }
      }
    }
    
    // We completed successfully.  Therefore return the status set in the forked
    // thread.
    return returnStatus;
  }

  private class OperationRunnable implements Runnable
  {
    public void run()
    {
      try
      {
        returnStatus = operation.execute(tempMonitor, tempInfo);
      }
      catch( Throwable exc )
      {
        String message = exc.getMessage();
        
        returnStatus = new Status( IStatus.ERROR,"id", 0, message == null ? "" : message, exc );
      }
      finally
      {
        synchronized( operation )
        {
          operationComplete = true;
          operation.notify();
        }        
      }
    }
  }
  
  public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return operation.redo(monitor, info);
  }

  public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return operation.undo(monitor, info);
  }
  public IUndoContext[] getContexts()
  {
    return operation.getContexts();
  }

  public boolean hasContext(IUndoContext context)
  {
    return operation.hasContext(context);
  }

  public void addContext(IUndoContext context)
  {
    operation.addContext(context);
  }

  public boolean canExecute()
  {
    return operation.canExecute();
  }

  public boolean canRedo()
  {
    return operation.canRedo();
  }

  public boolean canUndo()
  {
    return operation.canUndo();
  }

  public void dispose()
  {
    operation.dispose();
  }

  public String getLabel()
  {
    return operation.getLabel();
  }

  public void removeContext(IUndoContext context)
  {
    operation.removeContext(context);
  }

  public void setLabel(String name)
  {
    operation.setLabel(name);
  }
}
