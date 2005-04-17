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
package org.eclipse.wst.command.internal.env.core.data;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;


public class DataMappingRegistryImpl implements DataMappingRegistry
{  
  private Hashtable rulesTable_ = new Hashtable();

  public Vector getRuleEntries( String targetType )
  {
    return (Vector)rulesTable_.get( targetType );
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry#addMapping(java.lang.Class, java.lang.String, java.lang.Class, java.lang.String, org.eclipse.wst.command.internal.provisional.env.core.data.Transformer)
   */
  public void addMapping( Class sourceType, String sourceProperty,
                          Class targetType, String targetProperty, 
                          Transformer transformer) 
  {
    Vector    ruleEntries = (Vector)rulesTable_.get( targetType.getName() );
    RuleEntry ruleEntry   = null;
    
    if( ruleEntries == null )
    {
      ruleEntries = new Vector();
      rulesTable_.put( targetType.getName(), ruleEntries );
    }
    
    // Find the rule entry
    for( int index = 0; index < ruleEntries.size(); index++ )
    {
      RuleEntry newEntry = (RuleEntry)ruleEntries.elementAt( index );
    
      if( sourceProperty.equals( newEntry.sourceProperty_ ) &&
          sourceType.equals( newEntry.sourceType_ ) &&
          targetProperty.equals( newEntry.targetProperty_ ) )
      {
        // The entry already exists
        ruleEntry = newEntry;
        break;
      }
    }
    
    if( ruleEntry == null )
    {
      // The rule didn't exist already so we will create a new one.
      ruleEntry = new RuleEntry(sourceType.getName(), sourceProperty, targetProperty, transformer );
      ruleEntries.add( ruleEntry );
    }
    else
    {
      // Just update the transformer.
      ruleEntry.transformer_ = transformer;  
    }
  }
    
    //ruleEntries_.
//    String    sourceClass = sourceType.getName();
//    String    targetClass = targetType.getName();
//    Vector    entries     = (Vector)ruleEntries_.get( sourceClass ); 
//    RuleEntry ruleEntry   = null;
//    
//    if( entries != null )
//    {
//      // Check to see if this mapping already exists.
//      for( int index = 0; index < entries.size(); index++ )
//      {
//        RuleEntry foundEntry = (RuleEntry)entries.elementAt( index );
//        
//        if( sourceProperty.equals( foundEntry.sourceProperty_ ) &&
//            targetType.equals( foundEntry.targetType_ ) &&
//            targetProperty.equals( foundEntry.targetProperty_ ) )
//        {
//          ruleEntry = foundEntry;
//          ruleEntry.transformer_ = transformer;
//        }
//      }
//      
//      // There is an existing vector for this sourceClass, but it didn't
//      // contain this new rule so we will add it in.
//      if( ruleEntry == null )
//      {
//        ruleEntry = new RuleEntry( sourceProperty, targetClass, targetProperty, transformer );
//        entries.add( ruleEntry );
//      }
//    }
//    else
//    {
//      // We need to create a new vector for this sourceClass.
//      entries = new Vector();
//      ruleEntry = new RuleEntry( sourceProperty, targetClass, targetProperty, transformer );
//      entries.add( ruleEntry );
//      ruleEntries_.put( sourceClass, entries );  
//    }    
//  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry#addMapping(java.lang.Class, java.lang.String, java.lang.Class)
   */
  public void addMapping(Class sourceType, String sourceProperty, Class targetType) 
  {
    addMapping( sourceType, sourceProperty, targetType, sourceProperty, null );
  }
}
