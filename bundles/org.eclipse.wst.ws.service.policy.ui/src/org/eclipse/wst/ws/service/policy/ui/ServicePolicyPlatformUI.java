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
import java.util.Set;

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
   * @param operationId the operation ID
   * @return returns the service policy operation given it ID.
   */
  public IPolicyOperation getOperation( String operationId )
  {
    return platformUI.getOperation( operationId );
  }
  
  /**
   * 
   * @return returns all the operations that are known to the platform.
   */
  public List<IPolicyOperation> getAllOperations()
  {
    return platformUI.getAllOperations();
  }
  
  /**
   * 
   * @param policiesSelected the selected service policies.
   * @param isWorkspace indicates if this method is being called from the workspace
   * preference page or from a project property page.
   * @return returns the set of operations that are applicable to the list
   * selected service policies.  This set is further restricted by the
   * isWorkspace parameter.  If this parameter is false then operations that 
   * are only associated with the workspace will be removed. 
   */
  public Set<IPolicyOperation> getSelectedOperations( List<IServicePolicy> policiesSelected, boolean isWorkspace )
  {
    return platformUI.getSelectedOperations( policiesSelected, isWorkspace );
  }
}
