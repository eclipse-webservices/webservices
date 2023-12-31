/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20080515          pmoogk@ca.ibm.com - Peter Moogk
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
   * Returns the service policy for this list.
   * @return returns a service policy.
   */
  public IServicePolicy getPolicy();
  
  /**
   * 
   * Returns the state enumeration list items.
   * @return returns a list of state enumeration items.
   */
  public List<IStateEnumerationItem> getEnumerationList();  
}
