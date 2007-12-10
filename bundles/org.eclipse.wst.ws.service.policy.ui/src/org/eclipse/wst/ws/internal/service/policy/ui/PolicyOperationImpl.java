/***************************************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;

public class PolicyOperationImpl implements IPolicyOperation
{
  private BaseOperationImpl baseOperation;
  private IServicePolicy    policy;
  
  public PolicyOperationImpl( BaseOperationImpl baseOperation, IServicePolicy policy )
  {
    this.baseOperation = baseOperation;
    this.policy        = policy;
  }
  
  public String getDefaultItem()
  {
    return baseOperation.getDefaultItem();
  }

  public IDescriptor getDescriptor()
  {
    return baseOperation.getDescriptor();
  }

  public String getEnumerationId()
  {
    return baseOperation.getEnumerationId();
  }

  public String getId()
  {
    return baseOperation.getId();
  }

  public OperationKind getOperationKind()
  {
    return baseOperation.getOperationKind();
  }

  public String getPolicyIdPattern()
  {
    return baseOperation.getPolicyIdPattern();
  }

  public boolean isEnabled( List<IServicePolicy> selectedPolicies )
  {
    return baseOperation.isEnabled( selectedPolicies );
  }

  public boolean isWorkspaceOnly()
  {
    return baseOperation.isWorkspaceOnly();
  }

  public void launchOperation( List<IServicePolicy> selectedPolicies)
  {
    baseOperation.launchOperation( policy, selectedPolicies );
  }

  public IServicePolicy getServicePolicy()
  {
    return policy;
  }

  public String getStateItem( IProject project )
  {
    IPolicyState state       = getState( project );
    String       key         = baseOperation.getId();
    String       defaultItem = baseOperation.getDefaultItem();
    
    if( baseOperation.isUseDefaultData() )
    {
      key = IPolicyState.DefaultValueKey;
    }
    
    if( defaultItem != null )
    {
      state.putDefaultValue( key, defaultItem );
    }
    
    return state.getValue( key );
  }

  public void setStateItem( IProject project, String stateItem )
  {
    IPolicyState state = getState( project );
    String       key   = baseOperation.getId();
    
    if( baseOperation.isUseDefaultData() )
    {
      key = IPolicyState.DefaultValueKey;
    }
    
    state.putValue( key, stateItem );
  }
  
  private IPolicyState getState( IProject project )
  {
    return project == null ? policy.getPolicyState() : policy.getPolicyState( project );   
  }

  public boolean isUseDefaultData() 
  {
	  return baseOperation.isUseDefaultData();
  }
}
