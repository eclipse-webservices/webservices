/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class ObjectSelectionRegistry
{
  private static ObjectSelectionRegistry instance;
  private static IConfigurationElement[] elements;
  
  private ObjectSelectionRegistry()
  {
    elements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.jst.ws.consumption.ui", "objectSelectionWidget");
  }
  
  public static ObjectSelectionRegistry getInstance()
  {
    if (instance == null)
      instance = new ObjectSelectionRegistry();
    return instance;
  }

  public IConfigurationElement[] getConfigurationElements()
  {
    return elements;
  }
}