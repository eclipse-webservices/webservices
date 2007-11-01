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


public class ServicePolicyPlatformUI
{
  private static ServicePolicyPlatformUI instance;
  
  private ServicePolicyPlatformUIImpl platformUI;
  
  private ServicePolicyPlatformUI()
  {
    platformUI = new ServicePolicyPlatformUIImpl();
  }
  
  public static ServicePolicyPlatformUI getInstance()
  {
    if( instance == null )
    {
      instance = new ServicePolicyPlatformUI();
    }
    
    return instance;
  }
  
  public IPolicyOperation getOperation( String operationId )
  {
    return platformUI.getOperation( operationId );
  }
  
  public List<IPolicyOperation> getAllOperations()
  {
    return platformUI.getAllOperations();
  }
  
  public Set<IPolicyOperation> getSelectedOperations( List<IServicePolicy> policiesSelected, boolean isWorkspace )
  {
    return platformUI.getSelectedOperations( policiesSelected, isWorkspace );
  }
}
