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

import java.util.Hashtable;
import org.eclipse.wst.command.internal.env.core.registry.CommandRegistry;


public class ExtensionFragment extends AbstractCommandFragment 
{
  private String[]        ids_;;
  private CommandRegistry extensionRegistry_;
  private Hashtable       fragments_;
  
  /**
   *  The default constructor
   *
   */
  public ExtensionFragment()
  {
    super( null, "" );  
    fragments_ = new Hashtable();
  }
    
  /**
   * Copy constructor.
   * @param Fragment the fragment to copy
   */
  protected ExtensionFragment( ExtensionFragment fragment )
  {
    super( fragment );
    
    ids_               = fragment.ids_;
    extensionRegistry_ = fragment.extensionRegistry_;
    fragments_         = fragment.fragments_;
  }
  
  /**
   * 
   * @param ids Sets the IDs for this extension fragment.
   */
  public void setExtensionIds( String[] ids )
  {
    ids_ = ids;
  }
  
  /**
   * 
   * @param registry Sets the registry for this extension fragment.
   */
  public void setExtensionRegistry( CommandRegistry registry )
  {
    extensionRegistry_ = registry; 
  }
      
  /**
   * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#getFirstSubFragment()
   */
  public CommandFragment getFirstSubFragment() 
  {
    CommandFragmentFactoryFactory factory  = extensionRegistry_.getFactoryFactory( ids_ );
    
    if( factory == null ) return null;
    
    CommandFragment               fragment = (CommandFragment)fragments_.get( factory );
    
    if( fragment == null )
    {
      fragment = factory.create().create();
      fragments_.put( factory, fragment );
    }
    
    return  fragment;
  }
  
  /**
   * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#getNextSubFragment(org.eclipse.wst.command.internal.env.core.fragment.CommandFragment)
   */
  public CommandFragment getNextSubFragment(CommandFragment fragment) 
  {
    return null;
  }
  
  /**
   * @see java.lang.Object#clone()
   */
  public Object clone() 
  {
    return new ExtensionFragment( this );
  }
}
