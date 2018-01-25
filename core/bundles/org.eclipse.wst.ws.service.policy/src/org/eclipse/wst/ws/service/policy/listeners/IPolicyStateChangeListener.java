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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.listeners;

import org.eclipse.wst.ws.service.policy.IServicePolicy;

/**
 * 
 * This interface is used when the developer wants to listener to state
 * changes for a particular service policy.
 *
 */
public interface IPolicyStateChangeListener
{
  /**
   * Called when a state change event occurs for a service policy.
   * 
   * @param policy the service policy
   * @param key the key of the state that changed.
   * @param oldValue the old value of the state that changed.
   * @param newValue the new value of the state that changed.
   */
  public void policyStateChange( IServicePolicy policy, String key, String oldValue, String newValue );
}
