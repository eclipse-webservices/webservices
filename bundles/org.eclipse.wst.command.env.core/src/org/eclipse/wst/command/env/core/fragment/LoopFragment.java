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
import org.eclipse.wst.command.env.core.common.RangeVector;


/**
  * This class returns a dynamically determined number of
  * fragments.  This loop will return the fragment parameter
  * as long as the stopCondition evaluates to false.  Otherwise,
  * null is returned.
**/
public class LoopFragment extends AbstractCommandFragment
{
  private LoopCondition   stopCondition_;
  private CommandFragment fragment_;
  private RangeVector     fragmentHistory_;

  /**
    * Copy contructor for this fragment.
  **/
  protected LoopFragment( LoopFragment fragment )
  {
    this( fragment.fragment_,
    	  fragment.stopCondition_,
    	  fragment.getCommandFactory(),
    	  fragment.getId() );
  }
  
  public LoopFragment( CommandFragment fragment,
                        LoopCondition   stopCondition )
  {
    this( fragment, stopCondition, null, "" );
  }                       
  
  /**
    * @param fragment the fragment that will be returned by the loop.
    * @param stopCondition when false the fragment will be returned, 
    * otherwise null is returned.
    * @param state the state passed to the condition.
    * @param command the executable command for this fragment.
  **/
  public LoopFragment( CommandFragment fragment,
                       LoopCondition   stopCondition,
                       CommandFactory  commandFactory,
                       String          id   )
  {
    super( commandFactory, id );

    fragment_        = fragment;
    stopCondition_   = stopCondition;
    fragmentHistory_ = new RangeVector();
  }
  
  /**
    * Makes a copy of the CommandFragment.
    *
    * @return returns a copy of this fragment.
  **/
  public Object clone()
  {
    return new LoopFragment( this );
  }

  /**
    * Returns the index of the fragment
    *
    * @param fragment the fragment to search.
    * @return the index of this fragment.
  **/
  public int indexOf( CommandFragment fragment )
  {
    return fragmentHistory_.indexOf( fragment );
  }


  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment()
  {    
    return getNextSubFragment( null );
  }

  /**
    * Gets the next child fragment for this fragment.
    *
    * @return returns the next child fragment for this fragment.  Returns null
    * when there is no next child.
  **/
  public CommandFragment getNextSubFragment( CommandFragment frag )
  {
    CommandFragment nextFrag  = null;
    
    if( stopCondition_.evaluate( this, frag ) )
    {
      // The stop condition has been met.
      // Do nothing.
    }
    else
    {      
      int fragIndex = indexOf( frag );

      // Note: when fragment == null the fragIndex
      //       should be -1.  Therefore, if there
      //       is already a first fragment in
      //       fragmentHistory that will be returned.
      if( fragIndex + 1 < fragmentHistory_.size() )
      {
        // We have a copy of the fragment already.
        nextFrag = (CommandFragment)(fragmentHistory_.elementAt( fragIndex + 1 )); 
      }
      else
      {
        nextFrag = (CommandFragment)(fragment_.clone());
        fragmentHistory_.add( nextFrag );
      }
    }

    return nextFrag;
  }
}
