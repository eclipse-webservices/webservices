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
package org.eclipse.wst.command.internal.env.core.fragment;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.command.internal.env.core.CommandFactory;


/**
  * This class implements a sequence of CommandFragments.
**/
public class SequenceFragment extends AbstractCommandFragment
{
  private List fragmentList_;

  protected SequenceFragment( SequenceFragment frag )
  {
  	super( frag.getCommandFactory(), frag.getId() );
  	
    fragmentList_ = new Vector();
    
    for( int index = 0; index < frag.fragmentList_.size(); index++ )
    {
      Object newFrag = ((CommandFragment)frag.fragmentList_.get(index)).clone();
      fragmentList_.add( newFrag );
    }
  }
  
  public SequenceFragment( CommandFragment[] fragments, 
						   CommandFactory    commandFactory, 
					       String            id  )
  {
  	super( commandFactory, id );
  	
    if( fragments != null )
    {
      fragmentList_ = Arrays.asList( fragments );
    }
    else
    {
      fragmentList_ = new Vector();
    }
  }
  
  public SequenceFragment()
  {
  	super( null, "" );
  	
  	fragmentList_ = new Vector();
  }

  /**
    * Appends a fragment to the sequence. 
  **/
  public void add( CommandFragment fragment )
  {
    fragmentList_.add( fragment );
  }
  
  /**
    * Makes a copy of the CommandFragment.
    *
    * @return returns a copy of this fragment.
  **/
  public Object clone()
  {
    return new SequenceFragment( this );
  }

  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment()
  {
    CommandFragment fragment = null;

    if( fragmentList_ == null || fragmentList_.size() == 0 )
    {
      fragment = null;
    }
    else
    {      
      fragment = (CommandFragment)( fragmentList_.get(0) );
    }

    return fragment;    
  }

  /**
    * Gets the next child fragment for this fragment.
    *
    * @return returns the next child fragment for this fragment.  Returns null
    * when there is no next child.
  **/
  public CommandFragment getNextSubFragment( CommandFragment fragment )
  {
    int index = fragmentList_.indexOf( fragment );

    if( index == -1 )
    {
      throw new IllegalArgumentException( "Fragment not found in sequence.");
    }
    else
    {
      index++;

      if( index >= fragmentList_.size() )
      {
        // There is nothing following this fragment so return null;
        return null;
      }
      else
      {
        // Return the next fragment.
        return (CommandFragment)(fragmentList_.get( index ));
      }
    }
  }	
}  
