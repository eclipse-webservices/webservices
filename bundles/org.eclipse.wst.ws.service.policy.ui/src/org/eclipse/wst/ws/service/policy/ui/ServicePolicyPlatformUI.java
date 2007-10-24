/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import java.util.List;

import org.eclipse.wst.ws.service.policy.IServicePolicy;


public class ServicePolicyPlatformUI
{
  private static ServicePolicyPlatformUI instance;
  
  private ServicePolicyPlatformUI()
  {
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
    return null;
  }
  
  public List<IPolicyOperation> getAllOperations()
  {
    return null;
  }
  
  public List<IPolicyOperation> getSelectedOperations( List<IServicePolicy> policiesSelected )
  {
    return null;
  }
}
