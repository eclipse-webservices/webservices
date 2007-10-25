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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding of service policy
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyChildChangeListener;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;

public class ServicePolicyPlatformUIImpl
{
  private Map<String, PolicyOperationImpl>           operationMap;
  private Map<IServicePolicy, Set<IPolicyOperation>> policyCache;
  
  public ServicePolicyPlatformUIImpl()
  {
    ServicePolicyPlatform   platform  = ServicePolicyPlatform.getInstance();
    ServicePolicyRegistryUI registry  = new ServicePolicyRegistryUI();
    Set<String>             policyIds = platform.getAllPolicyIds();
    
    operationMap = new HashMap<String, PolicyOperationImpl>();
    policyCache  = new HashMap<IServicePolicy, Set<IPolicyOperation>>();
    registry.load( operationMap );
    createOperationCache( policyIds );
  }
  
  public IPolicyOperation getOperation( String operationId )
  {
    return operationMap.get( operationId );
  }
  
  public List<IPolicyOperation> getAllOperations()
  {
    return new Vector<IPolicyOperation>( operationMap.values() );
  }
  
  public Set<IPolicyOperation> getSelectedOperations( List<IServicePolicy> policiesSelected )
  {
    Set<IPolicyOperation> operations = new HashSet<IPolicyOperation>();
    
    for( IServicePolicy policy : policiesSelected )
    {
      Set<IPolicyOperation> policyOperations = policyCache.get( policy );
      
      if( policyOperations != null )
      {
        operations.addAll( policyOperations );
      }
    }
    
    return operations;
  }
  
  private void createOperationCache( Set<String> policyIds )
  {
    ServicePolicyPlatform           platform   = ServicePolicyPlatform.getInstance();
    Collection<PolicyOperationImpl> operations = operationMap.values();
    
    for( PolicyOperationImpl operation : operations )
    {
      String  policyPattern = operation.getPolicyIdPattern();
      Pattern pattern       = Pattern.compile( policyPattern );
      
      for( String policyId : policyIds )
      {
        Matcher matcher = pattern.matcher( policyId );
        
        if( matcher.matches() )
        {
          IServicePolicy        policy        = platform.getServicePolicy( policyId );
          Set<IPolicyOperation> operationsSet = policyCache.get(policy);
          
          if( operationsSet == null )
          {
            operationsSet = new HashSet<IPolicyOperation>();
            policyCache.put( policy, operationsSet );
          }
          
          operationsSet.add( operation );
        }
      }
    }
    
    // Now add listeners to each node.
    for( String policyId : policyIds )
    {
      IServicePolicy policy = platform.getServicePolicy( policyId );
      policy.addPolicyChildChangeListener( new ChildListener() );
    }
  }
  
  private class ChildListener implements IPolicyChildChangeListener
  {
    public void childChange(IServicePolicy child, boolean added)
    {
      if( added )
      {
        Set<String> idSet = new HashSet<String>();
        
        idSet.add( child.getId() );
        createOperationCache( idSet );
      }
      else
      {
        policyCache.remove( child );
      }
    }    
  }
}
