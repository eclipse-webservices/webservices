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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;

public class ServicePolicyPlatformUIImpl
{
  Map<String, PolicyOperationImpl> operationMap = new HashMap<String, PolicyOperationImpl>();
  
  public ServicePolicyPlatformUIImpl()
  {
    ServicePolicyRegistryUI registry = new ServicePolicyRegistryUI();
    
    operationMap = new HashMap<String, PolicyOperationImpl>();
    registry.load( operationMap );
  }
  
  public IPolicyOperation getOperation( String operationId )
  {
    return operationMap.get( operationId );
  }
  
  public List<IPolicyOperation> getAllOperations()
  {
    return new Vector<IPolicyOperation>( operationMap.values() );
  }
  
  public List<IPolicyOperation> getSelectedOperations( List<IServicePolicy> policiesSelected )
  {
    //TODO implement getting the list of operations from selected policies.
    return null;
  }
}
