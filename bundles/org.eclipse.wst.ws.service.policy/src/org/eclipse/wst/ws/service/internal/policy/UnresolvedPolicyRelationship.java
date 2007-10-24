/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
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

import java.util.List;

public class UnresolvedPolicyRelationship
{
  private String       policyId;
  private List<String> enumList;
  
  public UnresolvedPolicyRelationship( String policyId, List<String> enumList )
  {
    this.policyId = policyId;
    this.enumList = enumList;
  }
  
  public String getPolicyId()
  {
    return policyId;
  }
  
  public List<String> getEnumList()
  {
    return enumList;
  }
}
