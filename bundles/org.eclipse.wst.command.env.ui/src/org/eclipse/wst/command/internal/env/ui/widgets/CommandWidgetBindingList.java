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

import java.util.Vector;

import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;


public class CommandWidgetBindingList implements CommandWidgetBinding
{
  private Vector                 bindings_;
  private Vector                 fragments_;
  private CommandFragmentFactory factory_;
  
  /**
   * This CommandWidgetBinding combines a list of CommandWidgetBindings.
   * There should be the same number of entries in both the bindings
   * parameter and the fragments parameter.  Entries in either of the
   * bindings for fragments vectors are allowed to be null.
   * 
   * @param bindings   the bindings to combine.
   * @param fragments  These fragments will be interspersed with the
   *                   fragments associated with each binding.  The first
   *                   fragment will be put before the fragment from the
   *                   first binding.  Following this will be the second
   *                   fragment from the fragments vector and then the
   *                   second fragment from the bindings vector, etc.
   */
  public CommandWidgetBindingList( Vector bindings, Vector fragments )
  {
    bindings_  = bindings; 
    fragments_ = fragments;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry) 
  {
    int length = bindings_.size();
    
    for( int index = 0; index < length; index++ )
    {
      CommandWidgetBinding binding = (CommandWidgetBinding)bindings_.elementAt( index );
      
      if( binding != null ) binding.registerCanFinish( canFinishRegistry );
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) 
  {
    int length = bindings_.size();
    
    for( int index = 0; index < length; index++ )
    {
      CommandWidgetBinding binding = (CommandWidgetBinding)bindings_.elementAt( index );
      if( binding != null ) binding.registerDataMappings( dataRegistry );
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) 
  {
    int length = bindings_.size();
    
    for( int index = 0; index < length; index++ )
    {
      CommandWidgetBinding binding = (CommandWidgetBinding)bindings_.elementAt( index );
      if( binding != null ) binding.registerWidgetMappings( widgetRegistry );
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactoryFactory#create()
   */
  public CommandFragmentFactory create() 
  {
    if( factory_ == null )
    {
      factory_ = new CommandFragmentFactory()
        {
          public CommandFragment create()
          {
            SequenceFragment fragmentList = new SequenceFragment();
            int              length       = bindings_.size();
            
            for( int index = 0; index < length; index++ )
            {
              CommandFragment fragment = (CommandFragment)fragments_.elementAt( index );
              
              if( fragment != null )
              {
                fragmentList.add( fragment );
              }
              
              CommandWidgetBinding binding = (CommandWidgetBinding)bindings_.elementAt( index );
              
              if( binding != null )
              {
                fragmentList.add( binding.create().create() );
              }
            }
            
            return fragmentList;
          }
        }; 
    }
    
    return factory_;
  }
}
