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
 * 20080324   222095 pmoogk@ca.ibm.com - Peter Moogk, UI now listens for state changes.
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.listeners;

import org.eclipse.core.resources.IProject;


public interface IPolicyPlatformProjectLoadListener
{
  public void load( IProject project );
}
