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
package org.eclipse.wst.command.env.core.fragment;

import org.eclipse.wst.command.env.core.CommandFactory;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;


/**
  * This class is used as the base class for other standard fragment classes.
  * 
**/
public abstract class AbstractCommandFragment implements CommandFragment
{
  private CommandFactory commandFactory_;
  private String         id_;
  private boolean        doNotRunInTransaction_ = false;

  /**
   * Copy constructor.
   * @param fragment
   */
  protected AbstractCommandFragment( AbstractCommandFragment fragment )
  {
    commandFactory_ = fragment.commandFactory_;
    id_             = fragment.id_;
  }
  
  /**
    * Creates a new AbstractCommandFragment.
    *
    * @param command the executable command for this fragment.
  **/
  public AbstractCommandFragment( CommandFactory commandFactory, String id )
  {
    commandFactory_ = commandFactory;  
    id_      = id;
  } 
  
  public String getId()
  {
    return id_;
  }
   
  public void setId( String id )
  {
    id_ = id; 
  }
  
  /** 
    * Gets executable command associated with this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFactory getCommandFactory()
  {
    return commandFactory_;
  }
  
  /**
   *  This method is called retrieve the data mappings for this command fragment.
   */
  public void registerDataMappings( DataMappingRegistry registry )
  {
    // The default behaviour is not to add any entries to the registry.
  }
  
  /**
   * 
   * @return If the commands for this fragment should not be run within a transaction then
   * this method should return true.  If the fragment does not care if it is run in a transaction
   * or not it should return false.
   */
  public boolean doNotRunInTransaction()
  {
    return doNotRunInTransaction_;
  }
  
  /**
   * Sets the run in transaction property.
   * @param doNotRunInTransaction
   */
  public void setDoNotRunInTransaction( boolean doNotRunInTransaction )
  {
    doNotRunInTransaction_ = doNotRunInTransaction;
  }
  
  /**
    * All fragments need to be cloneable.
  **/
  abstract public Object clone();  
  
}
