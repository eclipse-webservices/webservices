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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.ws.service.policy.IServicePolicy;

/**
 * 
 * This interface is used by code that is interested in listening to status
 * change events of service policies. 
 *
 */
public interface IStatusChangeListener
{
  /**
   * Called when a status change event occurs.
   * 
   * @param policy the service policy.
   * @param oldStatus the old status.
   * @param newStatus the new status.
   */
  public void statusChange( IServicePolicy policy, IStatus oldStatus, IStatus newStatus );
}
