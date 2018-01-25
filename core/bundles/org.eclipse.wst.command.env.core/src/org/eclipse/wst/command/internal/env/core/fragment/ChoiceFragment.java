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

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.wst.command.internal.env.core.CommandFactory;
import org.eclipse.wst.command.internal.env.core.common.Evaluate;


/**
  * This class returns a fragment from a list of fragments based
  * on the object returned from the Evaluate object.
**/
public class ChoiceFragment extends AbstractCommandFragment 
{
  private Hashtable      fragmentTable_;
  private Evaluate       evaluate_;

  /**
    * Create a clone with new a new hashtable.
  **/
  protected ChoiceFragment( ChoiceFragment frag )
  {
    super( frag.getCommandFactory(), frag.getId() );
    
    evaluate_      = frag.evaluate_;
    fragmentTable_ = new Hashtable();
   
    Enumeration keys = frag.fragmentTable_.keys();

    // Clone the fragments in the table.
    while( keys.hasMoreElements() )
    {
      Object key = keys.nextElement();
      fragmentTable_.put( key, ((CommandFragment)frag.fragmentTable_.get(key)).clone() );
    }
  }
  
  public ChoiceFragment( Object[]          keys,
                         CommandFragment[] fragments )
  {
  	this( keys, fragments, null, null, "" );    
  }
  
  public ChoiceFragment( Object[]          keys,
                         CommandFragment[] fragments,
                         Evaluate          evaluate )
  {
  	this( keys, fragments, evaluate, null, "" );
  }                         
  
  public void setEvaluate( Evaluate evaluate )
  {
    evaluate_ = evaluate;  
  }
  
  /**
    * Constructs a choice fragment.  The key at index X is mapped to 
    * to the fragment at index X.
    *
    * @param keys these keys must be unique as determined by the equals method.
    * They are used to identify which fragment to return.
    * @param fragments these are the fragments to be returned.  Null is not allowed
    * as an entry in the array.
    * @param evaluate the object returned by this evaluate object is used
    * as the key to locate a fragment.
    * @param state the state passed to evaluate.
  **/
  public ChoiceFragment( Object[]          keys,
                         CommandFragment[] fragments,
                         Evaluate          evaluate,
                         CommandFactory    commandFactory,
                         String            id )
  {
    super( commandFactory, id );

    evaluate_      = evaluate;
    fragmentTable_ = new Hashtable();

    if( keys == null || fragments == null || keys.length != fragments.length )
    {
      throw new IllegalArgumentException( "Bad keys or fragments." );
    }
    else
    {
      // Create the fragment table.
      for( int index = 0; index < keys.length; index++ )
      {
        fragmentTable_.put( keys[index], fragments[index] );
      }     
    }
  }
  
  /**
    * Makes a copy of the CommandFragment.
    *
    * @return returns a copy of this fragment.
  **/
  public Object clone()
  {
    return new ChoiceFragment( this );
  }
  
  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment()
  {    
    Object key              = evaluate_.evaluate();
    CommandFragment fragment = (CommandFragment)fragmentTable_.get(key);

    if( fragment == null )
    {
      throw new IllegalArgumentException( "Key not found in table. Key=" + key );
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
    return null;
  }
}
