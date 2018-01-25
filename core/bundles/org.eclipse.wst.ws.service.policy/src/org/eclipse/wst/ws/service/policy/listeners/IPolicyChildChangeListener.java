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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.listeners;

import java.util.List;

import org.eclipse.wst.ws.service.policy.IServicePolicy;

public interface IPolicyChildChangeListener
{
  /**
   * The method is called with an array of child changes.  Each child item
   * is associated with the added item at the same index value.
   * 
   * @param child service policies that have changed.
   * @param added indicates if the associate policy was added or removed.
   */
  public void childChange( List<IServicePolicy> child, List<Boolean> added );
}
