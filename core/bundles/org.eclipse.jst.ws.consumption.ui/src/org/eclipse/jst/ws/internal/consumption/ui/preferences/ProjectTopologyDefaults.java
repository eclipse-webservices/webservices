/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.preferences;

import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;

public class ProjectTopologyDefaults
{
  
  public static final String[] getServiceTypes()
  {
    return WebServiceRuntimeExtensionUtils2.getAllServiceProjectTypes();
  }
  
  public static final String[] getClientTypes()
  {
    return WebServiceRuntimeExtensionUtils2.getAllClientProjectTypes();
  }

  public static final boolean isUseTwoEARs()
  {
    return true;
  }
}
