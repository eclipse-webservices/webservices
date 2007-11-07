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
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.wst.ws.service.policy.IFilter;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyPlatformLoadListener;


public class ServicePolicyPlatformImpl 
{
  private List<IPolicyPlatformLoadListener>        loadListeners;
  private Map<String, ServicePolicyImpl>           committedPolicyMap;
  private Map<String, ServicePolicyImpl>           policyMap;
  private Map<String, List<IStateEnumerationItem>> enumList;
  private Map<String, StateEnumerationItemImpl>    enumItemList;
  
  public ServicePolicyPlatformImpl()
  {
  }
  
  public void load()
  {
    ServicePolicyRegistry registry  = new ServicePolicyRegistry( this );
    List<String>          localIds  = LocalUtils.getLocalPolicyIds();
    
    loadListeners = new Vector<IPolicyPlatformLoadListener>();
    policyMap     = new HashMap<String, ServicePolicyImpl>();
    enumList      = new HashMap<String, List<IStateEnumerationItem>>();
    enumItemList  = new HashMap<String, StateEnumerationItemImpl>();
    
    //Load local policies
    for( String localPolicyId : localIds )
    {
      ServicePolicyImpl localPolicy = LocalUtils.loadLocalPolicy( localPolicyId, this );
      
      policyMap.put( localPolicyId, localPolicy );
    }
   
    registry.load( loadListeners, policyMap, enumList, enumItemList );
    
    for( IPolicyPlatformLoadListener loadListener : loadListeners )
    {
      loadListener.load();
    }
    
    commitChanges( false );    
  }
  
  public void commitChanges( boolean saveLocals )
  {
    List<String> localIds = new Vector<String>();
    
    if( saveLocals ) LocalUtils.removeAllLocalPolicies();
    
    for( ServicePolicyImpl policy : policyMap.values() )
    {
      policy.commitChanges();
      
      if( saveLocals && !policy.isPredefined() )
      {
        LocalUtils.saveLocalPolicy( policy );
        localIds.add( policy.getId() );
      }
    }
    
    if( saveLocals ) LocalUtils.saveLocalIds( localIds );
    
    committedPolicyMap = new HashMap<String, ServicePolicyImpl>();
    committedPolicyMap.putAll( policyMap );
    ServicePolicyActivator.getDefault().savePluginPreferences();
  }
  
  public void removePolicy( ServicePolicyImpl policy )
  {
    policyMap.remove( policy.getId() );
  }
  
  public void commitChanges( IProject project )
  {
    for( ServicePolicyImpl policy : policyMap.values() )
    {
      ((PolicyStateImpl)policy.getPolicyState( project )).commitChanges();
    }
    
  }
  
  public void discardChanges()
  {
    for( ServicePolicyImpl policy : policyMap.values() )
    {
      policy.discardChanges();
    }
    
    policyMap = new HashMap<String, ServicePolicyImpl>();
    policyMap.putAll( committedPolicyMap );
  }
  
  public void discardChanges( IProject project )
  {
    for( ServicePolicyImpl policy : policyMap.values() )
    {
      ((PolicyStateImpl)policy.getPolicyState( project )).discardChanges();
    }
  }
  
  public void restoreDefaults()
  {
    for( ServicePolicyImpl policy : policyMap.values() )
    {
      policy.restoreDefaults();
    }
  }
  
  public void restoreDefaults( IProject project )
  {
    for( ServicePolicyImpl policy : policyMap.values() )
    {
      policy.restoreDefaults( project );
    }    
  }
  
  public Set<String> getAllPolicyIds()
  {
    return policyMap.keySet();
  }
    
  public List<IServicePolicy> getRootServicePolicies( IFilter filter )
  {
    List<IServicePolicy> rootPolicies = new Vector<IServicePolicy>();
    
    for( String policyId : policyMap.keySet() )
    {
      ServicePolicyImpl policy = policyMap.get( policyId );
      
      if( policy.getParentPolicy() == null )
      {
        rootPolicies.add( policy );
      }
    }
    
    return rootPolicies;
  }
  
  public ServicePolicyImpl createServicePolicy( IServicePolicy parent, 
                                                String         id, 
                                                String         enumListId, 
                                                String         defaultEnumId )
  {
    String            uniqueId = makeUniqueId( id );
    ServicePolicyImpl policy   = new ServicePolicyImpl( false, uniqueId, this );
    
    policy.setParent( (ServicePolicyImpl)parent );
    policy.setEnumListId( enumListId );
    policy.setDefaultEnumId( defaultEnumId );
    policyMap.put( uniqueId, policy );
    
    return policy;
  }
  
  public ServicePolicyImpl getServicePolicy( String id )
  {
    return id == null ? null : policyMap.get( id );  
  }
  
  public List<IStateEnumerationItem> getStateEnumeration( String enumId )
  {
    return enumList.get( enumId ); 
  }
  
  public IStateEnumerationItem getStateItemEnumeration( String stateItemId )
  {
    return enumItemList.get( stateItemId );
  }
  
  public boolean isProjectPreferencesEnabled( IProject project )
  {
    String              pluginId          = ServicePolicyActivator.PLUGIN_ID;
    IEclipsePreferences projectPreference = new ProjectScope( project ).getNode( pluginId );
    String              key               = pluginId + ".projectEnabled"; //$NON-NLS-1$
    
    return projectPreference.getBoolean( key, false );
  }
  
  public void setProjectPreferencesEnabled( IProject project, boolean value )
  {
    String              pluginId          = ServicePolicyActivator.PLUGIN_ID;
    IEclipsePreferences projectPreference = new ProjectScope( project ).getNode( pluginId );
    String              key               = pluginId + ".projectEnabled"; //$NON-NLS-1$

    projectPreference.putBoolean( key, value );
  }
  
  private String makeUniqueId( String id )
  {
    String  result  = id;
    int     idCount = 1;
    Pattern pattern = Pattern.compile( "\\d*$" ); // Match any numerical digits at the end //$NON-NLS-1$
                                                  // of the string.
    
    while( policyMap.containsKey( result ) )
    {
      Matcher matcher = pattern.matcher( result );
    
      result = matcher.replaceFirst( "" ) + idCount;
      idCount++;
    }
    
    return result;
  }
}
