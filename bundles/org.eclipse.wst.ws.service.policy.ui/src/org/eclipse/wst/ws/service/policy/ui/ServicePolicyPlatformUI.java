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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.ws.internal.service.policy.ui.ServicePolicyPlatformUIImpl;
import org.eclipse.wst.ws.service.policy.IServicePolicy;

/**
 * 
 * This class is used to get Service policy UI information.
 *
 */
public class ServicePolicyPlatformUI
{
  private static ServicePolicyPlatformUI instance;
  
  private ServicePolicyPlatformUIImpl platformUI;
  
  private ServicePolicyPlatformUI()
  {
    platformUI = new ServicePolicyPlatformUIImpl();
  }
  
  /**
   * 
   * @return returns an instance of this service policy platform UI object.
   */
  public static ServicePolicyPlatformUI getInstance()
  {
    if( instance == null )
    {
      instance = new ServicePolicyPlatformUI();
    }
    
    return instance;
  }
  
  /**
   * 
   * @return returns all the operations for all known policies.
   */
  public List<IPolicyOperation> getAllOperations()
  {
    return platformUI.getAllOperations();
  }
  
  public IPolicyOperation getOperation( IServicePolicy policy, String id )
  {
    List<IPolicyOperation> operations = getOperations( policy, true );
    IPolicyOperation       result     = null;
    
    for( IPolicyOperation operation : operations )
    {
      if( operation.getId().equals( id ) )
      {
        result = operation;
        break;
      }
    }
    
    return result;
  }
  
  /**
   * 
   * @param policy the policy
   * @param isWorkspace true if this is a preference page context
   * @return returns the list of operations for this policy.
   */
  public List<IPolicyOperation> getOperations( IServicePolicy policy, boolean isWorkspace )
  {
    return new Vector<IPolicyOperation>( platformUI.getOperationsForPolicy( policy, isWorkspace ) );
  }
   
  /**
   * 
   * @param policies
   * @param isWorkspace
   * @return returns a list that contains a list of operations.  Each list
   * as the top level contains list of IPolicyOperations that reference the 
   * same base operation.  However, they will each have different associated
   * policies.
   */
  public List<List<IPolicyOperation>> getOperationsList( List<IServicePolicy> policies, boolean isWorkspace )
  {
    return platformUI.getOperationsList( policies, isWorkspace );
  }
  
  /**
   * 
   * @param status
   * @return returns a list of quick fixes for this status object.  If
   * no quick fixes are available an empty list will be returned.
   */
  public List<IQuickFixActionInfo> getQuickFixes( IStatus status )
  {
    return platformUI.getQuickFixes( status );
  }
}
