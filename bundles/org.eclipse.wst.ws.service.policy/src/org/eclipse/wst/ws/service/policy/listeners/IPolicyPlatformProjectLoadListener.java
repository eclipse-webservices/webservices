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
 * 20080324   222095 pmoogk@ca.ibm.com - Peter Moogk, UI now listens for state changes.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.listeners;

import org.eclipse.core.resources.IProject;

/**
 * 
 * This interface is used to register code that needs to loaded when a particular
 * project is loaded into the service policy platform.
 *
 */
public interface IPolicyPlatformProjectLoadListener
{
  /**
   * This method is called once when a project loaded into the service policy
   * platform.
   * 
   * @param project the project
   */
  public void load( IProject project );
}
