/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyStateChangeListener;

public class PolicyStateImpl implements IPolicyState
{
  private List<IPolicyStateChangeListener> stateChangeListeners;
  private List<IPolicyStateChangeListener> stateChangeListenersOnlyOnCommit;
  private IPreferencesService              service;
  private IEclipsePreferences[]            nodes;
  private boolean                          mutable;
  private IServicePolicy                   policy;
  private Map<String,TableEntry>           table;

  public PolicyStateImpl( IServicePolicy policy,IEclipsePreferences[] nodes )
  {
    this.service              = Platform.getPreferencesService();
    this.nodes                = nodes;
    this.policy               = policy;
    this.mutable              = policy.isPredefined() ? false : true;
  }
  
  public void commitChanges()
  {
    if( table != null )
    {
      for( Map.Entry<String, TableEntry> entry : table.entrySet() )
      {
        String     key        = entry.getKey();
        String     storeKey   = makeStoreKey( key );
        TableEntry tableEntry = entry.getValue();
        String     value      = tableEntry.value;
      
        if( value != null )
        {
          String oldValue = getValue( key );
        
          nodes[0].put( storeKey, tableEntry.value );
          firePolicyStateChange( stateChangeListenersOnlyOnCommit, key, oldValue, value );        
        }
      }
    }
  }
  
  public void discardChanges()
  {
    this.table = null;
  }
  
  /** 
   * The method gets the value from the following places in the given order.
   * 
   * 1) From the local table.
   * 2) From the preference store nodes.  Note: this could include searching
   *         the project preference store and the plugin preference store.
   * 3) From the default in the local table.
   */
  public String getValue(String key)
  {
    String     result = null;
    TableEntry entry  = null;
    
    if( table != null )
    {
      entry = table.get( key );
      
      if( entry != null )
      {
        result = entry.value;
      }
    }
    
    if( result == null )
    {
      // We don't have a local value for this key so we will look in the
      // preference store.
      String defaultValue = "";
      String storeKey     = makeStoreKey( key );
      
      if( entry != null && entry.defaultValue != null )
      {
        defaultValue = entry.defaultValue;
      }
      
      service.get( storeKey, defaultValue, nodes);
    }
    
    return result;
  }

  private String makeStoreKey( String key )
  {
    return policy.getId() + "." + key;   
  }
  
  public boolean isMutable()
  {
    return mutable;
  }

  public void putDefaultValue(String key, String defaultValue)
  {
    createTableIfNull();
    
    TableEntry entry = table.get( key );
    
    if( entry == null )
    {
      entry = new TableEntry();
    }
    
    entry.defaultValue = defaultValue;
  }

  public void putValue(String key, String value)
  {
    createTableIfNull();
    
    TableEntry entry = table.get( key );
    
    if( entry == null )
    {
      entry = new TableEntry();
    }

    String oldValue = getValue( key );
    
    entry.value = value;
    firePolicyStateChange( stateChangeListeners, key, oldValue, value );
  }

  public void setMutable(boolean mutable)
  {
    if( policy.isPredefined() )
    {
      ServicePolicyActivator.logError( "Attempt to set mutability on a predefined service policy.", null );
    }
    else
    {
      this.mutable = mutable;
    }
  }  
  
  public void addPolicyStateChangeListener(IPolicyStateChangeListener listener, boolean notifyOnCommitOnly )
  {
    if( notifyOnCommitOnly )
    {
      if( stateChangeListenersOnlyOnCommit == null )
      {
        stateChangeListenersOnlyOnCommit = new Vector<IPolicyStateChangeListener>();
      }
      
      stateChangeListenersOnlyOnCommit.add( listener );
    }
    else
    {
      if( stateChangeListeners == null )
      {
        stateChangeListeners = new Vector<IPolicyStateChangeListener>();
      }
      
      stateChangeListeners.add( listener );
    }
  }
  
  public void removePolicyStateChangeListener(IPolicyStateChangeListener listener)
  {
    if( stateChangeListeners != null )
    {
      stateChangeListeners.remove( listener );
    }
    
    if( stateChangeListenersOnlyOnCommit != null )
    {
      stateChangeListenersOnlyOnCommit.remove( listener );
    }
  }
  
  private void firePolicyStateChange( List<IPolicyStateChangeListener> listeners,
                                      String                           key, 
                                      String                           oldValue, 
                                      String                           newValue )
  {
    if( listeners != null )
    {
      for( IPolicyStateChangeListener listener : listeners )  
      {
        listener.policyStateChange( policy, key, oldValue, newValue );
      }
    }
  }
  
  private void createTableIfNull()
  {
    if( table == null )
    {
      table = new HashMap<String, TableEntry>();
    }
  }
  
  private class TableEntry
  {
    String value;
    String defaultValue;
  }
}
