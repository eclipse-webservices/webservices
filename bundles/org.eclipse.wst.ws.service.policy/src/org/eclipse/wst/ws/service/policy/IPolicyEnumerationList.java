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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

import java.util.List;

/**
 * 
 * This interface encapsulates the combination of a policy and a list
 * of state enumeration items.  This interface is used with the IPolicyRelationship
 * interface.
 *
 */
public interface IPolicyEnumerationList
{
  /**
   * 
   * @return returns a service policy.
   */
  public IServicePolicy getPolicy();
  
  /**
   * 
   * @return returns a list of state enumeration items.
   */
  public List<IStateEnumerationItem> getEnumerationList();  
}
