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

import org.eclipse.wst.ws.service.policy.IFilter;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyPlatformLoadListener;


public class ServicePolicyPlatformImpl 
{
  private List<IPolicyPlatformLoadListener>        loadListeners;
  private Map<String, ServicePolicyImpl>           policyMap;
  private Map<String, List<IStateEnumerationItem>> enumList;
  private Map<String, StateEnumerationItemImpl>    enumItemList;
  
  public ServicePolicyPlatformImpl()
  {
    ServicePolicyRegistry registry  = new ServicePolicyRegistry( this );
    
    loadListeners = new Vector<IPolicyPlatformLoadListener>();
    policyMap     = new HashMap<String, ServicePolicyImpl>();
    enumList      = new HashMap<String, List<IStateEnumerationItem>>();
    enumItemList  = new HashMap<String, StateEnumerationItemImpl>();
    registry.load( loadListeners, policyMap, enumList, enumItemList );
  }
  
  public void commitChanges()
  {
    //TODO 
  }
  
  public void discardChanges()
  {
    //TODO
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
    //TODO
    return null;
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
}
