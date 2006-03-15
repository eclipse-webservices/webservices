/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060223   129232 pmoogk@ca.ibm.com - Peter Moogk
 * 20060313   130958 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.core.fragment;

import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.core.CommandFactory;
import org.eclipse.wst.command.internal.env.core.CommandManager;
import org.eclipse.wst.command.internal.env.core.EnvironmentCoreMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;



/**
 * The CommandFragmentEngine provides a convienent way to traverse CommandFragments
 * and possiblity execute its associate Command.
 */
public class CommandFragmentEngine implements CommandManager
{
  private Stack            commandStack_;
  private FragmentListener undoFragmentListener_;
  private FragmentListener nextFragmentListener_;
  private FragmentListener afterExecuteFragmentListener_;
  private FragmentListener peekFragmentListener_;
  private DataFlowManager  dataManager_;
  private IEnvironment     environment_;
  private IStatus          lastStatus_;
  
  /**
   * Creates a CommandFragmentEngine.
   * 
   * @param startFragment the root fragment where traversal will begin.
   * @param dataManager the data manager containing all of the data mappings.
   * @param environment the environment.
   */
  public CommandFragmentEngine( CommandFragment startFragment, DataFlowManager dataManager, IEnvironment environment )
  {
  	SequenceFragment root = new SequenceFragment();
  	root.add( startFragment );
  	  	
  	commandStack_ = new Stack();
  	
  	addToStack( root, -1 );
  	addToStack( startFragment, 0 );
  	
  	dataManager_ = dataManager;
  	environment_ = environment;
  } 
  
  public String toString()
  {
    String      newline = System.getProperty("line.separator");    
    StringBuffer buffer = new StringBuffer();
    
    buffer.append(newline);
    buffer.append("Command stack start:");
    buffer.append(newline);
    
    for( int index = 0; index < commandStack_.size(); index++ )
    {
      CommandListEntry entry = (CommandListEntry)commandStack_.elementAt(index);
      entry.dump(buffer, index);
    }
    
    buffer.append("Command start end:");
    buffer.append(newline);
    
    return buffer.toString();
  }
  
  /**
   * @return returns the Data mapping registry.
   */
  public DataMappingRegistry getMappingRegistry()
  {
	return dataManager_.getMappingRegistry();
  }
  
  /**
   * 
   * @return the Status from the last Command executed.
   */
  public IStatus getLastStatus()
  {
    return lastStatus_;
  }
  
  /**
   */
  public boolean isUndoEnabled()
  {
    return true;
  }
  
  /**
   * Sets the next fragment listener for this engine.  This listener will be
   * called for each fragment that is traversed in moveForwardToNextStop operation.
   * 
   * @param listener the fragment listener.
   */
  public void setNextFragmentListener( FragmentListener listener )
  {
    nextFragmentListener_ = listener;
  }
  
  public void setAfterExecuteFragmentListener( FragmentListener listener )
  {
    afterExecuteFragmentListener_ = listener;
  }
  
  /**
   * Sets the next fragment listener for this engine.  This listener will be
   * called for each fragment that is traversed in peekForwardToNextStop operation.
   * 
   * @param listener the fragment listener.
   */
  public void setPeekFragmentListener( FragmentListener listener )
  {
    peekFragmentListener_ = listener;
  }
  
  /**
   * Sets the peek fragment listener for this engine.  This listener will be
   * called for each fragment that is traversed in undoToLastStop operation.
   * 
   * @param listener the fragment listener.
   */
  public void setUndoFragmentListener( FragmentListener listener )
  {
    undoFragmentListener_ = listener;
  }
  
  /**
   * Traverse the CommandFragments starting with the CommandFragment on the
   * top of the command fragment stack.  The operation does NOT change the
   * command fragment stack and does not execute any command associated with
   * a CommandFragment.
   */
  public void peekForwardToNextStop()
  {
  	CommandListEntry topEntry      = (CommandListEntry)commandStack_.lastElement();  	
  	int              parentIndex   = topEntry.parentIndex_;
  	CommandFragment  childFragment = topEntry.fragment_; 
  	boolean          continueLoop  = navigateChildFragments( childFragment, false );
  	  	
  	while( parentIndex != -1 && continueLoop )
  	{  	
  	  CommandListEntry parentEntry    = (CommandListEntry)commandStack_.elementAt( parentIndex );
  	  CommandFragment  parentFragment = parentEntry.fragment_;
  	  CommandFragment  nextFragment   = parentFragment.getNextSubFragment( childFragment );
  	  
  	  if( nextFragment == null )
  	  {
  	  	// There are no more sibling fragments to navigate so we will pop up to the parent
  	  	// an continue navigation there.
  	  	parentIndex   = parentEntry.parentIndex_;
  	  	childFragment = parentFragment; 
  	  }
  	  else
  	  {  	  	
  	  	if( navigateChildFragments( nextFragment, true ) ) 
  	  	{
  	  	  // We are continuing to navigate.  This time we want to traverse the sibling
  	  	  // of nextFragment. 
  	  	  childFragment = nextFragment;
  	  	}
  	  	else
  	  	{
  	  	  // We are stopping our navigation.
  	  	  continueLoop = false;
  	  	}
  	  }
  	}
  }
  
  
  /**
   * Traverse the CommandFragments starting with the CommandFragment on the
   * top of the command fragment stack.  This operation does change the
   * command fragment stack and does execute any command associated with
   * a CommandFragment.
   */
  public void moveForwardToNextStop( IProgressMonitor monitor )
  {
  	CommandListEntry topEntry        = (CommandListEntry)commandStack_.lastElement();  	
  	int              parentIndex     = topEntry.parentIndex_;
  	CommandFragment  currentFragment = topEntry.fragment_;  
  	boolean          continueExecute = visitTop( monitor );
  	CommandFragment  childFragment    = currentFragment.getFirstSubFragment();
  	    
  	// If the current fragment has child fragments we need to traverse these children.
  	while( childFragment != null && continueExecute )
  	{
  	  parentIndex = commandStack_.size() - 1;
  	  addToStack( childFragment, parentIndex );
  	  continueExecute = visitTop( monitor );
  	  currentFragment = childFragment;
  	  childFragment   = currentFragment.getFirstSubFragment();
  	}
  	
  	// The previous while loop has guaranteed that currentFragment has no
  	// child fragments. This while loop assumes this to be the case.
  	while( parentIndex != -1 && continueExecute )
  	{  	   	
  	  CommandListEntry parentEntry    = (CommandListEntry)commandStack_.elementAt( parentIndex );
  	  CommandFragment  parentFragment = parentEntry.fragment_;
  	  CommandFragment  nextFragment   = null;
  	  
  	  if( currentFragment == null )
  	  {
  	    nextFragment = parentFragment.getFirstSubFragment();
  	  }
  	  else
  	  {	
  	    nextFragment = parentFragment.getNextSubFragment( currentFragment );
  	  }
  	  
  	  if( nextFragment == null )
  	  {
  	  	// There are no more sibling fragments to navigate so we will pop up to the parent
  	  	// and continue navigation there.
  	  	parentIndex     = parentEntry.parentIndex_;
  	  	currentFragment = parentFragment; 
  	  }
  	  else
  	  {  	
  	  	// We have a new fragment that we need to add to the top of the stack.
  	  	addToStack( nextFragment, parentIndex );  	  	
  	    parentIndex     = commandStack_.size() - 1;  	
  	    continueExecute = visitTop( monitor );
  	    currentFragment = null;  	  	
  	  }
  	}
  }
  
  /**
   * 
   * @return returns true if the there is not longer any elements on the stack.  Note:
   *                 that last two entries are always left on the stack.
   */
  public boolean undoToLastStop()
  {    
  	CommandListEntry topEntry = (CommandListEntry)commandStack_.lastElement();	
  	
	  if( topEntry.fragmentStopped_ && !topEntry.beforeExecute_ )
    {
      // Normally the command at the top of the stack has not been executed.  If
      // it has been execute, it means that we tried to execute and it failed.
      // The first command in the command stack failed.  Therefore, we should
      // only undo this command.
      performUndo( topEntry );
      return topEntry.parentIndex_ == 0;
    }
    
  	performUndo( topEntry );
  		
    while( topEntry.parentIndex_ != 0 )
  	{
      commandStack_.pop();
  	  topEntry = (CommandListEntry)commandStack_.lastElement();	  
      
  	  performUndo( topEntry );
  	  
  	  if( topEntry.fragmentStopped_ )
  	  {
  	    break;
  	  }
  	}
  	
  	return topEntry.parentIndex_ == 0;
  }
   
  private void performUndo( CommandListEntry entry )
  {
    if( entry.parentIndex_ == 0 ) return;
    
	  AbstractDataModelOperation cmd = entry.command_;
  	  
  	if( cmd != null && cmd.canUndo() && !entry.beforeExecute_ )
	{
    try
    {
	    cmd.undo( null, null );
      dataManager_.unprocess(cmd);
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
    }
    
	  entry.beforeExecute_ = true;
	}
  	  
	undoFragmentListener_.notify( entry.fragment_ );  	
  }
  
  private boolean navigateChildFragments( CommandFragment fragment, boolean visitCurrent )
  {
  	boolean         continueNavigate = true;
  	CommandFragment childFrag        = null; 
  	  
  	if( visitCurrent )
  	{
  	  continueNavigate = peekFragmentListener_.notify( fragment );
      dataManager_.process( fragment );
  	}
  	
  	childFrag = fragment.getFirstSubFragment();
   	
  	while( childFrag != null && continueNavigate )
  	{
  	  continueNavigate = navigateChildFragments( childFrag, true );
  	  childFrag        = fragment.getNextSubFragment( childFrag );
  	}
  	
  	return continueNavigate;
  }
    
  private boolean visitTop( IProgressMonitor monitor )
  {  
  	CommandListEntry entry           = (CommandListEntry)commandStack_.lastElement();
  	boolean         continueNavigate = nextFragmentListener_.notify( entry.fragment_ );
  	  
  	if( continueNavigate )
  	{
  	  // Call the setters for this fragment.
      dataManager_.process( entry.fragment_ );
      
      // Add any rules to the mapping registry before we execute the command.
      entry.fragment_.registerDataMappings( dataManager_.getMappingRegistry() );
   
  	  lastStatus_ = runCommand( entry, monitor );
  	  
      if( afterExecuteFragmentListener_ != null )
      {
        continueNavigate = afterExecuteFragmentListener_.notify( entry.fragment_ );  
        
        if( !continueNavigate )
        {
          // The after execution listener has indicated that execution should stop.
          // Therefore, we will upgrade the severity of the last status to ERROR.
          lastStatus_ = new Status( IStatus.ERROR, 
                                    lastStatus_.getPlugin(), 
                                    lastStatus_.getCode(), 
                                    lastStatus_.getMessage(),
                                    lastStatus_.getException() );
        }
      }
      
  	  if( continueNavigate && lastStatus_.getSeverity() == IStatus.ERROR ) continueNavigate = false;
  	}
  	
   	if( !continueNavigate ) entry.fragmentStopped_ = true;
   	
  	return continueNavigate;
  }
  
  private void addToStack( CommandFragment fragment, int parentIndex )
  {
    CommandListEntry entry = new CommandListEntry( fragment, parentIndex );
    commandStack_.push( entry );
  }
  
  // Subclasses can do initialization before the execution of a command here
  protected IStatus initBeforeExecute( AbstractDataModelOperation operation )
  {  
	  return Status.OK_STATUS;
  }
  
  private IStatus runCommand( CommandListEntry entry, IProgressMonitor monitor )
  {
	CommandFactory factory  = entry.fragment_.getCommandFactory();
  	IStatus      status   = Status.OK_STATUS;
  	
  	if( factory != null )
  	{
  	  AbstractDataModelOperation cmd  = factory.create();
      
  	  entry.command_ = cmd;
  	    
  	  if( cmd != null )
  	  {
  	    dataManager_.process( cmd );
  	  	  
  	    try
  	    {
          status = initBeforeExecute( cmd );
          
  	      environment_.getLog().log(ILog.INFO, "command", 5001, this, "runCommand", "Executing: " + cmd.getClass().getName());
  	  	    
 	        cmd.setEnvironment( environment_ );
          status = cmd.execute( monitor, null );
          
		      entry.beforeExecute_ = false;
  	    }
  	    catch( Throwable exc )
  	    {
  	      IStatus      unexpectedError = StatusUtils.errorStatus( exc );
          MultiStatus  parentStatus    = new MultiStatus( "id", 0, new IStatus[]{unexpectedError}, 
                                                          EnvironmentCoreMessages.MSG_ERROR_UNEXPECTED_ERROR, null );
  	      environment_.getStatusHandler().reportError( parentStatus );
  	      status = unexpectedError;
  	    }
  	    finally
  	    {
  	      String message = "Ok";
  	      
  	      if( status.getSeverity() == Status.ERROR )
  	      {
  	        message = "Error: " + status.getMessage(); 
  	      }
  	      
	      environment_.getLog().log(ILog.INFO, "command", 5001, this, "runCommand", "Execution status: " + message );
  	    }
  	  }
  	}
  	
  	return status;
  }
    
  private class CommandListEntry
  {
  	public CommandListEntry( CommandFragment fragment, int parentIndex )
  	{
  	  fragment_        = fragment;
  	  parentIndex_     = parentIndex;
  	  fragmentStopped_ = false;
	  beforeExecute_   = true;
  	}
  	  	
  	public AbstractDataModelOperation command_;
  	public CommandFragment fragment_;
  	public int             parentIndex_;
  	public boolean         fragmentStopped_;
  	public boolean         beforeExecute_;
    
    public void dump( StringBuffer buffer, int index )
    {
      String newline = System.getProperty("line.separator");    
      String line    = "  " + index + "- frag: " + fragment_.getId() + " parIndex: " + parentIndex_ + " stop: " + fragmentStopped_ + " beforeExecute: " + beforeExecute_ ;
      buffer.append(line); 
      buffer.append(newline);
    }
  }    
}
