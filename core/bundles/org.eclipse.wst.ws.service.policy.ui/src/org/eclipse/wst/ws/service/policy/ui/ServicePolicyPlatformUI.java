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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
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
   * Returns an instance of this service policy platform UI object.
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
   * Returns all the operations for all known policies.
   * 
   * @return returns all the operations for all known policies.
   */
  public List<IPolicyOperation> getAllOperations()
  {
    return platformUI.getAllOperations();
  }
  
  /**
   * Returns an operation that is associated with a particular service policy
   * and has the specified id.
   * 
   * @param policy the service policy
   * @param id the operation id.
   * @return returns the operation being searched for or null is not found. 
   */
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
   * Returns the list of operations for this policy.
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
   * Returns a list of list policy operations which are associated with
   * the specified service policies.
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
   * Returns a list of quick fix info for the specified status object.
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
