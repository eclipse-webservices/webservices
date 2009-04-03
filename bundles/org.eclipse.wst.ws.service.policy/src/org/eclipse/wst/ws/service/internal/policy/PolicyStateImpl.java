/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
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
 * 20071221   213492 pmoogk@ca.ibm.com - Peter Moogk
 * 20080109   214818 pmoogk@ca.ibm.com - Peter Moogk
 * 20080325   222095 pmoogk@ca.ibm.com - Peter Moogk
 * 20080430   221578 pmoogk@ca.ibm.com - Peter Moogk, Fixed commit problem.
 * 20080625   238482 pmoogk@ca.ibm.com - Peter Moogk, Adding thread safety to the service platform api.
 * 20090327   270283 mahutch@ca.ibm.com - Mark Hutchinson, IPolicyStateChangeListener not notified first time change is made
 * 20090401   258858 ericdp@ca.ibm.com - Eric D. Peters, Service Policies Framework: IPolicyStateChangeListener not notified when project specific settings enabled/disabled
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyStateChangeListener;

public class PolicyStateImpl implements IPolicyState
{
  private List<IPolicyStateChangeListener> stateChangeListeners;
  private List<IPolicyStateChangeListener> stateChangeListenersOnlyOnCommit;
  private IPreferencesService              service;
  private IProject                         project;
  private boolean                          mutable;
  private IServicePolicy                   policy;
  private Map<String,TableEntry>           table;
  private ServicePolicyPlatform            platform;

  public PolicyStateImpl( ServicePolicyPlatform platform, IServicePolicy policy, IProject project)
  {
    this.service              = Platform.getPreferencesService();
    this.platform             = platform;
    this.project              = project;
    this.policy               = policy;
    this.mutable              = policy.isPredefined() ? false : true;
    this.table                = new HashMap<String, TableEntry>();
  }
  
  public void commitChanges()
  {
    // We don't do anything if this policy is not mutable.
    if( !mutable ) return;
    
    IEclipsePreferences preferences = null;
    
    if( project == null  || projectPreferencesGotDisabled())
    {
      //the project preferences are disabled, so use workspace preferences
      preferences = new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID );
    }
    else
    {
      preferences = new ProjectScope( project ).getNode( ServicePolicyActivator.PLUGIN_ID );
    }

    for( Map.Entry<String, TableEntry> entry : table.entrySet() )
    {
      String     key        = entry.getKey();
      String     storeKey   = makeStoreKey( key );
      TableEntry tableEntry = entry.getValue();
      String     value;
      if (projectPreferencesGotDisabled())
    	  //the project preferences are disabled, so use value from workspace preferences
    	  value = preferences.get(storeKey, "");
      else
    	  value = tableEntry.value;

      if( value != null )
      {
        String oldValue = tableEntry.lastCommittedValue;
       
        // Always need to put the value in the backing store even if it is the same value
        // since we delete all preferences before committing them.
        preferences.put( storeKey, value );
        tableEntry.lastCommittedValue = value;
        
        if( oldValue == null )
        { 
          //if the old value is null, and the new value is not null, need to fire a change
          firePolicyStateChange( stateChangeListenersOnlyOnCommit, key, oldValue, value );
        	
          // Temporarily remove the tableEntry value so that getValue will get the old value.
          tableEntry.value = null;
          oldValue = getValue( key );
        
          // Restore the tableEntry value;
          tableEntry.value = value;
        }
        
        if( !value.equals( oldValue ) ) 
        {          
          firePolicyStateChange( stateChangeListenersOnlyOnCommit, key, oldValue, value );
        }
      }
    }
  }

private boolean projectPreferencesGotDisabled() {
	return project !=null && !ServicePolicyPlatform.getInstance().isProjectPreferencesEnabled(project);
}
  
  private IEclipsePreferences[] getNodes()
  {
    IEclipsePreferences[] result = null;
    
    if( project != null && platform.isProjectPreferencesEnabled(project) )
    { 
      result    = new IEclipsePreferences[2];
      result[0] = new ProjectScope( project ).getNode( ServicePolicyActivator.PLUGIN_ID );
      result[1] = new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID );
    }
    else
    {
      result = new IEclipsePreferences[1];
      result[0] = new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID );
    }
    
    return result;
  }
  
  public void discardChanges()
  {
    if( !mutable ) return;
    
    for( TableEntry entry : table.values() )
    {
      entry.value = null;
    }
  }
  
  public void restoreDefaults()
  {
    // We want to restore the setting to there default state, but we don't
    // want change the backend eclipse preferences, since the user might not
    // commit these changes.  Therefore, we will set the values for all the
    // table entries to their default value.  
    if( project == null )
    {
      for( TableEntry entry : table.values() )
      {
         entry.value = entry.defaultValue;
      }
    }
    else
    {
      // Note: since we are restoring the state for a project we need to ensure
      //       that only the instance scope values are used.  (ie. we want to
      //       ignore project scope values.)
      IEclipsePreferences[] nodes = new IEclipsePreferences[]{new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID )};
      
      for( Map.Entry<String,TableEntry> entry : table.entrySet() )
      {
        String     storeKey     = makeStoreKey( entry.getKey() );
        TableEntry tableEntry   = entry.getValue();
        String     defaultValue = "";
        
        if( tableEntry.defaultValue != null )
        {
          defaultValue = tableEntry.defaultValue; 
        }
        
        tableEntry.value = service.get( storeKey, defaultValue, nodes );        
      }
    }
    
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
    TableEntry entry  = table.get( key );
    
    if( entry == null )
    {
      entry = new TableEntry();
      table.put( key, entry );
    }
    
    if( project == null ||
        ( project != null && platform.isProjectPreferencesEnabled( project ) ) )
    { 
        result = entry.value;
    }
    
    if( result == null )
    {
      // We don't have a local value for this key so we will look in the
      // preference store.
      String defaultValue = ""; //$NON-NLS-1$
      String storeKey     = makeStoreKey( key );
      
      if( entry.defaultValue != null )
      {
        defaultValue = entry.defaultValue;
      }
      
      result = service.get( storeKey, defaultValue, getNodes() );
    }
    
    return result;
  }

  private String makeStoreKey( String key )
  {
    return policy.getId() + "." + key;    //$NON-NLS-1$
  }
  
  public boolean isMutable()
  {
    return mutable;
  }

  public void putDefaultValue( String key, String defaultValue, boolean overrideExisting )
  {
    if( !mutable ) return;
    
    TableEntry entry = table.get( key );
    
    if( entry == null )
    {
      entry = new TableEntry();
      table.put( key, entry );
    }
    
    if( entry.defaultValue == null || overrideExisting )
    {
      entry.defaultValue = defaultValue;
    }
    
  }

  public void putValue(String key, String value)
  {
    if( !mutable ) return;
    
    TableEntry entry = table.get( key );
    
    if( entry == null )
    {
      entry = new TableEntry();
      table.put( key, entry );
    }

    String oldValue = getValue( key );
    
    entry.value = value;
    firePolicyStateChange( stateChangeListeners, key, oldValue, value );
  }

  public void internalSetMutable( boolean mutable )
  {
    this.mutable = mutable;  
  }
  
  public void setMutable(boolean mutable)
  {
    if( policy.isPredefined() )
    {
      ServicePolicyActivator.logError( "Attempt to set mutability on a predefined service policy.", null ); //$NON-NLS-1$
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
  
  private class TableEntry
  {
    String value;
    String lastCommittedValue;
    String defaultValue;
  }
}
