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

import org.eclipse.wst.command.internal.env.core.CommandFactory;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
  * This class implements an empty of WizardFragment.
**/
public class SimpleFragment extends AbstractCommandFragment
{
  public SimpleFragment()
  {
    this( (CommandFactory)null, "" );
  }
  
  public SimpleFragment( String id )
  {
    this( (CommandFactory)null, id );  
  }
  
  public SimpleFragment( final AbstractDataModelOperation operation, String id )
  {
    super( new CommandFactory()
           {
             public AbstractDataModelOperation create()
             {
               return operation;
             }
           }, id );  
  }
  
  public SimpleFragment( CommandFactory commandFactory, String id )
  {
  	super( commandFactory, id );
  }
  
  /**
    * Copy constructor for fragment.
  **/
  protected SimpleFragment( SimpleFragment fragment )
  {
    super( fragment.getCommandFactory(), fragment.getId() );
  }
    
  /**
    * All wizard fragments need to be cloneable.
  **/
  public Object clone()
  {
    return new SimpleFragment( this );
  }


  /** 
    * Gets the first child fragment for this fragment.
    *
    * @return returns the first child fragment for this fragment.  Returns
    * null when there is no first child.
  **/
  public CommandFragment getFirstSubFragment(){ return null; }

  /**
    * Gets the next child fragment for this fragment.
    * Since this is a simple fragment, there is no next fragment
    * so we will always return null. 
    *
    * @return returns the next child fragment for this fragment.  Returns null
    * when there is no next child.
  **/  
  public CommandFragment getNextSubFragment( CommandFragment fragment ){ return null; }
}  
