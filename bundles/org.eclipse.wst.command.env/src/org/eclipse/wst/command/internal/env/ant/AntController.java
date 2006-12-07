/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

/**
 * 
 * Central point of control for web service Ant tasks.
 * Constructs Ant environment, root fragment and command manager.  Starts execution of the command stack.
 * 
 * @author joan
 *
 */

public class AntController {
	
	
   private AntOperationManager operationManager_;
	
   public AntController(Hashtable properties)
   {
	   // construct the environment - passing in the property table	   
	   // --maintains link to property table plus any other environment properties
	   // --code to access extension point mappings for operations to retrieve data from property table
	   TransientResourceContext  resourceContext = (TransientResourceContext)PersistentResourceContext.getInstance().copy();
	   AntStatusHandler          handler         = new AntStatusHandler();
	   AntEnvironment            environment     = new AntEnvironment(this, resourceContext, handler, properties);
       
	   // construct data manager for maintaining state across operations
	   DataFlowManager dataManager = new DataFlowManager( new DataMappingRegistryImpl(), environment);
       
	   //  set up operation fragments - conditional on options by user... service or client
	   //  also need to initialize the "selection" or input file (WSDL, Java) here
	   
	   CommandFragment rootFragment =  environment.getRootCommandFragment();
	   
	   if (rootFragment != null)
	   {		   
	       //construct the engine - manages execution of operations
		   createOperationManager(rootFragment, dataManager, environment);
		   
		   DataMappingRegistryImpl    dataRegistry_   = new DataMappingRegistryImpl();
		   rootFragment.registerDataMappings(dataRegistry_);		   
	   }
	   else  //problem getting the root fragment - scenario type is likely missing
	   {
		   handler.reportError(new Status(IStatus.ERROR, "ws_ant", 9999, EnvironmentMessages.MSG_ERROR_ANT_SCENARIO_TYPE, null));
		   return;
	   }
	      
	   //ready to start running operations
 	   ((AntOperationManager)getOperationManager()).moveForwardToNextStop(new NullProgressMonitor());

 	   if (!operationManager_.getLastStatus().isOK()) 		   
 		   operationManager_.undoToLastStop();
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
	   

	
	
   
   }
