/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.core.fragment;

import java.util.Vector;
import org.eclipse.wst.command.internal.provisional.env.core.CommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.ICommandFactory;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public abstract class CommandFactoryFragment implements CommandFragment 
{
  private Vector commands_;
  
  /**
   * Subclasses should override this method and return
   * an CommandFactory.
   * @return
   */
  public abstract ICommandFactory getICommandFactory();
  
  /**
   * All wizard fragments need to be cloneable.
  **/
  public abstract Object clone();
  
  protected CommandFactoryFragment( CommandFactoryFragment frag )
  {
    commands_ = frag.commands_;  
  }
  
  public CommandFactoryFragment()
  {
  }
  
  /**
   * 
   * @return Returns a unique identifier for this fragment.
   */
  public String getId()
  {
    return "";
  }
  
  /** 
    * Gets executable command associated with this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFactory getCommandFactory()
  {
    return null;
  }

  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment()
  {
	CommandFragment result = null;
	
    commands_ = createCommands();	

	if( commands_.size() > 0 )
	{
	  result = (ChildFragment)commands_.elementAt(0);
	}
	
	return result;
  }

  /**
    * Gets the next child fragment for this fragment.
    *
    * @return returns the next child fragment for this fragment.  Returns null
    * when there is no next child.
  **/
  public CommandFragment getNextSubFragment( CommandFragment fragment )
  {
	CommandFragment result = null;
		
	if( commands_ != null && fragment instanceof ChildFragment ) 
	{
	  ChildFragment child = (ChildFragment)fragment;
	  
	  int index = child.index_;
	
      if( index != -1 )
	  {
	    index++;
	  
	    if( index < commands_.size() )
		{
		  result = (CommandFragment)commands_.elementAt( index );
		}
	  }
	}
	
	return result;
  }
  
  /*
   * This method is called to retrieve the data mappings for this command fragment.
   */
  public void registerDataMappings( DataMappingRegistry registry )
  {
  }

  /**
   * 
   * @return If the commands for this fragment should not be run within a transaction then
   * this method should return true.  If the fragment does not care if it is run in a transaction
   * or not it should return false.
   */
  public boolean doNotRunInTransaction()
  {
    return false;
  }  
  
  private Vector createCommands()
  {
	Vector          commands = new Vector();
	ICommandFactory factory  = getICommandFactory();
	int             index    = 0;
		  
	while( factory != null && factory.hasNext() )
	{
	  AbstractDataModelOperation command = factory.getNextCommand();
			
	  commands.add( new ChildFragment( command, index++ ) );
	}
	
	return commands;
  }
  
  private class ChildFragment extends SimpleFragment
  {
	  int index_;
	
	  public ChildFragment( AbstractDataModelOperation command, int index )
	  {
	    super( command, command.getID() );
	    index_ = index;
	  }
  }
}
