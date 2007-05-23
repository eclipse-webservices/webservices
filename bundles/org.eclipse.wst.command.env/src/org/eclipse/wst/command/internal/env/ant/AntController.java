/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060726   151614 pmoogk@ca.ibm.com - Peter Moogk
 * 20061011   159283 makandre@ca.ibm.com - Andrew Mak, project not associated to EAR when using ant on command-line
 * 20070522   176943 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.ant;

import java.util.Hashtable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.EnvironmentMessages;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.CommandManager;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistryImpl;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.FragmentListener;
import org.eclipse.wst.command.internal.env.eclipse.BaseStatusHandler;

/**
 * 
 * Central point of control for web service Ant tasks.
 * Constructs Ant environment, root fragment and command manager.  Starts execution of the command stack.
 * 
 * @author joan
 *
 */

public class AntController 
{	
   private AntOperationManager operationManager_;
   private String              errorMessage_ = null;
   private AntEnvironment      environment_;
	
   public AntController(Hashtable properties)
   {
	   // construct the environment - passing in the property table	   
	   // --maintains link to property table plus any other environment properties
	   // --code to access extension point mappings for operations to retrieve data from property table
	   TransientResourceContext  resourceContext = (TransientResourceContext)PersistentResourceContext.getInstance().copy();
	   AntStatusHandler          handler         = new AntStatusHandler();
     
	   environment_ = new AntEnvironment(this, resourceContext, handler, properties);
       
	   // construct data manager for maintaining state across operations
	   DataFlowManager dataManager = new DataFlowManager( new DataMappingRegistryImpl(), environment_ );
       
	   //  set up operation fragments - conditional on options by user... service or client
	   //  also need to initialize the "selection" or input file (WSDL, Java) here
	   
	   CommandFragment rootFragment =  environment_.getRootCommandFragment();
	   
	   if (rootFragment != null)
	   {		   
	       //construct the engine - manages execution of operations
		   createOperationManager( rootFragment, dataManager, environment_ );
		   
		   DataMappingRegistryImpl    dataRegistry_   = new DataMappingRegistryImpl();
		   rootFragment.registerDataMappings(dataRegistry_);		   
	   }
	   else  //problem getting the root fragment - scenario type is likely missing
	   {
       errorMessage_ = EnvironmentMessages.MSG_ERROR_ANT_SCENARIO_TYPE;
		   handler.reportError(new Status(IStatus.ERROR, "ws_ant", 9999, errorMessage_, null));
		   return;
	   }
	      
	   //ready to start running operations
 	   operationManager_.moveForwardToNextStop(new NullProgressMonitor());

     IStatus lastStatus = operationManager_.getLastStatus();
     
 	   if ( !lastStatus.isOK() ) 
     {
       errorMessage_ = lastStatus.getMessage();
 		   operationManager_.undoToLastStop();
     }
   }
   
   public String getErrorMessage()
   {
     return errorMessage_;  
   }
   
   private void createOperationManager(CommandFragment frag, DataFlowManager mgr, AntEnvironment env)
   {		   
		   operationManager_ = new AntOperationManager(frag, mgr, env);			
	
		   operationManager_.setPeekFragmentListener( 
				      new FragmentListener()
				      {
				        public  boolean notify( CommandFragment fragment )
				        {
				          return peekFragment( fragment );
				        }
				      } );  
				                  
		   operationManager_.setNextFragmentListener( 
				      new FragmentListener()
				      {
				        public  boolean notify( CommandFragment fragment )
				        {
				          return nextFragment( fragment );
				        }
				      } );  
				                                                             
		   operationManager_.setUndoFragmentListener(
				      new FragmentListener()
				      {
				        public boolean notify( CommandFragment fragment )
				        {
				          return undoFragment( fragment );
				        }
				      } );
       
       operationManager_.setAfterExecuteFragmentListener(
            new FragmentListener()
            {
              public boolean notify( CommandFragment fragment )
              {
                return afterExecuteNextFragment( fragment );
              }
            } );
   }
	
	protected CommandManager getOperationManager()
	{
		return operationManager_;
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
	    return true;
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
    
    protected boolean afterExecuteNextFragment( CommandFragment fragment )
    {
      boolean           continueExecute = true;
      BaseStatusHandler statusHandler   = (BaseStatusHandler)environment_.getStatusHandler();
      IStatus           commandStatus   = operationManager_.getLastStatus();
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
        errorMessage_ = handlerStatus.getMessage();
        continueExecute = false;   
      }
      
      return continueExecute;
    }
}
