/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060223   129232 pmoogk@ca.ibm.com - Peter Moogk
 * 20070314   176886 pmoogk@ca.ibm.com - Peter Moogk
 * 20070730   197144 pmoogk@ca.ibm.com - Peter Moogk, Pass progress monitor to undo commands.
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentEngine;
import org.eclipse.wst.command.internal.env.core.fragment.FragmentListener;
import org.eclipse.wst.command.internal.env.eclipse.BaseStatusHandler;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.common.environment.ILog;


/**
 * This class manages the execution of Commands in a flow of CommandFragments.
 * The bulk of this work is done by the CommandFragmentEngine.  The main job of
 * this class is run the fragments in a particular transactional environment.
 * A fragment has a method called doNotRunInTransaction which is used to determine
 * if its commands should be run in a transaction or not.  If this method returns
 * true(ie. not in a transaction) the Commands are wrapped in a IRunnableWithProgress.
 * If false( ie. in a transaction ) the Commands are wrapped in a WorkspaceModifyOperation. 
 *
 */
public class SimpleCommandEngineManager
{
  protected CommandFragmentEngine engine_;
  protected DataFlowManager       dataManager_;
  protected   EclipseEnvironment    environment_;
  
  private   boolean               foundStop_;
  private   boolean               doNotRunInTransaction_;
  
  public SimpleCommandEngineManager( EclipseEnvironment environment, DataFlowManager dataManager )
  {
    environment_ = environment;
    dataManager_ = dataManager;
  }
  
  /**
   * Sets the root fragment.  The execution engine will start executing commands
   * at the beginning of this root fragment.
   * 
   * @param root the root fragment.
   */
  public void setRootFragment( CommandFragment root )
  {
  	engine_ = new CommandFragmentEngine( root, dataManager_, environment_ );
	
	  environment_.setCommandManager( engine_ );
  	
    engine_.setPeekFragmentListener( 
      new FragmentListener()
      {
        public  boolean notify( CommandFragment fragment )
        {
          return peekFragment( fragment );
        }
      } );  
                  
    engine_.setNextFragmentListener( 
      new FragmentListener()
      {
        public  boolean notify( CommandFragment fragment )
        {
          return nextFragment( fragment );
        }
      } );  
    
    engine_.setAfterExecuteFragmentListener( 
        new FragmentListener()
        {
          public  boolean notify( CommandFragment fragment )
          {
            return afterExecuteNextFragment( fragment );
          }
        } );  
                                                             
	  engine_.setUndoFragmentListener(
      new FragmentListener()
      {
        public boolean notify( CommandFragment fragment )
        {
          return undoFragment( fragment );
        }
      } );               
  }

  protected boolean afterExecuteNextFragment( CommandFragment fragment )
  {
    boolean           continueExecute = true;
    BaseStatusHandler statusHandler   = (BaseStatusHandler)environment_.getStatusHandler();
    IStatus           commandStatus   = engine_.getLastStatus();
    IStatus           handlerStatus   = statusHandler.getStatus();
    
    if( commandStatus.getSeverity() == IStatus.ERROR &&
        handlerStatus.getSeverity() != IStatus.ERROR )
    {
      // The command returned an error status for the engine to stop,
      // but the status handler did not have report and error.
      // Therefore, we will report this status returned by the command
      // if there is a message to display.  
      String errorMessage = commandStatus.getMessage();
      
      if( errorMessage != null && errorMessage.length() > 0 )
      {
        statusHandler.reportError( commandStatus );
      }
    }
    else if( commandStatus.getSeverity() != IStatus.ERROR &&
             handlerStatus.getSeverity() == IStatus.ERROR )
    {
      // The last command didn't return an error, but the status handler
      // did report and error.  Therefore, we should stop.
      continueExecute = false;   
    }
    
    return continueExecute;
  }
  
  /**
   * The CommandFragmentEngine calls this method when it is peeking forward
   * in the fragments.  When peeking forward the command stack state in the
   * engine is not changes.
   * 
   * 
   * @param fragment the fragment that it is peeking at.
   * @return Indicates whether peeking should stop or not.
   */
  protected boolean peekFragment( CommandFragment fragment )
  {     
    return true;
  }
  
  /**
   * The CommandFragmentEngine calls this method when it is moving forward
   * in the fragments.   When moving forward the command stack state is saved
   * at each fragment is traversed.
   * 
   * @param fragment the fragment that is being traversed.
   * @return indicates if the forward traversal should continue.  
   */
  protected boolean nextFragment( CommandFragment fragment )
  {     
    if( fragment.doNotRunInTransaction() != doNotRunInTransaction_ )
    {
      // The fragment is requesting a different transaction environment than
      // the one that we are in so we must stop and change environments.
      doNotRunInTransaction_ = fragment.doNotRunInTransaction();
      foundStop_             = false;      
    }
    
    return foundStop_;
  }
  
  /**
   * This method is called for each fragment when the command engine is unwinding
   * its stack during an undo operation.
   * 
   * @param fragment  the fragment being undone.
   * @return returns true if the undo process should continue.
   */
  protected boolean undoFragment( CommandFragment fragment )
  {  	  	
    return true;
  }
    
  /**
   * This method undoes the commands that were executed in the runForwardToNextStop
   * method.
   * @param context the runnable context.
   * @return returns if all commands have been undone or not.
   */
  public boolean runUndoToNextStop( IRunnableContext context )
  {
    final boolean result[] = new boolean[]{true};
    
    IRunnableWithProgress undoOperation 
      = new IRunnableWithProgress()
        {
          public void run(IProgressMonitor monitor)
          {
            result[0] = engine_.undoToLastStop(monitor);
          }
        };
        
    try
    {      
      if( context == null )
      {
        // We don't have a container yet so just run the operation.
        undoOperation.run( null );
      }
      else
      {
        // We have a container where this operation can run and have
        // its progress displayed.
        context.run( false, false, undoOperation );
      }
    }
    catch( Exception exc )
    {
      // For now we will ignore all exceptions.
    } 
    
    return result[0];
  }
  
  /**
   * The method executes the CommandFragment commands in the context provided.
   * 
   * @param context the context
   * @return returns the status of executing the commands.
   */
  public IStatus runForwardToNextStop( IRunnableContext context )
  {
    IRunnableWithProgress operation = null;
    
  	doNotRunInTransaction_ = false;
  	
  	do
  	{
  	  // We will stop unless we are changing transaction modes.
      foundStop_ = true;
      
  	  if( doNotRunInTransaction_ )
  	  {
  	    operation = getNoTransactionOperation();
  	  }
  	  else
  	  {
  	    operation = getTransactionOperation();
  	  }
  	  
   	  try
  	  {  	   
  	    if( context == null )
  	    {
  	   	  // We don't have a container yet so just run the operation.
  	   	  operation.run( null );
  	    }
  	    else
  	    {
  	   	  // We have a container where this operation can run and have
  	   	  // its progress displayed.
  	      context.run( false, false, operation );
  	    }
  	  }
  	  catch( InterruptedException exc )
  	  {
  	    //TODO should log these exceptions.
  	    exc.printStackTrace();
  	  }
  	  catch( InvocationTargetException exc )
  	  {
  	    //TODO should log these exceptions.
  	    exc.printStackTrace();
  	  }
  	}  
  	while( !foundStop_ ); 
  	
  	return engine_.getLastStatus();
  }
  
  private IRunnableWithProgress getTransactionOperation()
  {
    IRunnableWithProgress operation = new IRunnableWithProgress()
    {
      public void run( IProgressMonitor monitor )
      {
        environment_.getLog().log(ILog.INFO, "command", 5002, this, "getTransactionOperation", "Start of transaction");
        
        BaseStatusHandler statusHandler = (BaseStatusHandler)environment_.getStatusHandler();
        
        statusHandler.resetStatus();
        engine_.moveForwardToNextStop( monitor );
        
        
        environment_.getLog().log(ILog.INFO, "command", 5003, this, "getTransactionOperation", "End of transaction");
      }
    };
    
    return operation;
  }
  
  private IRunnableWithProgress getNoTransactionOperation()
  {
    IRunnableWithProgress operation = new IRunnableWithProgress()
    {
      public void run( IProgressMonitor monitor )
      {
        environment_.getLog().log(ILog.INFO, "command", 5085, this, "getNoTransactionOperation", "Start of NON transaction");
        
        BaseStatusHandler statusHandler = (BaseStatusHandler)environment_.getStatusHandler();
        
        statusHandler.resetStatus();
        engine_.moveForwardToNextStop( monitor );
        environment_.getLog().log(ILog.INFO, "command", 5086, this, "getNoTransactionOperation", "End of NON transaction");
      }
    }; 
    
    return operation;
  }
}
