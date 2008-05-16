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
 * 20071120   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import org.eclipse.wst.ws.service.policy.IDescriptor;

/**
 * 
 * This interface is used to store quick fix action information.
 *
 */
public interface IQuickFixActionInfo
{
  /**
   * Returns a descriptor for this quick fix.
   * 
   * @return returns a descriptor for this quick fix.
   */
  public IDescriptor getDescriptor();
  
  /**
   * Returns the action for this quick fix.
   * 
   * @return returns the action for this quick fix.
   */
  public IQuickFixAction getAction();
}
