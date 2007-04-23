/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.core.fragment;

import org.eclipse.wst.command.internal.env.core.CommandFactory;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;


/**
  * This interface describes a list of interruptable commands.  The
  * order of this list of commands is defined by the getFirstSubFragment and
  * getNextSubFragment methods.
**/
public interface CommandFragment extends Cloneable
{ 
  /**
   * 
   * @return Returns a unique identifier for this fragment.
   */
  public String getId();
  
  /** 
    * Gets executable command associated with this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFactory getCommandFactory();

  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment();

  /**
    * Gets the next child fragment for this fragment.
    *
    * @return returns the next child fragment for this fragment.  Returns null
    * when there is no next child.
  **/
  public CommandFragment getNextSubFragment( CommandFragment fragment );
  
  /*
   * This method is called to retrieve the data mappings for this command fragment.
   */
  public void registerDataMappings( DataMappingRegistry registry );

  /**
   * 
   * @return If the commands for this fragment should not be run within a transaction then
   * this method should return true.  If the fragment does not care if it is run in a transaction
   * or not it should return false.
   */
  public boolean doNotRunInTransaction();
  
  /**
    * All wizard fragments need to be cloneable.
  **/
  public Object clone();  
}  
