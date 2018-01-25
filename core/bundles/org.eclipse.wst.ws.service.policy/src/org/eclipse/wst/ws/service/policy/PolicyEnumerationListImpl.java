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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

import java.util.List;

/**
 * 
 * This class provides a simple implementation of the IPolicyEnumerationList interface.
 *
 */
public class PolicyEnumerationListImpl implements IPolicyEnumerationList
{
  private List<IStateEnumerationItem> enumList;
  private IServicePolicy              policy;
  
  /**
   * A simple constructor for this class.
   * 
   * @param enumList
   * @param policy
   */
  public PolicyEnumerationListImpl( List<IStateEnumerationItem> enumList, IServicePolicy policy )
  {
    this.enumList = enumList;
    this.policy   = policy;
  }
  
  /**
   * 
   * Returns a list of state enumeration items.
   * 
   * @return returns a list of state enumeration items.
   */
  public List<IStateEnumerationItem> getEnumerationList()
  {
    return enumList;
  }

  /**
   * 
   * Returns a service policy.
   * 
   * @return returns a service policy.
   */
  public IServicePolicy getPolicy()
  {
    return policy;
  }
}
