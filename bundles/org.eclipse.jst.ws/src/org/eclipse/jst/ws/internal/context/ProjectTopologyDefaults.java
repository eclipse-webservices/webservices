/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.context;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;

public class ProjectTopologyDefaults
{
  public static final String[] getClientTypes()
  {
    IPluginRegistry reg = Platform.getPluginRegistry();
    IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.eclipse.jst.ws.consumption.ui", "clientProjectType");
    String[] types = new String[elements.length];
    for (int i = 0; i < types.length; i++)
      types[i] = elements[i].getAttribute("id");
    return types;
  }

  public static final boolean isUseTwoEARs()
  {
    return true;
  }
}
