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
 * This class provides a simple implementation of the IPolicyRelationship
 * interface.
 *
 */
public class PolicyRelationshipImpl implements IPolicyRelationship
{
  private IPolicyEnumerationList       policyEnumerationList;
  private List<IPolicyEnumerationList> relatedPolices;
  
  public PolicyRelationshipImpl( IPolicyEnumerationList       policyEnumerationList,
                                 List<IPolicyEnumerationList> relatedPolicies )
  {
    this.policyEnumerationList = policyEnumerationList;
    this.relatedPolices        = relatedPolicies;
  }
  
  /**
   * 
   * Returns the policy enumeration list for this relationship.
   * 
   * @return This method returns the source service policy along with a list
   * of possible states that this source policy could be in.
   */
  public IPolicyEnumerationList getPolicyEnumerationList()
  {
    return policyEnumerationList;
  }

  /**
   * 
   * Returns the related policies for this relationship.
   * 
   * @return returns a list of other target service policies and the states that
   * these policies must be in to satisfy the relationship.
   */
  public List<IPolicyEnumerationList> getRelatedPolicies()
  {
    return relatedPolices;
  } 
}
