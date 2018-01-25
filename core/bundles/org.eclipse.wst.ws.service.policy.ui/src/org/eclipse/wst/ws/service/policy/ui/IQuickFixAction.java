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
 * 20071120   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import org.eclipse.wst.ws.service.policy.IServicePolicy;

/**
 * 
 * This interface is used by extenders to add quick fixes to the service policy ui
 * framework.
 *
 */
public interface IQuickFixAction
{
  /**
   * This method is called when the user has selected this action to resolve
   * a particular service policy problem.
   * 
   * @param policy The policy that this quick fix applies to.
   */
  public void action( IServicePolicy policy);
}
