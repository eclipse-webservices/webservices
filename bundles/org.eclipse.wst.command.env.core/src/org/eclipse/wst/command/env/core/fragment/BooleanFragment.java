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
import org.eclipse.wst.command.env.core.common.Condition;


/**
  * This class returns either the true fragment or the false fragment
  * based on the response from the condition object.
**/
public class BooleanFragment extends AbstractCommandFragment
{
  private CommandFragment trueFragment_;
  private CommandFragment falseFragment_;
  private Condition       condition_;

  /**
   * Create a BooleanFragment with default values.
   *
   */
  public BooleanFragment()
  {
    this( null, null, new Condition()
                      {
                        public boolean evaluate()
                        {
                          return true;
                        }
                      }, 
                      null, "" );  
  }
  
  /**
   * 
   * @param trueFragment  The fragment chosen if the condition is true.
   * @param falseFragment The fragment chosen if the condition is false.
   * @param condition     The condition.
   */
  public BooleanFragment( CommandFragment trueFragment,
                          CommandFragment falseFragment,
                          Condition       condition )
  {
    this( trueFragment, falseFragment, condition, null, "" );
  }                          

  /**
    * Creates a new BooleanFragment.
    *
    * @param trueFragment returned if condition is true.
    * @param falseFragment returned if condition is false.
    * @param condition the condition for this fragment.
    * @param state the state passed to the condition.
    * @param command the exectable command for this fragment.
  **/
  public BooleanFragment( CommandFragment  trueFragment,
                           CommandFragment falseFragment,
                           Condition       condition,
                           CommandFactory  commandFactory,
                           String          id )
  {
    super( commandFactory, id );

    trueFragment_       = trueFragment;
    falseFragment_      = falseFragment;
    condition_          = condition; 
  }

  /**
   * Copy constructor.
   * @param frag
   */
  protected BooleanFragment( BooleanFragment frag )
  {
    this( null,
    	  null,
    	  frag.condition_,
    	  frag.getCommandFactory(),
    	  frag.getId() );

    // Now we have to clone in the true and false
    // fragments.
    trueFragment_  = (CommandFragment)trueFragment_.clone();
    falseFragment_ = (CommandFragment)falseFragment_.clone();
  }
  
  /**
    * Makes a copy of the CommandFragment.
    *
    * @return returns a copy of this fragment.
  **/
  public Object clone()
  {
    return new BooleanFragment( this );
  }

  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment()
  {    
    return condition_.evaluate() ? trueFragment_ : falseFragment_;
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
  
  /**
   * Sets the condition.
   * @param condition
   */
  public void setCondition( Condition condition )
  {
    condition_ = condition;    
  }
  
  /**
   * Sets the true fragment.
   * @param fragment
   */
  public void setTrueFragment( CommandFragment fragment )
  {
    trueFragment_ = fragment;
  }
  
  /**
   * Sets the false fragment.
   * @param fragment
   */
  public void setFalseFragment( CommandFragment fragment )
  {
    falseFragment_ = fragment;
  } 
}
