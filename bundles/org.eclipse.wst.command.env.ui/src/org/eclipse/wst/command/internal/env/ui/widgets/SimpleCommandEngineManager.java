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
package org.eclipse.wst.command.internal.env.ui.widgets;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.env.core.fragment.CommandFragmentEngine;
import org.eclipse.wst.command.env.core.fragment.FragmentListener;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseProgressMonitor;


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
  private   EclipseEnvironment    environment_;
  
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
                                                             
	engine_.setUndoFragmentListener(
      new FragmentListener()
      {
        public boolean notify( CommandFragment fragment )
        {
          return undoFragment( fragment );
        }
      } );               
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
   * The method executes the CommandFragment commands in the context provided.
   * 
   * @param context the context
   * @return returns the status of executing the commands.
   */
  public Status runForwardToNextStop( IRunnableContext context )
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
    //WorkspaceModifyOperation operation = new WorkspaceModifyOperation()
    //{
    //  public void execute( IProgressMonitor monitor )
      {
        EclipseProgressMonitor eclipseMonitor = (EclipseProgressMonitor)environment_.getProgressMonitor();
        eclipseMonitor.setMonitor( monitor );
        environment_.getLog().log(Log.INFO, "command", 5002, this, "getTransactionOperation", "Start of transaction");
        engine_.moveForwardToNextStop();
        environment_.getLog().log(Log.INFO, "command", 5003, this, "getTransactionOperation", "End of transaction");
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
        EclipseProgressMonitor eclipseMonitor = (EclipseProgressMonitor)environment_.getProgressMonitor();
        eclipseMonitor.setMonitor( monitor );
        environment_.getLog().log(Log.INFO, "command", 5085, this, "getNoTransactionOperation", "Start of NON transaction");
        engine_.moveForwardToNextStop();
        environment_.getLog().log(Log.INFO, "command", 5086, this, "getNoTransactionOperation", "End of NON transaction");
      }
    }; 
    
    return operation;
  }
}
