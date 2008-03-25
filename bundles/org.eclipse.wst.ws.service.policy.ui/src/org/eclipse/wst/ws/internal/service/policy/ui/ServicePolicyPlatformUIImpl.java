/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
 * 20080109   213497 pmoogk@ca.ibm.com - Peter Moogk
 * 20080325   222095 pmoogk@ca.ibm.com - Peter Moogk
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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyChildChangeListener;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;
import org.eclipse.wst.ws.service.policy.ui.IQuickFixActionInfo;

public class ServicePolicyPlatformUIImpl
{
  private Map<String, BaseOperationImpl>             baseOperationMap;
  private Map<IServicePolicy, Set<IPolicyOperation>> policyCache;
  private Map<String,List<IQuickFixActionInfo>>      quickFixes;
  
  public ServicePolicyPlatformUIImpl()
  {
    ServicePolicyPlatform   platform  = ServicePolicyPlatform.getInstance();
    ServicePolicyRegistryUI registry  = new ServicePolicyRegistryUI();
    Set<String>             policyIds = platform.getAllPolicyIds();
    
    baseOperationMap = new HashMap<String, BaseOperationImpl>();
    policyCache      = new HashMap<IServicePolicy, Set<IPolicyOperation>>();
    quickFixes       = new HashMap<String, List<IQuickFixActionInfo>>();
    registry.load( baseOperationMap, quickFixes );
    createOperationCache( policyIds );
    
    platform.addChildChangeListener( new ChildListener(), false );
  }
      
  public List<IQuickFixActionInfo> getQuickFixes( IStatus status )
  {
    String                    pluginId = status.getPlugin();
    int                       code     = status.getCode();
    String                    key      = pluginId + ":" + code; //$NON-NLS-1$
    List<IQuickFixActionInfo> result   = quickFixes.get( key );
    
    if( result == null )
    {
      result = new Vector<IQuickFixActionInfo>();
      quickFixes.put( key, result );
    }
    
    return result;
  }
  
  public List<IPolicyOperation> getAllOperations()
  {
    List<IPolicyOperation> operations = new Vector<IPolicyOperation>();
    
    for( Set<IPolicyOperation> operationSet : policyCache.values() )
    {
      operations.addAll( operationSet );  
    }
    
    return operations;
  }
  
  public Set<IPolicyOperation> getOperationsForPolicy( IServicePolicy policy, boolean isWorkspace )
  {
    Set<IPolicyOperation> policyOperations   = policyCache.get( policy );
    Set<IPolicyOperation> result             = new HashSet<IPolicyOperation>();
    
    if( policyOperations != null )
    {
      result.addAll( policyOperations );  
    
      if( !isWorkspace )
      {
        for( IPolicyOperation operation : policyOperations )
        {
          if( operation.isWorkspaceOnly() )
          {
            result.remove( operation );
          }
        }
      }
    }
    
    return result;
  }
  
  public List<List<IPolicyOperation>> getOperationsList( List<IServicePolicy> policies, boolean isWorkspace )
  {
    Map<String, List<IPolicyOperation>> operationMap = new HashMap<String, List<IPolicyOperation>>();
    
    for( IServicePolicy policy : policies )
    {
      Set<IPolicyOperation> operations = policyCache.get( policy );

      if( operations != null )
      {
        for( IPolicyOperation operation : operations )
        {
          String                 id        = operation.getId();
          List<IPolicyOperation> entryList = operationMap.get( id );
        
          // We only want to add this operation if it is for the workspace preference page or
          // if we are on the project preference page and the operation is not only
          // for the workspace level.
          if( isWorkspace || !operation.isWorkspaceOnly() )
          {
            if( entryList == null )
            {
              entryList = new Vector<IPolicyOperation>();
              operationMap.put( id, entryList );
            }
        
            entryList.add( operation );
          }
        }
      }
    }
    
    return new Vector<List<IPolicyOperation>>( operationMap.values() );
  }
  
  private void createOperationCache( Set<String> policyIds )
  {
    ServicePolicyPlatform         platform   = ServicePolicyPlatform.getInstance();
    Collection<BaseOperationImpl> operations = baseOperationMap.values();
    
    for( BaseOperationImpl operation : operations )
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
          
          operationsSet.add( new PolicyOperationImpl( operation, policy ) );
        }
      }
    }    
  }
  
  private class ChildListener implements IPolicyChildChangeListener
  {
    public void childChange(List<IServicePolicy> childList, List<Boolean> addedList)
    {
      Set<String> idSet = new HashSet<String>();
      
      for( int index = 0; index < childList.size(); index++ )
      {
        IServicePolicy policy = childList.get( index );
        boolean        added  = addedList.get( index );
        
        if( added )
        {
        
          idSet.add( policy.getId() );
        }
        else
        {
          policyCache.remove( policy );
        }
      }
      
      if( idSet.size() > 0 )
      {
        createOperationCache( idSet );
      }
    }    
  }
}
