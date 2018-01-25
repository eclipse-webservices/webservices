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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import java.util.List;

import org.eclipse.wst.ws.service.policy.IServicePolicy;

/**
 * 
 * This interface is associated with the enabledClass attribute of the
 * enabled element.
 *
 */
public interface IEnableOperation
{
  /**
   * Returns whether the service policy operation should be enabled or not.
   * 
   * @param selectedPolicies the selected service policies.
   * @return returns whether the service policy operation should be enabled or not.
   */
  public boolean isEnabled( List<IServicePolicy> selectedPolicies );
}
